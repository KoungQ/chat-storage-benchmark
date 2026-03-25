#!/usr/bin/env python3
"""
PostgreSQL(RDB) + MongoDB 채팅 메시지 시더
"""

import argparse
import random
import sys
import time
from datetime import datetime, timedelta

try:
    import psycopg2
    from faker import Faker
    from pymongo import MongoClient
except ImportError as e:
    print(f"[ERROR] 필수 패키지가 없습니다: {e}")
    print("실행: pip install -r requirements.txt")
    sys.exit(1)

fake = Faker("ko_KR")

CHAT_CONTENTS = [
    "오늘 청소 당번 누구예요?",
    "퇴근 몇 시에 해요?",
    "냉장고 음식 좀 정리해 주세요",
    "에어컨 온도 조금 낮춰도 될까요?",
    "오늘 저녁 같이 먹을 사람 있어요?",
    "택배 왔는데 어디에 뒀어요?",
    "화장실 휴지 다 떨어졌어요",
    "설거지 좀 부탁드려요",
    "내일 몇 시에 일어나세요?",
    "소음 좀 줄여줄 수 있어요?",
]


def generate_messages(count: int) -> list[dict]:
    print(f"데이터 {count:,}건 생성 중...")
    base_time = datetime.now() - timedelta(days=365)
    messages = []

    for i in range(1, count + 1):
        messages.append(
            {
                "id": i,
                "room_id": random.randint(1, 1000),
                "sender_id": random.randint(1, 5000),
                "content": random.choice(CHAT_CONTENTS),
                "created_at": base_time + timedelta(seconds=i * 3),
                "is_read": random.choice([True, False]),
            }
        )
        if i % 10000 == 0:
            print(f"  생성: {i:,}/{count:,}")

    return messages


def connect_postgres(host: str, port: int, user: str, password: str, database: str):
    return psycopg2.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        dbname=database,
    )


def seed_postgresql(
    messages: list[dict],
    host: str,
    port: int,
    user: str,
    password: str,
    database: str,
) -> None:
    print(f"\n[PostgreSQL:RDB] {host}:{port}/{database} 연결 중...")
    conn = connect_postgres(host, port, user, password, database)
    conn.autocommit = False
    cursor = conn.cursor()

    cursor.execute(
        """
        CREATE TABLE IF NOT EXISTS chat_message (
            id BIGSERIAL PRIMARY KEY,
            room_id BIGINT NOT NULL,
            sender_id BIGINT NOT NULL,
            content VARCHAR(500) NOT NULL,
            created_at TIMESTAMP NOT NULL,
            is_read BOOLEAN NOT NULL DEFAULT FALSE
        )
        """
    )
    cursor.execute("TRUNCATE TABLE chat_message RESTART IDENTITY")
    conn.commit()

    sql = """
        INSERT INTO chat_message (id, room_id, sender_id, content, created_at, is_read)
        VALUES (%s, %s, %s, %s, %s, %s)
    """
    batch_size = 1000
    total = len(messages)
    start = time.time()

    for i in range(0, total, batch_size):
        batch = messages[i : i + batch_size]
        values = [
            (m["id"], m["room_id"], m["sender_id"], m["content"], m["created_at"], m["is_read"])
            for m in batch
        ]
        cursor.executemany(sql, values)
        conn.commit()
        done = min(i + batch_size, total)
        print(f"  [PostgreSQL:RDB] {done:,}/{total:,} ({done/total*100:.1f}%)")

    cursor.execute(
        "SELECT setval(pg_get_serial_sequence('chat_message', 'id'), COALESCE(MAX(id), 1), true) FROM chat_message"
    )
    conn.commit()
    cursor.close()
    conn.close()
    print(f"[PostgreSQL:RDB] 완료: {total:,}건 삽입 ({time.time()-start:.1f}s)")

def seed_mongodb(
    messages: list[dict],
    host: str,
    port: int,
    database: str,
) -> None:
    print(f"\n[MongoDB] {host}:{port}/{database} 연결 중...")
    client = MongoClient(host=host, port=port, serverSelectionTimeoutMS=5000)
    collection = client[database]["chat_messages"]

    collection.drop()

    batch_size = 1000
    total = len(messages)
    start = time.time()
    for i in range(0, total, batch_size):
        batch = messages[i : i + batch_size]
        docs = [
            {
                "_id": m["id"],
                "roomId": m["room_id"],
                "senderId": m["sender_id"],
                "content": m["content"],
                "createdAt": m["created_at"],
                "isRead": m["is_read"],
            }
            for m in batch
        ]
        collection.insert_many(docs, ordered=False)
        done = min(i + batch_size, total)
        print(f"  [MongoDB] {done:,}/{total:,} ({done/total*100:.1f}%)")

    client.close()
    print(f"[MongoDB] 완료: {total:,}건 삽입 ({time.time()-start:.1f}s)")


def main() -> None:
    parser = argparse.ArgumentParser(description="채팅 스토리지 벤치마크 시더")
    parser.add_argument("--count", type=int, default=100_000)
    parser.add_argument("--postgres-host", default="localhost")
    parser.add_argument("--postgres-port", type=int, default=55432)
    parser.add_argument("--postgres-user", default="postgres")
    parser.add_argument("--postgres-password", default="password")
    parser.add_argument("--postgres-db", default="benchmark")
    parser.add_argument("--mongo-host", default="localhost")
    parser.add_argument("--mongo-port", type=int, default=37017)
    parser.add_argument("--mongo-db", default="benchmark")
    parser.add_argument("--skip-postgresql", action="store_true")
    parser.add_argument("--skip-mongo", action="store_true")
    args = parser.parse_args()

    total_start = time.time()
    messages = generate_messages(args.count)

    if not args.skip_postgresql:
        seed_postgresql(
            messages,
            args.postgres_host,
            args.postgres_port,
            args.postgres_user,
            args.postgres_password,
            args.postgres_db,
        )

    if not args.skip_mongo:
        seed_mongodb(messages, args.mongo_host, args.mongo_port, args.mongo_db)

    print("\n========================================")
    print("  시딩 완료!")
    print(f"  총 소요 시간: {time.time()-total_start:.1f}s")
    print("========================================")


if __name__ == "__main__":
    main()
