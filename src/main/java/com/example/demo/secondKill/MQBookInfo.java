package com.example.demo.secondKill;

import lombok.Data;

import java.io.Serializable;

@Data
 class MQBookInfo implements Serializable {
  String startDate;
  String endDate;
  String roomType;
  String hotelName;
  Integer cost;
  String username;
  Integer roomtypeid;
  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public String getRoomType() {
    return roomType;
  }

  public String getHotelName() {
    return hotelName;
  }

  public Integer getCost() {
    return cost;
  }

  public String getUsername() {
    return username;
  }

  public MQBookInfo() {
  }
}
