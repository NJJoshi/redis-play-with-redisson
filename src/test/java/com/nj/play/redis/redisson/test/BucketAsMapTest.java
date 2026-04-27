package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BucketAsMapTest extends BaseTest{

    @Test
    public void bucketAsMapTest() {
        Mono<Void> mono = this.redissonClient.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:1:visit", "student:1")
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(mono).verifyComplete();
    }
}
