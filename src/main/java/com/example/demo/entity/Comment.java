package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

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

    public Comment(Integer commentid, Integer score, String commenttime, String words, String picture1, String picture2, String picture3, String video) {
        this.commentid = commentid;
        this.score = score;
        this.commenttime = commenttime;
        this.words = words;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
        this.video = video;
    }
    public Comment(){

    }
}
