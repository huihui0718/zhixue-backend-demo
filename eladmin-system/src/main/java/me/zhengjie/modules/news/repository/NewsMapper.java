package me.zhengjie.modules.news.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.newSystem.newsPost.domain.NewsPost;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.domain.NewsComment;
import me.zhengjie.modules.news.service.dto.NewsDto;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface NewsMapper extends BaseMapper<News> {

    @Update("update news n set news_like = n.news_like+1 where news_id = #{newsId}")
    Integer updateByNewsId1(Integer newsId);

    @Update("update news n set news_like = n.news_like-1 where news_id = #{newsId}")
    Integer updateByNewsId2(Integer newsId);

    @Update("update news n set news_star= n.news_star+1 where news_id = #{newsId}")
    Integer updateStarByNewsId1(Integer newsId);

    @Update("update news n set news_star = n.news_star-1 where news_id = #{newsId}")
    Integer updateStarByNewsId2(Integer newsId);

    @Select("SELECT n.*,u.nick_name,u.avatar_name" +
            "FROM news n " +
            "join sys_user u on n.user_id = u.user_id " +
            "where n.type = #{type} " +
            "and n.news_title like concat('%',#{search},'%') "+
            "ORDER BY ${sort} ${order}")
    @Results({
            @Result(property = "newsId", column = "news_id"),
            @Result(property = "nickName",column = "nick_name"),
            @Result(property = "avatarName",column = "avatar_name"),
            @Result(property = "newsComments", column = "news_id",
                    javaType = List.class, many = @Many(select = "findCommentsByNewsId")),
            @Result(property = "commentCount", column = "news_id",
                    javaType = Integer.class, one = @One(select = "countCommentsByNewsId"))})
    IPage<NewsDto>  findNewsWithComments(Page<News> newsPage,
                                         @Param("sort") String sort,
                                         @Param("order") String order,
                                         @Param("type") Integer type,
                                         @Param("search") String search);

    @Select("SELECT * " +
            "FROM news_post " +
            "WHERE news_id = #{newsId} " +
            "ORDER BY post_likes ASC " +
            "LIMIT 1")
    List<NewsPost> findCommentsByNewsId(Integer newsId);

    @Select("SELECT COUNT(*) " +
            "FROM news_post " +
            "WHERE news_id = #{newsId}")
    int countCommentsByNewsId(Integer newsId);

    @Select("select n.*,u.nick_name,u.avatar_name " +
            "from news n " +
            "join news_user_star nus on n.news_id = nus.news_id and nus.star_room_id = #{starRoomId} " +
            "join sys_user u on n.user_id = u.user_id " +
            "${ew.customSqlSegment}")
    @Results({
            @Result(property = "newsId", column = "news_id"),
            @Result(property = "nickName",column = "nick_name"),
            @Result(property = "avatarName",column = "avatar_name"),
            @Result(property = "commentCount", column = "news_id",
                    javaType = Integer.class, one = @One(select = "countCommentsByNewsId"))
    })
    IPage<NewsDto> getNewsByStarRoom(Page<News> newsPage,
                                     @Param("starRoomId") Integer starRoomId,
                                     @Param("ew") QueryWrapper<News> qw);
}
