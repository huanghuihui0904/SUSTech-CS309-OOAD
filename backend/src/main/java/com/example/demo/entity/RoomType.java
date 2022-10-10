package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "roomtype")
public class RoomType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer roomtypeid;
  Integer hotelid;
  String roomname;
  Integer remain;
  Integer price;
  String introduction;


}
