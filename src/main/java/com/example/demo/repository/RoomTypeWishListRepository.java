package com.example.demo.repository;

import com.example.demo.entity.RoomTypeWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface RoomTypeWishListRepository extends JpaRepository<RoomTypeWishList,Integer> {
  //    @Select( "select * from customer where customerid=#{id}")
  public RoomTypeWishList findRoomTypeWishListByRoomtypewishlistid(Integer id);

  //    @Query(value = "select * from customer",nativeQuery = true)
  public List<RoomTypeWishList> findAll();

  public List<RoomTypeWishList> findRoomTypeWishListByCustomerid(Integer id);
}
