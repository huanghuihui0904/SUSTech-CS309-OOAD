package com.example.demo.controller;


import com.example.demo.repository.xyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class xyHandler {
  @Autowired
  xyRepository syrepository ;


  @Autowired
  JdbcTemplate jdbcTemplate;




}
