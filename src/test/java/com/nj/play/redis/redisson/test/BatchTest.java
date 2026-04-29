package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BatchTest extends BaseTest {

    @Test
    public void testBatch() {
        RBatchReactive batch = this.redissonClient.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("number-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("number-set", LongCodec.INSTANCE);
        for (long i = 0; i < 100_000; i++) {
            list.add(i);
            set.add(i);
        }
        StepVerifier.create(batch.execute().then()).verifyComplete();
    }

    @Test
    public void testBatch2() {
        RListReactive<Long> list = this.redissonClient.getList("number-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = this.redissonClient.getSet("number-set", LongCodec.INSTANCE);
        Mono<Void> mono = Flux.range(1, 100_000)
                                .map(Long::valueOf)
                                .flatMap(i -> list.add(i).then(set.add(i)))
                                .then();
        StepVerifier.create(mono).verifyComplete();
    }
}
