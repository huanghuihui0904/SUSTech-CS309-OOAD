package com.example.demo.repository;

import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface xyRepository extends JpaRepository<xy,Integer> {
  //    @Select( "select * from customer where customerid=#{id}")
//  public RoomTypeWishList findRoomTypeWishListByRoomtypewishlistid(Integer id);

//      @Query(value = "select * from customer",nativeQuery = true)
//  public List<xy> findAll();


}
