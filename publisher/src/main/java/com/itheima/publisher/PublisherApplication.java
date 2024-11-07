package com.itheima.publisher;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PublisherApplication {
    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // 在每次发消息之前添加一个id，在消费者哪里进行处理，
        // 比如处理完成之后保存到数据库，在处理的时候，查看是否存在，即可保证幂等性
        // 如果业务本身能够通过状态等操作处理幂等，不推荐这种方式
        converter.setCreateMessageIds(true);
        return converter;
    }

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class);
    }
}
