package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
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

    @RequestMapping(value = "/getbyid/{id}",method = RequestMethod.GET)
    public Comment getbyid(@PathVariable Integer id){
        Comment comment=  commentRepository.findAllByCommentid(id);
        return comment;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Comment> comments= commentRepository.findAll();
        return comments;
    }

    @GetMapping("/deletebyid/{id}")
    public String deletebyid(@PathVariable("id") int id){
        // 删除语句
        String sql = "delete from comment where commentid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }



    @GetMapping("/insert/{commentid}/{score}/{commenttime}")
    public String insert(@PathVariable("commentid") int commentid,@PathVariable("score")int score,@PathVariable("commenttime") String commenttime){
        // 删除语句
        String sql = "insert into comment values (?,?,?);";
        jdbcTemplate.update(sql,commentid,score,commenttime);
        // 查询
        return "insert ok";
    }
    @GetMapping("/insert/{score}/{commenttime}")
    public String insert2(@PathVariable("score")int score,@PathVariable("commenttime") String commenttime){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(commentid) from comment", Integer.class);
        String sql = "insert into comment values (?,?,?);";
        jdbcTemplate.update(sql,((int)maxId+1),score,commenttime);

        return "insert ok "+maxId;
    }


}
