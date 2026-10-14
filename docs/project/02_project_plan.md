# 2. 프로젝트 기획서

[프로젝트 문서 홈](README.md) · [이전: 플로 차트](01_flowchart.md) · [다음: 요구사항 정의서](03_requirements.md) · [Repository README](../../README.md)

## 1. 프로젝트 개요

**ZipChatGo / 집찾GO**는 지도와 대화로 매물을 찾고 주변시설·시장 정보·법률 근거를 함께 확인하도록 만든 부동산 웹 서비스입니다. 개발 목적은 집을 찾는 과정 전체를 하나의 서비스 흐름으로 연결하는 것입니다. 기획 방향은 [AI Agent 개발 계획](../real_estate_ai_agent_development_plan_final.txt), 현재 구현 기준은 [시스템 아키텍처 설계서](05_system_architecture.md)에 정리했습니다.

## 2. 문제 정의

집을 고를 때 매물, 생활시설, 시장 흐름, 계약 관련 법률 정보가 여러 곳에 흩어져 있습니다. 사용자는 필터와 지도를 반복해서 조작하고, 후보를 비교하며, 앞서 본 매물을 다시 찾아야 합니다. 가격 한 가지로는 입지와 거래 위험을 판단하기 어렵습니다.

## 3. 목표와 핵심 영역

| 영역 | 제공하려는 경험 | 현재 코드의 대표 경로 |
|---|---|---|
| 서비스 소개 | 이용 흐름과 프로젝트 가치 파악 | [홈](../../src/main/resources/templates/index.html), [서비스 소개](../../src/main/resources/templates/support/service.html) |
| 지도 탐색 + AI | 매물·POI 탐색, 자연어 질의, 허용된 지도 Action | [지도](../../src/main/resources/templates/property/map.html), [ai-chat.js](../../src/main/resources/static/js/ai-chat.js), [FastAPI Agent](../../ai-server/app/providers/openai_provider.py) |
| 부동산 분석 | 실거래 요약과 지역 비교, 참고용 비용 계산 | [분석 허브](../../src/main/resources/templates/market/trend.html), [MarketService.java](../../src/main/java/com/onrender/zipchatgo/market/MarketService.java) |
| 매물 등록 + 회원/지원 | 로그인, 등록·심사·내 매물, 문의 | [AuthController.java](../../src/main/java/com/onrender/zipchatgo/member/AuthController.java), [등록 화면](../../src/main/resources/templates/property/register.html), [문의 API](../../src/main/java/com/onrender/zipchatgo/inquiry/InquiryApiController.java) |

## 4. 대상 사용자

- 구매·임차 후보를 지역과 생활 여건으로 비교하려는 사용자
- 자연어로 조건을 설명하고 지도 결과를 확인하려는 사용자
- 자신의 매물을 등록하고 심사 상태를 확인하려는 회원
- 실거래 요약과 계산기를 참고하려는 일반 사용자

## 5. 핵심 가치와 구현 범위

지도 매물·POI·역 검색 결과를 AI Tool의 입력으로 쓰고, AI 응답을 제한된 지도 Action으로 연결합니다. 후속 질문에는 브라우저가 전달한 최근 매물 ID와 App State를 사용합니다. 법률 질문은 사전 구축한 조문 Vector Store를 검색할 수 있습니다. 관심매물은 같은 브라우저 세션에서 관리합니다. 각 기능의 구현 수준은 [요구사항 정의서](03_requirements.md)에서 확인할 수 있습니다.

## 6. 주요 기술

| 영역 | 기술과 역할 |
|---|---|
| Frontend | Thymeleaf 템플릿, HTML/CSS/JavaScript, Naver Maps, 지도 클러스터 |
| Backend | Spring Boot, Spring Security·세션, JDBC 기반 서비스·Repository |
| AI | FastAPI, Pydantic, OpenAI Responses API Tool Calling, 법률 Vector Store 검색 |
| DB·Storage | MySQL 호환 DB 연결, Supabase Storage, 정적 JSON·GeoJSON, 브라우저 `sessionStorage` |
| 외부 API | 국토교통부 실거래가, OAuth 제공자, 지도 SDK, OpenAI |
| 배포 설정 | Docker·Compose, EC2 GitHub Actions, Render 설정 파일 |

근거: [build.gradle](../../build.gradle), [docker-compose.yml](../../docker-compose.yml), [application.properties](../../src/main/resources/application.properties), [FastAPI requirements](../../ai-server/requirements.txt).

## 7. 범위와 한계

- 회원 등록 매물은 `member_property`에 저장됩니다. 지도 매물 `properties`로 승인 후 자동 복사되는 경로는 **현재 코드에서 확인되지 않음**.
- 관심매물 ID는 계정별 DB 저장이 아니라 브라우저 `sessionStorage` 기반입니다. AI 대화 원문도 서버 DB 저장 코드가 확인되지 않았습니다.
- 시장 요약은 국토교통부 API 설정에 따라 실자료 또는 demo이고, 일부 화면의 시계열·AI 리포트는 합성·고정 예시입니다. 실시간 예측 모델 성능으로 소개하지 않습니다.
- 법률 RAG는 근거 검색·인용 제한을 수행하지만 모든 법률 판단의 정확성을 보증하지 않습니다.
- 코드에 배포 설정이 있다는 것과 현재 EC2·Render 서비스가 운영 중이라는 것은 별개의 주장입니다. 실제 운영 환경은 **확인 필요**입니다.

자세한 경계와 근거는 [최종 현황 보고서](../presentation/ZipChatGo_Final_Project_Report.md)를 참고하세요.

---

[프로젝트 문서 홈](README.md) · [이전: 플로 차트](01_flowchart.md) · [다음: 요구사항 정의서](03_requirements.md) · [Repository README](../../README.md)
