package com.example.demo.repository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Customer;
import com.example.demo.entity.HotelWishList;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelWishListRepository extends JpaRepository<HotelWishList,Integer> {
  //    @Select( "select * from customer where customerid=#{id}")
  public HotelWishList findHotelWishListByHotelwishlistid(Integer id);

  //    @Query(value = "select * from customer",nativeQuery = true)
  public List<HotelWishList> findAll();
}
