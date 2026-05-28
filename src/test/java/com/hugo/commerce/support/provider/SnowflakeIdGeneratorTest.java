package com.hugo.commerce.support.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SnowflakeIdGeneratorTest {

    private final SnowflakeIdGenerator generator = new SnowflakeIdGenerator();

    @Test
    @DisplayName("유효하지 않은 workerId로 생성 시 예외 발생")
    void throwsException_whenWorkerIdIsInvalid() {
        // when & then
        assertThatThrownBy(() -> new SnowflakeIdGenerator(-1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new SnowflakeIdGenerator(1024))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성된 ID는 양수")
    void generatedId_isPositive() {
        // when
        long id = generator.generate();

        // then
        assertThat(id).isPositive();
    }

    @Test
    @DisplayName("연속 생성된 ID는 단조 증가")
    void generatedIds_areMonotonicallyIncreasing() {
        // when
        long first = generator.generate();
        long second = generator.generate();

        // then
        assertThat(second).isGreaterThan(first);
    }

    @Test
    @DisplayName("단일 스레드에서 대량 생성 시 ID 중복 없음")
    void generatedIds_areUniqueInSingleThread() {
        // given
        int count = 10_000;
        Set<Long> ids = new HashSet<>();

        // when
        for (int i = 0; i < count; i++) {
            ids.add(generator.generate());
        }

        // then
        assertThat(ids).hasSize(count);
    }

    @Test
    @DisplayName("멀티 스레드에서 동시 생성 시 ID 중복 없음")
    void generatedIds_areUniqueAcrossMultipleThreads() throws InterruptedException {
        // given
        int threadCount = 16;
        int idsPerThread = 1_000;
        Set<Long> ids = ConcurrentHashMap.newKeySet(threadCount * idsPerThread);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < idsPerThread; j++) {
                            ids.add(generator.generate());
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }
        latch.await();

        // then
        assertThat(ids).hasSize(threadCount * idsPerThread);
    }

    @Test
    @DisplayName("다른 workerId를 가진 인스턴스 간 ID 중복 없음")
    void generatedIds_areUniqueAcrossDifferentWorkers() {
        // given
        SnowflakeIdGenerator worker0 = new SnowflakeIdGenerator(0);
        SnowflakeIdGenerator worker1 = new SnowflakeIdGenerator(1);
        Set<Long> ids = new HashSet<>();

        // when
        for (int i = 0; i < 1_000; i++) {
            ids.add(worker0.generate());
            ids.add(worker1.generate());
        }

        // then
        assertThat(ids).hasSize(2_000);
    }
}
