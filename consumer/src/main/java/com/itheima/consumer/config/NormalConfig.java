package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过消息的过期时间模拟延迟消息
 * 当一个消息在队列中超时没有处理，如果没有指定交换机就会被销毁
 * 如果指定死信交换机，在该消息超时后，会投递给死信交换机，然后监听死信队列即可模拟出延迟队列
 */
@Configuration
public class NormalConfig {
    @Bean("normal.direct")
    public DirectExchange errorExchange() {
        return new DirectExchange("normal.direct");
    }

    @Bean("normal.queue")
    public Queue errorQueue() {
        return QueueBuilder.durable("normal.queue")
                .deadLetterExchange("dlx.direct") // 声明超时消息投递的队列
                .build();
    }

    @Bean
    public Binding normalExchangeBinding(@Qualifier("normal.queue") Queue errorQueue,
                                         @Qualifier("normal.direct") DirectExchange exchange) {
        // 绑定的key要和死信交换机的routingKey保持一直
        return BindingBuilder.bind(errorQueue).to(exchange).with("hi");
    }

}
