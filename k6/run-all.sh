#!/bin/bash
set -e
#
# ┌─────────────────────────────────────────────────────────────┐
# │  PostgreSQL vs MongoDB 벤치마크 실행 스크립트                    │
# │                                                             │
# │  1차 (인덱스 없음):                                          │
# │    ./run-all.sh                                             │
# │                                                             │
# │  2차 (인덱스 추가 후):                                       │
# │    cd ../seed && python3 add-indexes.py                     │
# │    cd ../k6  && ./run-all.sh                                │
# │                                                             │
# │  Grafana에서 시간 범위 선택으로 1차/2차 결과 구분              │
# └─────────────────────────────────────────────────────────────┘

INFLUXDB_URL=${INFLUXDB_URL:-http://localhost:8086}
BASE_URL=${BASE_URL:-http://localhost:8080}
RUNS=${RUNS:-1}

K6_CMD="k6 run --out influxdb=${INFLUXDB_URL}/k6 -e BASE_URL=${BASE_URL}"

run_test() {
  local label=$1
  local script=$2
  echo ""
  echo "======================================================"
  echo "  테스트: ${label}"
  echo "======================================================"
  for i in $(seq 1 $RUNS); do
    echo "--- 회차 ${i}/${RUNS} ---"
    ${K6_CMD} "${script}"
    sleep 3
  done
  echo "[완료] ${label}"
}

echo "======================================================"
echo "  PostgreSQL vs MongoDB 벤치마크 시작"
echo "  InfluxDB: ${INFLUXDB_URL}"
echo "  App:      ${BASE_URL}"
echo "  회차:     ${RUNS}회"
echo "======================================================"

# ── PostgreSQL ──────────────────────────────────────────────────
run_test "PostgreSQL 01 전체 조회"    ./postgresql/01_read_all.js
run_test "PostgreSQL 02 PK 조회"      ./postgresql/02_read_by_id.js
run_test "PostgreSQL 03 조건 조회"    ./postgresql/03_read_by_condition.js
run_test "PostgreSQL 04 페이지 조회"  ./postgresql/04_read_page.js
run_test "PostgreSQL 05 삽입"         ./postgresql/05_create.js
run_test "PostgreSQL 06 수정"         ./postgresql/06_update.js
run_test "PostgreSQL 07 삭제"         ./postgresql/07_delete.js

# ── MongoDB ────────────────────────────────────────────────
run_test "Mongo 01 전체 조회"         ./mongo/01_read_all.js
run_test "Mongo 02 PK 조회"           ./mongo/02_read_by_id.js
run_test "Mongo 03 조건 조회"         ./mongo/03_read_by_condition.js
run_test "Mongo 04 페이지 조회"       ./mongo/04_read_page.js
run_test "Mongo 05 삽입"              ./mongo/05_create.js
run_test "Mongo 06 수정"              ./mongo/06_update.js
run_test "Mongo 07 삭제"              ./mongo/07_delete.js

echo ""
echo "======================================================"
echo "  전체 벤치마크 완료!"
echo "  Grafana 대시보드: http://localhost:3000"
echo "======================================================"
