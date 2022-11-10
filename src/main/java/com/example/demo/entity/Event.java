package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "event")
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer eventid;
  Integer hotelid;
  Integer roomtypeid;

  String begintime;
  String endtime;

  Integer discountprice;


}
