package com.example.demo.repository;

import com.example.demo.entity.Orders;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
//    @Select( "select * from orders where orderid=#{id}")
    public Orders findOrderByOrderid (Integer id);

//    @Query(value = "select * from orders",nativeQuery = true)
    public List<Orders> findAll();
}
