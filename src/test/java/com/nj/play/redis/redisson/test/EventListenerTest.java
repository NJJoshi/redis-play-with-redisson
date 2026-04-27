package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class EventListenerTest extends BaseTest {

    @Test
    public void expiredEventTest() {
        RBucketReactive<String> bucket = this.redissonClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("NJ", Duration.ofSeconds(10));
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        Mono<Void> event = bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String name) {
                System.out.println("Expired event for : " + name);
            }
        }).then();
        StepVerifier.create(set.concatWith(get).concatWith(event)).verifyComplete();
        sleep(11000);
    }

    @Test
    public void deleteEventTest(){
        RBucketReactive<String> bucket = this.redissonClient.getBucket("user:1", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("NJ");
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        Mono<Void> delete = bucket.addListener(new DeletedObjectListener() {
            @Override
            public void onDeleted(String name) {
                System.out.println("Deleted event for : " + name);
            }
        }).then();
        StepVerifier.create(set.concatWith(get).concatWith(delete)).verifyComplete();
        sleep(30000);
    }
}
