package me.zhengjie.modules.newchatroom.rest;


import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.FileProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.chat.domain.Chat;
import me.zhengjie.modules.chat.service.ChatService;
import me.zhengjie.modules.chat.service.dto.ChatDto;
import me.zhengjie.modules.chatroom.domain.ChatModule;
import me.zhengjie.modules.chatroom.repository.ChatModuleMapper;
import me.zhengjie.modules.mnt.websocket.WebSocketServer;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.service.NewChatMessageService;
import me.zhengjie.util.KedaApi;
import me.zhengjie.util.Mp3ToPcm;
import me.zhengjie.util.RestTemplateUtils;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newChatRoomMessage")
public class NewChatMessageController {

    private final NewChatMessageService newChatMessageService;
    private final WebSocketServer webSocketServer;
    private final FileProperties fileProperties;
    private final ChatService chatService;
    private final ChatModuleMapper chatModuleMapper;

    @PostMapping
    public ResponseEntity<Object> addMessage(@RequestBody NewChatMessage newChatMessage)
    {
        String roomId = newChatMessage.getRoomId() + "";
        Set<String> sidset =webSocketServer.getUsersInRoom(roomId);
        newChatMessageService.create(newChatMessage);
        String jsonMessage = JSONUtil.toJsonStr(newChatMessage);
        if (sidset != null) {
            webSocketServer.sendMoreMessage(sidset, jsonMessage);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findByRoomId/{roomId}")
    public ResponseEntity<Object> findByRoomId(@PathVariable String roomId,
                                               Pageable pageable)
    {
        return new ResponseEntity<>(newChatMessageService.findAll(roomId,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/addImage")
    public ResponseEntity<Object> addImage(@RequestParam(value = "file") MultipartFile file,
                                           @RequestParam(value = "roomId") Integer roomId,
                                           @RequestParam(value = "moduleId") Integer moduleId,
                                           HttpServletRequest request) throws IOException, OpenXML4JException, XmlException {
        FileUtil.checkSize(fileProperties.getAvatarMaxSize(), file.getSize());
        String TYPE = "doc docx pdf mp3";
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if(suffix!=null&&!TYPE.contains(suffix)){
            throw  new BadRequestException("仅支持doc,docx,pdf,mp3类型的文件");
        }
        String type = FileUtil.getFileType(suffix);
        File file1 = FileUtil.upload(file,  fileProperties.getPath().getPath() + type +  File.separator);
        String fileType = FileUtil.getExtensionName(file.getOriginalFilename());
        Chat resources = new Chat();
        String result="";
        resources.setDate(new Timestamp(System.currentTimeMillis()));
        resources.setPid(0);
        resources.setType(0);
        resources.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        resources.setRoomId(roomId);
        resources.setChatType(type);
        resources.setPath(file1.getPath());
        resources.setPathName(file1.getName());
        resources.setChatLike(0);
        ChatDto chatDto = chatService.create(resources);
        Integer pid = chatDto.getId();
        ChatModule chatModule = chatModuleMapper.selectById(moduleId);
        if (fileType.equals("docx")) {
            assert file1 != null;
            OPCPackage opcPackage = POIXMLDocument.openPackage(file1.getPath());
            POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
            result = extractor.getText();
            extractor.close();
        }
        else if(fileType.equals("doc")) {
            InputStream is;
            assert file1 != null;
            is = new FileInputStream(file1.getPath());
            WordExtractor re = new WordExtractor(is);
            result = re.getText();
                re.close();
            }
        else if (fileType.equals("pdf")){
            StringBuilder result1 = new StringBuilder();
            FileInputStream is;
            assert file1 != null;
            is = new FileInputStream(file1);
            PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
            parser.parse();
            PDDocument doc = parser.getPDDocument();
            PDFTextStripper textStripper = new PDFTextStripper();
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                textStripper.setStartPage(i);
                textStripper.setEndPage(i);
                textStripper.setSortByPosition(true);
                String s = textStripper.getText(doc);
                result1.append(s);
            }
            result=result1.toString();
            doc.close();
        }
        else if (fileType.equals("mp3")){
            String filePath = file1.getPath();
            String pcmFilePath = filePath.replace(".mp3", ".pcm");
            String wavFilePath = filePath.replace(".mp3", ".wav");
            Mp3ToPcm.Mp3ToPcm(filePath,pcmFilePath);
            Mp3ToPcm.pcmToWav(pcmFilePath,wavFilePath);
            KedaApi.getApi(wavFilePath);
        }
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", result);
//        List<ArrayList> history = new ArrayList<>();
//        //history.add(new ArrayList<>());
//        requestBody.put("history",history);
        try{
            Map post = RestTemplateUtils.post(chatModule.getModuleUrl(), requestBody, Map.class,request);
            Chat chat = new Chat();
            chat.setPid(pid);
            chat.setDate(new Timestamp(System.currentTimeMillis()));
            chat.setContent(post.get("response").toString());
            chat.setRoomId(resources.getRoomId());
            chat.setSenderId(0);
            chat.setType(1);
            chat.setChatLike(0);
            chat.setChatType("TEXT");
            chat.setUserId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
            ChatDto response = chatService.create(chat);
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }catch (Exception e) {
            throw new BadRequestException("请求超时");
        }
    }

    @GetMapping("/findByType/{roomId}")
    public ResponseEntity<Object> findByType(@PathVariable String roomId,
                                               @RequestParam String type,
                                               Pageable pageable)
    {
        return new ResponseEntity<>(newChatMessageService.findByType(roomId,type,pageable),HttpStatus.OK);
    }
}
