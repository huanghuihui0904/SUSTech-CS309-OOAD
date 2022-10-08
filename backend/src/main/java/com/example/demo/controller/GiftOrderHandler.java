package com.example.demo.controller;


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

    @RequestMapping(value = "/getbyid/{id}",method = RequestMethod.GET)
    public GiftOrder getbyid(@PathVariable Integer id){
        GiftOrder giftOrder=  giftOrderRepository.findGiftOrderByGiftorderid(id);
        return giftOrder;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<GiftOrder> giftOrders= giftOrderRepository.findAll();
        return giftOrders;
    }

    @GetMapping("/deletebygiftorderid/{id}")
    public String deletebygiftorderid(@PathVariable("id") int id){
        // 删除语句
        String sql = "delete from giftorder where giftorderid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }
    @GetMapping("/deletebycustomerid/{id}")
    public String deletebycustomerid(@PathVariable("id") int id){
        // 删除语句
        String sql = "delete from giftorder where customerid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }






    @GetMapping("/insert/{customerid}/{giftid}/{amount}/{ordertime}")
    public String insert2(@PathVariable("customerid")int customerid,@PathVariable("giftid") int giftid,
                          @PathVariable("amount")int amount,@PathVariable("ordertime")String ordertime){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(giftorderid) from giftorder", Integer.class);
        String sql = "insert into giftorder values (?,?,?,?,?);";
        jdbcTemplate.update(sql,((int)maxId+1),customerid,giftid,amount,ordertime);

        return "insert ok "+maxId;
    }

}
