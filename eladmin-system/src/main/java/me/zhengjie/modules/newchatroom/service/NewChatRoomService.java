package me.zhengjie.modules.newchatroom.service;


import me.zhengjie.modules.newchatroom.domain.ChatUser;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.repository.ChatUserMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface NewChatRoomService {
    
    List<NewChatRoom> findbyid(Integer userid);


    NewChatRoom create(NewChatRoom resources);

    void updateById(NewChatRoom resources);

    void deleteAll(Integer[] ids);

    List<NewChatRoom> findAll(Pageable pageable);

    List<ChatUser> findOnlinUser(String roomId);
}
