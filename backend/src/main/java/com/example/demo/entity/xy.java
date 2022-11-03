package com.example.demo.entity;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;


@Entity
@Data  //提供读写属性eg.getter setter tostring......

public class xy {
  @Id
  Integer x;
  Integer y;


  public xy(int j, int i) {
    this.x=j;
    this.y=i;
  }
}
