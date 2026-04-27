package com.nj.play.redis.redisson.test;

import com.nj.play.redis.redisson.test.config.RedissonConfig;
import com.nj.play.redis.redisson.test.dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class LocalCacheMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> studentMap;

    @BeforeAll
    public void setupClient() {
        RedissonConfig redissonConfig = new RedissonConfig();
        RedissonClient redissonClient = redissonConfig.getRedissonClient();
        LocalCachedMapOptions<Integer, Student> mapOption = LocalCachedMapOptions.<Integer, Student>name("students")
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
                .codec(new TypedJsonJacksonCodec(Integer.class, Student.class))
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);

        this.studentMap = redissonClient.getLocalCachedMap(mapOption);
    }

    @Test
    public void appServer1() {
        Student s1 = new Student("sam", 10, "atlanta", List.of(1, 2, 3));
        Student s2 = new Student("Julie", 12, "atlanta", List.of(3,2,1));
        this.studentMap.put(1, s1);
        this.studentMap.put(2, s2);
        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println(i + " ==> " + studentMap.get(1)))
                .subscribe();
        sleep(600000);
    }

    @Test
    public void appServer2() {
        Student s1 = new Student("sam-updated", 10, "atlanta", List.of(1101, 1201, 1301));
        this.studentMap.put(1, s1);
    }
}
