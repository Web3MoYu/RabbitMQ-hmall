package com.itheima.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {

    @Bean("hmall.fanout")
    public FanoutExchange fanoutExchange() {
        // 默认持久
        return new FanoutExchange("hmall.fanout");
    }

    @Bean("fanout.queue1")
    public Queue fanoutQueue1() {
        // 默认持久
        return new Queue("fanout.queue1");
    }

    @Bean
    public Binding fanoutQueue1Binding(
            @Qualifier("fanout.queue1") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean("fanout.queue2")
    public Queue fanoutQueue2() {
        return new Queue("fanout.queue2");
    }

    @Bean
    public Binding fanoutQueue2Binding(
            @Qualifier("fanout.queue2") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
