# 4. 프로세스 정의서

[프로젝트 문서 홈](README.md) · [이전: 요구사항 정의서](03_requirements.md) · [다음: 시스템 아키텍처 설계서](05_system_architecture.md) · [Repository README](../../README.md)

아래 절차는 현재 코드의 대표 경로입니다. 성공 조건에는 외부 API·DB·스토리지 설정이 필요할 수 있습니다. 회원 등록 매물과 지도 매물은 서로 다른 데이터 경로입니다.

## PR-001 일반 회원가입·로그인

- **참여자 / 선행 조건:** 사용자, 브라우저, Spring, 회원 DB / 가입 시 입력값, 로그인 시 기존 계정.
- **입력:** 회원 정보 또는 로그인 자격증명.
- **처리 절차:** 1. 인증 화면에서 입력·검증한다. 2. Spring이 가입 또는 로그인 요청을 처리한다. 3. 로그인 성공 시 회원 세션을 사용한다.
- **출력 / 예외:** 가입·로그인 결과와 세션 상태 / 중복·불일치·입력 오류 시 API 오류를 표시한다.
- **관련 API / 코드:** `POST /api/auth/signup`, `POST /api/auth/login`, `POST /api/auth/logout`, `GET /api/auth/check`; [AuthController.java](../../src/main/java/com/onrender/zipchatgo/member/AuthController.java), [auth.js](../../src/main/resources/static/js/auth.js).

## PR-002 OAuth 로그인

- **참여자 / 선행 조건:** 사용자, Spring Security, Google·Naver·Kakao, 회원 DB / 제공자별 자격증명·callback 설정.
- **입력:** 선택한 OAuth 제공자.
- **처리 절차:** 1. 제공자 시작 경로로 이동한다. 2. 제공자 인증 후 사용자 정보를 받는다. 3. 회원 연결 후 `loginMemberId` 세션을 설정한다.
- **출력 / 예외:** 원래 목적지 또는 홈으로 이동 / 제공자 설정·인증 실패 시 로그인 오류 화면으로 이동한다.
- **관련 API / 코드:** `GET /oauth/start/{provider}`, OAuth2 authorization callback; [OAuthStartController.java](../../src/main/java/com/onrender/zipchatgo/member/OAuthStartController.java), [SecurityConfig.java](../../src/main/java/com/onrender/zipchatgo/config/SecurityConfig.java), [OAuth2LoginHandler.java](../../src/main/java/com/onrender/zipchatgo/config/OAuth2LoginHandler.java).

## PR-003 지도 기반 매물 탐색

- **참여자 / 선행 조건:** 사용자, 브라우저, Spring, 지도 SDK, 지도 DB 또는 정적 대체자료 / 지도 페이지 접근.
- **입력:** 지도 중심·범위와 확대 단계.
- **처리 절차:** 1. 지도 화면을 연다. 2. 범위에 맞는 매물 자료를 조회한다. 3. 마커·클러스터를 표시한다. 4. 마커 선택 시 상세를 표시한다.
- **출력 / 예외:** 지도와 매물 카드 / 지도 SDK 또는 API 실패 시 화면 처리·대체자료 상태를 확인해야 한다.
- **관련 API / 코드:** `GET /property/map`, `GET /api/map/properties`, `GET /api/map/properties/search`; [map.js](../../src/main/resources/static/js/map.js), [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java), [MapDataService.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataService.java).

## PR-004 검색·필터

- **참여자 / 선행 조건:** 사용자, 브라우저, Spring / 지도 로드.
- **입력:** 지역, 검색어, 매물·거래 유형, 가격 등 화면 필터.
- **처리 절차:** 1. 사용자가 조건을 바꾼다. 2. 지도 UI가 화면 필터와 API 조회 조건을 적용한다. 3. 결과 마커·목록을 갱신한다.
- **출력 / 예외:** 조건에 맞는 후보 / 결과 0건 또는 잘못된 API 인자는 빈 결과·오류로 처리한다.
- **관련 API / 코드:** `GET /api/map/properties/search`; [map.js](../../src/main/resources/static/js/map.js), [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java). UI 필터 전체가 AI Tool 인자와 같지는 않습니다.

## PR-005 POI 조회

- **참여자 / 선행 조건:** 사용자, 브라우저, Spring, POI DB 또는 정적 대체자료 / 지도 열림.
- **입력:** 시설 종류·지역·범위.
- **처리 절차:** 1. POI 종류를 선택한다. 2. API가 조건에 맞는 시설을 찾는다. 3. 브라우저가 지도에 결과를 표시한다.
- **출력 / 예외:** 주변시설 목록·마커 / 데이터가 없거나 조회 실패하면 빈 결과·대체자료 범위를 확인한다.
- **관련 API / 코드:** `GET /api/map/pois`, `GET /api/map/pois/search`; [map.js](../../src/main/resources/static/js/map.js), [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java).

## PR-006 관심매물

