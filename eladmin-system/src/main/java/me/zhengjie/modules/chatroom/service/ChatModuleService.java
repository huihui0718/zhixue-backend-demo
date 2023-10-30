package me.zhengjie.modules.chatroom.service;

import me.zhengjie.modules.chatroom.domain.ChatModule;
import java.util.List;

public interface ChatModuleService {
    ChatModule CreateChatModule(ChatModule resources);

    List<ChatModule> RetrieveChatModule();

    void UpdateChatModule(ChatModule resources);

    void DeleteChatModule(ChatModule resources);
}
