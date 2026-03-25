#!/usr/bin/env python3
"""
MySQL & MongoDB 채팅 메시지 시더
채팅 서비스와 유사한 형태의 데이터 10만 건을 생성해 두 DB에 동일하게 삽입한다.

Usage:
    pip install -r requirements.txt
    python seed.py [OPTIONS]

Options:
    --count         삽입할 데이터 건수 (default: 100000)
    --mysql-host    MySQL 호스트 (default: localhost)
    --mysql-port    MySQL 포트 (default: 3306)
    --mysql-user    MySQL 사용자 (default: root)
    --mysql-password MySQL 비밀번호 (default: password)
    --mysql-db      MySQL 데이터베이스명 (default: benchmark)
    --mongo-host    MongoDB 호스트 (default: localhost)
    --mongo-port    MongoDB 포트 (default: 27017)
    --mongo-db      MongoDB 데이터베이스명 (default: benchmark)
    --skip-mysql    MySQL 시딩 건너뜀
    --skip-mongo    MongoDB 시딩 건너뜀
"""

import argparse
import random
import sys
import time
from datetime import datetime, timedelta

try:
    from faker import Faker
    import mysql.connector
    from pymongo import MongoClient
except ImportError as e:
    print(f"[ERROR] 필수 패키지가 없습니다: {e}")
    print("실행: pip install -r requirements.txt")
    sys.exit(1)

fake = Faker('ko_KR')

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
    "공과금 정산 언제 해요?",
    "쓰레기 버리는 날 언제예요?",
    "분리수거 어떻게 해요?",
    "잠깐 얘기 좀 할 수 있어요?",
    "오늘 좀 늦게 들어올 것 같아요",
    "현관 도어락 비밀번호 바꿀까요?",
    "인터넷이 느려요, 공유기 확인해봤어요?",
    "가스 잠갔어요?",
    "창문 잠갔죠?",
    "밥은 먹었어요?",
]


def generate_messages(count: int) -> list[dict]:
    """채팅 메시지 데이터 생성"""
    print(f"데이터 {count:,}건 생성 중...")
    base_time = datetime.now() - timedelta(days=365)
    messages = []

    for i in range(1, count + 1):
        messages.append({
            "id": i,
            "room_id": random.randint(1, 1000),
            "sender_id": random.randint(1, 5000),
            "content": random.choice(CHAT_CONTENTS),
            "created_at": base_time + timedelta(seconds=i * 3),
            "is_read": random.choice([True, False]),
        })

        if i % 10000 == 0:
            print(f"  생성: {i:,}/{count:,}")

    return messages


def seed_mysql(
    messages: list[dict],
    host: str,
    port: int,
    user: str,
    password: str,
    database: str,
) -> None:
    """MySQL에 배치 삽입 (1,000건씩)"""
    print(f"\n[MySQL] {host}:{port}/{database} 연결 중...")
    conn = mysql.connector.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        database=database,
        autocommit=False,
    )
    cursor = conn.cursor()

    cursor.execute("""
        CREATE TABLE IF NOT EXISTS chat_message (
            id          BIGINT       NOT NULL,
            room_id     BIGINT       NOT NULL,
            sender_id   BIGINT       NOT NULL,
            content     VARCHAR(500) NOT NULL,
            created_at  DATETIME     NOT NULL,
            is_read     TINYINT(1)   NOT NULL DEFAULT 0,
            PRIMARY KEY (id)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
    """)
    conn.commit()

    cursor.execute("SELECT COUNT(*) FROM chat_message")
    existing = cursor.fetchone()[0]
    if existing > 0:
        print(f"[MySQL] 기존 데이터 {existing:,}건 삭제 중...")
        cursor.execute("TRUNCATE TABLE chat_message")
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
            (m["id"], m["room_id"], m["sender_id"], m["content"], m["created_at"], int(m["is_read"]))
            for m in batch
        ]
        cursor.executemany(sql, values)
        conn.commit()
        done = min(i + batch_size, total)
        elapsed = time.time() - start
        print(f"  [MySQL] {done:,}/{total:,} ({done/total*100:.1f}%) — {elapsed:.1f}s 경과")

    cursor.close()
    conn.close()
    print(f"[MySQL] 완료: {total:,}건 삽입 ({time.time()-start:.1f}s)")


def seed_mongodb(
    messages: list[dict],
    host: str,
    port: int,
    database: str,
) -> None:
    """MongoDB에 배치 삽입 (1,000건씩)"""
    print(f"\n[MongoDB] {host}:{port}/{database} 연결 중...")
    client = MongoClient(host=host, port=port, serverSelectionTimeoutMS=5000)
    db = client[database]
    collection = db["chat_messages"]

    existing = collection.count_documents({})
    if existing > 0:
        print(f"[MongoDB] 기존 데이터 {existing:,}건 삭제 중...")
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
        elapsed = time.time() - start
        print(f"  [MongoDB] {done:,}/{total:,} ({done/total*100:.1f}%) — {elapsed:.1f}s 경과")

    client.close()
    print(f"[MongoDB] 완료: {total:,}건 삽입 ({time.time()-start:.1f}s)")


def main() -> None:
    parser = argparse.ArgumentParser(description="MySQL & MongoDB 채팅 메시지 시더")
    parser.add_argument("--count", type=int, default=100_000, help="삽입할 데이터 건수")
    parser.add_argument("--mysql-host", default="localhost")
    parser.add_argument("--mysql-port", type=int, default=3306)
    parser.add_argument("--mysql-user", default="root")
    parser.add_argument("--mysql-password", default="password")
    parser.add_argument("--mysql-db", default="benchmark")
    parser.add_argument("--mongo-host", default="localhost")
    parser.add_argument("--mongo-port", type=int, default=27017)
    parser.add_argument("--mongo-db", default="benchmark")
    parser.add_argument("--skip-mysql", action="store_true", help="MySQL 시딩 건너뜀")
    parser.add_argument("--skip-mongo", action="store_true", help="MongoDB 시딩 건너뜀")
    args = parser.parse_args()

    total_start = time.time()

    # 1. 데이터 생성
    gen_start = time.time()
    messages = generate_messages(args.count)
    print(f"데이터 생성 완료: {time.time()-gen_start:.1f}s")

    # 2. MySQL 시딩
    if not args.skip_mysql:
        seed_mysql(
            messages,
            host=args.mysql_host,
            port=args.mysql_port,
            user=args.mysql_user,
            password=args.mysql_password,
            database=args.mysql_db,
        )

    # 3. MongoDB 시딩
    if not args.skip_mongo:
        seed_mongodb(
            messages,
            host=args.mongo_host,
            port=args.mongo_port,
            database=args.mongo_db,
        )

    print(f"\n========================================")
    print(f"  시딩 완료!")
    print(f"  총 소요 시간: {time.time()-total_start:.1f}s")
    print(f"  MySQL: {args.count:,}건")
    print(f"  MongoDB: {args.count:,}건")
    print(f"========================================")


if __name__ == "__main__":
    main()
