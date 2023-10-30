package me.zhengjie.modules.newchatroom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.newchatroom.domain.NewChatComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewChatCommentMapper extends BaseMapper<NewChatComment> {
}
