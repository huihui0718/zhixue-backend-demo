package me.zhengjie.modules.newchatroom.service;

import me.zhengjie.modules.newchatroom.domain.NewChatComment;
import me.zhengjie.modules.newchatroom.repository.NewChatCommentMapper;

import java.util.List;

public interface NewChatCommentService {


    NewChatComment insert(NewChatComment newChatComment);

    List<NewChatComment> get(Integer parentId);

    void delete(Integer[] ids);
}
