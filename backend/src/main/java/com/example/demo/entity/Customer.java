package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data  //提供读写属性eg.getter setter tostring......
public class Customer {
    @Id
    Integer customerid;
    Integer hotelwishlistid;
    Integer roomtypewishlistid;
    String name;
    String loginpassword;
    String telephone;
    Integer money;
    Integer credits;
}
