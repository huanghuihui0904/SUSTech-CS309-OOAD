package com.example.demo.controller;


import com.example.demo.entity.Hotel;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping(value = "/roomtype")
public class RoomTypeHandler {
  @Autowired
  RoomTypeRepository roomTypeRepository;

  @Autowired
  HotelRepository hotelRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @GetMapping()
  public RoomType getbyid(@RequestParam("id") int id) {
    RoomType roomType = roomTypeRepository.findRoomTypeByRoomtypeid(id);
    return roomType;
  }

  @GetMapping("/getAll")
  public List<RoomType> findAll() {
    List<RoomType> roomTypes = roomTypeRepository.findAll();
    return roomTypes;
  }

  @GetMapping("/hotel")
  public List<RoomType> byHotel(@RequestParam("hotelName") String hotelName) throws UnsupportedEncodingException {
    System.out.println(hotelName);

    List<Hotel> hotels=hotelRepository.findHotelsByHotelname(hotelName);
    int hotelId=hotels.get(0).getHotelid();
    System.out.println(hotelId);
    List<RoomType> roomTypes = roomTypeRepository.findRoomTypeByHotelid(hotelId);

    return roomTypes;
  }




  @PostMapping("/addRoom")
  public String addRoom(@RequestBody String str) throws JSONException {
    JSONObject js=new JSONObject(str);
    String hotelName=js.getString("hotelName");
    String roomName=js.getString("roomName");
    Integer price= Integer.valueOf(js.getString("price"));
    Integer remain= Integer.valueOf(js.getString("remain"));
    String introduction=js.getString("introduction");
    Integer number= Integer.valueOf(js.getString("number"));
List<Hotel> hotels=hotelRepository.findHotelsByHotelname(hotelName);
int hotelId=hotels.get(0).getHotelid();
RoomType newRoom=new RoomType(hotelId,roomName,remain,price,introduction,number);
RoomType re=roomTypeRepository.save(newRoom);
if(re!=null){
  return "add ok";
}else {
  return "add fail";
}

  }


  @PostMapping("/updateRoomType")
  public String updateRoomType(@RequestBody String str) throws JSONException {
    JSONObject js=new JSONObject(str);
    int roomTypeId=  js.getInt("roomTypeId");
    RoomType tempt=roomTypeRepository.findRoomTypeByRoomtypeid(roomTypeId);

    //
    String roomTypeName=js.getString("roomTypeName");
    int price= js.getInt("price");
    String introduction=js.getString("introduction");
    int number=js.getInt("number");
//
    int hotelid=tempt.getHotelid();
    int remain=tempt.getRemain();


    RoomType newRoomType=new RoomType(roomTypeId,hotelid,roomTypeName,remain,price,introduction,number);
RoomType re=roomTypeRepository.save(newRoomType);
if(re!=null){
  return "update ok";
}else {
  return "update fail";
}
  }

@GetMapping("/deleteRoom")
@Transactional
  public String deleteRoom(@RequestParam("roomID")int roomTypeId ){
  System.out.println(roomTypeId);
roomTypeRepository.deleteRoomTypeByRoomtypeid(roomTypeId);
return "delete ok";
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

//  @Data
//  class AddRoomInfo{
//    String hotelName;
//    String roomName;
//    Integer price;
//    Integer remain;
//    String introduction;
//
//    Integer number;
//
//    public AddRoomInfo(String hotelName, String roomName, Integer price, Integer remain, String introduction,Integer number) {
//      this.hotelName = hotelName;
//      this.roomName = roomName;
//      this.price = price;
//      this.remain = remain;
//      this.introduction = introduction;
//      this.number=number;
//    }}
}


