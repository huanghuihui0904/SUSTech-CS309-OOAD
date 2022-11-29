package com.example.demo.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "roomtype")
public class RoomType implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer roomtypeid;
  Integer hotelid;
  String roomname;
  String remain;
  Integer price;
  String introduction;
  Integer number;
  @Autowired
public RoomType(){

}
  public RoomType(Integer hotelid, String roomname, String remain, Integer price, String introduction, Integer number) {
    this.hotelid = hotelid;
    this.roomname = roomname;
    this.remain = remain;
    this.price = price;
    this.introduction = introduction;
    this.number = number;
  }
  public RoomType(Integer roomtypeid,Integer hotelid, String roomname, String remain, Integer price, String introduction, Integer number) {
  this.roomtypeid=roomtypeid;
  this.hotelid = hotelid;
    this.roomname = roomname;
    this.remain = remain;
    this.price = price;
    this.introduction = introduction;
    this.number = number;
  }
}
