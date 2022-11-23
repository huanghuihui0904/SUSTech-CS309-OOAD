package com.example.demo.service;

import com.example.demo.dto.MessageDTO;
import com.example.demo.dto.MessageGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {
    Calendar calendar = Calendar.getInstance();

//    @Autowired
    // send messages to users who have subscribed to a specific topic.
//    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;


    //used in websocket personal chat
    //store a message into database and send back to destination subscribe topic
    public void sendMessage(Integer to, MessageDTO messageDTO) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;   //month from 0~11, 1 for offset
        int d = calendar.get(Calendar.DATE);
//        int h2 = calendar.get(Calendar.HOUR_OF_DAY);
//        int min = calendar.get(Calendar.MINUTE);
//        int s = calendar.get(Calendar.SECOND);
        String curTime = y + "-" + m + "-" + d;
//        String curTime = y + "-" + m + "-" + d + " " + h2 + ":" + min + ":" + s;
//        System.out.println("messageFromId: " + messageDTO.getFromLogin());
//        System.out.println("messageToId: " + to);
        System.out.println("curTime: " + curTime);
        System.out.println("content: " + messageDTO.getMessage());
        int fromId = messageDTO.getFromLogin();
        String str;
        try {
            str = jdbcTemplate.queryForObject("select name from customer where customerID = ?;", new Object[]{fromId}, java.lang.String.class);
        } catch (EmptyResultDataAccessException e) {
            str = jdbcTemplate.queryForObject("select managername from manager where managerid = ?;", new Object[]{fromId}, java.lang.String.class);
        }
        System.out.println("str: " + str);
        jdbcTemplate.update("insert into message(messageFromId, messageFromName, messageToId, messageTime, content) " +
                "values (?, ?, ?, ?, ?);", fromId, str, to, curTime, messageDTO.getMessage());
//        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, messageDTO);
    }


    //get the list of message between any two user
    public List<Map<String, Object>> getListMessage(
            @PathVariable("from") Integer from, @PathVariable("to") Integer to) {
        return jdbcTemplate.queryForList("select * from message\n" +
                "where (messageFromId = ? and messageToId = ?)\n" +
                "   or (messageToId = ? and messageFromId = ?)\n" +
                "order by messageId", from, to, from, to);
    }

    //retrieve a list of messages from user in group chat
    public List<Map<String, Object>> getListMessageGroups(@PathVariable("groupid") Integer groupid) {
        return jdbcTemplate.queryForList(
                "select gm.*, m.managerName as name\n" +
                        "from group_message gm\n" +
                        "         join manager m on m.managerID = gm.user_id\n" +
                        "where gm.groupId = ?\n" +
                        "union\n" +
                        "select gm.*, c.name as name\n" +
                        "from group_message gm\n" +
                        "         join customer c on c.customerID = gm.user_id\n" +
                        "where gm.groupId = ?\n" +
                        "order by groupMessageId;", groupid, groupid);
    }

    //storing a message into database and send back to destination subscribe topic group.
    public void sendMessageGroup(Integer to, MessageGroupDTO messageGroupDTO) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;   //month from 0~11, 1 for offset
        int d = calendar.get(Calendar.DATE);
//        int h2 = calendar.get(Calendar.HOUR_OF_DAY);
//        int min = calendar.get(Calendar.MINUTE);
//        int s = calendar.get(Calendar.SECOND);
        String curTime = y + "-" + m + "-" + d;
//        String curTime = y + "-" + m + "-" + d + " " + h2 + ":" + min + ":" + s;
        jdbcTemplate.update(
                "insert into group_message(groupId, user_id, content, messageTime)\n" +
                        "VALUES (?, ?, ?, ?);", to, messageGroupDTO.getFromLogin(), messageGroupDTO.getMessage(), curTime);
        messageGroupDTO.setGroupId(to);
//        simpMessagingTemplate.convertAndSend("/topic/messages/group/" + to, messageGroupDTO);
    }
}