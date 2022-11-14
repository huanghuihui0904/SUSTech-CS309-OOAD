package com.example.demo.controller;


import com.example.demo.entity.RoomType;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/roomtype")
public class RoomTypeHandler {
  @Autowired
  RoomTypeRepository roomTypeRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @GetMapping()
  public RoomType getbyid(@RequestParam("id") int id) {
    RoomType roomType = roomTypeRepository.findRoomTypeByRoomtypeid(id);
    return roomType;
  }

  @GetMapping("/getAll")
  public List findAll() {
    List<RoomType> roomTypes = roomTypeRepository.findAll();
    return roomTypes;
  }
//
//
//
//  @PostMapping("insert")
//  public String insert(@RequestBody RoomType roomType) {
//
//    Integer maxId = jdbcTemplate.queryForObject("select MAX(roomtypeid) from roomtype", Integer.class);
//    roomType.setRoomtypeid(maxId+1);
//    RoomType result = roomTypeRepository.save(roomType);
//    if(result!=null){
//      return "insert ok";
//    }else {
//      return "insert fail";
//    }
//
//
//  }


}