package com.example.demo.repository;


import com.example.demo.entity.Chat;
import com.example.demo.entity.Login;
import com.example.demo.entity.cityHotelRoom;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRepository extends JpaRepository<Login,String> {
//    @Select("select * from chat where chatid=#{id}")

  @Query(value="select loginpassword from customer where name=?; ",nativeQuery = true)
  String getPassword(String name);

//  @Select("select * from chat")
//  public List<Chat> findAll();

//    @Delete("delete from gift where giftname=#{name}")
//    public void deleteChatByChatid



}
