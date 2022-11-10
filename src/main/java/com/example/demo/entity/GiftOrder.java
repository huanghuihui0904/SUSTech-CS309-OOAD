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
    Integer giftid;
    Integer amount;
    String ordertime;

    String address;

    public GiftOrder(Integer customerid, Integer giftid, Integer amount, String ordertime,String address) {

        this.customerid = customerid;
        this.giftid = giftid;
        this.amount = amount;
        this.ordertime = ordertime;
        this.address=address;
    }

    public GiftOrder() {

    }
}
