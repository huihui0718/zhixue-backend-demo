package me.zhengjie.modules.newSystem.newsPost.repository;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import me.zhengjie.modules.newSystem.newsPost.domain.NewsPost;
import me.zhengjie.modules.newSystem.newsPost.service.dto.NewsPostDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface newsPostMapper extends BaseMapper<NewsPost> {

    @Select("select p.*,u.nick_name,u.avatar_name " +
            "from news_post p join sys_user u on p.user_id = u.user_id " +
            "where news_id = #{newsId} and parent_id = 0")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "nickName",column = "nick_name"),
            @Result(property = "avatarName",column = "avatar_name"),
            @Result(property = "newsPosts",column = "id",javaType = List.class,many = @Many(select = "findPostByParentId")),
            @Result(property = "postCounts",column = "id",javaType = Integer.class,one = @One(select = "findPostCountsByParentId"))
    })
    IPage<NewsPost> getPostByParentId(@Param(value = "newsId")Integer newsId, Page<NewsPost> posts);

    @Select("select p.*,u.nick_name,u.avatar_name " +
            "from news_post p join sys_user u on p.user_id = u.user_id " +
            "where parent_id = #{parentId} " +
            "order by create_time ASC " +
            "limit 3")
    List<NewsPostDto> findPostByParentId(Integer parentId);

    @Select("SELECT COUNT(*) " +
            "FROM news_post " +
            "WHERE parent_id = #{parentId} ")
    int findPostCountsByParentId(Integer parentId);

    @Update("update news_post p " +
            "set news_post = p.post_likes + 1 " +
            "where id = #{parentId}")
    int updateByPostId(Integer parentId);

    @Update("update news_post p " +
            "set news_post = p.post_likes - 1 " +
            "where id = #{parentId}")
    int updateByPostId2(Integer parentId);
}
