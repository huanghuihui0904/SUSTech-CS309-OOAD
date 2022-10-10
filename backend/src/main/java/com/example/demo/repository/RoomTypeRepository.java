package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType,Integer> {
  //    @Select( "select * from orders where orderid=#{id}")
  public RoomType findRoomTypeByRoomtypeid (Integer id);

  //    @Query(value = "select * from orders",nativeQuery = true)
  public List<RoomType> findAll();
}
