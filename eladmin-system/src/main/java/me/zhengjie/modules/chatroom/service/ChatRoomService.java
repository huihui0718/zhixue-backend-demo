package me.zhengjie.modules.chatroom.service;

import me.zhengjie.modules.chatroom.domain.ChatRoom;

import java.util.List;

public interface ChatRoomService {

    ChatRoom create(ChatRoom chatRoom);

    List<ChatRoom> Retrieve(Integer userId);

    int delete(ChatRoom chatRoom);

    int update(ChatRoom chatRoom);
}
