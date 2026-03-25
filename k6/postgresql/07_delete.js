import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, defaultOptions, randomId } from '../config.js';

export const options = defaultOptions;

export default function () {
  const id = randomId();
  const res = http.del(`${BASE_URL}/api/postgresql/messages/${id}`, null, {
    tags: { name: 'postgresql_delete' },
  });

  check(res, {
    'status is 200 or 500': (r) => r.status === 200 || r.status === 500,
  });

  sleep(0.01);
}
