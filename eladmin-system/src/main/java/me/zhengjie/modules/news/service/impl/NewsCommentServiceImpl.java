package me.zhengjie.modules.news.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.news.domain.Criteria;
import me.zhengjie.modules.news.domain.NewsComment;
import me.zhengjie.modules.news.repository.NewsCommentMapper;
import me.zhengjie.modules.news.service.NewsCommentService;
import me.zhengjie.utils.AliPayStatusEnum;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

import static org.apache.commons.lang.CharSetUtils.count;

@Service
@RequiredArgsConstructor
public class NewsCommentServiceImpl implements NewsCommentService {

    private final NewsCommentMapper newsCommentMapper;

    @Override
    public NewsComment addComment(NewsComment newsComment) {
        newsComment.setParentId(0);
        newsComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newsComment.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newsCommentMapper.insert(newsComment);
        return newsComment;
    }

    @Override
    public NewsComment addCommentByParentId(NewsComment newsComment) {
        newsComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newsComment.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newsCommentMapper.insert(newsComment);
        newsCommentMapper.insert(newsComment);
        return newsComment;
    }

    @Override
    public List<NewsComment> findCommentByCriteria(Integer newsId, String sortBy, String sortOrder, Pageable pageable) {
        Page<NewsComment> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        IPage<NewsComment> newsCommentIPage = newsCommentMapper.findByNewsId(newsId,sortBy,sortOrder,page);
        return newsCommentIPage.getRecords();
    }

    @Override
    public List<NewsComment> findCommentByParentId(Integer parentId, Pageable pageable) {
        Page<NewsComment> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<NewsComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        IPage<NewsComment> newsCommentIPage = newsCommentMapper.selectPage(page,queryWrapper);
        return newsCommentIPage.getRecords();
    }

    @Override
    public void deleteComment(Integer[] ids) {
        for (Integer newsId : ids) {
            newsCommentMapper.deleteById(newsId);
        }
    }
}
