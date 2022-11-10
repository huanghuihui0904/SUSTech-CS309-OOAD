package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data  //提供读写属性eg.getter setter tostring......
public class Customer {
    @Id
    @JsonIgnore
    Integer customerid;
    @JsonIgnore
    Integer hotelwishlistid;
    @JsonIgnore
    Integer roomtypewishlistid;
    String name;
    @JsonIgnore
    String loginpassword;
    String telephone;
    Integer money;

    Integer credits;

    public Customer(Integer hotelwishlistid, Integer roomtypewishlistid, String name, String loginpassword, String telephone, Integer money, Integer credits) {
//        this.customerid = customerid;
        this.hotelwishlistid = hotelwishlistid;
        this.roomtypewishlistid = roomtypewishlistid;
        this.name = name;
        this.loginpassword = loginpassword;
        this.telephone = telephone;
        this.money = money;
        this.credits = credits;
    }

    public Customer() {
    }





}
