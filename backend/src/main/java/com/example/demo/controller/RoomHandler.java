package com.example.demo.controller;


import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/room")
public class RoomHandler {
  @Autowired
  RoomRepository roomRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
  public Room getbyid(@RequestParam("id") int id) {
    Room room = roomRepository.findRoomByRoomid(id);
    return room;
  }

  @GetMapping("/findAll")
  public List findAll() {
    List<Room> rooms = roomRepository.findAll();
    return rooms;
  }




@PostMapping("insert")
public String insert(@RequestBody Room room) {

  Integer maxId = jdbcTemplate.queryForObject("select MAX(roomid) from room", Integer.class);
room.setRoomid(maxId);
  Room result = roomRepository.save(room);
  if(result!=null){
    return "insert ok";
  }else {
    return "insert fail";
  }


}


}
