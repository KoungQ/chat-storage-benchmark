import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, defaultOptions, randomSenderId } from '../config.js';

export const options = defaultOptions;

export default function () {
  const senderId = randomSenderId();
  const res = http.get(`${BASE_URL}/api/postgresql/messages/search?senderId=${senderId}`, {
    tags: { name: 'postgresql_read_by_condition' },
  });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response code is COMMON200': (r) => JSON.parse(r.body).code === 'COMMON200',
  });

  sleep(0.01);
}
