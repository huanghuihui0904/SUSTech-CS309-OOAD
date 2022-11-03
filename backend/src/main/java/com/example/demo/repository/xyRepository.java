package com.example.demo.repository;

import com.example.demo.entity.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface xyRepository extends JpaRepository<xy,Integer> {
  //    @Select( "select * from customer where customerid=#{id}")
//  public RoomTypeWishList findRoomTypeWishListByRoomtypewishlistid(Integer id);

  //    @Query(value = "select * from customer",nativeQuery = true)
//  public List<RoomTypeWishList> findAll();
}
