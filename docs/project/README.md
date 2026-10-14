# ZipChatGo 프로젝트 문서

[프로젝트 문서 홈](README.md) · [Repository README](../../README.md) · [다음: 플로 차트](01_flowchart.md)

집찾GO는 지도 탐색, AI 대화, 부동산 시장 정보, 회원 매물 등록을 하나의 웹 서비스로 연결합니다. 이 문서들은 **현재 저장소 코드**를 기준으로 작성했습니다. 화면이나 API가 구현되어 있어도 외부 키·DB·배포 환경에서 실제로 작동하는지는 별도로 확인해야 합니다. 초기 데모 설명과 차이가 있으면 이 문서와 연결된 코드 경로를 우선 확인하세요.

## 문서 순서

| 순서 | 문서 | 핵심 질문 |
|---|---|---|
| 1 | [플로 차트](01_flowchart.md) | 사용자는 어떤 순서로 기능을 쓰는가? |
| 2 | [프로젝트 기획서](02_project_plan.md) | 어떤 문제를 해결하려는가? |
| 3 | [요구사항 정의서](03_requirements.md) | 어떤 기능이 어느 수준까지 구현됐는가? |
| 4 | [프로세스 정의서](04_process_definition.md) | 각 기능은 어떤 입력과 예외를 처리하는가? |
| 5 | [시스템 아키텍처 설계서](05_system_architecture.md) | 데이터와 요청은 어디로 흐르는가? |
| 6 | [화면 설계서](06_screen_design.md) | 현재 화면은 무엇을 보여 주는가? |

## 읽기 전에 알아둘 경계

- 관심매물 ID는 회원 DB가 아니라 브라우저 `sessionStorage`에 저장됩니다. [근거: favorite.js](../../src/main/resources/static/js/favorite.js).
- AI 대화 원문은 현재 페이지의 메모리에서 최근 메시지로 전달됩니다. 서버 DB에 대화 원문을 저장하는 코드는 확인되지 않았습니다. [근거: ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [openai_provider.py](../../ai-server/app/providers/openai_provider.py).
- 회원이 등록한 `member_property`와 지도 검색용 `properties`는 별도 경로입니다. 관리자 승인 후 지도 자동 노출은 현재 코드에서 확인되지 않습니다. [근거: MemberPropertyService.java](../../src/main/java/com/onrender/zipchatgo/property/MemberPropertyService.java), [MapDataService.java](../../src/main/java/com/onrender/zipchatgo/map/MapDataService.java).
- 분석 화면에는 국토교통부 API 기반 요약과 데모·합성 시계열·고정 예시가 함께 있습니다. [근거: MarketService.java](../../src/main/java/com/onrender/zipchatgo/market/MarketService.java), [market-api.js](../../src/main/resources/static/js/market-api.js).
- Docker·EC2·Render 설정은 저장소에 있지만 현재 운영 중인 서버 상태까지 이 문서가 보증하지는 않습니다.

기존의 상세 현황 기록은 [최종 프로젝트 현황 보고서](../presentation/ZipChatGo_Final_Project_Report.md), AI 개발 방향은 [AI Agent 개발 계획](../real_estate_ai_agent_development_plan_final.txt)에 있습니다. 보고서의 평가 수치는 해당 실행 시점의 기록입니다.

---

[프로젝트 문서 홈](README.md) · [Repository README](../../README.md) · [이전: Repository README](../../README.md) · [다음: 플로 차트](01_flowchart.md)
