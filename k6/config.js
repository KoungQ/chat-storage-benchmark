export const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

/** 전체 조회 — 1,000 requests (10 VUs × 100 iterations) */
export const lightOptions = {
  scenarios: {
    default: {
      executor: 'per-vu-iterations',
      vus: 10,
      iterations: 100,
      startTime: '0s',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<60000'],
    http_req_failed: ['rate<0.01'],
  },
};

/** 기본 (PK 조회, 조건 조회, 페이지 조회, 삭제) — 10,000 requests (100 VUs × 100 iterations) */
export const defaultOptions = {
  scenarios: {
    default: {
      executor: 'per-vu-iterations',
      vus: 100,
      iterations: 100,
      startTime: '0s',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<10000'],
    http_req_failed: ['rate<0.01'],
  },
};

/** 삽입 — 10,000 requests (1 VU × 10,000 iterations, 동시성 최소화) */
export const insertOptions = {
  scenarios: {
    default: {
      executor: 'per-vu-iterations',
      vus: 1,
      iterations: 10000,
      startTime: '0s',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<10000'],
    http_req_failed: ['rate<0.01'],
  },
};

/** 수정 — 10,000 requests (10 VUs × 1,000 iterations) */
export const updateOptions = {
  scenarios: {
    default: {
      executor: 'per-vu-iterations',
      vus: 10,
      iterations: 1000,
      startTime: '0s',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<10000'],
    http_req_failed: ['rate<0.01'],
  },
};

/** 1~100000 범위 랜덤 Long ID 생성 */
export function randomId() {
  return Math.floor(Math.random() * 100000) + 1;
}

/** 1~5000 범위 랜덤 senderId 생성 */
export function randomSenderId() {
  return Math.floor(Math.random() * 5000) + 1;
}

/** 1~1000 범위 랜덤 roomId 생성 */
export function randomRoomId() {
  return Math.floor(Math.random() * 1000) + 1;
}
