package me.zhengjie.modules.newchatroom.service;

import me.zhengjie.modules.newchatroom.domain.NewChatUser;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.DecimalMax;
import java.util.List;

public interface NewChatUserService {
    List<NewChatUser> findAll(Pageable pageable, Integer roomId);

    NewChatUser create(NewChatUser resources);

    void deleteAll(Integer[] ids);

    void updateById(NewChatUser resources);
}
