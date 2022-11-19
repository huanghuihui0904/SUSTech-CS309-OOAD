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
  Integer number;
public RoomType(){

}
  public RoomType(Integer hotelid, String roomname, Integer remain, Integer price, String introduction, Integer number) {
    this.hotelid = hotelid;
    this.roomname = roomname;
    this.remain = remain;
    this.price = price;
    this.introduction = introduction;
    this.number = number;
  }
  public RoomType(Integer roomtypeid,Integer hotelid, String roomname, Integer remain, Integer price, String introduction, Integer number) {
  this.roomtypeid=roomtypeid;
  this.hotelid = hotelid;
    this.roomname = roomname;
    this.remain = remain;
    this.price = price;
    this.introduction = introduction;
    this.number = number;
  }
}
