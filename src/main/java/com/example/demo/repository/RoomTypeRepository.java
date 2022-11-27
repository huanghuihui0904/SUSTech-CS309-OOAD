package com.example.demo.repository;

import com.example.demo.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType,Integer> {
  //    @Select( "select * from orders where orderid=#{id}")
  public RoomType findRoomTypeByRoomtypeid (Integer id);

  //    @Query(value = "select * from orders",nativeQuery = true)
  public List<RoomType> findAll();

  public List<RoomType> findRoomTypesByRoomname(String RoomName);


  public List<RoomType> findRoomTypesByHotelid(Integer hotelId);



  public void deleteRoomTypeByRoomtypeid(Integer roomTypeId);


}