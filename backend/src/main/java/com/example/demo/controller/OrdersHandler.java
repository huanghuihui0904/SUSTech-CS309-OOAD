package com.example.demo.controller;


import com.example.demo.entity.Chat;
import com.example.demo.entity.Orders;
import com.example.demo.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrdersHandler {
    @Autowired
    OrdersRepository ordersRepository;


    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
    public Orders getbyid(@RequestParam("id") int id){
        Orders gift=  ordersRepository.findOrderByOrderid(id);
        return gift;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Orders> gifts= ordersRepository.findAll();
        return gifts;
    }

    @PostMapping("/deletebyid")
    public String deletebyid(@RequestParam("id") int id){
        // 删除语句
        String sql = "delete from orders where orderid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }




    @PostMapping("/insert")
    public String insert2(@RequestBody Orders orders){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
        orders.setOrderid(maxId+1);
        Orders result = ordersRepository.save(orders);
        if(result!=null){
            return "insert ok";
        }else {
            return "insert fail";
        }
    }



}
