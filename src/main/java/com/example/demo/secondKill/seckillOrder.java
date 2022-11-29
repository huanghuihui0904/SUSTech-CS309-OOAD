package com.example.demo.secondKill;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data  //提供读写属性eg.getter setter tostring......
@Table(name = "seckillorder")
public class seckillOrder implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer orderid;
  String username;
  String goodsname;

  public seckillOrder() {
  }

  public seckillOrder(String username, String goodsname) {
    this.username = username;
    this.goodsname = goodsname;
  }
}

