package me.zhengjie.modules.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.system.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper1 extends BaseMapper<User> {
}
