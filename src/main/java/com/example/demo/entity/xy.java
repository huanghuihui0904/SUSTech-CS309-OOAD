package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data  //提供读写属性eg.getter setter tostring......

public class xy {
  @Id
  String time;
  Integer y;


  public xy(String time, int i) {
    this.time=time;
    this.y=i;
  }

  public xy() {

  }
}
