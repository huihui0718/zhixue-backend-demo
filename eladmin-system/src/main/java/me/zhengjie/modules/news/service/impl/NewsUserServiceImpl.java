package me.zhengjie.modules.news.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.news.domain.Criteria;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.domain.NewsComment;
import me.zhengjie.modules.news.domain.NewsUser;
import me.zhengjie.modules.news.repository.NewsUserMapper;
import me.zhengjie.modules.news.service.NewsUserService;
import me.zhengjie.utils.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsUserServiceImpl implements NewsUserService {

    private final NewsUserMapper newsUserMapper;

    @Override
    public List<News> findAll(Pageable pageable) {
        Page<News> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        IPage<News> newsCommentIPage = newsUserMapper.findByUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()),page);
        return newsCommentIPage.getRecords();
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id: ids){
            newsUserMapper.deleteById(id);
        }
    }

    @Override
    public NewsUser addNewsUser(NewsUser newsUser) {
        newsUser.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newsUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return newsUser;
    }
}
