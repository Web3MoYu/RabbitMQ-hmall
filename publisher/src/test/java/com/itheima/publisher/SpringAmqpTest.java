package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
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

    @Test
    // 将消息发送给topic交换机
    public void testTopicExchange() {
        String exchange = "hmall.topic";
        String message = "hello, topic";
        rabbitTemplate.convertAndSend(exchange, "www.news", message);
    }

    @Test
    // 将消息发送给topic交换机
    public void testSendObject() {
        String exchange = "object.queue";
        Map<String, Object> msg = new HashMap<>();
        msg.put("name", "jack");
        msg.put("age", 21);
        rabbitTemplate.convertAndSend(exchange, msg);
    }

    @Test
    // 发送者确认
    public void testConfirmCallback() throws InterruptedException {
        // 0.创建correlationData
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString()); // id要确保唯一性
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("spring amqp 处理确认结果异常", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 判断是否成功
                if (result.isAck()) {
                    log.debug("收到ConfirmCallBack ack: 消息发送成功");
                } else {
                    log.error("收到ConfirmCallBack nack: 消息发送失败：{}", result.getReason());
                    // 消息重发
                }
            }
        });
        String exchange = "hmall.direct";
        String msg = "ttttt";
        rabbitTemplate.convertAndSend(exchange, "blue", msg, cd);
        Thread.sleep(2000);
    }
}