- **참여자 / 선행 조건:** 사용자, 브라우저 / 조회한 매물 ID.
- **입력:** 하트 선택·해제, 목록 열기.
- **처리 절차:** 1. 브라우저가 매물 ID를 `sessionStorage` 목록에 추가·제거한다. 2. 관심매물 화면이 ID로 매물 정보를 조회한다.
- **출력 / 예외:** 목록 카드와 지도 복귀 / 세션 종료·다른 기기에서는 ID가 유지되지 않는다.
- **관련 API / 코드:** `GET /favorites`, `GET /api/map/properties/by-ids`; [favorite.js](../../src/main/resources/static/js/favorite.js), [map.js](../../src/main/resources/static/js/map.js). 회원 DB의 favorite 저장 API로 설명하지 않습니다.

## PR-007 AI 매물 검색

- **참여자 / 선행 조건:** 사용자, 브라우저, Spring, FastAPI, OpenAI, 지도 API / AI 키와 서버 연결.
- **입력:** 자연어 질문, App State, history, recentContext.
- **처리 절차:** 1. Spring이 요청을 FastAPI에 전달한다. 2. Agent가 `search_properties`를 선택한다. 3. FastAPI가 Spring 지도 검색 API를 호출한다. 4. Tool 결과를 모델에 전달해 설명·Action을 만든다.
- **출력 / 예외:** 검색 설명과 매물 ID·Action / OpenAI·Spring·Tool 실패 시 502 등이 발생할 수 있다. 검색 결과가 없으면 없는 상태를 설명한다.
- **관련 API / 코드:** `POST /api/ai/chat` → `POST /agent/chat` → `GET /api/map/properties/search`; [AiService.java](../../src/main/java/com/onrender/zipchatgo/ai/AiService.java), [main.py](../../ai-server/app/main.py), [property_search.py](../../ai-server/app/tools/property_search.py).

## PR-008 AI 지도 Action

- **참여자 / 선행 조건:** FastAPI, 브라우저 지도 / Agent 응답에 허용된 Action 포함.
- **입력:** `MOVE_MAP`, `FIT_BOUNDS`, `HIGHLIGHT_PROPERTIES`, `OPEN_PROPERTY` 등 구조화된 Action.
- **처리 절차:** 1. FastAPI가 Action 구조를 검증한다. 2. 브라우저가 종류·좌표·ID를 다시 확인한다. 3. 정해진 지도 함수만 호출한다.
- **출력 / 예외:** 지도 이동·강조·상세 / 잘못된 Action·ID는 실행하지 않는다.
- **관련 API / 코드:** `POST /agent/chat` 응답; [schemas.py](../../ai-server/app/schemas.py), [openai_provider.py](../../ai-server/app/providers/openai_provider.py), [map.js](../../src/main/resources/static/js/map.js).

## PR-009 Multi-turn

