import http from 'k6/http';
import { check } from 'k6';
import { BASE_URL, insertOptions, randomRoomId, randomSenderId } from '../config.js';

export const options = insertOptions;

const CONTENTS = [
  '오늘 청소 당번 확인해줘!',
  '퇴근 몇 시야?',
  '냉장고 음식 좀 정리해줘',
  '에어컨 온도 낮춰도 될까요?',
  '오늘 저녁 같이 먹을 사람?',
  '택배 왔는데 어디 뒀어요?',
  '화장실 휴지 없어요',
  '설거지 좀 부탁해요',
  '내일 몇 시에 일어나요?',
  '소음 좀 줄여줄 수 있어요?',
];

export default function () {
  const payload = JSON.stringify({
    roomId: randomRoomId(),
    senderId: randomSenderId(),
    content: CONTENTS[Math.floor(Math.random() * CONTENTS.length)],
  });

  const res = http.post(`${BASE_URL}/api/postgresql/messages`, payload, {
    headers: { 'Content-Type': 'application/json' },
    tags: { name: 'postgresql_create' },
  });

  check(res, {
    'status is 201': (r) => r.status === 201,
  });
}
