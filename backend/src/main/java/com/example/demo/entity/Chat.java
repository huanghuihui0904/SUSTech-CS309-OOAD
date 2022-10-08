package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data  //提供读写属性eg.getter setter tostring......
//@Table(name = "chat")
public class Chat {
    @Id
    Integer chatid;
    Integer customerid;
    String chattime;
    String content;
    Integer iscustomer;
}
