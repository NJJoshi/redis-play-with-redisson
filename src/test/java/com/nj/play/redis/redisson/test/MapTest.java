package com.nj.play.redis.redisson.test;

import com.nj.play.redis.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class MapTest extends BaseTest{
    @Test
    public void testMap1(){
        RMapReactive<String, String> map = this.redissonClient.getMap("user:1:map", StringCodec.INSTANCE);
        Map<String, String> myMap = Map.of(
                "name", "John",
                "age", "17",
                "city", "LA"
        );
        StepVerifier.create(map.putAll(myMap).then()).verifyComplete();
    }

    @Test
    public void testMap2(){
        RMapReactive<String, String> map = this.redissonClient.getMap("user:2:map", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "dena");
        Mono<String> age = map.put("age", "19");
        Mono<String> city = map.put("city", "London");
        StepVerifier.create(name.concatWith(age).concatWith(city).then()).verifyComplete();
    }

    @Test
    public void testMap3(){
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.redissonClient.getMap("user:3:map", codec);
        Student s1 = new Student("Steve", 39, "California", List.of(1,2,3,4));
        Student s2 = new Student("Mark", 38, "NY", List.of(10,20,30));
        Mono<Student> mono1 = map.put(1, s1);
        Mono<Student> mono2 = map.put(2, s2);
        StepVerifier.create(mono1.concatWith(mono2).then()).verifyComplete();
    }
}
