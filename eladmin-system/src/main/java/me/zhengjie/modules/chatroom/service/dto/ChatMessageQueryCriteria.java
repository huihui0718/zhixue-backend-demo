package me.zhengjie.modules.chatroom.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;


@Data
public class ChatMessageQueryCriteria {
    @Query
    private Integer senderId;
    @Query
    private Integer userId;

}
