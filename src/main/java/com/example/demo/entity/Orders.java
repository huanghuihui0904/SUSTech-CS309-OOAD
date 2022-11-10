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

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }

    public Integer getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(Integer amountpaid) {
        this.amountpaid = amountpaid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getCommentid() {
        return commentid;
    }

    public void setCommentid(Integer commentid) {
        this.commentid = commentid;
    }

    public Integer getHotelid() {
        return hotelid;
    }

    public void setHotelid(Integer hotelid) {
        this.hotelid = hotelid;
    }

    public Integer getRoomtypeid() {
        return roomtypeid;
    }

    public void setRoomtypeid(Integer roomtypeid) {
        this.roomtypeid = roomtypeid;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }


}


