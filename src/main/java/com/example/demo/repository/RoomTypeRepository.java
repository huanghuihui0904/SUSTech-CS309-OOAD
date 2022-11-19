package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import javax.swing.plaf.PanelUI;
import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType,Integer> {
  //    @Select( "select * from orders where orderid=#{id}")
  public RoomType findRoomTypeByRoomtypeid (Integer id);

  //    @Query(value = "select * from orders",nativeQuery = true)
  public List<RoomType> findAll();

  public List<RoomType> findRoomTypesByRoomname(String RoomName);


  public List<RoomType> findRoomTypeByHotelid(Integer hotelId);

public void deleteRoomTypeByRoomtypeid(Integer roomTypeId);


}