package com.example.demo.secondKill;

import java.io.Serializable;

public class seckillOrderInfo implements Serializable {

  String username;
    String  goodsname;

  public seckillOrderInfo(String username, String goodsname) {
    this.username = username;
    this.goodsname = goodsname;
  }

  public seckillOrderInfo() {
    }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getGoodsname() {
    return goodsname;
  }

  public void setGoodsname(String goodsname) {
    this.goodsname = goodsname;
  }
}
