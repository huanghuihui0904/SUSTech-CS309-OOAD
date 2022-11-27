package com.example.demo.repository;

import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    //    @Select( "select * from orders where orderid=#{id}")
    public Orders findOrderByOrderid (Integer id);
    public List<Orders> findOrdersByCustomerid(Integer id);


    public List<Orders> findOrdersByRoomtypeid(Integer id);

    public List<Orders> findOrdersByHotelid(Integer id);

    public List<Orders> findOrdersByCustomeridAndHotelid(Integer customerid,Integer hotelid);

//    public List<Orders> findOrdersByCustomerid(Integer id);






    //    @Query(value = "select * from orders",nativeQuery = true)
    public List<Orders> findAll();
}