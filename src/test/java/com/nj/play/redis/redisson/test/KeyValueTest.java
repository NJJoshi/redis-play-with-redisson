package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class KeyValueTest  extends BaseTest {

    @Test
    public void keyValueAccessTest() {
        RBucketReactive<String> bucket = this.redissonClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Nirav Joshi");
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        StepVerifier.create(set.concatWith(get)).verifyComplete();
    }

    @Test
    public void keyValueExpiryTest() {
        RBucketReactive<String> bucket = this.redissonClient.getBucket("user:2:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Sam Joshi", Duration.ofSeconds(20));
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        StepVerifier.create(set.concatWith(get)).verifyComplete();
    }

    @Test
    public void keyValueExtendedExpiryTest() {
        RBucketReactive<String> bucket = this.redissonClient.getBucket("user:3:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("John Joshi", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        StepVerifier.create(set.concatWith(get)).verifyComplete();

        //Extend
        sleep(5000);
        Mono<Boolean> mono = bucket.expire(Duration.ofSeconds(60));
        StepVerifier.create(mono)
                .expectNext(true).verifyComplete();

        //access expiration time
        Mono<Void> mono1 = bucket.remainTimeToLive()
                            .doOnNext(System.out::println)
                            .then();
        StepVerifier.create(mono1).verifyComplete();
    }
}
