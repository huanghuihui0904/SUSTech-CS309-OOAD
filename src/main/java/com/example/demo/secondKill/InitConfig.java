//package com.example.demo.secondKill;
//
//
//import com.example.demo.RedisUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author code
// * @Date 2022/6/29 14:08
// * Description 初始化
// * Version 1.0
// */
//@Component
//public class InitConfig {
//  @Autowired
//  private RedisUtil redisutil;
//  /**
//   * redis初始化商品的库存量和信息
//   * @param
//   * @throws Exception
//   */
//  @PostConstruct
//  public void init() {
//    redisutil.set("1", 60000, 20000);
//  }
//}
//
//