- **참여자 / 선행 조건:** 사용자, 브라우저, FastAPI / 같은 페이지의 선행 대화.
- **입력:** 최근 history, `recentContext`, 지도 App State, 후속 질문.
- **처리 절차:** 1. 브라우저가 제한된 대화와 최근 매물 ID를 보낸다. 2. Agent가 “그중 두 번째” 같은 참조를 현재 문맥에 연결한다. 3. 필요 시 Tool로 확인한다.
- **출력 / 예외:** 문맥을 반영한 답변 / 이전 목록이 없거나 ID가 맞지 않으면 확인을 요청하거나 참조를 거절한다. 새로고침 후 원문 복원은 확인되지 않는다.
- **관련 API / 코드:** `POST /api/ai/chat`; [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [schemas.py](../../ai-server/app/schemas.py), [openai_provider.py](../../ai-server/app/providers/openai_provider.py).

## PR-010 법률 RAG

- **참여자 / 선행 조건:** 사용자, FastAPI, OpenAI Vector Store / Store ID·API 키 설정.
- **입력:** 부동산 법률 질문과 선택된 검색 query·법령 단서.
- **처리 절차:** 1. 법령명·조문번호를 탐지한다. 2. 정확 검색 후 필요하면 의미검색·필터 없는 검색을 수행한다. 3. 반환 근거를 Agent에 넣고 조문·출처를 점검한다.
- **출력 / 예외:** 확인된 근거를 구분한 답변 / 결과 0건·설정 오류에서는 확인하지 못한 조문을 단정하지 않는다. 법률 정확도 보증은 아니다.
- **관련 API / 코드:** `POST /agent/chat`, `search_real_estate_law` Tool; [real_estate_law.py](../../ai-server/app/tools/real_estate_law.py), [openai_vector_store_law_retriever.py](../../ai-server/app/retrievers/openai_vector_store_law_retriever.py).

## PR-011 시장 분석

- **참여자 / 선행 조건:** 사용자, 브라우저, Spring, 국토교통부 API / 분석 페이지.
- **입력:** 지역 코드와 거래월.
- **처리 절차:** 1. 분석 화면이 요약 API를 호출한다. 2. 서버가 설정에 따라 실거래 API 응답을 집계하거나 demo를 반환한다. 3. 화면이 값·출처를 표시한다.
- **출력 / 예외:** 매매·전월세·거래량 요약 / API 오류 시 분석 허브는 `auto-demo`를 표시할 수 있다. 시계열 화면 일부는 합성·고정 예시다.
- **관련 API / 코드:** `GET /market/trend`, `GET /api/market/summary`; [MarketService.java](../../src/main/java/com/onrender/zipchatgo/market/MarketService.java), [market-api.js](../../src/main/resources/static/js/market-api.js).

## PR-012 매물 등록

- **참여자 / 선행 조건:** 로그인 회원, 브라우저, Spring, DB / 로그인 세션과 필수 입력.
- **입력:** 매물·거래 유형, 주소·좌표, 면적·가격, 소유자 정보.
- **처리 절차:** 1. 기본·상세 정보를 입력한다. 2. Kakao 주소 확인과 미리보기를 거친다. 3. 서버에 등록 JSON을 보낸다. 4. `member_property`가 `PENDING`으로 저장된다.
- **출력 / 예외:** 등록 ID·내 매물 이동 / 미로그인·검증·저장 오류는 등록 실패로 처리한다. 등록만으로 지도 노출은 확인되지 않는다.
- **관련 API / 코드:** `GET /property/register`, `POST /api/properties`; [register.js](../../src/main/resources/static/js/register.js), [PropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/PropertyController.java), [MemberPropertyService.java](../../src/main/java/com/onrender/zipchatgo/property/MemberPropertyService.java).

## PR-013 사진·서류 업로드

- **참여자 / 선행 조건:** 등록 회원, Spring, Supabase Storage, DB / 등록 매물 ID와 파일.
- **입력:** 사진·서류 파일과 종류.
- **처리 절차:** 1. 브라우저가 등록 JSON 성공 후 파일을 개별 전송한다. 2. 서버가 파일을 Storage에 올린다. 3. DB에 파일 경로와 메타데이터를 저장한다.
- **출력 / 예외:** 첨부된 파일·제한 시간 URL / 업로드 실패 시 등록 본문과 첨부 상태를 구분해 확인해야 한다.
- **관련 API / 코드:** `POST /api/properties/{propertyId}/photos`, `/documents`; [register.js](../../src/main/resources/static/js/register.js), [SupabaseStorageService.java](../../src/main/java/com/onrender/zipchatgo/property/SupabaseStorageService.java), [PropertyPhotoController.java](../../src/main/java/com/onrender/zipchatgo/property/PropertyPhotoController.java).

## PR-014 내 매물 관리

- **참여자 / 선행 조건:** 로그인 회원, Spring, DB / 본인 등록 매물.
- **입력:** 목록·상세 조회, 수정 또는 삭제.
- **처리 절차:** 1. 본인 매물만 조회한다. 2. 수정 저장 시 상태를 다시 `PENDING`으로 돌린다. 3. 삭제 요청을 처리한다.
- **출력 / 예외:** 변경된 목록·상태 / 소유권 불일치·없는 ID·DB 오류는 API에서 거절한다.
- **관련 API / 코드:** `GET/PUT/DELETE /api/my/properties/{propertyId}`; [MyPropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/MyPropertyController.java), [MemberPropertyService.java](../../src/main/java/com/onrender/zipchatgo/property/MemberPropertyService.java).

## PR-015 관리자 승인·반려

- **참여자 / 선행 조건:** 관리자, Spring, DB / 관리자 권한과 대기 매물.
- **입력:** 매물 ID와 승인·반려 선택.
- **처리 절차:** 1. 관리자가 대기 목록을 본다. 2. API가 권한·대상을 확인한다. 3. `member_property.status`를 `APPROVED` 또는 `REJECTED`로 바꾼다.
- **출력 / 예외:** 상태 변경 결과 / 권한·ID 오류는 거절한다. `properties` 테이블 자동 반영은 확인되지 않는다.
- **관련 API / 코드:** `GET /api/admin/properties`, `POST /api/admin/properties/{id}/approve`, `/reject`; [AdminPropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/AdminPropertyController.java), [AdminInterceptor.java](../../src/main/java/com/onrender/zipchatgo/config/AdminInterceptor.java).

## PR-016 고객센터 문의

- **참여자 / 선행 조건:** 사용자, 브라우저, Spring, DB / 고객센터 화면.
- **입력:** 문의 양식.
- **처리 절차:** 1. 사용자가 문의를 작성한다. 2. 브라우저가 제출한다. 3. Spring이 입력을 처리하고 저장한다.
- **출력 / 예외:** 등록 결과 / 입력·저장 실패를 표시한다. 문의 답변·관리자 답변 화면은 현재 코드에서 확인되지 않는다.
- **관련 API / 코드:** `GET /support/contact`, `POST /api/inquiries`; [contact.js](../../src/main/resources/static/js/contact.js), [InquiryApiController.java](../../src/main/java/com/onrender/zipchatgo/inquiry/InquiryApiController.java), [InquiryService.java](../../src/main/java/com/onrender/zipchatgo/inquiry/InquiryService.java).

---

[프로젝트 문서 홈](README.md) · [이전: 요구사항 정의서](03_requirements.md) · [다음: 시스템 아키텍처 설계서](05_system_architecture.md) · [Repository README](../../README.md)
