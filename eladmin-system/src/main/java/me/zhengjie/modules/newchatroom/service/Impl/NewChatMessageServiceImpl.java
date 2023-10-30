package me.zhengjie.modules.newchatroom.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.config.FileProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.chatroom.domain.ChatMessage;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.repository.NewChatMessageMapper;
import me.zhengjie.modules.newchatroom.service.NewChatMessageService;
import me.zhengjie.modules.newchatroom.until.MessageSender;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class NewChatMessageServiceImpl implements NewChatMessageService {

    private final NewChatMessageMapper newChatMessageMapper;
    private final FileProperties fileProperties;

    @Override
    public int create(NewChatMessage resources) {
        resources.setType("TEXT");
        resources.setTimestamp(new Timestamp(System.currentTimeMillis()));
        resources.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        return newChatMessageMapper.insert(resources);
    }

    @Override
    public List<NewChatMessage> findByRoomId(String roomId) {
        QueryWrapper<NewChatMessage> wrapper = new QueryWrapper<>();
        wrapper.eq("room_id",roomId);
        List<NewChatMessage> newChatMessages = newChatMessageMapper.selectList(wrapper);
        return newChatMessages;
    }

    @Override
    public NewChatMessage createImage(MultipartFile multipartFile,String roomId) {
        // 文件大小验证
        FileUtil.checkSize(fileProperties.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        if(fileType != null && !image.contains(fileType)){
            throw new BadRequestException("文件格式错误！, 仅支持 " + image +" 格式");
        }
        File file = FileUtil.upload(multipartFile,  fileProperties.getPath().getPath());
        NewChatMessage newChatMessage = new NewChatMessage();
        newChatMessage.setRoomId(Integer.valueOf(roomId));
        newChatMessage.setType("IMAGE");
        newChatMessage.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newChatMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        newChatMessage.setPath(Objects.requireNonNull(file).getPath());
        newChatMessage.setPathName(file.getName());
        newChatMessageMapper.insert(newChatMessage);
        return newChatMessage;
    }

    @Override
    public NewChatMessage createFile(MultipartFile multipartFile,String roomId) {
        // 文件大小验证
        FileUtil.checkSize(fileProperties.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String file1 = "text doc docx pdf";
        String fileType = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        if(fileType != null && !file1.contains(fileType)){
            throw new BadRequestException("文件格式错误！, 仅支持 " + file1 +" 格式");
        }
        File file = FileUtil.upload(multipartFile,  fileProperties.getPath().getPath());
        NewChatMessage newChatMessage = new NewChatMessage();
        newChatMessage.setRoomId(Integer.valueOf(roomId));
        newChatMessage.setType("FILE");
        newChatMessage.setSenderId(Math.toIntExact(SecurityUtils.getCurrentUserId()));
        newChatMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        newChatMessage.setPath(Objects.requireNonNull(file).getPath());
        newChatMessage.setPathName(file.getName());
        newChatMessageMapper.insert(newChatMessage);
        return newChatMessage;
    }

    @Override
    public List<NewChatMessage> findAll(String roomId, Pageable pageable) {
        Page<NewChatMessage> newChatMessagePage = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<NewChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id",roomId)
                    .orderByDesc("id");
        IPage<NewChatMessage> chatMessageIPage = newChatMessageMapper.selectPage(newChatMessagePage,queryWrapper);
        return  chatMessageIPage.getRecords();
    }

    @Override
    public List<NewChatMessage> findByType(String roomId, String type, Pageable pageable) {
        Page<NewChatMessage> newChatMessagePage = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<NewChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",type)
                .orderByDesc("id");
        IPage<NewChatMessage> chatMessageIPage = newChatMessageMapper.selectPage(newChatMessagePage,queryWrapper);
        return  chatMessageIPage.getRecords();
    }
}
