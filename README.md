# MySQL vs MongoDB — Chat Benchmark

> DAU ~1,000 규모의 실시간 채팅 서비스에서 MySQL과 MongoDB 중 어느 DB가 적합한지 판단하기 위한 성능 벤치마크

![k6](https://img.shields.io/badge/k6-7D64FF?style=flat&logo=k6&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8.0-4479A1?style=flat&logo=mysql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB_7.0-47A248?style=flat&logo=mongodb&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=flat&logo=grafana&logoColor=white)
![InfluxDB](https://img.shields.io/badge/InfluxDB_1.8-22ADF6?style=flat&logo=influxdb&logoColor=white)

---

## 배경

채팅 기능 도입을 앞두고 **"MongoDB가 채팅에 좋다고 하던데, DAU 1,000 수준에서도 의미 있는 차이가 날까?"** 라는 질문에 정량적으로 답하기 위해 진행했다.

기존 블로그 벤치마크(users 테이블 + JMeter)와의 차이:
- 실제 채팅 쿼리 패턴 반영: 페이지 조회, 읽음 처리, 발신자별 검색
- k6 + Grafana 실시간 대시보드 (자동 프로비저닝, 수동 설정 불필요)
- Python faker로 재현 가능한 10만 건 시드 데이터

---

## 핵심 결과

**테스트 데이터**: 채팅 메시지 10만 건 / 각 시나리오 5회 평균 / 인덱스 없음(1차) · 인덱스 추가(2차) 두 차례 측정

### 1차 — 인덱스 없음

| 시나리오 | MySQL avg | MongoDB avg | 승자 |
|---------|-----------|-------------|------|
| 전체 조회 | 1,440 ms | 1,890 ms | MySQL 1.3x |
| PK 조회 | 5.42 ms | 4.69 ms | MongoDB 1.2x |
| 조건 조회 (Full Scan) | 363 ms | 524 ms | MySQL 1.4x |
| **페이지 조회** ← 채팅 핵심 | **195 ms** | **604 ms** | **MySQL 3.1x** |
| 삽입 | 1.46 ms | 0.678 ms | MongoDB 2.2x |
| 수정 | 3.03 ms | 2.49 ms | MongoDB 1.2x |
| 삭제 | 23.7 ms | 3.99 ms | MongoDB 5.9x |

### 2차 — 인덱스 추가 후

> 추가 인덱스: MySQL `idx_sender_id`, `idx_room_id`, `idx_created_at` / MongoDB `senderId_1`, `roomId_1`, `createdAt_-1`

| 시나리오 | MySQL avg | MongoDB avg | 승자 | 1차 대비 변화 |
|---------|-----------|-------------|------|------------|
| 전체 조회 | 1,730 ms | 1,920 ms | MySQL 1.1x | 유사 |
| PK 조회 | 6.49 ms | 3.82 ms | MongoDB 1.7x | 유사 |
| 조건 조회 (인덱스 사용) | 9.13 ms | 7.02 ms | MongoDB 1.3x | **양쪽 -97%+** |
| **페이지 조회** ← 채팅 핵심 | **226 ms** | **658 ms** | **MySQL 2.9x** | **우위 유지** |
| 삽입 | 1.52 ms | 0.731 ms | MongoDB 2.1x | 유사 |
| 수정 | 3.21 ms | 2.50 ms | MongoDB 1.3x | 유사 |
| 삭제 | 28.6 ms | 6.95 ms | MongoDB 4.1x | 양쪽 소폭 악화 |

### 전체 지표 비교

| 지표 | MySQL 1차 | MySQL 2차 | MongoDB 1차 | MongoDB 2차 |
|------|----------|----------|------------|------------|
| 전체 평균 응답 시간 | **111 ms** | **73.3 ms** | 222 ms | 142 ms |
| 평균 RPS | **102 req/s** | 57.0 req/s | 40.7 req/s | **83.8 req/s** |
| P95 평균 | **975 ms** | **1,850 ms** | 1,800 ms | 2,050 ms |
| P99 평균 | **1,100 ms** | **1,990 ms** | 1,960 ms | 2,200 ms |

![응답 시간 비교 1차](report/visualization/round1/응답%20시간%20비교.png)
![응답 시간 비교 2차](report/visualization/round2/응답%20시간%20비교.png)

---

## 결론

**MySQL 단독 유지** — 1차·2차 모두 동일한 결론

채팅의 핵심 쿼리(페이지 조회)에서 MySQL이 인덱스 유무와 관계없이 일관되게 앞선다 (3.1x → 2.9x).
인덱스 추가로 조건 조회 격차는 사라졌으나, 페이지 조회 우위는 인덱스로 해결되지 않는다.
MongoDB 도입 시 이중 DB 운영 복잡도(백업·모니터링·마이그레이션)를 정당화할 성능 이득이 없다.

> DAU 10,000 이상 도달하거나 채팅 메시지 스키마가 다형적으로 변경될 시점에 재검토한다.

---

## 테스트 환경

| 항목 | 사양 |
|------|------|
| OS | macOS 26.3.1 |
| CPU | 8 cores |
| RAM | 16 GB |
| 구동 방식 | Docker Compose 단일 머신 |
| MySQL | 8.0 (innodb_buffer_pool_size 기본값 128MB) |
| MongoDB | 7.0 (WiredTiger cache 기본값 ~7.5GB) |

> 모든 컴포넌트를 단일 머신에서 동시 실행했으므로 절대 수치보다 **MySQL:MongoDB 상대 비율**에 의미가 있다.

---

## How to Run

```bash
# 1. 전체 스택 기동
docker compose up -d

# 2. 앱 기동 확인 (약 60초 소요)
docker compose logs -f app

# 3. 데이터 시딩 (앱 기동 완료 후)
cd seed
python3 -m pip install -r requirements.txt
python3 seed.py

# 4. k6 테스트 실행
cd ../k6
./run-all.sh        # 전체 14개 시나리오 × 5회

# 5. 결과 확인
# Grafana: http://localhost:3000  (admin / admin)
# 대시보드: Benchmark > MySQL vs MongoDB Benchmark
```

---

## 프로젝트 구조

```
.
├── docker-compose.yml        — MySQL, MongoDB, Spring Boot, InfluxDB, Grafana
├── seed/
│   ├── seed.py               — 10만 건 시드 데이터 생성 (Python faker)
│   └── requirements.txt
├── k6/
│   ├── config.js             — 공통 옵션 (VUs, iterations)
│   ├── mysql/                — MySQL 시나리오 7개
│   ├── mongo/                — MongoDB 시나리오 7개
│   └── run-all.sh            — 전체 시나리오 5회 순차 실행
├── grafana/
│   └── provisioning/         — Grafana 자동 설정 (datasource + dashboard)
├── spring-app/               — Spring Boot 3.5.3 벤치마크 앱
└── report/
    ├── db-benchmark-report.md  — 상세 보고서 (배경 · 설계 · 분석 · 결론)
    └── visualization/          — Grafana 스크린샷
```

---

## 상세 보고서

배경·가설·시나리오 설계·상세 분석·결론 전문 → [report/db-benchmark-report.md](report/db-benchmark-report.md)
