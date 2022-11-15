package com.example.demo.dto;

//Data Transfer Object
public class MessageGroupDTO extends MessageDTO{
    private Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "MessageGroupDTO{" +
                "groupId=" + groupId +
                '}';
    }
}
