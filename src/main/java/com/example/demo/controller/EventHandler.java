package com.example.demo.controller;


import com.example.demo.entity.Event;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomTypeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping(value = "/event")
public class EventHandler {
  @Autowired
  EventRepository eventRepository;

  @Autowired
  HotelRepository hotelRepository;

@Autowired
  RoomTypeRepository roomTypeRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
  public Event getbyid(@RequestParam("id") int id) {
    Event event = eventRepository.findEventByEventid(id);

    return event;
  }

  @GetMapping("/haveEvent")
  public EventInfo findEvent() {
    List<Event> events = eventRepository.findAll();
    String cur = curTime();
    EventInfo eventInfo= new EventInfo();
    for (int i = 0; i < events.size(); i++) {
      if (events.get(i).getBegintime().compareTo(cur) < 0 && cur.compareTo(events.get(i).getEndtime()) < 0) {
        Event re = events.get(i);
        eventInfo=new EventInfo(re);
        return eventInfo;
      }
    }
    return eventInfo;
  }


  @PostMapping("insert")
  public String insert(@RequestBody Event event) {
    Integer maxId = jdbcTemplate.queryForObject("select MAX(eventid) from event", Integer.class);

    event.setEventid(maxId + 1);
    Event result = eventRepository.save(event);
    if (result != null) {
      return "insert ok";
    } else {
      return "insert fail";
    }


  }

  public String curTime() {
    DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    String date = sdf1.format(new Date());
    return date;
  }



  @Data
  class EventInfo{
    Integer eventid;
    Integer hotelid;
    String hotelname;
    Integer roomtypeid;
    String roomtypename;

    String begintime;
    String endtime;

    Double discountprice;

    public EventInfo(Event event){
      this.eventid=event.getEventid();
      this.hotelid=event.getHotelid();
      this.roomtypeid=event.getRoomtypeid();
      this.begintime=event.getBegintime();
      this.endtime=event.getEndtime();
      this.discountprice=event.getDiscountprice();
      Hotel hotel=hotelRepository.findHotelByHotelid(hotelid);
      this.hotelname=hotel.getHotelname();
      RoomType r=roomTypeRepository.findRoomTypeByRoomtypeid(event.getRoomtypeid());
      this.roomtypename=r.getRoomname();
    }
    public EventInfo(){

    }

  }
}