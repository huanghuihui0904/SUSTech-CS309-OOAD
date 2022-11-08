package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data  //提供读写属性eg.getter setter tostring......
public class Gift {
    @Id
    @JsonIgnore
    Integer giftid;
    String giftname;
    Integer credits;
    String description;
}
