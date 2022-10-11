package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
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
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentHandler {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
    public Comment getbyid(@RequestParam("id") int id){
        Comment comment=  commentRepository.findAllByCommentid(id);
        return comment;
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
    public String insert2(@RequestBody Comment comment){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(commentid) from comment", Integer.class);
        comment.setCommentid(maxId+1);
        Comment result = commentRepository.save(comment);
        if(result!=null){
            return "insert ok";
        }else {
            return "insert fail";
        }
    }


}
