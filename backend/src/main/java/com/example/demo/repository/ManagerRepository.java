package com.example.demo.repository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Manager;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager,Integer> {
  //    @Select( "select * from customer where customerid=#{id}")
  public Manager findManagerByManagerid(Integer id);

  //    @Query(value = "select * from customer",nativeQuery = true)
  public List<Manager> findAll();
}
