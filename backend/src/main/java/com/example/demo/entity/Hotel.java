package com.example.demo.entity;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "hotel")
public class Hotel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer hotelid;
  String hotelname;
  String cityname;
  String telephone;
  String address;
  String email;
  String explanation;
  String introduction;


}
