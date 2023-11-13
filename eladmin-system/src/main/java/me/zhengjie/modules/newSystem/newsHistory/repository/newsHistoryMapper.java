package me.zhengjie.modules.newSystem.newsHistory.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.newSystem.newsHistory.domain.NewsHistory;
import me.zhengjie.modules.news.domain.News;
import org.apache.ibatis.annotations.*;

@Mapper
public interface newsHistoryMapper extends BaseMapper<NewsHistory> {

    @Select("select * " +
            "from news n " +
            "join news_history h on n.news_id = h.news_id and h.user_id = #{userId} " +
            "${ew.customSqlSegment}")
    @Results({@Result(property = "newsId",column = "news_id")})
    IPage<News> getNewsByHistory(Page<News> newsPage,
                                 @Param("userId") Integer userId,
                                 @Param("ew") QueryWrapper<NewsHistory> qw);
}
