package com.example.demo.controller;


import com.example.demo.entity.Hotel;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Room;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/manager")
public class ManagerHandler {
  @Autowired
  ManagerRepository managerRepository;


  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
  public Manager getbyid(@RequestParam("id") int id) {
    Manager manager = managerRepository.findManagerByManagerid(id);
    return manager;
  }

  @GetMapping("/findAll")
  public List findAll() {
    List<Manager> managers = managerRepository.findAll();
    return managers;
  }




  @PostMapping("insert")
  public String insert(@RequestBody Manager manager) {

    Integer maxId = jdbcTemplate.queryForObject("select MAX(managerid) from manager", Integer.class);
manager.setManagerid(maxId);
    Manager result = managerRepository.save(manager);
    if(result!=null){
      return "insert ok";
    }else {
      return "insert fail";
    }


  }


}
