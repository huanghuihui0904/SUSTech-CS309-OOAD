package com.example.demo.controller;

import com.example.demo.entity.Gift;
import com.example.demo.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/gift")
public class GiftHandler {
    @Autowired
    GiftRepository giftRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
    public Gift getbyid(@RequestParam("id") int id){
        Gift gift=  giftRepository.findGiftByGiftid(id);
        return gift;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Gift> gifts= giftRepository.findAll();
        return gifts;
    }

    @PostMapping("/deletebygiftid")
    public String deletebyid(@RequestParam("id") int id){
        // 删除语句
        String sql = "delete from gift where giftid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }


    @PostMapping("/insert")
    public String insert(@RequestBody Gift gift){
        Integer maxId = jdbcTemplate.queryForObject("select MAX(giftid) from gift", Integer.class);
        if (maxId==null)maxId=0;
        gift.setGiftid(maxId+1);
        Gift result = giftRepository.save(gift);
        if(result!=null){
            return "insert ok";
        }else {
            return "insert fail";
        }
    }


// 样例
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    /**
//     *     查询employee表中所有数据
//     *     List 中的1个 Map 对应数据库的 1行数据
//     *     Map 中的 key 对应数据库的字段名，value 对应数据库的字段值
//     */
//    @GetMapping("/list")
//    public List<Map<String, Object>> userList(){
//        String sql = "select * from employee";
//        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
//        return maps;
//    }
//
//    /**
//     * 新增一个用户
//     */
//    @GetMapping("/add")
//    public String addUser(){
//        // 插入语句，注意时间问题
//        String sql = "insert into employee(last_name, email,gender,department,birth)" +
//                " values ('夸克','243357594@qq.com',1,101,'"+ new Date().toLocaleString() +"')";
//        jdbcTemplate.update(sql);
//        // 查询
//        return "addOk";
//    }
//
//    /**
//     * 修改用户信息
//     */
//    @GetMapping("/update/{id}")
//    public String updateUser(@PathVariable("id") int id){
//        // 插入语句
//        String sql = "update employee set last_name=?,email=? where id="+id;
//        // 数据
//        Object[] objects = new Object[2];
//        objects[0] = "subei";
//        objects[1] = "243357594@163.com";
//        jdbcTemplate.update(sql,objects);
//        // 查询
//        return "updateOk";
//    }
//
//    /**
//     * 删除用户
//     */
//    @GetMapping("/delete/{id}")
//    public String delUser(@PathVariable("id") int id){
//        // 删除语句
//        String sql = "delete from employee where id=?";
//        jdbcTemplate.update(sql,id);
//        // 查询
//        return "deleteOk";
//    }



}