package me.zhengjie.modules.newchatroom.service.Impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import me.zhengjie.modules.newchatroom.domain.NewChatUser;
import me.zhengjie.modules.newchatroom.repository.NewChatRoomUserMapper;
import me.zhengjie.modules.newchatroom.repository.NewChatUserMapper;
import me.zhengjie.modules.newchatroom.service.NewChatUserService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NewChatUserServiceImpl implements NewChatUserService {

    private final NewChatUserMapper newChatUserMapper;
    private final WebSocketServer webSocketServer;

    @Override
    public List<NewChatUser> findAll(Pageable pageable, Integer toUserId) {
        Page<NewChatUser>  newChatUserPage = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        Integer senderId = Math.toIntExact(SecurityUtils.getCurrentUserId());
        QueryWrapper<NewChatUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(i -> i.eq("sender_id", senderId).eq("to_user_id", toUserId))
                .or(j -> j.eq("sender_id", toUserId).eq("to_user_id", senderId))
                .orderByDesc("id");
        IPage<NewChatUser> newChatUserIPage = newChatUserMapper.selectPage(newChatUserPage,queryWrapper);
        return newChatUserIPage.getRecords();
    }

    @Override
    public NewChatUser create(NewChatUser resources) {
        resources.setTime(new Timestamp(System.currentTimeMillis()));
        newChatUserMapper.insert(resources);
        String jsonMessage = JSONUtil.toJsonStr(resources);
        webSocketServer.sendOneMessage(resources.getToUserId(), jsonMessage);
        return resources;
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids){
            newChatUserMapper.deleteById(id);
        }
    }

    @Override
    public void updateById(NewChatUser resources) {
         newChatUserMapper.updateById(resources);
    }
}
