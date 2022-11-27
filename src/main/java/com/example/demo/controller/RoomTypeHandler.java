package com.example.demo.controller;

import com.example.demo.entity.Hotel;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.demo.JsonUtil;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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


//  @GetMapping("/findByParameter")
//  public List<RoomType> findByParameter(@RequestParam("hotelName") String hotelName) throws UnsupportedEncodingException {
//    System.out.println(hotelName);
//
//    List<Hotel> hotels = hotelRepository.findHotelsByHotelname(hotelName);
//    int hotelId = hotels.get(0).getHotelid();
//    System.out.println(hotelId);
//    List<RoomType> roomTypes = roomTypeRepository.findRoomTypeByHotelid(hotelId);
//
//    return roomTypes;
//  }


  @GetMapping("/hotel")
  public List<RoomType> byHotel(@RequestParam("hotelName") String hotelName) throws UnsupportedEncodingException {
    System.out.println(hotelName);

    List<Hotel> hotels = hotelRepository.findHotelsByHotelname(hotelName);
    int hotelId = hotels.get(0).getHotelid();
    System.out.println(hotelId);
    List<RoomType> roomTypes = roomTypeRepository.findRoomTypesByHotelid(hotelId);

    return roomTypes;
  }


  @PostMapping("/addRoom")
  public String addRoom(@RequestBody String str) {
    List<String[]> js = JsonUtil.decodeJSON(str);
//    for (int i = 0; i < js.size(); i++) {
//      System.out.println(js.get(i)[0]);
//      System.out.println(js.get(i)[1]);
//    }
    String hotelName = "";
    String roomName = "";
    Integer price = 0;
    Integer remain = 0;
    String introduction = "";
    Integer number = 0;
    //匹配
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("hotelName")) {
        hotelName = js.get(i)[1];
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("roomName")) {
        roomName = js.get(i)[1];
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("price")) {
        price = Integer.valueOf(js.get(i)[1]);
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("remain")) {
        remain = Integer.valueOf(js.get(i)[1]);
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("introduction")) {
        introduction = js.get(i)[1];
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("number")) {
        number = Integer.valueOf(js.get(i)[1]);
      }
    }
    //
    List<Hotel> hotels = hotelRepository.findHotelsByHotelname(hotelName);
    int hotelId = hotels.get(0).getHotelid();
    RoomType newRoom = new RoomType(hotelId, roomName, remain, price, introduction, number);
    RoomType re = roomTypeRepository.save(newRoom);
    if (re != null) {
      return "add ok";
    } else {
      return "add fail";
    }
  }


  @PostMapping("/updateRoomType")
  public String updateRoomType(@RequestBody String str) {
    List<String[]> js = JsonUtil.decodeJSON(str);

    int roomTypeId = 0;
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("roomTypeId")) {
        roomTypeId = Integer.parseInt(js.get(i)[1]);
      }
    }
    RoomType tempt = roomTypeRepository.findRoomTypeByRoomtypeid(roomTypeId);

    //
    String roomTypeName = "";
    int price = 0;
    String introduction = "";
    int number = 0;
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("roomTypeName")) {
        roomTypeName = js.get(i)[1];
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("price")) {
        price = Integer.parseInt(js.get(i)[1]);
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("introduction")) {
        introduction = js.get(i)[1];
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("number")) {
        number = Integer.parseInt(js.get(i)[1]);
      }
    }
//
    int hotelid = tempt.getHotelid();
    int remain = tempt.getRemain();


    RoomType newRoomType = new RoomType(roomTypeId, hotelid, roomTypeName, remain, price, introduction, number);
    RoomType re = roomTypeRepository.save(newRoomType);
    if (re != null) {
      return "update ok";
    } else {
      return "update fail";
    }
  }

  @GetMapping("/deleteRoom")
  @Transactional
  public String deleteRoom(@RequestParam("roomID") int roomTypeId) {
    System.out.println(roomTypeId);
    roomTypeRepository.deleteRoomTypeByRoomtypeid(roomTypeId);
    return "delete ok";
  }


  @GetMapping("/findByParameter")
  public List<RoomType> byParameter(@RequestParam("roomName") String roomName, @RequestParam("minPrice") Integer minPrice, @RequestParam("maxPrice") Integer maxPrice, @RequestParam("guestNum") Integer guestNum, @RequestParam("introduction") String introduction) throws UnsupportedEncodingException {
    List<RoomType> all = roomTypeRepository.findAll();
    List<RoomType> remove = new ArrayList<>();
    System.out.println(roomName);
    System.out.println(minPrice);
    System.out.println(maxPrice);
    System.out.println(guestNum);
    System.out.println(introduction);
    for (int i = 0; i < all.size(); i++) {
      if (!roomName .equals("")) {
        if (!all.get(i).getRoomname().equals(roomName)) {
          remove.add(all.get(i));
          continue;
        }
      }
      if (minPrice != null) {
        if (!(all.get(i).getPrice() > minPrice)) {
          remove.add(all.get(i));
          continue;
        }
      }
      if (maxPrice != null) {
        if (!(all.get(i).getPrice() < maxPrice)) {
          remove.add(all.get(i));
          continue;
        }
      }
      if (guestNum != null) {
        if (all.get(i).getNumber() != guestNum) {
          remove.add(all.get(i));
          continue;
        }
      }
      if (introduction != null) {
        if (!all.get(i).getIntroduction().equals(introduction)) {
          remove.add(all.get(i));
          continue;
        }
      }
    }
    for (int i = 0; i < remove.size(); i++) {
      all.remove(remove.get(i));
    }

    return all;
  }

  class roomTypeInfo {
    String name;
    Integer area;
    boolean breakfast;

  }


}


