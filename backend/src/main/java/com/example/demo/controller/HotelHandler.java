package com.example.demo.controller;


import com.example.demo.entity.Hotel;
import com.example.demo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/hotel")
public class HotelHandler {
  @Autowired
  HotelRepository hotelRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
  public Hotel getbyid(@RequestParam("id") int id) {
    Hotel hotel = hotelRepository.findHotelByHotelid(id);
    return hotel;
  }

  @GetMapping("/findAll")
  public List findAll() {
    List<Hotel> hotels = hotelRepository.findAll();
    return hotels;
  }



  @PostMapping("insert")
  public String insert(@RequestBody Hotel hotel) {
    Integer maxId = jdbcTemplate.queryForObject("select MAX(hotelid) from hotel", Integer.class);
hotel.setHotelid(maxId+1);

    Hotel result = hotelRepository.save(hotel);
    if(result!=null){
      return "insert ok";
    }else {
      return "insert fail";
    }


  }


}
