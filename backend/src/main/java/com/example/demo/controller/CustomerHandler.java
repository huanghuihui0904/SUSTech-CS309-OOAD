package com.example.demo.controller;

import com.example.demo.entity.Chat;
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

    @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
    public Customer getbyid(@RequestParam("id") int id){
        Customer customer=  customerRepository.findAllByCustomerid(id);
        return customer;
    }

    @GetMapping("/findAll")
    public List findAll(){
        List<Customer> customers= customerRepository.findAll();
        return customers;
    }

    @PostMapping("/deletebyid")
    public String deletebyid(@RequestParam("id") int id){
        // 删除语句
        String sql = "delete from customer where customerid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return "delete by id Ok";
    }




    @PostMapping("/insert")
    public String insert2(@RequestBody Customer customer){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(customerid) from customer", Integer.class);
        customer.setCustomerid(maxId+1);
        Customer result = customerRepository.save(customer);
        if(result!=null){
            return "insert ok";
        }else {
            return "insert fail";
        }
    }

}
