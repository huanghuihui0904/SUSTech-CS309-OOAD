package com.example.demo.repository;

import com.example.demo.entity.GiftOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftOrderRepository extends JpaRepository<GiftOrder,Integer> {
    //    @Select( "select * from giftorder where giftorderid=#{id}")
    public GiftOrder findGiftOrderByGiftorderid (Integer id);
    public GiftOrder findGiftOrderByGiftname(String giftName);

    //    @Query(value = "select * from giftorder",nativeQuery = true)
    public List<GiftOrder> findAll();

}
