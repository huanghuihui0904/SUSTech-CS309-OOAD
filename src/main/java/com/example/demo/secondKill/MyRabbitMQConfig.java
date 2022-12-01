package com.example.demo.secondKill;


import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author code
 * @Date 2022/6/27 14:03
 * Description rabbitmq config
 * Version 1.0
 */
@Configuration
public class MyRabbitMQConfig {



@Bean
public Queue Queue() {
  return new Queue("order");
}
}

