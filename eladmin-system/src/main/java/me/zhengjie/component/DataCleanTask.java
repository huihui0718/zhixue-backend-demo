package me.zhengjie.component;


import me.zhengjie.modules.chatroom.repository.ChatMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataCleanTask {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨 3 点执行任务
    public void cleanExpiredData() {
        // 删除过期数据的代码
        // 计算一个月前的时间
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        chatMessageMapper.deleteByCreateTime(oneMonthAgo);
    }
}
