#!/usr/bin/env python3
"""
MySQL & MongoDB 인덱스 생성 스크립트 (2차 벤치마크용)

채팅 서비스 프로덕션 환경에 실제로 생성할 인덱스를 두 DB에 동일하게 추가한다.
인덱스 생성 후 run-all.sh를 재실행하면 2차 벤치마크 결과를 얻을 수 있다.

추가되는 인덱스:
    MySQL:
        idx_sender_id   — sender_id (조건 조회)
        idx_room_id     — room_id (채팅방별 메시지 조회)
        idx_created_at  — created_at DESC (최신 메시지 정렬)

    MongoDB:
        senderId_1      — senderId ASC
        roomId_1        — roomId ASC
        createdAt_-1    — createdAt DESC

Usage:
    python3 add-indexes.py [OPTIONS]

Options:
    --mysql-host    MySQL 호스트 (default: localhost)
    --mysql-port    MySQL 포트 (default: 3306)
    --mysql-user    MySQL 사용자 (default: root)
    --mysql-password MySQL 비밀번호 (default: password)
    --mysql-db      MySQL 데이터베이스명 (default: benchmark)
    --mongo-host    MongoDB 호스트 (default: localhost)
    --mongo-port    MongoDB 포트 (default: 27017)
    --mongo-db      MongoDB 데이터베이스명 (default: benchmark)
    --skip-mysql    MySQL 인덱스 생성 건너뜀
    --skip-mongo    MongoDB 인덱스 생성 건너뜀
    --drop          기존 인덱스 삭제 후 재생성
"""

import argparse
import sys
import time

try:
    import mysql.connector
    from pymongo import MongoClient, ASCENDING, DESCENDING
except ImportError as e:
    print(f"[ERROR] 필수 패키지가 없습니다: {e}")
    print("실행: pip install -r requirements.txt")
    sys.exit(1)


def add_mysql_indexes(
    host: str,
    port: int,
    user: str,
    password: str,
    database: str,
    drop: bool,
) -> None:
    print(f"\n[MySQL] {host}:{port}/{database} 연결 중...")
    conn = mysql.connector.connect(
        host=host, port=port, user=user, password=password, database=database
    )
    cursor = conn.cursor()

    indexes = [
        ("idx_sender_id",  "sender_id"),
        ("idx_room_id",    "room_id"),
        ("idx_created_at", "created_at"),
    ]

    for idx_name, col in indexes:
        if drop:
            try:
                cursor.execute(f"DROP INDEX {idx_name} ON chat_message")
                conn.commit()
                print(f"  [MySQL] 인덱스 삭제: {idx_name}")
            except Exception:
                pass  # 없으면 무시

        start = time.time()
        try:
            cursor.execute(f"CREATE INDEX {idx_name} ON chat_message ({col})")
            conn.commit()
            print(f"  [MySQL] 인덱스 생성: {idx_name} ({col}) — {time.time()-start:.1f}s")
        except mysql.connector.errors.DatabaseError as e:
            if "Duplicate key name" in str(e):
                print(f"  [MySQL] 이미 존재: {idx_name} (건너뜀)")
            else:
                raise

    # 인덱스 목록 출력
    cursor.execute("SHOW INDEX FROM chat_message")
    rows = cursor.fetchall()
    print(f"\n  [MySQL] 현재 인덱스 목록:")
    for row in rows:
        print(f"    - {row[2]} ({row[4]})")  # Key_name, Column_name

    cursor.close()
    conn.close()
    print(f"[MySQL] 인덱스 추가 완료")


def add_mongo_indexes(
    host: str,
    port: int,
    database: str,
    drop: bool,
) -> None:
    print(f"\n[MongoDB] {host}:{port}/{database} 연결 중...")
    client = MongoClient(host=host, port=port, serverSelectionTimeoutMS=5000)
    collection = client[database]["chat_messages"]

    if drop:
        collection.drop_indexes()
        print("  [MongoDB] 기존 인덱스 삭제 완료 (_id 제외)")

    indexes = [
        ("senderId_1",   [("senderId",  ASCENDING)]),
        ("roomId_1",     [("roomId",    ASCENDING)]),
        ("createdAt_-1", [("createdAt", DESCENDING)]),
    ]

    for idx_name, keys in indexes:
        start = time.time()
        try:
            result = collection.create_index(keys, name=idx_name)
            print(f"  [MongoDB] 인덱스 생성: {result} — {time.time()-start:.1f}s")
        except Exception as e:
            if "already exists" in str(e).lower():
                print(f"  [MongoDB] 이미 존재: {idx_name} (건너뜀)")
            else:
                raise

    # 인덱스 목록 출력
    print(f"\n  [MongoDB] 현재 인덱스 목록:")
    for idx in collection.list_indexes():
        print(f"    - {idx['name']}: {dict(idx['key'])}")

    client.close()
    print(f"[MongoDB] 인덱스 추가 완료")


def main() -> None:
    parser = argparse.ArgumentParser(description="MySQL & MongoDB 인덱스 생성")
    parser.add_argument("--mysql-host",     default="localhost")
    parser.add_argument("--mysql-port",     type=int, default=3306)
    parser.add_argument("--mysql-user",     default="root")
    parser.add_argument("--mysql-password", default="password")
    parser.add_argument("--mysql-db",       default="benchmark")
    parser.add_argument("--mongo-host",     default="localhost")
    parser.add_argument("--mongo-port",     type=int, default=27017)
    parser.add_argument("--mongo-db",       default="benchmark")
    parser.add_argument("--skip-mysql",     action="store_true")
    parser.add_argument("--skip-mongo",     action="store_true")
    parser.add_argument("--drop",           action="store_true", help="기존 인덱스 삭제 후 재생성")
    args = parser.parse_args()

    total_start = time.time()

    if not args.skip_mysql:
        add_mysql_indexes(
            host=args.mysql_host,
            port=args.mysql_port,
            user=args.mysql_user,
            password=args.mysql_password,
            database=args.mysql_db,
            drop=args.drop,
        )

    if not args.skip_mongo:
        add_mongo_indexes(
            host=args.mongo_host,
            port=args.mongo_port,
            database=args.mongo_db,
            drop=args.drop,
        )

    print(f"\n========================================")
    print(f"  인덱스 생성 완료!")
    print(f"  총 소요 시간: {time.time()-total_start:.1f}s")
    print(f"  이제 k6/run-all.sh 를 재실행하면 2차 벤치마크가 시작됩니다.")
    print(f"========================================")


if __name__ == "__main__":
    main()
