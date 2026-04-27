package com.nj.play.redis.redisson.test;

import com.nj.play.redis.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;


public class KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest(){
        Student student = new Student("John",21, "LA", Arrays.asList(1,2,3));
        RBucketReactive<Student> bucket = this.redissonClient.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set= bucket.set(student);
        Mono<Void> get = bucket.get().doOnNext(System.out::println).then();
        StepVerifier.create(set.concatWith(get)).verifyComplete();

    }

}
