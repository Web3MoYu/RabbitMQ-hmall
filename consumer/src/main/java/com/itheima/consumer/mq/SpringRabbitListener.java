package com.itheima.consumer.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String message) {
        log.info("监听到simple.queue的消息:{}", message);
    }

    /**
     * WorkQueue(任务模型)：多个消费者绑定到一个队列
     * 当两个消费者绑定同一个队列，该消息只会被处理一次
     * 默认是轮询模式，不管谁处理的快还是慢都是均匀分配，需要配置prefetch才能改变
     */
    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String message) throws InterruptedException {
        System.out.println("消费者1接收到消息：" + message + ", " + LocalTime.now());
        Thread.sleep(25);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String message) throws InterruptedException {
        System.err.println("消费者2接收到消息：" + message + ", " + LocalTime.now());
        Thread.sleep(200);
    }

    /**
     * fanout交换机 广播交换机
     * 该交换机是将生产者发送给交换机的消息，全部广播给绑定的队列，也就是说
     * 只要有队列与fanout绑定，该队列就会收到消息
     */
    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String message) {
        log.info("监听到fanout.queue1的消息:{}", message);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String message) {
        log.info("监听到fanout.queue2的消息:{}", message);
    }
}
