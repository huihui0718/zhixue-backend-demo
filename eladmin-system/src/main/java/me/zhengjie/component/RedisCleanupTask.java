package me.zhengjie.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newSystem.newsPostUserLike.domain.NewsPostUserLike;
import me.zhengjie.modules.newSystem.newsPostUserLike.repository.NewsPostUserLikeRepository;
import me.zhengjie.modules.newSystem.newsPostUserLike.repository.newsPostUserLikeMapper;
import me.zhengjie.modules.newSystem.newsUserLike.domain.NewsUserLike;
import me.zhengjie.modules.newSystem.newsUserLike.repository.NewsUserLikeRepository;
import me.zhengjie.modules.newSystem.newsUserLike.repository.newsUserLikeMapper;
import me.zhengjie.utils.RedisUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisCleanupTask {

    private final RedisUtils redisUtils;
    private final newsUserLikeMapper newsUserLikeMapper;
    private final newsPostUserLikeMapper newsPostUserLikeMapper;
    private final NewsUserLikeRepository newsUserLikeRepository;
    private final NewsPostUserLikeRepository newsPostUserLikeRepository;
    // 指定定时任务的执行时间间隔为2小时（单位：毫秒）
    @Scheduled(fixedDelay = 2*60*60* 1000)
    public void cleanupRedisData() {
        Map<Object, Object> redisData = redisUtils.hmget("NEWS_USER_LIKE");
        for (Map.Entry<Object, Object> entry : redisData.entrySet()) {
            String key = (String) entry.getKey();
            Boolean value = (Boolean) entry.getValue();
            String[] strings = key.split("::");
            NewsUserLike newsUserLike = newsUserLikeMapper.selectOne(new QueryWrapper<NewsUserLike>()
                            .eq("news_id",strings[0])
                            .eq("user_id",strings[1]));
            // 如果不存在点赞的对象，且redis中的点赞对象为真
            if(newsUserLike==null&&value){
                NewsUserLike newsUserLike1 = new NewsUserLike();
                newsUserLike1.setNewsId(Integer.valueOf(strings[0]));
                newsUserLike1.setUserId(Integer.valueOf(strings[1]));
                newsUserLike1.setCreateTime(new Timestamp(System.currentTimeMillis()));
                newsUserLikeRepository.save(newsUserLike1);
            // 如果存在点赞对象，且redis中点赞的对象为假
            } else if (newsUserLike!=null&&!value) {
                newsUserLikeRepository.delete(newsUserLike);
            }
        }
        redisUtils.del("NEWS_USER_LIKE");
    }

    @Scheduled(fixedDelay = 2*60*60* 1000)
    public void cleanupRedisData1() {
        Map<Object, Object> redisData = redisUtils.hmget("NEWS_POST_USER_LIKE");
        for (Map.Entry<Object, Object> entry : redisData.entrySet()) {
            String key = (String) entry.getKey();
            Boolean value = (Boolean) entry.getValue();
            String[] strings = key.split("::");
            NewsPostUserLike newsPostUserLike = newsPostUserLikeMapper.selectOne(new QueryWrapper<NewsPostUserLike>()
                    .eq("news_id",strings[0])
                    .eq("user_id",strings[1]));
            // 如果不存在点赞的对象，且redis中的点赞对象为真
            if(newsPostUserLike==null&&value){
                NewsPostUserLike newsPostUserLike1 = new NewsPostUserLike();
                newsPostUserLike1.setId(Integer.valueOf(strings[0]));
                newsPostUserLike1.setUserId(Integer.valueOf(strings[1]));
                newsPostUserLike1.setCreatrTime(new Timestamp(System.currentTimeMillis()));
                newsPostUserLikeRepository.save(newsPostUserLike1);
            // 如果存在点赞对象，且redis中点赞的对象为假
            } else if (newsPostUserLike!=null&&!value) {
                newsPostUserLikeRepository.delete(newsPostUserLike);
            }
        }
        redisUtils.del("NEWS_POST_USER_LIKE");
    }
}
