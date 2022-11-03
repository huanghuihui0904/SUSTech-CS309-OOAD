package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/manager")
public class ManagerHandler {
  @Autowired
  ManagerRepository managerRepository;
  @Autowired
  OrdersRepository ordersRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;


  @GetMapping("/orderedRoomNums")
  public int getOrderedRoomNums() {
        Integer nums = jdbcTemplate.queryForObject("select count(*) from room where isordered=1;", Integer.class);
return nums;
  }
  @GetMapping("/currentCustomer")
  public int getCurrentCustomer() {
    Integer nums = jdbcTemplate.queryForObject("select count(*) from orders where checkouttime is null;", Integer.class);
    return nums;
  }

  @GetMapping("/hotRoomType")
  public String getHotRoomTypeName() {
    String roomName = jdbcTemplate.queryForObject("with result as (select roomtypeid ri, count(*) num from orders group by roomtypeid order by num desc limit 1)\n" +
        "select roomname\n" +
        "from roomtype r\n" +
        "         join result on roomtypeid = result.ri;", String.class);
    return roomName;
  }
  @GetMapping("/hotHotel")
  public String getHotHotel() {
    String hotelName = jdbcTemplate.queryForObject("with result as (select hotelid hi, count(*) num from orders group by hotelid order by num desc limit 1)\n" +
        "select hotelname\n" +
        "from hotel r\n" +
        "         join result on hotelid = result.hi;", String.class);
    return hotelName;
  }
  @GetMapping("/hotCity")
  public String getHotCity() {
    String cityName = jdbcTemplate.queryForObject("with result as (select hotelid hi, count(*) num from orders group by hotelid )\n" +
        "select cityname from(\n" +
        "select cityname,sum(num) from(\n" +
        "select *\n" +
        "from hotel r\n" +
        "         join result on hotelid = result.hi)a group by cityname order by sum desc limit 1)b;", String.class);
    return cityName;
  }


  @GetMapping("/sales")
  public List<xy> sales() {
    List<Orders> orders = ordersRepository.findAll();
    List<String[]> orderArray = new ArrayList();
    int[] year = new int[373];
    for (int i = 0; i < orders.size(); i++) {
      orderArray.add(orders.get(i).toString().split(","));
      for (int j = 0; j < orderArray.get(i).length; j++) {
//        System.out.println(orderArray.get(i)[j]);
        if (orderArray.get(i)[j].contains("ordertime")) {
          String tempt = orderArray.get(i)[j].substring(11, 21);
          System.out.println(tempt);
          String[] time = tempt.split("-");
          int m = Integer.parseInt(time[1]);
          int d = Integer.parseInt(time[2]);
          int arr = (m - 1) * 30 + d;
          year[arr]++;
        }
      }


    }
    List<xy>result=new ArrayList<>();
    for (int j = 0; j < year.length; j++) {
      JSONObject js = new JSONObject();
      xy hh=new xy(j,year[j]);
      System.out.println(js);
      result.add(hh);
//        System.out.println("----" + result[j]);
    }

    return result;
  }
//  @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
//  public Manager getbyid(@RequestParam("id") int id) {
//    Manager manager = managerRepository.findManagerByManagerid(id);
//    return manager;
//  }
//
//  @GetMapping("/findAll")
//  public List findAll() {
//    List<Manager> managers = managerRepository.findAll();
//    return managers;
//  }
//
//
//
//
//  @PostMapping("insert")
//  public String insert(@RequestBody Manager manager) {
//
//    Integer maxId = jdbcTemplate.queryForObject("select MAX(managerid) from manager", Integer.class);
//manager.setManagerid(maxId+1);
//    Manager result = managerRepository.save(manager);
//    if(result!=null){
//      return "insert ok";
//    }else {
//      return "insert fail";
//    }
//
//
//  }


}
