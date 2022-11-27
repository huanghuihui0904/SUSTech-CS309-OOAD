package com.example.demo.controller;


import com.example.demo.entity.Customer;
import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftOrder;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.GiftOrderRepository;
import com.example.demo.repository.GiftRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/giftorder")
public class GiftOrderHandler {

    @Autowired
    GiftOrderRepository giftOrderRepository;
    @Autowired
    GiftRepository giftRepository;
    @Autowired
    CustomerRepository customerRepository;

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






    @PostMapping("/creategiftorder")
    public Boolean   insert(@RequestBody GiftOrderInfo giftOrderInfo){

        String giftName=giftOrderInfo.getGiftName();
        Integer customerid=giftOrderInfo.getUserID();
        Integer amount=giftOrderInfo.getAmount();

        Customer customer=customerRepository.findByCustomerid(customerid);
        Gift gift=giftRepository.findGiftByGiftname(giftName);
        if (customer==null||gift==null){
            System.out.println(customer);
            System.out.println(gift);
            System.out.println(giftName);
            gift=giftRepository.findGiftByGiftname("葡萄酒");
            System.out.println(gift);
            return false;
        }
        Integer credit=customer.getCredits();


        if (credit-amount*gift.getCredits()<0){
            System.out.println(credit-amount*gift.getCredits());
            return false;
        }



        Date now=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String ordertime=format.format(now);

        Integer maxId = jdbcTemplate.queryForObject("select MAX(giftorderid) from giftorder", Integer.class);
        if (maxId==null)maxId=0;


        String sql="update customer set credits=? where customerid=?";
        jdbcTemplate.update(sql,credit-amount*gift.getCredits(),customerid);

        GiftOrder giftOrder=new GiftOrder(customerid,giftName,amount,ordertime,giftOrderInfo.getAddress(),giftOrderInfo.getUserName());
        giftOrder.setGiftorderid(maxId+1);


        giftOrderRepository.save(giftOrder);

        return true;
    }



    @Data
    static
    class GiftOrderInfo{
        private Integer userID;
        private String giftName;
        private String userName;
        private String telephone;
        private Integer amount;
        private String address;


    }

}