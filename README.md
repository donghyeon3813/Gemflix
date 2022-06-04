# 영화 예매 사이트 Gemflix
***

![01](https://user-images.githubusercontent.com/72774476/171984187-0ca7b446-6e31-48f3-9848-0ff3afcec703.png)

***
## Presentation and Testing Video
- **Youtube:**
  - **[시연 영상 ![Youtube Badge](https://img.shields.io/badge/Youtube-ff0000?style=flat-square&logo=youtube&link=https://www.youtube.com/c/kyleschool)](https://youtu.be/njLeL-mE-js)**

***
## Contents
1. 소개
2. 설계의 주안점
3. Using
4. Main Function
5. ERD

***
## 소개
- 영화 정보를 조회하고 관람하는 영화 예매 사이트를 개발하였습니다.

***
## 설계의 주안점
- 영화 예매 사이트에 맞춰 다양한 영화 정보를 제공하고, 회원 관리를 위해 노력했습니다.

1. Spring Security, Oauth JWT 토큰을 이용한 로그인 시스템
2. Redis를 이용한 이메일 인증만료 & 토큰 관리
3. React를 이용한 SPA 구성
4. Redux를 이용한 로그인 / 장바구니 상태관리
5. WebClient를 이용한 Non-Blocking & 비동기 HTTP 통신
6. 다양한 API 활용(영화, 결제)

***
## Using
1. FrontEnd - REACT, JS, CSS3
2. BackEnd - Java(JDK 1.8), Spring Boot, JPA
3. OS - Windows
4. Library&API - KakaoLogin, JavaMail API, I'mport API, 공공 API
5. Document - Google Drive, ERC Cloud
6. CI - git(v2.28.0.windows.1), Github
7. DataBase - MySQL(v8.0)

***
## Main Function
- **관리자**
1. **관리자 로그인**
![02](https://user-images.githubusercontent.com/72774476/171986854-4ea965b9-903a-4b93-9b75-cd60b826b06a.png)
  
2. **관리자 계정으로 상품 등록/수정/삭제**
![03](https://user-images.githubusercontent.com/72774476/171987007-ae76345f-cb7b-4489-956f-fcbd5fa39e8c.png)
![03-2](https://user-images.githubusercontent.com/72774476/171988097-6b74e9a6-c9dc-481d-88bc-573684456d73.png)


- **사용자**
1. **사용자 회원가입 및 로그인** (Oauth JWT 토큰 사용, AccessToken 만료시 RefreshToken 자동발급, RefreshToken 만료시 자동 로그아웃)
![04](https://user-images.githubusercontent.com/72774476/171987177-f6d577dc-b101-40c2-b380-b28b4059344c.png)

2. **KAKAO 소셜로그인** (WebClient를 사용한 Non-Blocking & 비동기 HTTP 통신)
![05](https://user-images.githubusercontent.com/72774476/171987350-40bca8ef-cca3-4c9c-8532-8c5675492f65.png)

3. **이메일 인증** (Redis를 사용한 이메일 인증 링크 만료시간 설정)
![06](https://user-images.githubusercontent.com/72774476/171987391-ce7a8c38-0eaf-41ba-b0a2-8b6e2bba7688.png)

4. **영화 목록 조회/검색/페이징** (WebClient를 사용한 Non-Blocking & 비동기 HTTP 통신)
![07](https://user-images.githubusercontent.com/72774476/171987838-cf40b243-54f4-416e-ba14-0f16baab8ff6.png)

5. **영화 상세정보/필모그래피/예고편 조회**
![08](https://user-images.githubusercontent.com/72774476/171987891-6a769a55-d74b-4c88-a737-8d43bf0698c1.png)

6. **영화 평점 및 관람평 리뷰 등록/수정/삭제**
![09](https://user-images.githubusercontent.com/72774476/171987949-4fc2bab2-f598-46f0-813f-6b9b2c0d24f0.png)

7. **영화 예매/좌석선택**
![10](https://user-images.githubusercontent.com/72774476/171988189-e04909d2-9f41-4921-b79e-963bc061c84b.png)
![11](https://user-images.githubusercontent.com/72774476/171988257-151a6fec-916b-438e-9cd8-a5f40cde3f83.png)

8. **상품 장바구니 등록/삭제** (Redux를 사용한 장바구니 상태관리)
![12](https://user-images.githubusercontent.com/72774476/171988400-73f9de7d-eb5e-4d4e-a49f-5c9a2b8b44eb.png)
![13](https://user-images.githubusercontent.com/72774476/171988468-c7eef08c-3413-4ff4-a21a-00c2e3cb28bb.png)

9. **영화 및 상품 결제** (I'mport API 연동)
![14](https://user-images.githubusercontent.com/72774476/171988541-54449c4f-4e5e-4490-a5e4-89a2c59c7c6f.png)

10. **마이페이지 영화 예매내역, 상품 결젠역 조회**
![15](https://user-images.githubusercontent.com/72774476/171988596-96666385-ba77-473f-a9df-30ed873605c0.png)
![16](https://user-images.githubusercontent.com/72774476/171988660-1dde2fff-4839-45e5-8f74-e778ef4da97b.png)

11. **회원탈퇴**
![17](https://user-images.githubusercontent.com/72774476/171988702-48eb00f7-611a-4b49-9b7c-a7ee807bb099.png)

***
## ERD
![erd](https://user-images.githubusercontent.com/72774476/171988951-c9d42d93-7d9f-4a5b-acf6-9deaf706da8a.png)


