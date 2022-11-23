package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "hotelwishlist")
public class HotelWishList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer hotelwishlistid;

  Integer hotelid;

  Integer customerid;


  public HotelWishList(int hotelID, int userID) {
    this.hotelid=hotelID;
    this.customerid=userID;
  }

  public HotelWishList() {

  }
}
