import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL, defaultOptions } from '../config.js';

export const options = defaultOptions;

export default function () {
  const page = Math.floor(Math.random() * 1000);
  const size = 20;
  const res = http.get(`${BASE_URL}/api/mongo/messages/page?page=${page}&size=${size}`, {
    tags: { name: 'mongo_read_page' },
  });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response code is COMMON200': (r) => JSON.parse(r.body).code === 'COMMON200',
  });

  sleep(0.01);
}
