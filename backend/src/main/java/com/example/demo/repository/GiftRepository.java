package com.example.demo.repository;

import com.example.demo.entity.Gift;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift,Integer> {
    //    @Select( "select * from gift where giftid=#{id}")
    public Gift findGiftByGiftid (Integer id);

    //    @Query(value = "select * from gift",nativeQuery = true)
    public List<Gift> findAll();

    @Delete("delete from gift where giftname=#{name}")
    public void deleteGiftByGiftname(String name);

    @Delete("delete from gift where giftid=#{id}")
    public void deleteGiftByGiftid(Integer id);

}
