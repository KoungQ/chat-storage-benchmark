#!/usr/bin/env python3
"""
PostgreSQL(RDB) + MongoDB 인덱스 생성 스크립트
"""

import argparse
import sys

try:
    import psycopg2
    from pymongo import ASCENDING, DESCENDING, MongoClient
except ImportError as e:
    print(f"[ERROR] 필수 패키지가 없습니다: {e}")
    print("실행: pip install -r requirements.txt")
    sys.exit(1)


def connect_postgres(host: str, port: int, user: str, password: str, database: str):
    return psycopg2.connect(host=host, port=port, user=user, password=password, dbname=database)


def add_postgresql_indexes(host: str, port: int, user: str, password: str, database: str, drop: bool) -> None:
    print(f"\n[PostgreSQL:RDB] {host}:{port}/{database} 연결 중...")
    conn = connect_postgres(host, port, user, password, database)
    cursor = conn.cursor()
    indexes = [
        "idx_chat_message_sender_id",
        "idx_chat_message_room_id",
        "idx_chat_message_created_at",
    ]

    if drop:
        for name in indexes:
            cursor.execute(f"DROP INDEX IF EXISTS {name}")

    cursor.execute("CREATE INDEX IF NOT EXISTS idx_chat_message_sender_id ON chat_message (sender_id)")
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_chat_message_room_id ON chat_message (room_id)")
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_chat_message_created_at ON chat_message (created_at DESC)")
    conn.commit()
    cursor.close()
    conn.close()
    print("[PostgreSQL:RDB] 인덱스 반영 완료")

def add_mongo_indexes(host: str, port: int, database: str, drop: bool) -> None:
    print(f"\n[MongoDB] {host}:{port}/{database} 연결 중...")
    collection = MongoClient(host=host, port=port, serverSelectionTimeoutMS=5000)[database]["chat_messages"]

    if drop:
        collection.drop_indexes()

    collection.create_index([("senderId", ASCENDING)], name="senderId_1")
    collection.create_index([("roomId", ASCENDING)], name="roomId_1")
    collection.create_index([("createdAt", DESCENDING)], name="createdAt_-1")
    print("[MongoDB] 인덱스 반영 완료")


def main() -> None:
    parser = argparse.ArgumentParser(description="채팅 스토리지 벤치마크 인덱스 생성")
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
    parser.add_argument("--drop", action="store_true")
    args = parser.parse_args()

    if not args.skip_postgresql:
        add_postgresql_indexes(
            args.postgres_host,
            args.postgres_port,
            args.postgres_user,
            args.postgres_password,
            args.postgres_db,
            args.drop,
        )

    if not args.skip_mongo:
        add_mongo_indexes(args.mongo_host, args.mongo_port, args.mongo_db, args.drop)


if __name__ == "__main__":
    main()
