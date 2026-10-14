# 3. 요구사항 정의서

[프로젝트 문서 홈](README.md) · [이전: 프로젝트 기획서](02_project_plan.md) · [다음: 프로세스 정의서](04_process_definition.md) · [Repository README](../../README.md)

이 표의 **완료**는 코드 경로와 화면이 있다는 뜻입니다. 외부 키·DB·배포 상태까지 검증했다는 뜻은 아닙니다. **부분 구현**은 화면과 API의 범위가 다르거나 저장·자료 출처에 제한이 있다는 뜻입니다. 우선순위는 문서 독자를 위한 서비스 중요도입니다.

## 기능 요구사항

| ID | 구분 | 요구사항 | 우선순위 | 구현 상태 | 관련 기능·파일 |
|---|---|---|---|---|---|
| FR-001 | A. 서비스 소개 | 홈에서 핵심 기능과 서비스 소개로 이동 | 높음 | 완료 | [index.html](../../src/main/resources/templates/index.html), [PageController.java](../../src/main/java/com/onrender/zipchatgo/controller/PageController.java) |
| FR-002 | B. 회원 | 일반 회원가입·로그인·로그아웃과 세션 확인 | 높음 | 구현됨 / 운영 검증 필요 | [AuthController.java](../../src/main/java/com/onrender/zipchatgo/member/AuthController.java), [auth.js](../../src/main/resources/static/js/auth.js) |
| FR-003 | B. OAuth | Google·Naver·Kakao 로그인 시작과 성공 세션 연결 | 중간 | 구현됨 / 외부 설정 확인 필요 | [SecurityConfig.java](../../src/main/java/com/onrender/zipchatgo/config/SecurityConfig.java), [OAuth2LoginHandler.java](../../src/main/java/com/onrender/zipchatgo/config/OAuth2LoginHandler.java) |
| FR-004 | C. 지도 | 지도 이동·확대와 매물 마커·클러스터 표시 | 높음 | 구현됨 / 지도 SDK 확인 필요 | [map.html](../../src/main/resources/templates/property/map.html), [map.js](../../src/main/resources/static/js/map.js) |
| FR-005 | D. 매물 검색 | 지도 범위·법정동·가격·유형 조건으로 매물 조회 | 높음 | 구현됨 / DB 출처 확인 필요 | [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java), [MapDataService.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataService.java) |
| FR-006 | E. 필터 | 지도 UI의 검색·유형·가격 등 필터 적용 | 높음 | 완료. AI Tool 인자와 범위 다름 | [map.js](../../src/main/resources/static/js/map.js), [schemas.py](../../ai-server/app/schemas.py) |
| FR-007 | F. POI | 지도에서 주변시설 조회·표시 | 높음 | 구현됨 / DB·대체자료 확인 필요 | [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java), [poi_search.py](../../ai-server/app/tools/poi_search.py) |
| FR-008 | G. 관심매물 | 하트 선택·해제와 목록 보기 | 높음 | 완료. 브라우저 세션 저장 | [favorite.js](../../src/main/resources/static/js/favorite.js), [map.js](../../src/main/resources/static/js/map.js) |
| FR-009 | H. AI Chat | 지도 채팅에서 질문하고 응답 표시 | 높음 | 구현됨 / OpenAI 연결 확인 필요 | [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [AiController.java](../../src/main/java/com/onrender/zipchatgo/ai/AiController.java), [main.py](../../ai-server/app/main.py) |
| FR-010 | I. AI Tool | 허용된 매물·POI·역·법률 Tool 호출 | 높음 | 구현됨 / 결과는 외부 상태 의존 | [openai_provider.py](../../ai-server/app/providers/openai_provider.py), [tools/](../../ai-server/app/tools) |
| FR-011 | J. 지도 UI Action | AI가 반환한 허용 Action을 검증·실행 | 높음 | 완료 | [schemas.py](../../ai-server/app/schemas.py), [map.js](../../src/main/resources/static/js/map.js) |
| FR-012 | K. Multi-turn | 최근 질문·매물 ID·App State로 후속 질문 처리 | 높음 | 부분 구현. 페이지 메모리 기반 | [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [openai_provider.py](../../ai-server/app/providers/openai_provider.py) |
| FR-013 | L. 법률 RAG | 법령·조문 검색 결과를 활용해 근거 기반 답변 | 높음 | 구현됨 / Store 내용·정확성 확인 필요 | [real_estate_law.py](../../ai-server/app/tools/real_estate_law.py), [openai_vector_store_law_retriever.py](../../ai-server/app/retrievers/openai_vector_store_law_retriever.py) |
| FR-014 | M. 시장 분석 | 매매·전월세·거래량 요약과 지역 비교 화면 | 중간 | 부분 구현. 실자료·demo·합성 표시 혼재 | [MarketService.java](../../src/main/java/com/onrender/zipchatgo/market/MarketService.java), [market-api.js](../../src/main/resources/static/js/market-api.js), [sale.js](../../src/main/resources/static/js/sale.js) |
| FR-015 | N. 비용 계산 | 거래 조건 입력 후 중개보수·세금·현금 추정 | 중간 | 완료. 참고용 고정 계산식 | [cost.js](../../src/main/resources/static/js/cost.js), [cost.html](../../src/main/resources/templates/market/cost.html) |
| FR-016 | O. 매물 등록 | 로그인 회원의 다단계 입력·미리보기·서버 등록 | 높음 | 구현됨 / 저장 환경 확인 필요 | [register.js](../../src/main/resources/static/js/register.js), [PropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/PropertyController.java) |
| FR-017 | P. 사진·서류 | 등록 매물의 파일 업로드와 경로 저장 | 높음 | 구현됨 / Supabase 설정 확인 필요 | [PropertyPhotoController.java](../../src/main/java/com/onrender/zipchatgo/property/PropertyPhotoController.java), [PropertyDocumentController.java](../../src/main/java/com/onrender/zipchatgo/property/PropertyDocumentController.java) |
| FR-018 | Q. 내 매물 | 회원이 자신이 등록한 매물 조회·수정·삭제 | 중간 | 구현됨 / DB 확인 필요 | [MyPropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/MyPropertyController.java), [my-properties.js](../../src/main/resources/static/js/my-properties.js) |
| FR-019 | R. 관리자 심사 | 대기 매물을 승인·반려하고 상태 조회 | 중간 | 구현됨 / 관리자 권한 확인 필요 | [AdminPropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/AdminPropertyController.java), [AdminInterceptor.java](../../src/main/java/com/onrender/zipchatgo/config/AdminInterceptor.java) |
| FR-020 | S. 고객센터 | 고객센터 화면·문의 등록 | 중간 | 부분 구현. 문의 답변·관리 UI 확인되지 않음 | [contact.html](../../src/main/resources/templates/support/contact.html), [InquiryApiController.java](../../src/main/java/com/onrender/zipchatgo/inquiry/InquiryApiController.java) |
| FR-021 | 콘텐츠 | 서비스 소개·모델하우스·LIVE 화면 | 낮음 | 화면·콘텐츠 구현 | [PageController.java](../../src/main/java/com/onrender/zipchatgo/controller/PageController.java), [modelhouse.html](../../src/main/resources/templates/support/modelhouse.html), [live.html](../../src/main/resources/templates/support/live.html) |
| FR-022 | 지도 노출 | 승인된 회원 매물을 지도 검색에 자동 노출 | 중간 | **현재 코드에서 확인되지 않음** | [MemberPropertyService.java](../../src/main/java/com/onrender/zipchatgo/property/MemberPropertyService.java), [MapDataService.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataService.java) |

## 비기능 요구사항

| ID | 구분 | 요구사항 | 우선순위 | 구현 상태 | 관련 기능·파일 |
|---|---|---|---|---|---|
| NFR-001 | 성능 | 지도 범위 조회와 클러스터로 표시량 관리 | 높음 | 구현됨 / 운영 부하 측정 필요 | [map.js](../../src/main/resources/static/js/map.js), [MapDataService.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataService.java) |
| NFR-002 | 보안 | 로그인 세션·관리자 경로 접근 제한 | 높음 | 구현됨 / 실환경 점검 필요 | [AuthInterceptor.java](../../src/main/java/com/onrender/zipchatgo/config/AuthInterceptor.java), [AdminInterceptor.java](../../src/main/java/com/onrender/zipchatgo/config/AdminInterceptor.java) |
| NFR-003 | Secret | API 키와 DB 비밀번호를 환경변수로 주입 | 높음 | 구현됨 / 배포 설정 확인 필요 | [application.properties](../../src/main/resources/application.properties), [config.py](../../ai-server/app/config.py), [docker-compose.yml](../../docker-compose.yml) |
| NFR-004 | 입력 검증 | API 인자·AI Action·등록 폼 검증 | 높음 | 부분 구현. 모든 현실 조건의 검증 보증은 아님 | [schemas.py](../../ai-server/app/schemas.py), [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java), [register.js](../../src/main/resources/static/js/register.js) |
| NFR-005 | 오류 처리 | 외부 요청 실패를 사용자 응답과 구분 | 높음 | 부분 구현. FastAPI는 일부 실패를 502로 반환 | [main.py](../../ai-server/app/main.py), [AiService.java](../../src/main/java/com/onrender/zipchatgo/ai/AiService.java) |
| NFR-006 | Fallback | 지도 DB 오류와 시장 API 실패 시 대체 자료 표시 | 중간 | 구현됨. 대체 자료의 출처 표시 필요 | [MapDataService.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataService.java), [market-api.js](../../src/main/resources/static/js/market-api.js) |
| NFR-007 | 확장성 | AI 호출을 Provider 파일로 분리하고 데이터 Tool은 Spring API 재사용 | 중간 | 구현됨. 다중 Provider 구현은 없음 | [llm_provider.py](../../ai-server/app/providers/llm_provider.py), [property_search.py](../../ai-server/app/tools/property_search.py) |
| NFR-008 | 유지보수 | Java 서비스·Python AI·브라우저 Action 책임 구분 | 중간 | 구현됨 | [AiService.java](../../src/main/java/com/onrender/zipchatgo/ai/AiService.java), [openai_provider.py](../../ai-server/app/providers/openai_provider.py) |
| NFR-009 | UX | 반응형 화면과 대화·등록 진행 상태 제공 | 중간 | 화면 구현 / 기기별 검증 필요 | [map.html](../../src/main/resources/templates/property/map.html), [register.html](../../src/main/resources/templates/property/register.html) |
| NFR-010 | 브라우저 성능 | 최근 대화·매물 문맥 길이 제한 | 중간 | 구현됨 | [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [schemas.py](../../ai-server/app/schemas.py) |
| NFR-011 | 저장 정책 | 관심매물·대화·등록 매물의 보관 위치 명시 | 높음 | 문서화. 기능별 저장소가 다름 | [favorite.js](../../src/main/resources/static/js/favorite.js), [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [MemberProperty.java](../../src/main/java/com/onrender/zipchatgo/property/MemberProperty.java) |

배포 실행 여부와 외부 데이터 품질은 코드 존재만으로 확인할 수 없습니다. [시스템 아키텍처 설계서](05_system_architecture.md)의 저장소·운영 경계를 함께 참고하세요.

---

[프로젝트 문서 홈](README.md) · [이전: 프로젝트 기획서](02_project_plan.md) · [다음: 프로세스 정의서](04_process_definition.md) · [Repository README](../../README.md)
