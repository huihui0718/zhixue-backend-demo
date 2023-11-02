package me.zhengjie.modules.newchatroom.service;

import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface NewChatMessageService {

    int create(NewChatMessage resources);

    List<NewChatMessage> findByRoomId(String roomId);

    NewChatMessage createImage(MultipartFile multipartFile) throws IOException;

    NewChatMessage createFile(MultipartFile multipartFile,String roomId);

    List<NewChatMessage> findAll(String roomId, Pageable pageable);

    List<NewChatMessage> findByType(String roomId, String type, Pageable pageable);
}
