package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Event;
import com.example.demo.repository.ChatRepository;
import org.apache.ibatis.annotations.Delete;
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

    @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
    public Chat getbyid(@RequestParam("id") int id){
        Chat chat=chatRepository.getChatByChatid(id);
        return chat;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Chat>chats=chatRepository.findAll();
        return chats;
    }


    @Delete("/deletebychatid")
    public String deletebyid(@RequestParam("id") int id){

        // 删除语句
        String sql = "delete from chat where chatid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by chatid Ok";
    }


    @PostMapping("/insert")
    public String insert(@RequestBody Chat chat){
        // 删除语句
        Integer maxId = jdbcTemplate.queryForObject("select MAX(chatid) from chat", Integer.class);
        chat.setChatid(maxId+1);
        Chat result = chatRepository.save(chat);
        if(result!=null){
            return "insert ok";
        }else {
            return "insert fail";
        }

    }






}
