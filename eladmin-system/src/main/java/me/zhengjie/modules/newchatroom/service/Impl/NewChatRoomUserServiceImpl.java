package me.zhengjie.modules.newchatroom.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.domain.NewChatRoomUser;
import me.zhengjie.modules.newchatroom.repository.NewChatRoomUserMapper;
import me.zhengjie.modules.newchatroom.service.NewChatRoomUserService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewChatRoomUserServiceImpl implements NewChatRoomUserService {

    private final NewChatRoomUserMapper newChatRoomUserMapper;

    @Override
    public void updateById(NewChatRoomUser resources) {
        newChatRoomUserMapper.updateById(resources);
    }

    @Override
    public NewChatRoomUser create(NewChatRoomUser resources) {
        resources.setCreateBy(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newChatRoomUserMapper.insert(resources);
        return resources;
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids){
            newChatRoomUserMapper.deleteById(id);
        }
    }

    @Override
    public List<NewChatRoom> findAll(Pageable pageable) {
        Page<NewChatRoom>  newChatRooms =  new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        IPage<NewChatRoom> newChatRoomIPage = newChatRoomUserMapper.findByUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()),newChatRooms);
        return newChatRoomIPage.getRecords();
    }

    @Override
    public void create(Integer[] ids, Integer roomId) {
        NewChatRoomUser newChatRoomUser = new NewChatRoomUser();
        newChatRoomUser.setRoomId(roomId);
        newChatRoomUser.setCreateBy(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newChatRoomUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        for (Integer id : ids){
            newChatRoomUser.setUserId(id);
            newChatRoomUserMapper.insert(newChatRoomUser);
        }
    }
}
