package com.itheima.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
public class SpringAmqpTest {

    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    // 简单消息的发送
    public void testSimpleQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp!";
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    // workQueue
    public void testWorkQueue() {
        String queueName = "work.queue";
        for (int i = 1; i <= 50; i++) {
            String message = "hello, spring amqp_" + i;
            rabbitTemplate.convertAndSend(queueName, message);
        }

    }
}