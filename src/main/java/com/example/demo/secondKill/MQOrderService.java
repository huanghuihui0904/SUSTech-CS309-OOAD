package com.example.demo.secondKill;


import com.example.demo.RedisUtil;
import com.example.demo.controller.OrdersHandler;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


//下订单
@Service
@Slf4j
@RabbitListener(queues = "order")
public class MQOrderService {
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
//  @Autowired
//  private OrderService orderService;

  Calendar calendar = Calendar.getInstance();
//  /**
//   * 监听订单消息队列，并消费
//   *
//   * @param id
//   */
@RabbitHandler
public void booking(MQBookInfo bookInfo) throws ParseException {
  System.out.println("------------");
  Integer maxId=jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
  if (maxId==null)maxId=0;
  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  String roomTypeName= bookInfo.getRoomType();
  String hotelName=bookInfo.getHotelName();
  String userName=bookInfo.getUsername();
  Integer cost=bookInfo.getCost();

  Customer customer=customerRepository.findByName(userName);
  Hotel hotel=hotelRepository.findHotelByHotelname(hotelName);

  Integer customerid=customer.getCustomerid();
  Integer hotelid=hotel.getHotelid();
  List<RoomType> roomTypeList=roomTypeRepository.findRoomTypesByHotelid(hotelid);
  if (roomTypeList == null){
    System.out.println("ioioioi");
    return ;
  }
  RoomType roomtype = null;
  for (RoomType r: roomTypeList) {
    if (Objects.equals(r.getRoomname(), roomTypeName)){
      roomtype=r;
      break;
    }
  }
  if ( roomtype==null){
    System.out.println("ppppppp");
    return ;
  }

  Integer roomtypeid=roomtype.getRoomtypeid();
//
//  if(redisUtil.hasKey(roomtypeid+"")){
//    if(redisUtil.get(roomtypeid+"")!=null){
//      if(Integer.parseInt(redisUtil.get(roomtypeid+"").toString())>=0){
//        redisUtil.decrBy(roomtypeid);
//      }else {
//        System.out.println("jjjjjjj");
//        return ;
//      }
//    }else {
//
//    }
//  }else {
//
//  }
  //
  String startDate=bookInfo.getStartDate();

  String endDate=bookInfo.getEndDate();
  Date checkin=format.parse(startDate);
  Date checkout=format.parse(endDate);
  Date now1=new Date();
  String now1String=format.format(now1);
  String nowString=now1String.substring(0,11)+"00:00:00";
  Date now=format.parse(nowString);

  int startIndex= (int) ((checkin.getTime()-now.getTime())/(1000*60*60*24));
  int endIndex=(int) ((checkout.getTime()-now.getTime())/(1000*60*60*24))-1;


  List<Room> roomList=roomRepository.findRoomsByRoomtypeid(roomtypeid);
  Room room=null;
  for (Room r:roomList) {
    String isOrderedInterval=r.getIsordered().substring(startIndex*2,2*endIndex+2);
    if (!isOrderedInterval.contains("1")){
      System.out.println("R:"+isOrderedInterval);
      room=r;
      break;
    }
  }
  if (room==null){
    System.out.println("No room");

    return ;
  }
  String remain=roomtype.getRemain();
  System.out.println("Remain: "+remain);
  String[] remains=remain.split(",");
  String isOrdered=room.getIsordered();

  if (remain.substring(startIndex*2, 2*endIndex+2).contains("0")){
    System.out.println("No room");
    return ;
  }

  StringBuilder currentIsordered= new StringBuilder();
  StringBuilder currentRemain= new StringBuilder();


  for (int i = startIndex; i <=endIndex ; i++) {
    currentIsordered.append("1,");
    int cur=Integer.parseInt(remains[i])-1;
    currentRemain.append(cur).append(",");
  }
  String remainFin="";
  String isOrderedFin="";

  if (endIndex>=remain.length()) {

    remainFin = remain.substring(0, startIndex*2) + currentRemain.substring(0,currentRemain.length()-1) ;
    isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered.substring(0,currentIsordered.length()-1);
  }else {
    System.out.println("remain.substring(0, startIndex*2) "+remain.substring(0, startIndex*2) );
    System.out.println("currentRemain"+currentRemain);
    System.out.println("remain.substring(2*endIndex+1)"+remain.substring(2*endIndex+2));
    remainFin = remain.substring(0, startIndex*2) + currentRemain + remain.substring(2*endIndex+2);
    System.out.println("isOrdered.substring(0, startIndex*2) "+isOrdered.substring(0, startIndex*2) );
    System.out.println("currentisOrdered"+currentIsordered);
    System.out.println("isOrdered.substring(2*endIndex+1)"+isOrdered.substring(2*endIndex+2));
    isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered+isOrdered.substring(2*endIndex+2);
  }

  System.out.println(remainFin);
  System.out.println(isOrderedFin);


  if (customer.getMoney()<cost){
    System.out.println("cost");
    return ;
  }
  //订房扣钱
  String sql1 = "update customer set money=? where customerid=?";
  jdbcTemplate.update(sql1,customer.getMoney()-cost,customerid);

  //订房加积分
  String sql2 = "update customer set credits=? where customerid=?";
  jdbcTemplate.update(sql2,customer.getCredits()+cost,customerid);

  //订房，修改remain
  String sql3 = "update roomtype set remain=? where roomtypeid=?";
  jdbcTemplate.update(sql3,remainFin,roomtypeid);

  //订房，修改isordered
  String sql4 = "update room set isordered=? where roomid=?";
  jdbcTemplate.update(sql4,isOrderedFin,room.getRoomid());


  addOrders(bookInfo,room.getRoomid(),roomtypeid);

  //发送订房成功的信息
  int year = calendar.get(Calendar.YEAR);
  int month = calendar.get(Calendar.MONTH) + 1;   //month from 0~11, 1 for offset
  int day = calendar.get(Calendar.DATE);
  String curDate = year + "-" + month + "-" + day;
  String orderMessage = "尊敬的" + userName + ",您已成功预订" + startDate + "至" + endDate + "时段入住"
      + hotelName + "的" + roomTypeName + "。若您有任何疑问，请在聊天窗口与任意客服进行沟通，我们将随时为您提供24小时一对一管家式贴心服务。";
  jdbcTemplate.update("insert into message(messageFromId, messageFromName, messageToId, messageTime, content) " +
      "values (?, ?, ?, ?, ?);", 10000, "BOT", customerid, curDate, orderMessage);
  System.out.println("okokokokok");
  return ;
}




  public void addOrders(MQBookInfo bookInfo, Integer roomid, Integer roomtypeid) throws ParseException {
    Integer maxId=jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
    if (maxId==null)maxId=0;

    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date now1=new Date();
    String now1String=format.format(now1);
    String nowString=now1String.substring(0,11)+"00:00:00";
    String hotelName=bookInfo.getHotelName();
    String userName=bookInfo.getUsername();
    Integer cost=bookInfo.getCost();

    Customer customer=customerRepository.findByName(userName);
    Hotel hotel=hotelRepository.findHotelByHotelname(hotelName);

    Integer customerid=customer.getCustomerid();
    Integer hotelid=hotel.getHotelid();



    Orders orders=new Orders();
    orders.setOrderid(maxId+1);
    orders.setCustomerid(customerid);
    orders.setHotelid(hotelid);
    orders.setRoomtypeid(roomtypeid);
    orders.setRoomid(roomid);
    String ordertime=format.format(now1);
    orders.setOrdertime(ordertime);
    orders.setCheckintime(bookInfo.getStartDate());
    orders.setCheckouttime(bookInfo.getEndDate());
    orders.setAmountpaid(cost);

    ordersRepository.save(orders);
  }




}


