package me.zhengjie.modules.news.service;

import me.zhengjie.modules.news.domain.Criteria;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.domain.NewsComment;
import me.zhengjie.modules.news.service.dto.NewsCommentDto;
import me.zhengjie.modules.news.service.dto.NewsCommentQueryCriteria;
import me.zhengjie.modules.news.service.dto.NewsDto;
import me.zhengjie.modules.news.service.dto.NewsQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface NewsCommentService {
    NewsComment addComment(NewsComment newsComment);

    NewsComment addCommentByParentId(NewsComment newsComment);

    List<NewsComment> findCommentByCriteria(Integer newsId, String sortBy, String sortOrder, Pageable pageable);

    List<NewsComment> findCommentByParentId(Integer parentId, Pageable pageable);

    void deleteComment(Integer[] ids);
}
