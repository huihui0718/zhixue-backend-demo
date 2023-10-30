package me.zhengjie.modules.newchatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.domain.NewChatRoomUser;
import me.zhengjie.modules.news.domain.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NewChatRoomUserMapper extends BaseMapper<NewChatRoomUser> {
    @Select("select n.* " +
            "from new_chat_room n join new_chat_room_user u on n.id = u.room_id " +
            "where u.user_id= #{userId}")
    IPage<NewChatRoom> findByUserId(Integer userId, Page<NewChatRoom> page);
}
