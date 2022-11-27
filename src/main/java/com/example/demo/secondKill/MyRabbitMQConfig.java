package com.example.demo.secondKill;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
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

  //库存交换机
  public static final String STORY_EXCHANGE = "STORY_EXCHANGE";

  //订单交换机
  public static final String ORDER_EXCHANGE = "ORDER_EXCHANGE";

  //库存队列
  public static final String STORY_QUEUE = "STORY_QUEUE";

  //订单队列
  public static final String ORDER_QUEUE = "ORDER_QUEUE";

  //库存路由键
  public static final String STORY_ROUTING_KEY = "STORY_ROUTING_KEY";

  //订单路由键
  public static final String ORDER_ROUTING_KEY = "ORDER_ROUTING_KEY";
//  @Bean
//  public MessageConverter messageConverter() {
//    return new Jackson2JsonMessageConverter();
//  }
  //创建库存交换机
  @Bean
  public DirectExchange getStoryExchange() {
    return new DirectExchange(STORY_EXCHANGE,true,false);
  }
  //创建订单交换机
  @Bean
  public DirectExchange getOrderExchange() {
    return new DirectExchange(ORDER_EXCHANGE,true,false);
  }

  //创建库存队列
  @Bean
  public Queue getStoryQueue() {
    return new Queue(STORY_QUEUE);
  }
  //创建订单队列
  @Bean
  public Queue getOrderQueue() {
    return new Queue(ORDER_QUEUE);
  }
  //库存交换机和库存队列绑定
  @Bean
  public Binding bindStory() {
    return BindingBuilder.bind(getStoryQueue()).to(getStoryExchange()).with(STORY_ROUTING_KEY);
  }

//订单队列与订单交换机进行绑定
  @Bean
  public Binding bindOrder() {
    return BindingBuilder.bind(getOrderQueue()).to(getOrderExchange()).with(ORDER_ROUTING_KEY);
  }
}

