#  Spring Boot JWT 인증/인가 프로젝트

## 1. 프로젝트 개요

이 프로젝트는 JWT(Json Web Token)를 기반으로 사용자 인증 및 인가 기능을 구현한 Spring Boot 백엔드 애플리케이션입니다.  
회원가입, 로그인, 권한 확인, 관리자 권한 부여 등 기본적인 인증 흐름을 포함하고 있으며, Swagger UI를 통해 API 테스트가 가능합니다.

---

## 2. 기술 스택

| 항목 | 내용 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3, Spring Security |
| Build Tool | Gradle |
| Auth | JWT (AccessToken) |
| Infra | AWS EC2 |
| 문서화 | Swagger (springdoc-openapi) |

---

## 3. 실행 방법

```bash
#  로컬에서 실행

# 프로젝트 빌드
./gradlew clean build

# 애플리케이션 실행
java -jar build/libs/springboot-jwt-auth-0.0.1-SNAPSHOT.jar

# JAR 파일 전송
scp -i jwt-key.pem springboot-jwt-auth-0.0.1-SNAPSHOT.jar ubuntu@[EC2_PUBLIC_IP]:/home/ubuntu/

# EC2 접속
ssh -i jwt-key.pem ubuntu@[EC2_PUBLIC_IP]

# 서버 백그라운드 실행
nohup java -jar springboot-jwt-auth-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```
## 4. 배포 정보
Swagger UI 주소:
- http://3.107.199.173:8080/swagger-ui/index.html

EC2 API 엔드포인트:
- http://3.107.199.173:8080

## 5. API 명세

| 메서드   | URL                             | 설명                   |
| ----- | ------------------------------- | -------------------- |
| POST  | `/signup`                       | 회원가입                 |
| POST  | `/login`                        | 로그인 및 JWT 발급         |
| GET   | `/me`                           | 로그인된 사용자 정보 조회       |
| PATCH | `/admin/users/{username}/roles` | 관리자 권한 부여 (ADMIN 전용) |

인증이 필요한 API는 Swagger 우측 상단의 Authorize 버튼에서 JWT 입력 필요

### 6. JWT 인증 방식 요약
- 로그인 시 `AccessToken`이 응답으로 반환됩니다.
- 인증이 필요한 요청은 `Authorization: Bearer {AccessToken}` 헤더가 필요합니다.
- Swagger 상단 Authorize 버튼을 통해 토큰을 입력하고 인증된 API를 테스트할 수 있습니다.

### 7. 디렉토리 구조 

```bash
com.springbootjwtauth
├── config                # 시큐리티 설정
├── controller            # 인증 관련 컨트롤러
├── dto                   # 요청/응답 DTO
├── exception             # 공통 예외 처리 
├── filter                # JWT 인증 필터
├── handler               # 시큐리티 전용
├── model                 # User 모델 클래스
├── service               # 인증 관련 비즈니스 로직
├── store                 # 사용자 저장소
└── util                  # JWT 유틸리티
```
### 8. 테스트용 계정 예시
| 아이디       | 비밀번호      | 비고           |
| --------- | --------- | ------------ |
| testuser  | test1234  | 일반 사용자       |
| adminuser | admin1234 | 관리자 권한 부여 가능 |
