package me.zhengjie.modules.newchatroom.rest;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newchatroom.domain.NewChatComment;
import me.zhengjie.modules.newchatroom.repository.NewChatCommentMapper;
import me.zhengjie.modules.newchatroom.service.NewChatCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newChatComment")
public class NewChatCommentController {
    private final NewChatCommentService newChatCommentService;

    @PostMapping
    public ResponseEntity<Object> insertComment(@RequestBody NewChatComment newChatComment){
        return new ResponseEntity<>(newChatCommentService.insert(newChatComment), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getComment(@PathVariable Integer parentId){
        return new ResponseEntity<>(newChatCommentService.get(parentId), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteComment(@RequestParam Integer[] ids){
        newChatCommentService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
