package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customer")
public class CustomerHandler {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getbyid/{id}",method = RequestMethod.GET)
    public Customer getbyid(@PathVariable Integer id){
        Customer customer=  customerRepository.findAllByCustomerid(id);
        return customer;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Customer> customers= customerRepository.findAll();
        return customers;
    }

    @GetMapping("/deletebyid/{id}")
    public String deletebyid(@PathVariable("id") int id){
        // 删除语句
        String sql = "delete from customer where customerid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }




    @GetMapping("/insert/{hotelwishlistid}/{roomtypewishlistid}/{name}/{loginpassword}/{telephone}/{money}/{credits}")
    public String insert2(@PathVariable("hotelwishlistid")int hotelwishlistid,@PathVariable("roomtypewishlistid") String roomtypewishlistid,
                          @PathVariable("name")String name,@PathVariable("loginpassword")String loginpassword,
                          @PathVariable("telephone")String telephone,@PathVariable("money")int money,
                          @PathVariable("credits")int credits){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(customerid) from customer", Integer.class);
        String sql = "insert into customer values (?,?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql,((int)maxId+1),hotelwishlistid,roomtypewishlistid,name,loginpassword,telephone,money,credits);

        return "insert ok "+maxId;
    }

}
