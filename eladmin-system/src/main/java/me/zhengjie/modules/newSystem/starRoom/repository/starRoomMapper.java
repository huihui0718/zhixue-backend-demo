package me.zhengjie.modules.newSystem.starRoom.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.newSystem.starRoom.domain.StarRoom;
import org.apache.ibatis.annotations.*;

@Mapper
public interface starRoomMapper extends BaseMapper<StarRoom> {

    @Select("select * " +
            "from star_room " +
            "where user_id = #{userId} " +
            "order by ${sort} ${order}")
    @Results({
            @Result(property = "starRoomId",column = "star_room_id"),
            @Result(property = "starCount",column = "star_room_id",
            javaType = Integer.class,one = @One(select = "getStarCount"))
    })
    IPage<StarRoom> getStarRoomByPage(Page<StarRoom> starRooms,
                                      @Param(value = "sort") String sort,
                                      @Param(value = "order") String order,
                                      @Param(value = "userId") Integer userId);

    @Select("select count(*) " +
            "from news_user_star " +
            "where star_room_id=#{starRoomId}")
    Integer getStarCount(Integer starRoomId);
}
