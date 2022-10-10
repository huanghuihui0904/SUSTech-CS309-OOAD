package com.example.demo.repository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Customer;
import com.example.demo.entity.HotelWishList;
import com.example.demo.entity.RoomTypeWishList;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomTypeWishListRepository extends JpaRepository<RoomTypeWishList,Integer> {
  //    @Select( "select * from customer where customerid=#{id}")
  public RoomTypeWishList findRoomTypeWishListByRoomtypewishlistid(Integer id);

  //    @Query(value = "select * from customer",nativeQuery = true)
  public List<RoomTypeWishList> findAll();
}
