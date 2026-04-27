package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

public class ListQueueStackTest extends BaseTest{

    @Test
    public void testList() {
        RListReactive<Long> list = this.redissonClient.getList("number-list", LongCodec.INSTANCE);
        List<Long> longList = LongStream.rangeClosed(1, 10).boxed().toList();
        StepVerifier.create(list.addAll(longList).then()).verifyComplete();
        StepVerifier.create(list.size()).expectNext(10).verifyComplete();
    }
}
