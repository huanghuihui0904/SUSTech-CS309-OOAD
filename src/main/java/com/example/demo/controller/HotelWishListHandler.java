package com.example.demo.controller;


import com.example.demo.JsonUtil;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.HotelWishList;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.HotelWishListRepository;
import lombok.Data;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/hotelwishlist")
public class HotelWishListHandler {
  @Autowired
  HotelWishListRepository hotelWishListRepository;
  @Autowired
  HotelRepository hotelRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

@Data
  class wishlistInfo{
    int hotelID;
    String hotelName;
    String hotelTelephone;

    public wishlistInfo(int hotelID, String hotelName, String hotelTelephone) {
      this.hotelID = hotelID;
      this.hotelName = hotelName;
      this.hotelTelephone = hotelTelephone;
    }
  }

  @GetMapping()
  public List<wishlistInfo> getbyid(@RequestParam("userId") int id) {
    List<HotelWishList> hotelWishLists = hotelWishListRepository.findHotelWishListByCustomerid(id);
    List<wishlistInfo> re=new ArrayList<>();
    for (int i = 0; i <hotelWishLists.size() ; i++) {
      Hotel hotel=hotelRepository.findHotelByHotelid(hotelWishLists.get(i).getHotelid());
      wishlistInfo w=new wishlistInfo(hotel.getHotelid(),hotel.getHotelname(),hotel.getTelephone());
      re.add(w);
    }
    return re;
  }

  @PutMapping("/add")
  public String add(@RequestBody String str ) {
    List<String[]>js=JsonUtil.decodeJSON(str);
    int userID=0;
    int hotelID = 0;
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("userID")){
        userID= Integer.parseInt(js.get(i)[1].trim());
      }
    }
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("hotelID")){
        hotelID= Integer.parseInt(js.get(i)[1].trim());
      }
    }
    System.out.println(hotelID);
    System.out.println(userID);
    HotelWishList h=new HotelWishList(hotelID,userID);
   hotelWishListRepository.save(h);
      return "insert ok";


  }


  @PutMapping("/remove")
  public String remove(@RequestBody String str ) {
    List<String[]>js=JsonUtil.decodeJSON(str);
    int userID=0;
    int hotelID = 0;
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("userID")){
        userID= Integer.parseInt(js.get(i)[1].trim());
      }
    }
    for (int i = 0; i <js.size() ; i++) {
      if(js.get(i)[0].equals("hotelID")){
        hotelID= Integer.parseInt(js.get(i)[1].trim());
      }
    }
    System.out.println(hotelID);
    System.out.println(userID);
    HotelWishList h=new HotelWishList(hotelID,userID);
    int id=0;
    List<HotelWishList>all=hotelWishListRepository.findAll();
    for (int i = 0; i < all.size(); i++) {
      if(all.get(i).getHotelid()==hotelID&&all.get(i).getCustomerid()==userID){
        id=all.get(i).getHotelwishlistid();
      }
    }
    if(id!=0){
      hotelWishListRepository.deleteById(id);
      return "delete ok";
    }else {
      return "delete fail";
    }



  }




}


