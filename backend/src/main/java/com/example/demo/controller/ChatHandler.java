package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatHandler {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getbyid/{id}",method = RequestMethod.GET)
    public Chat getbyid(@PathVariable Integer id){
        Chat chat=chatRepository.getChatByChatid(id);
        return chat;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Chat>chats=chatRepository.findAll();
        return chats;
    }


    @GetMapping("/deletebychatid/{id}")
    public String deletebyid(@PathVariable("id") int id){
        // 删除语句
        String sql = "delete from chat where chatid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by chatid Ok";
    }

    @GetMapping("/deletebycustomerid/{id}")
    public String deleteByName(@PathVariable("id") int id){
        String sql = "delete from chat where customerid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by customerid Ok";
    }


    @GetMapping("/insert/{chatid}/{customerid}/{chattime}/{content}/{iscustomer}")
    public String insert(@PathVariable("chatid") int chatid,@PathVariable("customerid")int customerid,@PathVariable("chattime") String chattime,@PathVariable("content") String content,@PathVariable("iscustomer") int iscustomer){
        // 删除语句
        String sql = "insert into chat values (?,?,?,?,?);";
        jdbcTemplate.update(sql,chatid,customerid,chattime,content,iscustomer);
        // 查询
        return "insert ok";
    }






}
