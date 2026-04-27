package com.nj.play.redis.redisson.test;

import com.nj.play.redis.redisson.test.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient redissonClient;

    @BeforeAll
    public void setClient(){
        this.redissonClient = this.redissonConfig.getRedissonReactiveClient();
    }

    @AfterAll
    public void shutdownAll(){
        this.redissonConfig.getRedissonClient().shutdown();
    }

    protected void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
