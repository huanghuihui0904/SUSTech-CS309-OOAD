package com.example.demo.repository;


import com.example.demo.entity.Chat;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Integer> {
//    @Select("select * from chat where chatid=#{id}")

    public Chat getChatByChatid(Integer id);

    @Select("select * from chat")
    public List<Chat> findAll();

//    @Delete("delete from gift where giftname=#{name}")
//    public void deleteChatByChatid



}
