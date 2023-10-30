package me.zhengjie.modules.newchatroom.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.newchatroom.domain.NewChatRoom;
import me.zhengjie.modules.newchatroom.domain.NewChatRoomUser;
import me.zhengjie.modules.newchatroom.service.NewChatRoomUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newChatRoomUser")
public class NewChatRoomUserController {

    private final NewChatRoomUserService newChatRoomUserService;

    @GetMapping
    public ResponseEntity<Object> findAll(Pageable pageable){
        return new ResponseEntity<>(newChatRoomUserService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Object> create(@RequestBody Integer[] ids,@PathVariable Integer roomId){
        newChatRoomUserService.create(ids,roomId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids){
        newChatRoomUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateById(@RequestBody NewChatRoomUser resources){
        newChatRoomUserService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
