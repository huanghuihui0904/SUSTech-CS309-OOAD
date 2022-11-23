package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "roomtypewishlist")
public class RoomTypeWishList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer roomtypewishlistid;
  Integer hotelid;
  Integer roomtypeid;
  Integer customerid;

  public RoomTypeWishList(Integer hotelid, Integer roomtypeid, Integer customerid) {
    this.hotelid = hotelid;
    this.roomtypeid = roomtypeid;
    this.customerid = customerid;
  }

  public RoomTypeWishList() {
  }
}
