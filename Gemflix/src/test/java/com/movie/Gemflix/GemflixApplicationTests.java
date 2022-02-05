package com.movie.Gemflix;

import com.movie.Gemflix.entity.Member;
import com.movie.Gemflix.entity.MemberRole;
import com.movie.Gemflix.repository.MemberRepository;
import org.hibernate.type.LocalDateTimeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.IntStream;

@SpringBootTest
class GemflixApplicationTests {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@Description("passwordEcoder Test")
	void passwordEncode() {
		String password = "1111";
		String encodedPassword = passwordEncoder.encode(password);
		System.out.println("encodedPassword: "+ encodedPassword);
		boolean result = passwordEncoder.matches(password, encodedPassword);
		System.out.println("result: {}" + result);
	}

	@Test
	public void insertDummy(){
		// 1: none
		// 2: member
		// 3: admin
		long millis = System.currentTimeMillis();
		Instant instant = Instant.ofEpochMilli(millis);
		LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

		IntStream.rangeClosed(1, 3).forEach(i -> {
			MemberRole role = null;
			if(i == 1) role = MemberRole.NONE;
			else if(i == 2) role = MemberRole.MEMBER;
			else if(i == 3) role = MemberRole.ADMIN;

			Member member = Member.builder()
					.id("person" + i)
					.password(passwordEncoder.encode("person" + i))
//					.password("person" + i)
					.point(0)
					.phone("01012345678")
					.email("person" + i + "@gmail.com")
					.authority(role)
					.grade("1")
					.delStatus("0")
					.status("1")
					.build();

			System.out.println("member: " + member);
			memberRepository.save(member);
		});
	}

}
