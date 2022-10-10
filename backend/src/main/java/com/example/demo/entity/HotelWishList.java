package com.example.demo.entity;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "hotelwishlist")
public class HotelWishList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer hotelwishlistid;

  Integer hotelid;



}
