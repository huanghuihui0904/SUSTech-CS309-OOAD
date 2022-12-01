package com.example.demo.controller;


import com.example.demo.JsonUtil;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.HotelWishList;
import com.example.demo.entity.RoomType;
import com.example.demo.entity.RoomTypeWishList;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomTypeRepository;
import com.example.demo.repository.RoomTypeWishListRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/roomtypewishlist")
public class RoomTypeWishListHandler {
  @Autowired
  RoomTypeWishListRepository roomTypeWishListRepository;
@Autowired
  RoomTypeRepository roomTypeRepository;
  @Autowired
  HotelRepository hotelRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;

//  @GetMapping()
//  public RoomTypeWishList getbyid(@RequestParam("id") int id) {
//    RoomTypeWishList roomTypeWishList = roomTypeWishListRepository.findRoomTypeWishListByRoomtypewishlistid(id);
//    return roomTypeWishList;
//  }
  @Data
  class wishlistInfo{
    int roomTypeID;
    String hotelName;
    String roomTypeName;

  public wishlistInfo(int roomTypeID, String hotelName, String roomTypeName) {
    this.roomTypeID = roomTypeID;
    this.hotelName = hotelName;
    this.roomTypeName = roomTypeName;
  }
}

  @GetMapping()
  public List<wishlistInfo> getbyid(@RequestParam("userId") int id) {
    List<RoomTypeWishList> roomTypeWishLists = roomTypeWishListRepository.findRoomTypeWishListByCustomerid(id);
    List<wishlistInfo> re=new ArrayList<>();
    for (int i = 0; i <roomTypeWishLists.size() ; i++) {
      Hotel hotel=hotelRepository.findHotelByHotelid(roomTypeWishLists.get(i).getHotelid());
      RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomTypeWishLists.get(i).getRoomtypeid());
if(roomType==null ||hotel==null){
  continue;
}
        wishlistInfo w=new wishlistInfo(roomType.getRoomtypeid(),hotel.getHotelname(),roomType.getRoomname());
        re.add(w);


    }
    return re;
  }

  @PutMapping("/add")
  public String add(@RequestBody String str ) {
    List<String[]>js= JsonUtil.decodeJSON(str);
    int userID=0;
    int roomTypeID = 0;
    int hotelID=0;
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("hotelID")){
        hotelID= Integer.parseInt(js.get(i)[1].trim());
      }
    }
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("userID")){
        userID= Integer.parseInt(js.get(i)[1].trim());
      }
    }
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("roomTypeID")){
        roomTypeID= Integer.parseInt(js.get(i)[1].trim());
      }
    }

    RoomTypeWishList h=new RoomTypeWishList(hotelID,roomTypeID,userID);
    List<RoomTypeWishList>hs=roomTypeWishListRepository.findAll();
    for (int i = 0; i < hs.size(); i++) {
      if(hs.get(i).getRoomtypeid()==roomTypeID&&hs.get(i).getCustomerid()==userID&&hs.get(i).getHotelid()==hotelID){
        return "duplicate";
      }
    }
    roomTypeWishListRepository.save(h);
    return "insert ok";


  }


  @PutMapping("/remove")
  public String remove(@RequestBody String str ) {
    List<String[]>js= JsonUtil.decodeJSON(str);
    int userID=0;
    int roomTypeID = 0;
    int hotelID=0;
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("hotelID")){
        hotelID= Integer.parseInt(js.get(i)[1].trim());
        System.out.println("hotel"+hotelID);
      }
    }
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("userID")){
        userID= Integer.parseInt(js.get(i)[1].trim());
        System.out.println("userID"+userID);

      }
    }
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("roomTypeID")){
        roomTypeID= Integer.parseInt(js.get(i)[1].trim());
        System.out.println("roomTypeID"+roomTypeID);
      }
    }
int id=0;
    RoomTypeWishList h=new RoomTypeWishList(hotelID,roomTypeID,userID);
    List<RoomTypeWishList> all=roomTypeWishListRepository.findAll();
    for (int i = 0; i < all.size(); i++) {
      if(all.get(i).getRoomtypeid()==roomTypeID&&all.get(i).getHotelid()==hotelID&&all.get(i).getCustomerid()==userID){
        id=all.get(i).getRoomtypewishlistid();
      }
    }
    if(id!=0){
      roomTypeWishListRepository.deleteById(id);
      return "delete ok";

    }else {
    return   "delete fail";
    }



  }


}
