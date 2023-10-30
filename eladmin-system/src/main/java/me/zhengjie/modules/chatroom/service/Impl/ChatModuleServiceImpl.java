package me.zhengjie.modules.chatroom.service.Impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.chatroom.domain.ChatModule;
import me.zhengjie.modules.chatroom.repository.ChatModuleMapper;
import me.zhengjie.modules.chatroom.service.ChatModuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatModuleServiceImpl implements ChatModuleService {

    private final ChatModuleMapper chatModuleMapper;

    @Override
    public ChatModule CreateChatModule(ChatModule resources) {
        chatModuleMapper.insert(resources);
        return resources;
    }

    @Override
    public List<ChatModule> RetrieveChatModule() {
        return chatModuleMapper.selectList(null);
    }

    @Override
    public void UpdateChatModule(ChatModule resources) {
        chatModuleMapper.updateById(resources);
    }

    @Override
    public void DeleteChatModule(ChatModule resources) {
        chatModuleMapper.deleteById(resources);
    }
}
