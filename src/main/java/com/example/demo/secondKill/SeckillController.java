package com.example.demo.secondKill;

import com.example.demo.RedisUtil;
import com.example.demo.controller.OrdersHandler;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import io.lettuce.core.ScriptOutputType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@PostMapping()
  public ResponseEntity sec(@RequestBody MQBookInfo bookInfo)  {

    String message = null;
  System.out.println(bookInfo.roomtypeid);
    //调用redis给相应商品库存量减一
  if(redisUtil.hasKey(bookInfo.roomtypeid+"")){

  }else {
    return ResponseEntity.status(201).body(false);

  }
  System.out.println("0000000");
    Long decrByResult = redisUtil.decrBy(bookInfo.roomtypeid);
//    seckillOrderInfo seckillorderinfo=new seckillOrderInfo(username,goodsname);
    if (decrByResult >= 0) {
      /**
       * 说明该商品的库存量有剩余，可以进行下订单操作
       */
      System.out.println("111111111111111111111");
      //发消息给库存消息队列，将库存数据减一
//      rabbitTemplate.convertAndSend(MyRabbitMQConfig.STORY_EXCHANGE, MyRabbitMQConfig.STORY_ROUTING_KEY, bookInfo.roomtypeid);

      //发消息给订单消息队列，创建订单
      rabbitTemplate.convertAndSend("order",bookInfo);
return ResponseEntity.status(200).body(true);
      //      message = "酒店" + bookInfo.hotelName+" 房型 "+bookInfo.roomType + "秒杀成功";
    } else {
      /**
       * 说明该商品的库存量没有剩余，直接返回秒杀失败的消息给用户
       */
      System.out.println("999999999999999");
      return ResponseEntity.status(201).body(false);
//      message ="酒店" + bookInfo.hotelName+" 房型 "+bookInfo.roomType + "秒杀商品的库存量没有剩余,秒杀结束";
    }

  }

}



