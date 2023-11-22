package me.zhengjie.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newSystem.newsHistory.domain.NewsHistory;
import me.zhengjie.modules.newSystem.newsHistory.repository.NewsHistoryRepository;
import me.zhengjie.modules.newSystem.newsHistory.repository.newsHistoryMapper;
import me.zhengjie.modules.newSystem.newsPostUserLike.domain.NewsPostUserLike;
import me.zhengjie.modules.newSystem.newsPostUserLike.repository.NewsPostUserLikeRepository;
import me.zhengjie.modules.newSystem.newsPostUserLike.repository.newsPostUserLikeMapper;
import me.zhengjie.modules.newSystem.newsUserLike.domain.NewsUserLike;
import me.zhengjie.modules.newSystem.newsUserLike.repository.NewsUserLikeRepository;
import me.zhengjie.modules.newSystem.newsUserLike.repository.newsUserLikeMapper;
import me.zhengjie.utils.RedisUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisCleanupTask {

    private final RedisUtils redisUtils;
    private final newsUserLikeMapper newsUserLikeMapper;
    private final newsPostUserLikeMapper newsPostUserLikeMapper;
    private final NewsUserLikeRepository newsUserLikeRepository;
    private final NewsPostUserLikeRepository newsPostUserLikeRepository;
    // 查询历史信息的持久层
    private final newsHistoryMapper newsHistoryMapper;
    // 保存历史信息的持久层
    private final NewsHistoryRepository newsHistoryRepository;
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

    @Scheduled(fixedDelay = 2*60*60* 1000)
    public void cleanupRedisNewsHistory() {
        List<String> keyLists = redisUtils.scan("NEWS_HISTORY:");
        for(String key : keyLists){
            String userId = key.substring(13);
            Set<ZSetOperations.TypedTuple<Object>> results = redisUtils.getSetWithScore(key,0,-1);
            for (ZSetOperations.TypedTuple<Object> tuple : results) {
                String member = (String) tuple.getValue();
                long score =Long.parseLong(tuple.getScore().toString());
                NewsHistory newsHistory = newsHistoryMapper.selectOne(new QueryWrapper<NewsHistory>()
                                .eq("userId",userId).eq("news_id",member));
                if(newsHistory!=null){
                    newsHistory.setCreateTime(new Timestamp(score));
                    newsHistoryRepository.save(newsHistory);
                }
                else {
                    NewsHistory newsHistory1 = new NewsHistory();
                    newsHistory1.setCreateTime(new Timestamp(score));
                    newsHistory1.setNewsId(Integer.valueOf(member));
                    newsHistory1.setUserId(Integer.valueOf(userId));
                    newsHistoryRepository.save(newsHistory1);
                }
            }
        }
    }
}
