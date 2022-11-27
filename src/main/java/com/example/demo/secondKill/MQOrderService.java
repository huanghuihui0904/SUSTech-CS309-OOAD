package com.example.demo.secondKill;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author code
 * @Date 2022/6/27 15:19
 * Description mq 订单队列
 * Version 1.0
 */
//下订单
@Service
@Slf4j
@RabbitListener(queues = MyRabbitMQConfig.ORDER_QUEUE)
public class MQOrderService {
  @Autowired
  private OrderService orderService;
//  /**
//   * 监听订单消息队列，并消费
//   *
//   * @param id
//   */
@RabbitHandler

  public void createOrder(seckillOrderInfo info) {
    System.out.println("收到订单消息");
//    log.info("收到订单消息");
    orderService.createOrder(info.getUsername(),info.getGoodsname());
  }
}


