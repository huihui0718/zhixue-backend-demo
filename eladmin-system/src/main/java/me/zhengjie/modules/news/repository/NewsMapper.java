package me.zhengjie.modules.news.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.news.domain.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}