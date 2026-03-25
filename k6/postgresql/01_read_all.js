import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, lightOptions } from '../config.js';

export const options = lightOptions;

export default function () {
  const res = http.get(`${BASE_URL}/api/postgresql/messages`, {
    tags: { name: 'postgresql_read_all' },
  });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response code is COMMON200': (r) => JSON.parse(r.body).code === 'COMMON200',
  });

  sleep(0.1);
}
