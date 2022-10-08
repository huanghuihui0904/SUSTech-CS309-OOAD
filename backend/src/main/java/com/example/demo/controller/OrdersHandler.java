package com.example.demo.controller;


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

    @RequestMapping(value = "/getbyid/{id}",method = RequestMethod.GET)
    public Orders getbyid(@PathVariable Integer id){
        Orders gift=  ordersRepository.findOrderByOrderid(id);
        return gift;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Orders> gifts= ordersRepository.findAll();
        return gifts;
    }

    @GetMapping("/deletebyid/{id}")
    public String deletebyid(@PathVariable("id") int id){
        // 删除语句
        String sql = "delete from orders where orderid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }




    @GetMapping("/insert/{customerid}/{commentid}/{hotelid}/{roomtypeid}/{roomid}/{ordertime}")
    public String insert2(@PathVariable("customerid")int customerid,@PathVariable("commentid") int commentid,
                          @PathVariable("hotelid")int hotelid,@PathVariable("roomtypeid")int roomtypeid,
                          @PathVariable("roomid")int roomid,@PathVariable("ordertime")String ordertime){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
        String sql = "insert into orders values (?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql,((int)maxId+1),customerid,commentid,hotelid,roomtypeid,roomid,ordertime);

        return "insert ok "+maxId;
    }

    @GetMapping("/insert/{customerid}/{hotelid}/{roomtypeid}/{roomid}/{ordertime}")
    public String insert3(@PathVariable("customerid")int customerid,@PathVariable("hotelid")int hotelid,
                          @PathVariable("roomtypeid")int roomtypeid,
                          @PathVariable("roomid")int roomid,@PathVariable("ordertime")String ordertime){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
        String sql = "insert into orders(orderid, customerid, hotelid, roomtypeid, roomid, ordertime) values (?,?,?,?,?,?);";
        jdbcTemplate.update(sql,((int)maxId+1),customerid,hotelid,roomtypeid,roomid,ordertime);

        return "insert ok "+maxId;
    }

}
