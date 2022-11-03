package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.entity.cityHotelRoom;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Integer> {
  //    @Select( "select * from orders where orderid=#{id}")
  public Room findRoomByRoomid (Integer id);

  //    @Query(value = "select * from orders",nativeQuery = true)
  public List<Room> findAll();


}
