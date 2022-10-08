package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data  //提供读写属性eg.getter setter tostring......
//@Table(name = "comment")
public class Comment {
    @Id
    Integer commentid;
    Integer score;
    String commenttime;
    String words;
    String picture1;
    String picture2;
    String picture3;
    String video;
}
