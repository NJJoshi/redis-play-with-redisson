package com.nj.play.redis.redisson.test;

import com.nj.play.redis.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapCacheTest extends BaseTest{
    @Test
    public void testMapCache(){
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> mapCache = this.redissonClient.getMapCache("user:cache", codec);

        Student s1 = new Student("Sam", 39, "NYC", List.of(1,2,3,4));
        Student s2 = new Student("Lucy", 39, "DT", List.of(4,3,2,1));

        Mono<Student> st1 = mapCache.put(1, s1, 5, TimeUnit.SECONDS);
        Mono<Student> st2 = mapCache.put(2, s2, 10, TimeUnit.SECONDS);

        StepVerifier.create(st1.concatWith(st2).then()).verifyComplete();

        sleep(3000);

        //access students
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        sleep(3000);

        //access students
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

    }
}
