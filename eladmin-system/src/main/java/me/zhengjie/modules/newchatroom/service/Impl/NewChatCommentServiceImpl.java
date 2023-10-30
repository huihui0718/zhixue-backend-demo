package me.zhengjie.modules.newchatroom.service.Impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import me.zhengjie.modules.newchatroom.domain.NewChatComment;
import me.zhengjie.modules.newchatroom.repository.NewChatCommentMapper;
import me.zhengjie.modules.newchatroom.service.NewChatCommentService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class NewChatCommentServiceImpl implements NewChatCommentService {

    private final NewChatCommentMapper newChatCommentMapper;
    private final WebSocketServer webSocketServer;

    @Override
    public NewChatComment insert(NewChatComment newChatComment) {
        newChatComment.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newChatComment.setTimestamp(new Timestamp(System.currentTimeMillis()));
        newChatComment.setType("COMMENT");
        newChatCommentMapper.insert(newChatComment);
        Set<String> sidset =webSocketServer.getUsersInRoom(newChatComment.getRoomId()+"");
        if (!sidset.isEmpty()) {
            String jsonMessage = JSONUtil.toJsonStr(newChatComment);
            webSocketServer.sendMoreMessage(sidset,jsonMessage);
            return newChatComment;
        }
        return null;
    }

    @Override
    public List<NewChatComment> get(Integer parentId) {
        QueryWrapper<NewChatComment>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        return newChatCommentMapper.selectList(queryWrapper);
    }

    @Override
    public void delete(Integer[] ids) {
        for (Integer id : ids) {
            newChatCommentMapper.deleteById(id);
        }
    }
}
