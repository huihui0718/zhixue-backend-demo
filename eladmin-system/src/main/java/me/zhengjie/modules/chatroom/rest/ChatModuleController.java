package me.zhengjie.modules.chatroom.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.chatroom.domain.ChatModule;
import me.zhengjie.modules.chatroom.domain.ChatRoom;
import me.zhengjie.modules.chatroom.service.ChatModuleService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chatModule")
public class ChatModuleController {
    private final ChatModuleService chatModuleService;

    @GetMapping
    public ResponseEntity<Object> RetrieveChatModule(){
        return new ResponseEntity<>(chatModuleService.RetrieveChatModule(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> CreateChatModule(@RequestBody ChatModule resources){
        return new ResponseEntity<>(chatModuleService.CreateChatModule(resources),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> UpdateChatModule(@RequestBody ChatModule resources){
        chatModuleService.UpdateChatModule(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Object> DeleteChatModule(@RequestBody ChatModule resources) {
        chatModuleService.DeleteChatModule(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
