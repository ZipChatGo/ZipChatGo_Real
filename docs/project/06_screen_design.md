# 6. 화면 설계서

[프로젝트 문서 홈](README.md) · [이전: 시스템 아키텍처 설계서](05_system_architecture.md) · [다음: 문서 홈](README.md) · [Repository README](../../README.md)

이 문서는 현재 라우트·템플릿·스크립트 기준입니다. 재사용 가능한 확정 화면 캡처를 확인하지 못해 이미지를 새로 만들거나 다른 자료를 임의로 복사하지 않았습니다. 각 화면의 **화면 이미지: 확인 필요**는 UI 구현 부재가 아니라 검증된 캡처가 아직 없다는 뜻입니다. 외부 SDK·API·DB가 필요한 화면은 운영 환경에서 별도 확인이 필요합니다.

## SCR-001 홈

- **URL / 목적:** `/` / 지도·AI·분석·등록·서비스 소개의 진입점.
- **주요 UI / 사용자 동작:** AI 질문 입력, 추천 질문, 핵심 기능 카드와 상단 메뉴. 질문 시작 시 지도 페이지로 이동합니다.
- **Backend/API:** Spring 페이지 렌더링. 질문 문구는 브라우저 `sessionStorage`로 지도에 전달합니다.
- **관련 파일:** [index.html](../../src/main/resources/templates/index.html), [main.js](../../src/main/resources/static/js/main.js), [PageController.java](../../src/main/java/com/onrender/zipchatgo/controller/PageController.java).
- **화면 이미지:** 확인 필요.

## SCR-002 서비스 소개

- **URL / 목적:** `/about/service` / 제공 기능과 이용 흐름 소개.
- **주요 UI / 사용자 동작:** 소개 카드·서비스 링크 탐색.
- **Backend/API:** Spring 템플릿 렌더링. 별도 AI·DB 조회를 전제로 하지 않습니다.
- **관련 파일:** [service.html](../../src/main/resources/templates/support/service.html), [PageController.java](../../src/main/java/com/onrender/zipchatgo/controller/PageController.java).
- **화면 이미지:** 확인 필요.

## SCR-003 모바일 모델하우스

- **URL / 목적:** `/content/modelhouse` / 모델하우스 형식의 콘텐츠 탐색.
- **주요 UI / 사용자 동작:** 모델하우스 장면과 화면 내 탐색 요소.
- **Backend/API:** Spring 페이지 렌더링. 실제 매물 DB의 3D 실측 모델 제공 여부는 확인되지 않습니다.
- **관련 파일:** [modelhouse.html](../../src/main/resources/templates/support/modelhouse.html), [modelhouse.js](../../src/main/resources/static/js/modelhouse.js).
- **화면 이미지:** 확인 필요.

## SCR-004 중개 LIVE

- **URL / 목적:** `/support/live` / LIVE 형식의 추천 콘텐츠 확인.
- **주요 UI / 사용자 동작:** 추천 콘텐츠 카드와 탐색 동작.
- **Backend/API:** Spring 페이지 렌더링. 실시간 중개 방송·스트리밍 서비스로 검증된 것은 아닙니다.
- **관련 파일:** [live.html](../../src/main/resources/templates/support/live.html), [live.js](../../src/main/resources/static/js/live.js).
- **화면 이미지:** 확인 필요.

## SCR-005 지도 탐색

- **URL / 목적:** `/property/map` / 지도 범위에서 매물 검색·선택.
- **주요 UI / 사용자 동작:** Naver 지도, 검색·필터, 마커·클러스터, 목록·상세 패널. 사용자는 지역을 이동하고 마커를 엽니다.
- **Backend/API:** `GET /api/map/properties`, `GET /api/map/properties/search`, 필요 시 가격 이력 API. DB 실패 시 정적 자료 fallback이 가능합니다.
- **관련 파일:** [map.html](../../src/main/resources/templates/property/map.html), [map.js](../../src/main/resources/static/js/map.js), [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java).
- **화면 이미지:** 확인 필요.

