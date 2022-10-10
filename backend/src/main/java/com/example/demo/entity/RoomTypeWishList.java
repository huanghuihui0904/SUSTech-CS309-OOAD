package com.example.demo.entity;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

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


}
