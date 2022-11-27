package com.example.demo.secondKill;

import com.example.demo.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RestController
@RequestMapping(value = "/sec")
public class SeckillController {
  @Autowired
  RedisUtil redisUtil;

  @Autowired
  RabbitTemplate rabbitTemplate;
//  /**
//   * 使用redis+消息队列进行秒杀实现
//   *
//   * @param goodsid 商品id
//   * @return
//   */


  @GetMapping()
  public String sec(@RequestParam(value = "username") String username,@RequestParam(value = "goodsname") String goodsname) {

    String message = null;
    //调用redis给相应商品库存量减一
    Long decrByResult = redisUtil.decrBy(goodsname);
    seckillOrderInfo seckillorderinfo=new seckillOrderInfo(username,goodsname);
    if (decrByResult >= 0) {
      /**
       * 说明该商品的库存量有剩余，可以进行下订单操作
       */
      //发消息给库存消息队列，将库存数据减一
      rabbitTemplate.convertAndSend(MyRabbitMQConfig.STORY_EXCHANGE, MyRabbitMQConfig.STORY_ROUTING_KEY, seckillorderinfo);

      //发消息给订单消息队列，创建订单
      rabbitTemplate.convertAndSend(MyRabbitMQConfig.ORDER_EXCHANGE, MyRabbitMQConfig.ORDER_ROUTING_KEY, seckillorderinfo);
      message = "商品" + goodsname + "秒杀成功";
    } else {
      /**
       * 说明该商品的库存量没有剩余，直接返回秒杀失败的消息给用户
       */
      message ="商品" + goodsname + "秒杀商品的库存量没有剩余,秒杀结束";
    }
    return message;
  }
}