## SCR-006 지도 POI

- **URL / 목적:** `/property/map` 안의 POI 영역 / 주변 시설 확인.
- **주요 UI / 사용자 동작:** 교통·교육·의료 등 시설 종류 선택, POI 표시·상세 확인.
- **Backend/API:** `GET /api/map/pois`, `GET /api/map/pois/search`. 반환 가능한 시설 종류는 DB·대체자료 범위에 따릅니다.
- **관련 파일:** [map.html](../../src/main/resources/templates/property/map.html), [map.js](../../src/main/resources/static/js/map.js), [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java).
- **화면 이미지:** 확인 필요.

## SCR-007 AI Chat·지도 Action

- **URL / 목적:** `/property/map` 안의 AI 채팅 / 자연어로 조회하고 지도 결과를 확인.
- **주요 UI / 사용자 동작:** 질문·답변, 최근 문맥, 검색 결과 강조. 허용된 Action만 지도에서 실행합니다.
- **Backend/API:** `POST /api/ai/chat` → FastAPI `POST /agent/chat` → OpenAI·Tool → Action 응답. 대화 원문은 현재 페이지 메모리에서 관리합니다.
- **관련 파일:** [map.html](../../src/main/resources/templates/property/map.html), [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [AiController.java](../../src/main/java/com/onrender/zipchatgo/ai/AiController.java), [main.py](../../ai-server/app/main.py).
- **화면 이미지:** 확인 필요.

## SCR-008 관심매물

- **URL / 목적:** `/favorites` / 같은 브라우저 세션에서 선택한 매물 비교.
- **주요 UI / 사용자 동작:** 관심매물 카드, 제거, 지도 복귀.
- **Backend/API:** 관심 ID는 `sessionStorage`; 매물 상세는 `GET /api/map/properties/by-ids` 조회. 계정별 DB 영구 저장은 아닙니다.
- **관련 파일:** [favorite.html](../../src/main/resources/templates/favorite/favorite.html), [favorite.js](../../src/main/resources/static/js/favorite.js), [FavoritePageController.java](../../src/main/java/com/onrender/zipchatgo/controller/FavoritePageController.java).
- **화면 이미지:** 확인 필요.

## SCR-009 부동산 분석 허브

- **URL / 목적:** `/market/trend` / 매매·전월세·거래량과 상세 분석의 진입점.
- **주요 UI / 사용자 동작:** 요약 카드, 최근 거래 목록, 하위 화면 링크, 데이터 출처 표시.
- **Backend/API:** `GET /api/market/summary`. 키가 없으면 서버 demo, 조회 실패 시 프런트 `auto-demo`가 표시될 수 있습니다.
- **관련 파일:** [trend.html](../../src/main/resources/templates/market/trend.html), [market-api.js](../../src/main/resources/static/js/market-api.js), [MarketService.java](../../src/main/java/com/onrender/zipchatgo/market/MarketService.java).
- **화면 이미지:** 확인 필요.

## SCR-010 매매 지역 비교

- **URL / 목적:** `/market/sale` / 기준 지역과 비교 지역의 매매 요약을 나란히 보기.
- **주요 UI / 사용자 동작:** 지역·면적·기간 선택, 평균·변화율·거래표·차트 확인.
- **Backend/API:** `GET /api/market/summary`. 그래프의 기간별 선은 현재 요약값에서 생성한 **합성 표시**이며 실측 월별 시계열이 아닙니다.
- **관련 파일:** [sale.html](../../src/main/resources/templates/market/sale.html), [sale.js](../../src/main/resources/static/js/sale.js), [MarketApiController.java](../../src/main/java/com/onrender/zipchatgo/market/MarketApiController.java).
- **화면 이미지:** 확인 필요.

## SCR-011 비용 계산

- **URL / 목적:** `/market/cost` / 거래 전에 필요한 비용을 참고용으로 추정.
- **주요 UI / 사용자 동작:** 가격·대출·주택 수 등 조건 입력, 중개보수·취득 관련 세금·필요 현금 확인.
- **Backend/API:** 브라우저의 고정 계산식. 외부 금융 API 또는 법정 확정 견적이 아닙니다.
- **관련 파일:** [cost.html](../../src/main/resources/templates/market/cost.html), [cost.js](../../src/main/resources/static/js/cost.js).
- **화면 이미지:** 확인 필요.

## SCR-012 매물 등록

- **URL / 목적:** `/property/register` / 회원 매물을 단계적으로 등록.
- **주요 UI / 사용자 동작:** 기본정보 → 상세정보 → 주소·좌표·사진·서류 → 미리보기·제출. Kakao 주소 검색을 사용합니다.
- **Backend/API:** `POST /api/properties` 후 사진·서류 업로드. 신규 매물은 `PENDING`으로 저장됩니다. 승인 후 지도 자동 노출은 확인되지 않습니다.
- **관련 파일:** [register.html](../../src/main/resources/templates/property/register.html), [register.js](../../src/main/resources/static/js/register.js), [PropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/PropertyController.java).
- **화면 이미지:** 확인 필요.

## SCR-013 로그인·회원가입

- **URL / 목적:** `/member/login`, `/member/signup` / 계정 생성과 세션 로그인.
- **주요 UI / 사용자 동작:** 계정 정보 입력, 게스트 진입, OAuth 제공자 선택.
- **Backend/API:** `/api/auth/signup`, `/api/auth/login`, `/api/auth/guest`, OAuth 시작 경로. 외부 callback 운영 설정은 별도 확인이 필요합니다.
- **관련 파일:** [auth.html](../../src/main/resources/templates/member/auth.html), [auth.js](../../src/main/resources/static/js/auth.js), [AuthController.java](../../src/main/java/com/onrender/zipchatgo/member/AuthController.java).
- **화면 이미지:** 확인 필요.

## SCR-014 계정·내 매물

- **URL / 목적:** `/account`, `/my/properties` / 로그인 상태와 등록 매물 관리.
- **주요 UI / 사용자 동작:** 계정 정보, 내 매물 목록·상태·상세, 수정·삭제.
- **Backend/API:** `GET /api/auth/me`, `GET/PUT/DELETE /api/my/properties/{propertyId}`. 수정 후 재심사 상태로 돌아갈 수 있습니다.
- **관련 파일:** [account.html](../../src/main/resources/templates/member/account.html), [my-properties.html](../../src/main/resources/templates/member/my-properties.html), [MyPropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/MyPropertyController.java).
- **화면 이미지:** 확인 필요.

## SCR-015 고객센터

- **URL / 목적:** `/support/contact` / 이용 안내와 문의 접수.
- **주요 UI / 사용자 동작:** FAQ·문의 입력.
- **Backend/API:** `POST /api/inquiries`. 문의 조회·답변·관리자 답변 화면은 현재 코드에서 확인되지 않습니다.
- **관련 파일:** [contact.html](../../src/main/resources/templates/support/contact.html), [contact.js](../../src/main/resources/static/js/contact.js), [InquiryApiController.java](../../src/main/java/com/onrender/zipchatgo/inquiry/InquiryApiController.java).
- **화면 이미지:** 확인 필요.

## 화면 자료와 운영 확인

화면 이미지를 추후 추가할 때는 실제 현재 버전의 브라우저 캡처를 `docs/project/images/`에 저장하고, 촬영한 버전·화면 조건을 함께 기록하는 편이 안전합니다. 이번 문서에는 불확실한 이미지나 placeholder를 넣지 않았습니다. 지도 SDK 허용 도메인, OAuth callback, DB·Supabase·OpenAI 키, 실제 배포 상태는 화면 코드만으로 확인할 수 없습니다.

---

[프로젝트 문서 홈](README.md) · [이전: 시스템 아키텍처 설계서](05_system_architecture.md) · [다음: 문서 홈](README.md) · [Repository README](../../README.md)
