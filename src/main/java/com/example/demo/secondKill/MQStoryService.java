//package com.example.demo.secondKill;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * @author code
// * @Date 2022/6/27 15:22
// * Description mq商品信息
// * Version 1.0
// */
////减库存
//@Slf4j
//@Service
//@RabbitListener(queues = MyRabbitMQConfig.STORY_QUEUE)
//public class MQStoryService {
//  @Autowired
//  private OrderService Service;
////  /**
////   * 监听库存消息队列，并消费
////   * @param id
////   */
//@RabbitHandler
//  public void decrByOrder(seckillOrderInfo info) {
//    /**
//     * 调用数据库service给数据库对应商品库存减一
//     */
//    System.out.println("减库存");
////    log.info("减库存");
//    Service.decrByOrder(info.getGoodsname());
//  }
//}
//
//
