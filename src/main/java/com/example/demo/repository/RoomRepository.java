package com.example.demo.repository;

import com.example.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Integer> {
  //    @Select( "select * from orders where orderid=#{id}")
  public Room findRoomByRoomid (Integer id);
  public List<Room> findRoomsByRoomtypeid(Integer id);

  //    @Query(value = "select * from orders",nativeQuery = true)
  public List<Room> findAll();


}
