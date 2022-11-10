package com.example.demo.controller;


import com.example.demo.entity.cityHotelRoom;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.cityHotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/room")
public class RoomHandler {
  @Autowired
  RoomRepository roomRepository;
  @Autowired
  cityHotelRoomRepository cityhotelRoomRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

@PostMapping("/getRoom")
public List<cityHotelRoom> getRoom(@RequestBody cityHotelRoom str) {
  List<cityHotelRoom> cityhotelroom=cityhotelRoomRepository.selectDetail();
  List<cityHotelRoom> result=new ArrayList<>();

  int[] tf=new int[cityhotelroom.size()];
  for (int i = 0; i <cityhotelroom.size() ; i++) {
    if(!str.getCity().equals("null")&&!cityhotelroom.get(i).getCity().equals(str.getCity())){
      tf[i]++;
    }
    if(!str.getHotel().equals("null")&&!cityhotelroom.get(i).getHotel().equals(str.getHotel())){
      tf[i]++;
    }
    if(!str.getRoom().equals("null")&&!cityhotelroom.get(i).getRoom().equals(str.getRoom())){
      tf[i]++;
    }

    if( tf[i]==0&&cityhotelroom.get(i).getOrdered()==0){
      result.add(cityhotelroom.get(i));

    }  }
  return result;
}



//  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
//  public Room getbyid(@RequestParam("id") int id) {
//    Room room = roomRepository.findRoomByRoomid(id);
//    return room;
//  }
//
//  @GetMapping("/findAll")
//  public List findAll() {
//    List<Room> rooms = roomRepository.findAll();
//    return rooms;
//  }
//
//
//
//
//@PostMapping("insert")
//public String insert(@RequestBody Room room) {
//
//  Integer maxId = jdbcTemplate.queryForObject("select MAX(roomid) from room", Integer.class);
//room.setRoomid(maxId+1);
//  Room result = roomRepository.save(room);
//  if(result!=null){
//    return "insert ok";
//  }else {
//    return "insert fail";
//  }
//
//
//}


}
