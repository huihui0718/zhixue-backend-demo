package me.zhengjie.modules.news.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.domain.NewsComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NewsCommentMapper extends BaseMapper<NewsComment> {
    @Select("SELECT nc.*, COUNT(sc.id) AS commentCount " +
            "FROM news_comment nc LEFT JOIN news_comment sc ON nc.id = sc.parent_id " +
            "WHERE nc.news_id = #{newsId} GROUP BY nc.id"+
            "ORDER BY ${sortBy} ${sortOrder}")
    IPage<NewsComment> findByNewsId(@Param("newsId") Integer newsId,
                                    @Param("sortBy") String sortBy,
                                    @Param("sortOrder") String sortOrder,
                                    Page<NewsComment> page);
}
