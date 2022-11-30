package com.example.demo.controller;


import com.example.demo.entity.Event;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@EnableTransactionManagement
@EnableScheduling
public class TimedTask {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    Calendar calendar = Calendar.getInstance();

//    @PostConstruct // 构造函数之后执行
//    public void init(){
//        System.out.println("容器启动后执行");
//        startJob();
//    }


    //每次0点开始 持续24个小时抢当天的
    //0点更新 isordered和remain
    @Scheduled(cron = "0 0 0 ? * *")
//    @Scheduled(cron = "0 4 20 28 11 ?")  //test only
    public String startJob() {

        List<Room> roomList=roomRepository.findAll();
        List<RoomType> roomTypeList=roomTypeRepository.findAll();
        for (Room r:roomList) {
            String isordered=r.getIsordered().substring(2)+",0";
            String sql1 = "update room set isordered=? where roomid=?";
            jdbcTemplate.update(sql1,isordered,r.getRoomid() );
        }
        for (RoomType rt:roomTypeList) {
            int add=roomRepository.findRoomsByRoomtypeid(rt.getRoomtypeid()).size();
            String remain=rt.getRemain().substring(2)+","+add;
            String sql2 = "update roomtype set remain=? where roomtypeid=?";
            jdbcTemplate.update(sql2,remain,rt.getRoomtypeid() );
        }

        Integer maxId = jdbcTemplate.queryForObject("select MAX(eventid) from event", Integer.class);
        if (maxId == null) {
            maxId = 0;
        }

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Event event = new Event();
//        Integer count= jdbcTemplate.queryForObject("select count(*) from roomtype", Integer.class);
        RoomType roomType = null;
        while (true) {
            Integer roomTypeid = jdbcTemplate.queryForObject(" select roomtypeid from roomtype  offset floor(random()* (select count(*) from roomtype)) LIMIT 1", Integer.class);
            roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomTypeid);
            if (Integer.parseInt(String.valueOf(roomType.getRemain().charAt(0)))>0){
                break;
            }
        }

        event.setHotelid(roomType.getHotelid());
        event.setRoomtypeid(roomType.getRoomtypeid());
        event.setBegintime(format.format(now));
        Date d = new Date(now.getTime() + 24 * 60 * 60 * 1000 - 1);
        event.setEndtime(format.format(d));
        event.setDiscountprice(0.9);

        // 定时发送消息
        String roomTypeName = roomType.getRoomname();
        int hotelId = roomType.getHotelid();
        String hotelName = jdbcTemplate.queryForObject("select hotelname from hotel where hotelid = ?;", String.class, hotelId);
        String endTimeStr = format.format(d);
        Integer maxCustomerId = jdbcTemplate.queryForObject("select MAX(customerid) from customer;", Integer.class);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;   //month from 0~11, 1 for offset
        int day = calendar.get(Calendar.DATE);
        String curDate = year + "-" + month + "-" + day;
        String discountMessage = "为回馈新老顾客的支持, “" +
                hotelName + "”酒店" + roomTypeName +
                "即刻开始打9折促销，速来体验吧！" + "持续时间至" + endTimeStr;
        for (int i = 1; i <= maxCustomerId; i++) {   //遍历所有顾客
            jdbcTemplate.update("insert into message(messageFromId, messageFromName, messageToId, messageTime, content) " +
                    "values (?, ?, ?, ?, ?);", 10000, "BOT", i, curDate, discountMessage);
        }


        event.setEventid(maxId + 1);
        Event result = eventRepository.save(event);
        return event + "\ninsert ok";
        //        System.out.println("TimedTask");
//        System.out.println(format.format(now));
//        Date d=new Date(now.getTime()+2*24*60*60*1000);
//        System.out.println(format.format(d));

//        System.out.println(new Date().getTime());
    }

}
