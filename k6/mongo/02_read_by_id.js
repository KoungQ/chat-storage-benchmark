import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, defaultOptions, randomId } from '../config.js';

export const options = defaultOptions;

export default function () {
  const id = randomId();
  const res = http.get(`${BASE_URL}/api/mongo/messages/${id}`, {
    tags: { name: 'mongo_read_by_id' },
  });

  check(res, {
    'status is 200 or 500': (r) => r.status === 200 || r.status === 500,
    'has result': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.code !== undefined;
      } catch {
        return false;
      }
    },
  });

  sleep(0.01);
}
