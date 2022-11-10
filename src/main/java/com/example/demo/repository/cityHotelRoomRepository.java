package com.example.demo.repository;


import com.example.demo.entity.cityHotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface cityHotelRoomRepository extends JpaRepository<cityHotelRoom,Integer> {
//    @Select("select * from chat where chatid=#{id}")

  @Query(value="select (row_number() over()) as id ,cityname city,hotelname hotel,roomname room,isordered ordered from hotel join roomtype r on hotel.hotelid = r.hotelid join room r2 on r.roomtypeid = r2.roomtypeid;\n",nativeQuery = true)
  List<cityHotelRoom> selectDetail();

//  @Select("select * from chat")
//  public List<Chat> findAll();

//    @Delete("delete from gift where giftname=#{name}")
//    public void deleteChatByChatid



}
