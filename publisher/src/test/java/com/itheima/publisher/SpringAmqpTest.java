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

    @Test
    // 将消息发送给fanout交换机
    public void testFanoutExchange() {
        String exchange = "hmall.fanout";
        String message = "hello, every";
        // 注意填写三个参数，第一个参数代表交换机，routingKey为空即可，否则就发送给队列了
        rabbitTemplate.convertAndSend(exchange, "", message);

    }

    @Test
    // 将消息发送给direct交换机
    public void testDirectExchange() {
        String exchange = "hmall.direct";
        String message = "hello, every";
        rabbitTemplate.convertAndSend(exchange, "red", message);

    }
}