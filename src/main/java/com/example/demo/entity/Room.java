package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "room")
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer roomid;
  Integer roomtypeid;
  String location;
  Integer isordered;

  
}
