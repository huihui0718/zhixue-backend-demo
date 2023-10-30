package me.zhengjie.modules.news.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.domain.NewsUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NewsUserMapper extends BaseMapper<NewsUser>{
    @Select("select n.* " +
            "from news n join news_user u on n.news_id = u.news_id " +
            "where u.user_id= #{userId}")
    IPage<News> findByUserId(Integer userId, Page<News> page);
}
