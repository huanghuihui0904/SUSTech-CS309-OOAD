package com.example.demo.repository;

import com.example.demo.entity.HotelWishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelWishListRepository extends JpaRepository<HotelWishList,Integer> {
  //    @Select( "select * from customer where customerid=#{id}")
  public HotelWishList findHotelWishListByHotelwishlistid(Integer id);

  //    @Query(value = "select * from customer",nativeQuery = true)
  public List<HotelWishList> findAll();

  public List<HotelWishList> findHotelWishListByCustomerid(Integer id);
}
