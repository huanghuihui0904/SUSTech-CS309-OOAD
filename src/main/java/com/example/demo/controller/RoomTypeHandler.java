package com.example.demo.controller;

import com.example.demo.RedisUtil;
import com.example.demo.entity.Event;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomTypeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.demo.JsonUtil;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/roomtype")
public class RoomTypeHandler {
  @Autowired
  RoomTypeRepository roomTypeRepository;
@Autowired
  RedisUtil redisUtil;
  @Autowired
  HotelRepository hotelRepository;

  @Autowired
  EventRepository eventRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @Data
  class reInfo implements Serializable {
    Integer roomtypeid;
    Integer hotelid;
    String roomname;
    String remain;
    Integer price;
    String introduction;
    Integer number;
    Double afterEventPrice;
    Double discount;
String hotelname;
    public reInfo() {
    }

    public reInfo(RoomType r, Double afterEventPrice, Double discount) {
      this.roomtypeid = r.getRoomtypeid();
      this.hotelid = r.getHotelid();
      this.roomname = r.getRoomname();
      this.remain = r.getRemain();
      this.price = r.getPrice();
      this.introduction = r.getIntroduction();
      this.number = r.getNumber();
      this.afterEventPrice = afterEventPrice;
      this.discount = discount;
      Hotel hotel=hotelRepository.findHotelByHotelid(r.getHotelid());
      this.hotelname=hotel.getHotelname();
    }
  }

  @GetMapping()
  public reInfo getbyid(@RequestParam("id") int id) {
    reInfo re = new reInfo();
    RoomType roomType = roomTypeRepository.findRoomTypeByRoomtypeid(id);

    Event event = findEvent();
    if (event != null&&event.getRoomtypeid()==roomType.getRoomtypeid()) {

        re = new reInfo(roomType, roomType.getPrice() * event.getDiscountprice(), event.getDiscountprice());
        return re;


    } else {
      re = new reInfo(roomType, 0.0, 1.0);
      return re;
    }


  }

  @GetMapping("/getAll")
  public List<reInfo> findAll() {
    List<RoomType> roomTypes = roomTypeRepository.findAll();
    List<reInfo> re =new ArrayList<>();

    Event event = findEvent();
    for (int i = 0; i < roomTypes.size(); i++) {
      if (event != null&&event.getRoomtypeid()==roomTypes.get(i).getRoomtypeid()) {
        reInfo t = new reInfo(roomTypes.get(i), roomTypes.get(i).getPrice() * event.getDiscountprice(), event.getDiscountprice());
       re.add(t);
      } else {
        reInfo t = new reInfo(roomTypes.get(i), 0.0, 1.0);
        re.add(t);
      }
    }

    return re;
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
  public List<reInfo> byHotel(@RequestParam("hotelName") String hotelName) throws UnsupportedEncodingException {
    System.out.println(hotelName);

    List<Hotel> hotels = hotelRepository.findHotelsByHotelname(hotelName);
    int hotelId = hotels.get(0).getHotelid();
    System.out.println(hotelId);
    List<RoomType> roomTypes = roomTypeRepository.findRoomTypesByHotelid(hotelId);
    List<reInfo> re =new ArrayList<>();

    Event event = findEvent();
    for (int i = 0; i < roomTypes.size(); i++) {
      if (event != null&&event.getRoomtypeid()==roomTypes.get(i).getRoomtypeid()) {
        reInfo t = new reInfo(roomTypes.get(i), roomTypes.get(i).getPrice() * event.getDiscountprice(), event.getDiscountprice());
        re.add(t);
      } else {
        reInfo t = new reInfo(roomTypes.get(i), 0.0, 1.0);
        re.add(t);
      }
    }

    return re;
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
    String remain = "3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3";
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
//    for (int i = 0; i < js.size(); i++) {
//      if (js.get(i)[0].equals("remain")) {
//        remain = js.get(i)[1];
//      }
//    }
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
    for (int i = 0; i < js.size(); i++) {
      System.out.println(js.get(i)[0]+"  "+js.get(i)[1]);
    }
String managertoken="";
String managername="";
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
      if (js.get(i)[0].equals("managertoken")) {
        managertoken = js.get(i)[1].replace("\"","");
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("managername")) {
        managername = js.get(i)[1];
      }
    }
    for (int i = 0; i < js.size(); i++) {
      if (js.get(i)[0].equals("number")) {
        number = Integer.parseInt(js.get(i)[1]);
      }
    }
//token check
  if(!redisUtil.hasKey(managername)){
    System.out.println("has");
    return "token out of date";
  }
//    System.out.println(ob);
  String ob=redisUtil.get(managername).toString();
    System.out.println(ob);
  if(ob==null||!ob.equals(managertoken)){
    System.out.println("huhu");
    return "token wrong";
  }


    int hotelid = tempt.getHotelid();
    String remain = tempt.getRemain();


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
  public List<reInfo> byParameter(@RequestParam("roomName") String roomName, @RequestParam("minPrice") Integer minPrice, @RequestParam("maxPrice") Integer maxPrice, @RequestParam("guestNum") Integer guestNum, @RequestParam("introduction") String introduction) throws UnsupportedEncodingException {
    List<RoomType> all = roomTypeRepository.findAll();
    List<RoomType> remove = new ArrayList<>();
    System.out.println(roomName);
    System.out.println(minPrice);
    System.out.println(maxPrice);
    System.out.println(guestNum);
    System.out.println(introduction);
    for (int i = 0; i < all.size(); i++) {
      if (!roomName.equals("")) {
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
    List<reInfo> re =new ArrayList<>();

    Event event = findEvent();
    for (int i = 0; i < all.size(); i++) {
      if (event != null&&event.getRoomtypeid()==all.get(i).getRoomtypeid()) {
        reInfo t = new reInfo(all.get(i), all.get(i).getPrice() * event.getDiscountprice(), event.getDiscountprice());
        re.add(t);
      } else {
        reInfo t = new reInfo(all.get(i), 0.0, 1.0);
        re.add(t);
      }
    }

    return re;
  }

  class roomTypeInfo {
    String name;
    Integer area;
    boolean breakfast;

  }
  @GetMapping("/haveRoom")
  public int[] have(@RequestParam("roomtypeid") Integer roomtypeid)  {
  int[] have=new int[31];
RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeid);
String[] remain=roomType.getRemain().split(",");
    for (int i = 0; i < remain.length; i++) {
      if(Integer.parseInt(remain[i])>0){
        have[i]=1;
      }else {
        have[i]=0;
      }
    }





  return have;
  }

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

  public String curTime() {
    DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    String date = sdf1.format(new Date());
    return date;
  }

}


