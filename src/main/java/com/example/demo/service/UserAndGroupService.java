package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserAndGroupService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    // retrieve a list of customers except extractId
    public List<Map<String, Object>> getAllCustomer() {
        List<Map<String, Object>> getAllCustomer = jdbcTemplate.queryForList(
                "select * from customer");
        return getAllCustomer;
    }

    // retrieve groups where customer is in
    public List<Map<String, Object>> getAllGroup(Integer customerID) {
        List<Map<String, Object>> getAllGroup = jdbcTemplate.queryForList(
                "select cg.* from chat_group cg join group_members gm on cg.groupId = gm.groupId and gm.customerID = ?;\n", customerID);
        return getAllGroup;
    }
}
