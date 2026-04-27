package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class MessageQueueTest extends BaseTest{

    private RBlockingDequeReactive<Long>  queue;

    @BeforeAll
    public void setUp(){
        this.queue = this.redissonClient.getBlockingDeque("msg-queue", LongCodec.INSTANCE);
    }

    @Test
    public void producer() {
        Mono<Void> mono = Flux.range(1, 100)
                              .delayElements(Duration.ofSeconds(5))
                              .doOnNext(i -> System.out.println("Going to add " + i))
                              .flatMap(i -> this.queue.add(Long.valueOf(i)))
                              .then();
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    public void consumer1() {
        this.queue.takeElements()
                  .doOnNext(i -> System.out.println("Consumer 1 : " + i))
                  .subscribe();
        sleep(600_000);
    }

    @Test
    public void consumer2() {
        this.queue.takeElements()
                  .doOnNext(i -> System.out.println("Consumer 2 : " + i))
                  .subscribe();
        sleep(600_000);
    }

}
