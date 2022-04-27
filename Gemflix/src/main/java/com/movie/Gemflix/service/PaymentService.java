package com.movie.Gemflix.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.movie.Gemflix.common.Constant;
import com.movie.Gemflix.dto.member.MemberDto;
import com.movie.Gemflix.dto.payment.PaidProductDto;
import com.movie.Gemflix.dto.payment.PaymentDto;
import com.movie.Gemflix.dto.product.ProductDto;
import com.movie.Gemflix.entity.*;
import com.movie.Gemflix.repository.member.MemberRepository;
import com.movie.Gemflix.repository.payment.PaymentRepository;
import com.movie.Gemflix.repository.payment.PaymentRepositorySupport;
import com.movie.Gemflix.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${iamport.api.key}")
    private String key;

    @Value("${iamport.api.secret}")
    private String secretKey;

    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final PointService pointService;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentRepositorySupport paymentRepositorySupport;


    public Boolean completePayment(JSONObject requestBody, String memberId) {
        log.info("===== completePayment =====");

        String impUid = requestBody.getString("imp_uid");
        String marchantUid = requestBody.getString("merchant_uid");
        int requestPrice = requestBody.getIntValue("pay_amount");

        // 액세스 토큰(access token) 발급 받기
        String accessToken = getIamportToken();

        // imp_uid로 아임포트 서버에서 결제 정보 조회
        JSONObject paymentData = getPaymentData(impUid, accessToken);
        int paidPrice = paymentData.getIntValue("amount");
        String status = paymentData.getString("status");

        // 결제금액의 위변조 여부를 검증 (결제 되어야 하는 금액 == 실제 결제된 금액)
        if(requestPrice == paidPrice){
            // 결제 정보를 데이터베이스에 저장
            boolean result = savePaymentData(paymentData, requestBody, memberId);

            if("paid".equals(status) && result){ // 결제 완료 (가상계좌는 구현하지 않음)
                return true;
            }else{
                return false;
            }
        }else{
            // 위조된 결제 시도
            return false;
        }
    }

    private boolean savePaymentData(JSONObject paymentData, JSONObject requestBody, String memberId) {
        log.info("===== savePaymentData =====");

        int usePoint = requestBody.getIntValue("point");
        JSONArray carts = requestBody.getJSONArray("carts");
        JSONArray tickets = requestBody.getJSONArray("tickets");

        if(0 < carts.size()){ //상품결제
            settingCarts(carts, requestBody, paymentData, memberId);
            return pointService.minusPoint(memberId, usePoint, Constant.PointType.PAYMENT_POINT);

        }else if(0 < tickets.size()){ //영화결제
            //TODO:
        }
        return false;
    }

    private boolean settingCarts(JSONArray carts,
                                 JSONObject requestBody,
                                 JSONObject paymentData,
                                 String memberId) {
        log.info("===== settingCarts =====");

        try{
            // payment(결제정보) 세팅
            PaymentDto paymentDto = settingPayment(requestBody, paymentData, memberId);
            List<PaidProductDto> paidProducts = new ArrayList<>();

            for (Object thisProduct : carts) { //해당 상품
                JSONObject thisProductJson = JSON.parseObject(JSON.toJSONString(thisProduct));

                // paid product(결제상품들) 세팅
                JSONArray selectedCounts = thisProductJson.getJSONArray("selectedCounts");
                log.info("selectedCounts: {}", selectedCounts);

                for (Object tempThisCount : selectedCounts) { //해당상품의 count 갯수
                    log.info("tempThisCount: {}", tempThisCount);
                    JSONObject thisCount = JSON.parseObject(JSON.toJSONString(tempThisCount));
                    settingPaidProduct(thisCount, thisProductJson, paidProducts, paymentDto);

                } //thisCount for

            } //thisProduct for
            paymentDto.setPaidProducts(paidProducts);
            Payment payment = modelMapper.map(paymentDto, Payment.class);
            paymentRepository.save(payment);
            return true;

        }catch (Exception e){
            return false;
        }
    }

    private void settingPaidProduct(JSONObject thisCount,
                                    JSONObject thisProductJson,
                                    List<PaidProductDto> paidProducts,
                                    PaymentDto paymentDto) {
        log.info("===== savePaidProduct =====");

        long prId = thisProductJson.getLongValue("prId");
        ProductDto productDto = getProduct(prId);

        int count = thisCount.getIntValue("count");
        int totalPrice = thisCount.getIntValue("totalPrice");

        PaidProductDto paidProductDto = PaidProductDto.builder()
                .count(count)
                .price(totalPrice)
                .product(productDto)
                .payment(paymentDto)
                .build();

        log.info("paidProductDto: {}", paidProductDto);
        paidProducts.add(paidProductDto);
    }

    private ProductDto getProduct(long prId) {
        ProductDto productDto = null;
        Optional<Product> optProduct = productRepository.findByPrIdAndDelStatus(prId, Constant.BooleanStringValue.FALSE);
        if(optProduct.isPresent()){
            Product product = optProduct.get();
            productDto = modelMapper.map(product, ProductDto.class);
        }
        return productDto;
    }

    private PaymentDto settingPayment(JSONObject requestBody,
                                      JSONObject paymentData,
                                      String memberId) {
        log.info("===== savePayment =====");

        MemberDto memberDto = getMemberData(memberId);
        LocalDateTime now = LocalDateTime.now();

        //payment save
        PaymentDto paymentDto = PaymentDto.builder()
                .point(requestBody.getIntValue("point"))
                .proAmount(requestBody.getIntValue("pro_amount"))
                .disAmount(requestBody.getIntValue("dis_amount"))
                .payAmount(paymentData.getIntValue("amount"))
                .payType(requestBody.getString("pay_type"))
                .disType(requestBody.getString("dis_type"))
                .payDate(now)
                .payStatus(Constant.BooleanStringValue.TRUE)
                .payName(paymentData.getString("buyer_name"))
                .payPhone(paymentData.getString("buyer_tel"))
                .payAddress(paymentData.getString("buyer_addr"))
                .member(memberDto)
                .build();

        log.info("paymentDto: {}", paymentDto);
        return paymentDto;
    }


    private MemberDto getMemberData(String memberId) {
        log.info("===== getMemberData =====");
        Member member;
        MemberDto memberDto;
        Optional<Member> optMember = memberRepository.findById(memberId);
        if(optMember.isPresent()){
            member = optMember.get();
            memberDto = modelMapper.map(member, MemberDto.class);
            log.info("memberDto: {}", memberDto);
            return memberDto;
        }else{
            log.error("invalid member");
            return null;
        }
    }

    public String getIamportToken(){

        JSONObject body = new JSONObject();
        body.put("imp_key", key);
        body.put("imp_secret", secretKey);

        log.info("body: {}", body);

        Mono<JSONObject> responseMono = webClient.post()
                .uri("https://api.iamport.kr/users/getToken")
                .bodyValue(body)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(JSONObject.class)
                ;

        log.info("responseMono: {}", responseMono);
        JSONObject response = responseMono.share().block();
        log.info("response: {}", response);
        String accessToken = response.getJSONObject("response").getString("access_token");
        log.info("accessToken: {}", accessToken);
        return accessToken;
    }

    public JSONObject getPaymentData(String impUid, String accessToken) {

        Mono<JSONObject> paymentDataMono = webClient.get()
                .uri("https://api.iamport.kr/payments/" + impUid)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                    .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(JSONObject.class);

        JSONObject response = paymentDataMono.share().block();
        log.info("response: {}", response);
        JSONObject paymentData = response.getJSONObject("response");
        log.info("paymentData: {}", paymentData);
        return paymentData;
    }

    public Boolean savePaymentData(JSONObject requestBody, String memberId) {

        try{
            PaymentDto paymentDto = settingPayment(requestBody, memberId);
            List<PaidProductDto> paidProducts = new ArrayList<>();

            JSONArray carts = requestBody.getJSONArray("carts");
            for (Object thisProduct : carts) { //해당 상품
                JSONObject thisProductJson = JSON.parseObject(JSON.toJSONString(thisProduct));

                // paid product(결제상품들) 세팅
                JSONArray selectedCounts = thisProductJson.getJSONArray("selectedCounts");
                log.info("selectedCounts: {}", selectedCounts);

                for (Object tempThisCount : selectedCounts) { //해당상품의 count 갯수
                    log.info("tempThisCount: {}", tempThisCount);
                    JSONObject thisCount = JSON.parseObject(JSON.toJSONString(tempThisCount));
                    settingPaidProduct(thisCount, thisProductJson, paidProducts, paymentDto);
                } //thisCount for

            } //thisProduct for
            paymentDto.setPaidProducts(paidProducts);
            log.info("paymentDto: {}", paymentDto);
            Payment payment = modelMapper.map(paymentDto, Payment.class);
            log.info("payment: {}", payment);
            paymentRepository.save(payment);

            int usePoint = requestBody.getIntValue("point");
            return pointService.minusPoint(memberId, usePoint, Constant.PointType.PAYMENT_POINT);

        }catch (Exception e){
            return false;
        }
    }

    private PaymentDto settingPayment(JSONObject requestBody, String memberId) {
        MemberDto memberDto = getMemberData(memberId);
        LocalDateTime now = LocalDateTime.now();

        //payment save
        PaymentDto paymentDto = PaymentDto.builder()
                .point(requestBody.getIntValue("point"))
                .proAmount(requestBody.getIntValue("pro_amount"))
                .disAmount(requestBody.getIntValue("dis_amount"))
                .payAmount(requestBody.getIntValue("pay_amount"))
                .payType(requestBody.getString("pay_type"))
                .disType(requestBody.getString("dis_type"))
                .payDate(now)
                .payStatus(Constant.BooleanStringValue.TRUE)
                .payName(requestBody.getString("pay_name"))
                .payPhone(requestBody.getString("pay_phone"))
                .payAddress(requestBody.getString("pay_address"))
                .member(memberDto)
                .build();

        log.info("paymentDto: {}", paymentDto);
        return paymentDto;
    }

    public List<PaymentDto> getPayments(String memberId) throws Exception {
        List<Payment> payments = paymentRepositorySupport.findByMIdOrderByPayDateDesc(memberId);
        log.info("payments: {}", payments);
        if(payments.size() == 0) return null;

        List<PaymentDto> paymentDtos = payments.stream()
                .map(payment -> {
                    PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
                    return paymentDto;
                }).collect(Collectors.toList());
        log.info("paymentDtos: {}", paymentDtos);
        return paymentDtos;
    }
}