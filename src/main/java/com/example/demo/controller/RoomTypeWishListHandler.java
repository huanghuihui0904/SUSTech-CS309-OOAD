package com.example.demo.controller;


import com.example.demo.entity.RoomTypeWishList;
import com.example.demo.repository.RoomTypeWishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/roomtypewishlist")
public class RoomTypeWishListHandler {
  @Autowired
  RoomTypeWishListRepository roomTypeWishListRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @GetMapping()
  public RoomTypeWishList getbyid(@RequestParam("id") int id) {
    RoomTypeWishList roomTypeWishList = roomTypeWishListRepository.findRoomTypeWishListByRoomtypewishlistid(id);
    return roomTypeWishList;
  }
//
//  @GetMapping("/findAll")
//  public List findAll() {
//    List<RoomTypeWishList> roomTypeWishLists = roomTypeWishListRepository.findAll();
//    return roomTypeWishLists;
//  }
//
//
//
//
//  @PostMapping("insert")
//  public String insert(@RequestBody RoomTypeWishList roomTypeWishList) {
//
//    Integer maxId = jdbcTemplate.queryForObject("select MAX(roomtypewishlistid) from roomtypewishlist", Integer.class);
//roomTypeWishList.setRoomtypewishlistid(maxId+1);
//    RoomTypeWishList result = roomTypeWishListRepository.save(roomTypeWishList);
//    if(result!=null){
//      return "insert ok";
//    }else {
//      return "insert fail";
//    }
//
//
//  }


}