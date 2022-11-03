package com.example.demo.entity;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;


@Entity
@Data  //提供读写属性eg.getter setter tostring......

public class cityHotelRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  String city;
  String hotel;
  String room;
  int ordered;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


}
