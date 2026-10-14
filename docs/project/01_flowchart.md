# 1. 플로 차트

[프로젝트 문서 홈](README.md) · [이전: 문서 홈](README.md) · [다음: 프로젝트 기획서](02_project_plan.md) · [Repository README](../../README.md)

다이어그램은 현재 코드의 화면과 요청 경로를 보여 줍니다. 외부 서비스의 실시간 가용 여부는 별도 확인 대상입니다.

## A. 전체 서비스 사용자 흐름

```mermaid
flowchart LR
  U[사용자] --> H[홈 /]
  H --> L{로그인 또는 게스트}
  L --> M[지도 /property/map]
  M --> C[AI 대화]
  M --> P[매물·POI 확인]
  P --> F[관심매물 /favorites]
  H --> T[분석 /market/trend]
  L --> R[매물 등록 /property/register]
  H --> S[고객센터 /support/contact]
  C --> M
  F --> M
```

홈에서 시작한 질문은 `sessionStorage`를 통해 지도 채팅 입력으로 전달됩니다. 근거: [main.js](../../src/main/resources/static/js/main.js), [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [PageController.java](../../src/main/java/com/onrender/zipchatgo/controller/PageController.java).

## B. 지도 탐색

```mermaid
flowchart LR
  H[홈] --> M[지도]
  M --> Q[검색·필터와 지도 범위]
  Q --> A[Spring 지도 API]
  A --> K[매물 마커·클러스터]
  K --> D[매물 상세]
  D --> F[관심매물 ID 저장]
  F --> V[관심매물 목록]
```

지도 API는 DB 오류 시 정적 JSON으로 전환할 수 있습니다. 관심매물 ID의 저장 위치는 브라우저 `sessionStorage`입니다. 근거: [map.js](../../src/main/resources/static/js/map.js), [MapDataController.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataController.java), [favorite.js](../../src/main/resources/static/js/favorite.js).

## C. AI Agent와 UI Action

```mermaid
flowchart LR
  U[자연어 질문] --> B[Browser ai-chat.js]
  B --> S[Spring /api/ai/chat]
  S --> F[FastAPI /agent/chat]
  F --> O[OpenAI Responses]
  O --> T{허용된 Tool 선택}
  T -->|매물·POI·역| A[Spring 지도 API]
  T -->|법률| V[OpenAI Vector Store]
  T -->|지도 동작| X[검증된 UI Action]
  A --> F
  V --> F
  X --> F
  F --> S
  S --> B
  B --> M[지도 이동·강조·상세]
```

Action은 정해진 종류와 인자만 브라우저가 실행합니다. AI가 임의 JavaScript를 실행하지 않습니다. 근거: [AiService.java](../../src/main/java/com/onrender/zipchatgo/ai/AiService.java), [main.py](../../ai-server/app/main.py), [openai_provider.py](../../ai-server/app/providers/openai_provider.py), [map.js](../../src/main/resources/static/js/map.js).

## D. 후속 대화

```mermaid
flowchart LR
  Q[질문] --> C[history·recentContext·App State]
  C --> A[Agent]
  A --> P[최근 제시 매물 ID·순서]
  P --> N[그중 두 번째 등 후속 질문]
  N --> C
```

`history`와 `recentContext`는 페이지 메모리에서 관리되며 요청마다 다시 전달됩니다. 브라우저 새로고침 뒤 원문 대화를 DB에서 복원하는 경로는 확인되지 않습니다. 근거: [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [schemas.py](../../ai-server/app/schemas.py).

## E. 법률 RAG

```mermaid
flowchart LR
  Q[법률 질문] --> N[법령명·조문 단서 탐지]
  N --> E{정확한 법령·조문 쌍?}
  E -->|예| X[메타데이터 정확 검색]
  E -->|아니요| S[의미 검색]
  X --> R{검색 결과}
  S --> R
  R -->|0건이면| F[필터 없는 의미 검색]
  R -->|결과 있음| A[Agent에 근거 전달]
  F --> A
  A --> G[조문·출처 점검 후 답변]
```

운영 검색과 답변 점검은 [real_estate_law.py](../../ai-server/app/tools/real_estate_law.py), [openai_vector_store_law_retriever.py](../../ai-server/app/retrievers/openai_vector_store_law_retriever.py), [openai_provider.py](../../ai-server/app/providers/openai_provider.py)에 있습니다. 평가 전용 validator는 운영 응답의 별도 hard gate로 확인되지 않습니다.

## F. 매물 등록과 심사

```mermaid
flowchart LR
  L[회원 로그인] --> B[기본정보]
  B --> D[상세정보]
  D --> A[주소·좌표 확인]
  A --> U[사진·서류]
  U --> P[미리보기·등록]
  P --> S[member_property: PENDING]
  S --> R{관리자 심사}
  R -->|승인| Y[APPROVED]
  R -->|반려| N[REJECTED]
  Y --> M[내 매물 관리]
  N --> M
```

**등록·승인은 지도 `properties` 자동 반영을 뜻하지 않습니다.** 두 데이터 경로를 연결하는 자동 동기화 코드는 확인되지 않았습니다. 근거: [register.js](../../src/main/resources/static/js/register.js), [MemberPropertyService.java](../../src/main/java/com/onrender/zipchatgo/property/MemberPropertyService.java), [AdminPropertyController.java](../../src/main/java/com/onrender/zipchatgo/property/AdminPropertyController.java), [MapDataService.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataService.java).

---

[프로젝트 문서 홈](README.md) · [이전: 문서 홈](README.md) · [다음: 프로젝트 기획서](02_project_plan.md) · [Repository README](../../README.md)
