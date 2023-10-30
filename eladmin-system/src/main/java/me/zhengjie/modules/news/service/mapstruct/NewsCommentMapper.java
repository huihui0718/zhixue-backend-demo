package me.zhengjie.modules.news.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.domain.NewsComment;
import me.zhengjie.modules.news.service.dto.NewsCommentDto;
import me.zhengjie.modules.news.service.dto.NewsDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsCommentMapper extends BaseMapper<NewsCommentDto, NewsComment> {

}