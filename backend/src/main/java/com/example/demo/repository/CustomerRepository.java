package com.example.demo.repository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Customer;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
//    @Select( "select * from customer where customerid=#{id}")
    public Customer findAllByCustomerid(Integer id);

//    @Query(value = "select * from customer",nativeQuery = true)
    public List<Customer> findAll();
}
