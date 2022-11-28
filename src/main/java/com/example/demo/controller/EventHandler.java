package com.example.demo.controller;


import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
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
  JdbcTemplate jdbcTemplate;

  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
  public Event getbyid(@RequestParam("id") int id) {
    Event event = eventRepository.findEventByEventid(id);

    return event;
  }

  @GetMapping("/haveEvent")
  public Event findEvent() {
    List<Event> events = eventRepository.findAll();
    String cur = curTime();
    Event re = new Event();
    for (int i = 0; i < events.size(); i++) {
      if (events.get(i).getBegintime().compareTo(cur) < 0 && cur.compareTo(events.get(i).getEndtime()) < 0) {
        re = events.get(i);
        return re;
      }
    }
    return re;
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

}