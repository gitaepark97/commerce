package com.hugo.commerce.support.provider;

import org.springframework.stereotype.Component;

@Component
class SnowflakeIdGenerator implements IdGenerator {

    // 2026-01-01 기준 에포크. 타임스탬프 41비트를 최대한 활용하기 위해 현재 시점에 맞게 설정
    private static final long EPOCH = 1735689600000L;

    private static final int WORKER_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    // ~(-1L << N) 은 하위 N비트가 모두 1인 마스크를 생성하는 관용구
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final int WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final int TIMESTAMP_SHIFT = WORKER_ID_BITS + SEQUENCE_BITS;

    private final long workerId;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    SnowflakeIdGenerator() {
        this(0L);
    }

    SnowflakeIdGenerator(long workerId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("workerId must be between 0 and " + MAX_WORKER_ID);
        }
        this.workerId = workerId;
    }
    
    @Override
    public synchronized long generate() {
        long timestamp = currentTime();

        // NTP 동기화 등으로 시스템 클럭이 역행할 수 있어 중복 ID 발급을 차단
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate id.");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // sequence가 0으로 롤오버되면 같은 밀리초 내 발급 한도(4096개) 초과
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
            | (workerId << WORKER_ID_SHIFT)
            | sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTime();
        }
        return timestamp;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }

}
