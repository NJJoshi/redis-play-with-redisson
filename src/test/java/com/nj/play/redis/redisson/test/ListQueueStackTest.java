package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
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


    @Test
    public void testQueue() {
        RQueueReactive<Long> queue = this.redissonClient.getQueue("number-list", LongCodec.INSTANCE);
        Mono<Void> queuePoll = queue.poll().repeat(3).doOnNext(System.out::println).then();
        StepVerifier.create(queuePoll).verifyComplete();
        StepVerifier.create(queue.size()).expectNext(6).verifyComplete();
    }

    @Test
    public void testStack() {
        RDequeReactive<String> stack = this.redissonClient.getDeque("number-list", StringCodec.INSTANCE);
        Mono<Void> stackPoll = stack.pollLast().repeat(3).doOnNext(System.out::println).then();
        StepVerifier.create(stackPoll).verifyComplete();
        StepVerifier.create(stack.size()).expectNext(2).verifyComplete();
    }
}
