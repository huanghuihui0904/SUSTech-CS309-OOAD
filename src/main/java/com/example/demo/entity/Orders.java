package com.example.demo.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
public class Orders {
    @Id
    Integer orderid;
    Integer customerid;
    Integer commentid;
    Integer hotelid;
    Integer roomtypeid;
    Integer roomid;
    String ordertime;
    String checkintime;
    String checkouttime;
    Integer amountpaid;


}


