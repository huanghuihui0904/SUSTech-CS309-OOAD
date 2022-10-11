package com.example.demo.controller;


import com.example.demo.entity.Chat;
import com.example.demo.entity.GiftOrder;
import com.example.demo.repository.GiftOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/giftorder")
public class GiftOrderHandler {

    @Autowired
    GiftOrderRepository giftOrderRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
    public GiftOrder getbyid(@RequestParam("id") int id){
        GiftOrder giftOrder=  giftOrderRepository.findGiftOrderByGiftorderid(id);
        return giftOrder;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<GiftOrder> giftOrders= giftOrderRepository.findAll();
        return giftOrders;
    }

    @PostMapping("/deletebygiftorderid")
    public String deletebygiftorderid(@RequestParam("id") int id){
        // 删除语句
        String sql = "delete from giftorder where giftorderid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }
    @PostMapping("/deletebycustomerid")
    public String deletebycustomerid(@RequestParam("id") int id){
        // 删除语句
        String sql = "delete from giftorder where customerid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }






    @PostMapping("/insert")
    public String insert(@RequestBody GiftOrder giftOrder){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(giftorderid) from giftorder", Integer.class);
        giftOrder.setGiftorderid(maxId+1);
        GiftOrder result = giftOrderRepository.save(giftOrder);
        if(result!=null){
            return "insert ok";
        }else {
            return "insert fail";
        }
    }

}
