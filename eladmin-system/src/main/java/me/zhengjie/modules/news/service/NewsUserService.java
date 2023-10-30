package me.zhengjie.modules.news.service;

import me.zhengjie.modules.news.domain.Criteria;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.domain.NewsUser;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsUserService {
    List<News> findAll(Pageable pageable);

    void deleteAll(Integer[] ids);

    NewsUser addNewsUser(NewsUser newsUser);
}
