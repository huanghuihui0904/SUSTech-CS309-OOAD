package com.example.demo.secondKill;

import com.example.demo.RedisUtil;
import com.example.demo.controller.OrdersHandler;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/sec")
public class SeckillController {
  @Autowired
  RedisUtil redisUtil;
@Autowired
  JdbcTemplate jdbcTemplate;
@Autowired
  RoomRepository roomRepository;
@Autowired
  CustomerRepository customerRepository;
@Autowired
  RoomTypeRepository roomTypeRepository;
@Autowired
  HotelRepository hotelRepository;
@Autowired
  OrdersRepository ordersRepository;
  @Autowired
  RabbitTemplate rabbitTemplate;
//  /**
//   * 使用redis+消息队列进行秒杀实现
//   *
//   * @param goodsid 商品id
//   * @return
//   */

  Calendar calendar = Calendar.getInstance();
  @PostMapping("/booking")
  public String sec(@RequestBody MQOrderService.MQBookInfo bookInfo)  {

    String message = null;
    //调用redis给相应商品库存量减一
    Long decrByResult = redisUtil.decrBy(bookInfo.roomtypeid);
//    seckillOrderInfo seckillorderinfo=new seckillOrderInfo(username,goodsname);
    if (decrByResult >= 0) {
      /**
       * 说明该商品的库存量有剩余，可以进行下订单操作
       */

      //发消息给库存消息队列，将库存数据减一
//      rabbitTemplate.convertAndSend(MyRabbitMQConfig.STORY_EXCHANGE, MyRabbitMQConfig.STORY_ROUTING_KEY, bookInfo.roomtypeid);

      //发消息给订单消息队列，创建订单
      rabbitTemplate.convertAndSend(MyRabbitMQConfig.ORDER_EXCHANGE, MyRabbitMQConfig.ORDER_ROUTING_KEY, bookInfo);
      message = "酒店" + bookInfo.hotelName+" 房型 "+bookInfo.roomType + "秒杀成功";
    } else {
      /**
       * 说明该商品的库存量没有剩余，直接返回秒杀失败的消息给用户
       */
      message ="酒店" + bookInfo.hotelName+" 房型 "+bookInfo.roomType + "秒杀商品的库存量没有剩余,秒杀结束";
    }
    return message;
  }

}



