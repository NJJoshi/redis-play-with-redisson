package com.nj.play.redis.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.api.listener.PatternStatusListener;
import org.redisson.client.codec.StringCodec;

public class PubSubTest extends BaseTest {

    @Test
    public void subscribe1(){
        RTopicReactive topic = this.redissonClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnNext(message -> {
                    System.out.println("Received message from slack-room: " + message);
                })
                .doOnError(throwable -> {
                    System.out.println("Received error from slack-room: " + throwable.getMessage());
                })
                .subscribe();
        sleep(600_000);
    }

    @Test
    public void subscribe2(){
        RPatternTopicReactive patternTopic = this.redissonClient.getPatternTopic("slack-room*", StringCodec.INSTANCE);

        patternTopic.addListener(String.class, new PatternMessageListener() {

                    @Override
                    public void onMessage(CharSequence pattern, CharSequence channel, Object msg) {
                        System.out.println("Received message from slack-room: " + msg + " ,pattern: " + pattern + " ,channel:" + channel);
                    }
                }
        ).subscribe();
        sleep(600_000);
    }
}
