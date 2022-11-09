package com.example.demo.controller;


import com.example.demo.entity.Chat;
import com.example.demo.entity.GiftOrder;
import com.example.demo.repository.GiftOrderRepository;
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
    public String insert(@RequestBody GiftOrderInfo giftOrderInfo){

        Integer giftid=giftOrderInfo.getGiftID();
        Integer customerid=giftOrderInfo.getUserID();
        Integer amount=giftOrderInfo.getAmount();
        Date now=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String ordertime=format.format(now);

        Integer maxId = jdbcTemplate.queryForObject("select MAX(giftorderid) from giftorder", Integer.class);
        if (maxId==null)maxId=0;
        GiftOrder giftOrder=new GiftOrder(customerid,giftid,amount,ordertime,giftOrderInfo.getAddress());

        giftOrder.setGiftorderid(maxId+1);
        GiftOrder result = giftOrderRepository.save(giftOrder);
        if(result!=null){
            return "insert ok";
        }else {
            return "insert fail";
        }
    }



    static class GiftOrderInfo{
        private Integer userID;
        private Integer giftID;
        private String name;
        private String telephone;
        private Integer amount;
        private String address;

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public Integer getGiftID() {
            return giftID;
        }

        public void setGiftID(Integer giftID) {
            this.giftID = giftID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

}
