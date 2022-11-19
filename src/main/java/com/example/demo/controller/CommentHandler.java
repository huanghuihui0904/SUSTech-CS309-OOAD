package com.example.demo.controller;


import com.example.demo.entity.Comment;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Orders;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.OrdersRepository;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentHandler {
  @Autowired
  CommentRepository commentRepository;

  @Autowired
  HotelRepository hotelRepository;

  @Autowired
  OrdersRepository ordersRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;



  @GetMapping( "/{id}")
  public List<Comment> getbyid(@PathVariable("id") int id){
    Hotel hotel=hotelRepository.findHotelByHotelid(id);
    List<Orders> orders=ordersRepository.findOrdersByHotelid(id);
    List<Comment>commentList=new ArrayList<>();
    for (Orders o:
        orders) {
      Integer commentid=o.getCommentid();
      if (commentid!=null){
        Comment comment=commentRepository.findAllByCommentid(commentid);
        if (comment!=null && !commentList.contains(comment)){
          commentList.add(comment);
        }

      }

    }
    return commentList;
  }

  @GetMapping("/findAll")
  public List findAll(){
    List<Comment> comments= commentRepository.findAll();
    return comments;
  }

  @Delete("/deletebyid")
  public String deletebyid(@RequestParam("id") int id){
    // 删除语句
    String sql = "delete from comment where commentid=?";
    jdbcTemplate.update(sql,id);
    // 查询
    return "delete by id Ok";


  }



  @PostMapping("/insert")
  public String addComment(@RequestBody Comment comment){

    Integer maxId = jdbcTemplate.queryForObject("select MAX(commentid) from comment", Integer.class);
    if (maxId==null)maxId=0;
    comment.setCommentid(maxId+1);
    Comment result = commentRepository.save(comment);
    if(result!=null){
      return "insert ok";
    }else {
      return "insert fail";
    }
  }


}