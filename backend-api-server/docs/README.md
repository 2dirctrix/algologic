## 프로젝트 시작 가이드
### 1. 패키지 구조
```shell
├─authentication
│  ├─dto
│  ├─entrypoint
│  ├─filter
│  ├─service
│  ├─token
│  └─web
│      ├─controller
│      └─request
├─common
│  ├─domain
│  └─web
│      └─response
├─config
│  └─property
├─member
│  ├─domain
│  ├─repository
│  ├─service
│  └─web
│      ├─controller
│      ├─request
│      └─response
├─player
│  ├─domain
│  └─repository
├─problem
│  ├─domain
│  └─repository
├─room
│  ├─domain
│  └─repository
└─validation
    ├─exception
    └─handler
```
### 2. 패키지 설명
#### 2.1. authentication
> 인증/인가와 관련된 클래스들이 위치합니다.

* `filter`: 인증 필터들
  * `JwtAuthenticationFilter.java`: 요청 헤더에서 엑세스 토큰을 추출하여 검증한다.
* `token`: 토큰 관련 클래스들
  * `JwtManager.java`: JWT 토큰을 관리한다.
    * `generateToken()`: 엑세스 토큰과 리프레쉬 토큰을 포함한 `Token` 객체 반환 
    * `createAccessToken()`: 엑세스 토큰 생성
    * `createRefreshToken()`: 리프레쉬 토큰 발급
    * `refreshAccessToken()`: 리프레쉬 토큰을 기반으로 엑세스 토큰을 갱신한다. 

#### 2.2. common
> 프로젝트 전역에 걸쳐서 사용되는 클래스들이 위치합니다.

* `common/web/response/ApiResponse.java`
  * 공통 API 응답 스펙을 구현한 클래스
  * ```java
    @GetMapping()
    public ResponseEntity<ApiResponse<String>> foo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String data = userDetails.getMember().toString();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원 조회에 성공하였습니다.", data)); //다음과 같이 사용하면 됩니다.
    }
    ```

#### 2.3. config
> 설정 파일이 위치합니다.

* `RestClientConfig`: RestClient 에 대한 설정파일
* `SecurityConfig`: Spring Security 에 대한 설정파일
  * 인증이 필요없는 경로에 대한 목록 관리
* `SwaggerConfig`: Swagger 에 대한 설정파일

#### 2.4. validation
> 검증 및 예외와 관련된 클래스들이 위치합니다.

* `validation.exception`: 커스텀 예외를 정의합니다.
* `validation.handler`: 예외 핸들러를 정의합니다.

### 3. 프로젝트 설정 파일(`src/main/resources`)

1. 환경 설정 파일 분리
> 로컬 개발 환경과 운영 개발 환경을 분리하기 위해 다음과 같이 구성하였습니다.
   * `application-local.yml`: 로컬 환경에서 사용할 설정 파일
   * `application-prod.yml`: 운영 환경에서 사용할 설정 파일
   * 참고: 현재는 `application-local.yml`와 `application-prod.yml`가 동일하게 구성되어 있습니다.
   * <span style="color: red">주의: 로컬 환경에서 실행 시, 'local' 프로필로 Spring boot Application 을 실행해주세요.</span>
     * (인텔리제이에서 설정해둘 수 있는데 모르면 도와드릴게요)

2. `sql/data.sql`
> 로컬 환경에서 사용할 더미 데이터를 생성하는 스크립트
   * 현재 로컬 환경은 spring boot application 실행 시, DB의 모든 데이터를 지우고 해당 스크립트를 실행하는 방식
   * 즉, 더미 데이터가 필요하면 꼭 해당 스크립트에 작성해둘 것

3. `.env` 파일을 루트 디렉토리에 추가하기
> `.env` 파일에 프로젝트 설정 파일에서 사용하는 값들을 변수로 관리하고 있습니다.

### 4. 테스트
> 테스트 환경 설정 파일을 분리하였습니다.
   * `test/resources/application-test.yml`: 테스트 환경 설정 파일
     * 테스트 실행 시, test 프로필을 활성화 해주세요.
       ```
       @ActiveProfiles("test")
       ```
     * 테스트 시, 해당 yml 파일이 적용된다.
       * 현재는 `application-local.yml`과 `application-test.yml`이 동일하게 구성되어 있습니다.
       * <span style="color: red"> 혹시나 개발 중에 `application-local.yml` 파일을 수정했다면 수정 내용을 동일하게 test 설정파일에도 반영해주세요 <br> 
          하지 않으면 테스트 실패로 빌드가 되지 않을 수 있습니다. <br>
          @@@@@@@@@@@@@@@@@ ctrl + c + v 금지 @@@@@@@@@@@@@@@@ <br> 
         local과 test 설정파일이 미세하게 설정이 다르므로 반드시 본인 수정 내용만 반영해주세요
       </span>
