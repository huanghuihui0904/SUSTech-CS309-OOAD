package com.example.demo.controller;


import com.example.demo.entity.Event;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping("/findAll")
  public List findAll() {
    List<Event> events = eventRepository.findAll();
    return events;
  }




  @PostMapping("insert")
  public String insert(@RequestBody Event event) {
    Integer maxId = jdbcTemplate.queryForObject("select MAX(eventid) from event", Integer.class);

event.setEventid(maxId);
    Event result = eventRepository.save(event);
    if(result!=null){
      return "insert ok";
    }else {
      return "insert fail";
    }


  }


}
