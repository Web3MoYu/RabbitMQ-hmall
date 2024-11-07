package com.itheima.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ErrorMessageConfig {

    @Bean("error.direct")
    public DirectExchange errorExchange() {
        return new DirectExchange("error.direct");
    }

    @Bean("error.queue")
    public Queue errorQueue() {
        return new Queue("error.queue");
    }

    @Bean
    public Binding errorQueueBing(@Qualifier("error.queue") Queue errorQueue,
                                  @Qualifier("error.direct") DirectExchange exchange) {
        return BindingBuilder.bind(errorQueue).to(exchange).with("error");
    }

    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
//        return new RejectAndDontRequeueRecoverer(); // 默认的实现策略 重试次数耗尽 直接丢弃
//        return new ImmediateRequeueMessageRecoverer(); // 重试次数耗尽，重新把消息放到队列里
        // 重试次数耗尽，直接丢到指定的队列里
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
