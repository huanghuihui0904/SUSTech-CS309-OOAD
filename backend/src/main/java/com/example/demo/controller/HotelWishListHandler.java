package com.example.demo.controller;


import com.example.demo.entity.Hotel;
import com.example.demo.entity.HotelWishList;
import com.example.demo.entity.Room;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.HotelWishListRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/hotelwishlist")
public class HotelWishListHandler {
  @Autowired
  HotelWishListRepository hotelWishListRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @GetMapping()
  public HotelWishList getbyid(@RequestParam("id") int id) {
    HotelWishList hotelWishList = hotelWishListRepository.findHotelWishListByHotelwishlistid(id);
    return hotelWishList;
  }

//  @GetMapping("/findAll")
//  public List findAll() {
//    List<HotelWishList> hotelWishLists = hotelWishListRepository.findAll();
//    return hotelWishLists;
//  }
//
//
//
//
//  @PostMapping("insert")
//  public String insert(@RequestBody HotelWishList hotelWishList) {
//    Integer maxId = jdbcTemplate.queryForObject("select MAX(hotelwishlistid) from hotelwishlist", Integer.class);
//hotelWishList.setHotelwishlistid(maxId+1);
//
//    HotelWishList result = hotelWishListRepository.save(hotelWishList);
//    if(result!=null){
//      return "insert ok";
//    }else {
//      return "insert fail";
//    }
//
//
//  }


}
