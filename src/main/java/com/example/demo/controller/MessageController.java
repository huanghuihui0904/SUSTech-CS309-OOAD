package com.example.demo.controller;

import com.example.demo.dto.MessageDTO;
import com.example.demo.dto.MessageGroupDTO;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserAndGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
//@RequestMapping(value = "/chat")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserAndGroupService userAndGroupService;

    @MessageMapping("/chat/{to}")   // if a message is sent to "/chat/{to}", sendMessage is called
    public void sendMessagePersonal(@DestinationVariable Integer to, MessageDTO message) {
        messageService.sendMessage(to, message);
    }

    @GetMapping("/listmessage/{from}/{to}")
    public List<Map<String, Object>> getListMessageChat(@PathVariable("from") Integer from, @PathVariable("to") Integer to) {
        return messageService.getListMessage(from, to);
    }

    @MessageMapping("/chat/group/{to}")  // if a message is sent to "/chat/group/{to}", sendMessage is called
    public void sendMessageToGroup(@DestinationVariable Integer to, MessageGroupDTO message) {
        messageService.sendMessageGroup(to, message);
    }

    @GetMapping("/listmessage/group/{groupid}")
    public List<Map<String, Object>> getListMessageGroupChat(@PathVariable("groupid") Integer groupid) {
        return messageService.getListMessageGroups(groupid);
    }

    @GetMapping("/getallmanager")
    public List<Map<String, Object>> getAllManager() {
        return userAndGroupService.getAllManager();
    }

    @GetMapping("/getallcustomer")
    public List<Map<String, Object>> getAllCustomer() {
        return userAndGroupService.getAllCustomer();
    }

    @GetMapping("/getallgroup/{customerId}")
    public List<Map<String, Object>> getAllGroup(@PathVariable("customerId") Integer customerId) {
        return userAndGroupService.getAllGroup(customerId);
    }
}
