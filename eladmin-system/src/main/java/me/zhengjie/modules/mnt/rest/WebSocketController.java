package me.zhengjie.modules.mnt.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Api(tags = "websocket")
@RequiredArgsConstructor
@RequestMapping("/api/websocket")
public class WebSocketController {

    private final WebSocketServer webSocketServer;

    @AnonymousAccess
    @GetMapping("roomId/{roomId}")
    public ResponseEntity<Object> sendMessage(@PathVariable String roomId,@RequestBody String message) throws IOException {
        WebSocketServer.sendInfoToString(message,roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
