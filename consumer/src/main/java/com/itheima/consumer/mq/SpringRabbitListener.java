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

    /**
     * direct交换机  定向路由
     * 每一个队列都与交换机设定一个或多个routingKey
     * 发布者发布消息的时候，需要指定routingKey，该交换机机会按照routingKey发送给匹配的队列
     * 如果routingKey一样那么就相当于fanout交换机
     */

    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueue1(String message) {
        log.info("监听到direct.queue1的消息:{}", message);
    }

    @RabbitListener(queues = "direct.queue2")
    public void listenDirectQueue2(String message) {
        log.info("监听到direct.queue2的消息:{}", message);
    }

    /**
     * topic交换机  主题交换机
     * 也是根据routingKey做消息路由，但是routingKey通常是多个单词的组合，以.分割
     * 并且RoutingKey可以使用通配符
     * #：代表0个或多个单词
     * *：代表一个单词
     */
    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String message) {
        log.info("监听到topic.queue1的消息:{}", message);
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String message) {
        log.info("监听到topic.queue2的消息:{}", message);
    }
}
