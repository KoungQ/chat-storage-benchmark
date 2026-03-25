import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, updateOptions, randomId } from '../config.js';

export const options = updateOptions;

export default function () {
  const id = randomId();
  const payload = JSON.stringify({
    content: `수정된 메시지 - ${Date.now()}`,
    isRead: Math.random() > 0.5,
  });

  const res = http.put(`${BASE_URL}/api/postgresql/messages/${id}`, payload, {
    headers: { 'Content-Type': 'application/json' },
    tags: { name: 'postgresql_update' },
  });

  check(res, {
    'status is 200 or 500': (r) => r.status === 200 || r.status === 500,
  });

  sleep(0.01);
}
