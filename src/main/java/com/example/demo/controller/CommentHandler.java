package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentHandler {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;



    @GetMapping( "/{id}")
    public Comment getbyid(@PathVariable("id") int id){
        return commentRepository.findAllByCommentid(id);
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