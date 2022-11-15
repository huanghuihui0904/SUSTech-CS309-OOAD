package com.example.demo.controller;


import com.example.demo.entity.Event;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableTransactionManagement
@EnableScheduling
public class TimedTask {
  @Autowired
  EventRepository eventRepository;
  @Autowired
  RoomTypeRepository roomTypeRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

//    @PostConstruct // 构造函数之后执行
//    public void init(){
//        System.out.println("容器启动后执行");
//        startJob();
//    }

  @Scheduled(cron = "0 0 1 ? * SUN")
  public String startJob() {
    Integer maxId = jdbcTemplate.queryForObject("select MAX(eventid) from event", Integer.class);
    if (maxId==null){
      maxId=0;
    }
    Date now=new Date();
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Event event=new Event();
//        Integer count= jdbcTemplate.queryForObject("select count(*) from roomtype", Integer.class);
    Integer roomTypeid= jdbcTemplate.queryForObject(" select roomtypeid from roomtype  offset floor(random()* (select count(*) from roomtype)) LIMIT 1\n", Integer.class);
    RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomTypeid);
    event.setHotelid(roomType.getHotelid());
    event.setRoomtypeid(roomTypeid);
    event.setBegintime(format.format(now));
    Date d=new Date(now.getTime()+2*24*60*60*1000);
    event.setEndtime(format.format(d));
    event.setDiscountprice(90);

    event.setEventid(maxId+1);
    Event result = eventRepository.save(event);
    if(result!=null){
      return event+ "\ninsert ok";
    }else {
      return event+"\ninsert fail";
    }
//        System.out.println("TimedTask");
//        System.out.println(format.format(now));
//        Date d=new Date(now.getTime()+2*24*60*60*1000);
//        System.out.println(format.format(d));

//        System.out.println(new Date().getTime());
  }

}
