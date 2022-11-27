package com.example.demo.repository;


import com.example.demo.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login,String> {
//    @Select("select * from chat where chatid=#{id}")

  @Query(value="select loginpassword from customer where name=?; ",nativeQuery = true)
  String getPassword(String name);

  @Query(value="select loginpassword from manager where managername=?; ",nativeQuery = true)
  String getManagerPassword(String name);



}
