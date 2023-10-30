package me.zhengjie.modules.newchatroom.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.domain.NewChatUser;
import me.zhengjie.modules.newchatroom.service.NewChatUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/newChatUser")
public class NewChatUserController {

    private final NewChatUserService newChatUserService;

    @GetMapping("/{toUserId}")
    public ResponseEntity<Object> queryChat(Pageable pageable,Integer toUserId){

        return new ResponseEntity<>(newChatUserService.findAll(pageable,toUserId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createChat(@Validated @RequestBody NewChatUser resources){
        return new ResponseEntity<>(newChatUserService.create(resources),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids){
        newChatUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateById(@RequestBody  NewChatUser resources){
        newChatUserService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
