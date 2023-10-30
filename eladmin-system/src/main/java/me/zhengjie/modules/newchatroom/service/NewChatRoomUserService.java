package me.zhengjie.modules.newchatroom.service;

import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.domain.NewChatRoomUser;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NewChatRoomUserService {
    void updateById(NewChatRoomUser resources);

    NewChatRoomUser create(NewChatRoomUser resources);

    void deleteAll(Integer[] ids);

    List<NewChatRoom> findAll(Pageable pageable);

    void create(Integer[] ids, Integer roomId);
}
