package com.example.demo.repository;

import com.example.demo.entity.Gift;
import com.example.demo.entity.GiftOrder;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiftOrderRepository extends JpaRepository<GiftOrder,Integer> {
    //    @Select( "select * from giftorder where giftorderid=#{id}")
    public GiftOrder findGiftOrderByGiftorderid (Integer id);

    //    @Query(value = "select * from giftorder",nativeQuery = true)
    public List<GiftOrder> findAll();

}
