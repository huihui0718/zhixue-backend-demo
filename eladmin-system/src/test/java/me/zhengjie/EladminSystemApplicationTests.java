package me.zhengjie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.chatroom.repository.ChatMessageMapper;
import me.zhengjie.modules.chatroom.service.ChatMessageService;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.repository.NewChatMessageMapper;
import me.zhengjie.modules.newchatroom.repository.NewChatRoomMapper;
import me.zhengjie.modules.newchatroom.service.NewChatMessageService;
import me.zhengjie.modules.newchatroom.until.MessageSender;
import me.zhengjie.modules.news.domain.News;
import me.zhengjie.modules.news.repository.NewsUserMapper;
import me.zhengjie.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EladminSystemApplicationTests {

    @Autowired
    private NewChatMessageService newChatMessageService;
    @Autowired
    private  ChatMessageMapper chatMessageMapper;
    @Autowired
    private NewChatRoomMapper newChatRoomMapper;
    @Autowired
    private NewsUserMapper newsUserMapper;

    @Autowired
    private AmqpAdmin amqpAdmin;
    @Test
    public void createroom() {
        NewChatRoom newChatRoom = new NewChatRoom();
        newChatRoom.setCreatetime(new Timestamp(System.currentTimeMillis()));
        newChatRoom.setUserId(1);
        newChatRoom.setTitle("123");
        newChatRoom.setIsopen(1);
        System.out.println(newChatRoomMapper.insert(newChatRoom));
    }


    @Test
    public void sendmessage() throws IOException {
        WebSocketServer.sendInfoToString("1","111111");
    }

    @Test
    public void CRUDTEST(){
        Page<News> page = new Page<>(1, 5);
        IPage<News> newsCommentIPage = newsUserMapper.findByUserId(1,page);
        System.out.println(newsCommentIPage.getRecords());
    }

}

