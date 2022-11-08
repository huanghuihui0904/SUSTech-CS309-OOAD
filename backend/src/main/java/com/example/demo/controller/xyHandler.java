package com.example.demo.controller;


import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomTypeWishList;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeWishListRepository;
import com.example.demo.repository.xyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class xyHandler {
  @Autowired
  xyRepository syrepository ;


  @Autowired
  JdbcTemplate jdbcTemplate;




}
