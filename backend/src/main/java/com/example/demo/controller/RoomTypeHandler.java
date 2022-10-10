package com.example.demo.controller;


import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/roomtype")
public class RoomTypeHandler {
  @Autowired
  RoomTypeRepository roomTypeRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
  public RoomType getbyid(@RequestParam("id") int id) {
    RoomType roomType = roomTypeRepository.findRoomTypeByRoomtypeid(id);
    return roomType;
  }

  @GetMapping("/findAll")
  public List findAll() {
    List<RoomType> roomTypes = roomTypeRepository.findAll();
    return roomTypes;
  }



  @PostMapping("insert")
  public String insert(@RequestBody RoomType roomType) {

    Integer maxId = jdbcTemplate.queryForObject("select MAX(roomtypeid) from roomtype", Integer.class);
    roomType.setRoomtypeid(maxId);
    RoomType result = roomTypeRepository.save(roomType);
    if(result!=null){
      return "insert ok";
    }else {
      return "insert fail";
    }


  }


}
