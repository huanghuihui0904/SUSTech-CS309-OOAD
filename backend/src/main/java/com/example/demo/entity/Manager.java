package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "manager")
public class Manager {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer managerid;
  Integer hotelid;
  String loginpassword;
  String identity;


}
