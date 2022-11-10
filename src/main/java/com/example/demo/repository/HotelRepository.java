package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Integer> {
  //    @Select( "select * from orders where orderid=#{id}")
  public Hotel findHotelByHotelid (Integer id);

  public List<Hotel> findHotelsByHotelname(String name);

  public List<Hotel> findHotelsByCityname(String city);

  //    @Query(value = "select * from orders",nativeQuery = true)
  public List<Hotel> findAll();

}
