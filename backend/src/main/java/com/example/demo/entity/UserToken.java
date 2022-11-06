package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

public class UserToken implements Serializable {
  String name;
  String token;
  Date createTime;

  public UserToken(String name, String token, Date createTime) {
    this.name = name;
    this.token = token;
    this.createTime = createTime;
  }
}
