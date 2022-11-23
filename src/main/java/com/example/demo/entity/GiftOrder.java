package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "giftorder")
public class GiftOrder {
    @Id
    Integer giftorderid;
    Integer customerid;
    String giftname;
    Integer amount;
    String ordertime;

    String address;

    String recipients;

    public GiftOrder(Integer customerid, String giftName, Integer amount, String ordertime,String address,String recipients) {

        this.customerid = customerid;
        this.giftname=giftName;
        this.amount = amount;
        this.ordertime = ordertime;
        this.address=address;
        this.recipients=recipients;

    }

    public GiftOrder() {

    }
}
