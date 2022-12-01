//package com.example.demo.secondKill;
//
//
//
//import com.example.demo.RedisUtil;
//import com.example.demo.entity.*;
//import com.example.demo.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
///**
// * @author code
// * @Date 2022/6/24 9:25
// * Description 订单实现
// * Version 1.0
// */
//@Service
//public class OrderService {
////  @Autowired
////  private StockOrderMapper stockOrderMapper;
////
//@Autowired
//RedisUtil redisUtil;
//  @Autowired
//  JdbcTemplate jdbcTemplate;
//  @Autowired
//  RoomRepository roomRepository;
//  @Autowired
//  CustomerRepository customerRepository;
//  @Autowired
//  RoomTypeRepository roomTypeRepository;
//  @Autowired
//  HotelRepository hotelRepository;
//  @Autowired
//  OrdersRepository ordersRepository;
////  @Autowired
////  private seckillOrderRepository seckillorderRepository;
//
//  Calendar calendar = Calendar.getInstance();
//
////  @Transactional(rollbackFor = Exception.class)
////  public void createOrder(String username,String goodsname) {
////    //校验库存
////
////seckillOrder order=new seckillOrder(username,goodsname);
////seckillorderRepository.save(order);
////
////  }
//
////todo
//  public void decrByOrder(String roomtypeid){
//    String date="";
//    System.out.println("decrby");
//
//    RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(Integer.parseInt(roomtypeid));
//    String remian=roomType.getRemain();
//    String day=date.split("-")[2];
//    int dayint=(Integer.parseInt(day)-1)*2;
//    String daythat=remian.substring(dayint,dayint+1);
//    int thatramian=Integer.parseInt(daythat);
//    if(thatramian>0){
//      StringBuilder strBuilder = new StringBuilder(remian);
//      thatramian--;
//      char re=(thatramian+"").charAt(0);
//      strBuilder.setCharAt(dayint, re);
//      String str= strBuilder.toString();
//      roomType.setRemain(str);
//      roomTypeRepository.save(roomType);
//    }else {
//      System.out.println("no remain");
//    }
//
//  }
//  public boolean booking(@RequestBody SeckillController.BookInfo bookInfo) throws ParseException {
//    Integer maxId = jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
//    if (maxId == null) maxId = 0;
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    String roomTypeName = bookInfo.getRoomType();
//    String hotelName = bookInfo.getHotelName();
//    String userName = bookInfo.getUsername();
//    Integer cost = bookInfo.getCost();
//
//    Customer customer = customerRepository.findByName(userName);
//    Hotel hotel = hotelRepository.findHotelByHotelname(hotelName);
//
//    Integer customerid = customer.getCustomerid();
//    Integer hotelid = hotel.getHotelid();
//    List<RoomType> roomTypeList = roomTypeRepository.findRoomTypesByHotelid(hotelid);
//    if (roomTypeList == null) {
//      return false;
//    }
//    RoomType roomtype = null;
//    for (RoomType r : roomTypeList) {
//      if (Objects.equals(r.getRoomname(), roomTypeName)) {
//        roomtype = r;
//        break;
//      }
//    }
//    if (roomtype == null) {
//      return false;
//    }
//
//    Integer roomtypeid = roomtype.getRoomtypeid();
//
//
//    String startDate = bookInfo.getStartDate();
//    String endDate = bookInfo.getStartDate();
//    Date checkin = format.parse(startDate);
//    Date checkout = format.parse(endDate);
//    Date now = new Date();
//    int startIndex = (int) ((checkin.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
//    int endIndex = (int) ((checkout.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
//
//    List<Room> roomList = roomRepository.findRoomsByRoomtypeid(roomtypeid);
//    Room room = null;
//    for (Room r : roomList) {
//      String isOrderedInterval = r.getIsordered().substring(startIndex, endIndex + 1);
//      if (!isOrderedInterval.contains("1")) {
//        room = r;
//        break;
//      }
//    }
//    if (room == null) {
//      return false;
//    }
//
//
//    String remain = roomtype.getRemain();
//    String[] remains = remain.split(",");
//    String isOrdered = room.getIsordered();
//
//    StringBuilder currentIsordered = new StringBuilder();
//    StringBuilder currentRemain = new StringBuilder();
//
//
//    for (int i = startIndex; i <= endIndex; i++) {
//      currentIsordered.append("0,");
//      int cur = Integer.parseInt(remains[i]) + 1;
//      currentRemain.append(cur).append(",");
//    }
//    String remainFin = "";
//    String isOrderedFin = "";
//
//    if (endIndex >= remain.length()) {
//      remainFin = remain.substring(0, startIndex * 2) + currentRemain.substring(0, currentRemain.length() - 1);
//      isOrderedFin = isOrdered.substring(0, startIndex * 2) + currentIsordered.substring(0, currentIsordered.length() - 1);
//    } else {
//      remainFin = remain.substring(0, startIndex * 2) + currentRemain + remain.substring(2 * endIndex + 1);
//      isOrderedFin = isOrdered.substring(0, startIndex * 2) + currentIsordered + isOrdered.substring(2 * endIndex + 1);
//    }
//
//
//    if (customer.getMoney() < cost) {
//      return false;
//    }
//    //订房扣钱
//    String sql1 = "update customer set money=? where customerid=?";
//    jdbcTemplate.update(sql1, customer.getMoney() - cost, customerid);
//
//    //订房加积分
//    String sql2 = "update customer set credits=? where customerid=?";
//    jdbcTemplate.update(sql2, customer.getCredits() + cost, customerid);
//
//    //订房，修改remain
//    String sql3 = "update roomtype set remain=? where roomtypeid=?";
//    jdbcTemplate.update(sql3, remainFin, roomtypeid);
//
//    //订房，修改isordered
//    String sql4 = "update room set isordered=? where roomid=?";
//    jdbcTemplate.update(sql4, isOrderedFin, room.getRoomtypeid());
//
//
//    Orders orders = new Orders();
//    orders.setOrderid(maxId + 1);
//    orders.setCustomerid(customerid);
//    orders.setHotelid(hotelid);
//    orders.setRoomtypeid(roomtypeid);
//    orders.setRoomid(room.getRoomid());
//    String ordertime = format.format(now);
//    orders.setOrdertime(ordertime);
//    orders.setCheckintime(bookInfo.getStartDate());
//    orders.setCheckouttime(bookInfo.getEndDate());
//    orders.setAmountpaid(cost);
//
//    //订房增加order
//    ordersRepository.save(orders);
//
//    //发送订房成功的信息
//    int year = calendar.get(Calendar.YEAR);
//    int month = calendar.get(Calendar.MONTH) + 1;   //month from 0~11, 1 for offset
//    int day = calendar.get(Calendar.DATE);
//    String curDate = year + "-" + month + "-" + day;
//    String orderMessage = "尊敬的" + userName + ",您已成功预订" + startDate + "至" + endDate + "时段入住"
//        + hotelName + "的" + roomTypeName + "。若您有任何疑问，请在聊天窗口与任意客服进行沟通，我们将随时为您提供24小时一对一管家式贴心服务。";
//    jdbcTemplate.update("insert into message(messageFromId, messageFromName, messageToId, messageTime, content) " +
//        "values (?, ?, ?, ?, ?);", 10000, "BOT", customerid, curDate, orderMessage);
//    return true;
//  }
////  //校验库存
////  private Stock checkStock(Integer id) {
////    return stockMapper.checkStock(id);
////  }
////
////  //扣库存
////  private int updateSale(Stock stock){
////    return stockMapper.updateSale(stock);
////  }
//
//
//}
//
//
