package me.zhengjie.modules.newchatroom.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.config.FileProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.newchatroom.domain.NewChatMessage;
import me.zhengjie.modules.newchatroom.repository.NewChatMessageMapper;
import me.zhengjie.modules.newchatroom.service.NewChatMessageService;
import me.zhengjie.util.WordUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import static me.zhengjie.utils.FileUtil.getExtensionName;


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
    public NewChatMessage createImage(MultipartFile file) throws IOException {
        // 文件大小验证
        FileUtil.checkSize(fileProperties.getAvatarMaxSize(), file.getSize());
        // 验证文件上传的格式
        File file1 = FileUtil.upload(file,  fileProperties.getPath().getPath());
        NewChatMessage newChatMessage = new NewChatMessage();
        String originalFilename = file.getOriginalFilename();
        // 获取后缀下标
        // 截取文件后缀
        String suffix = getExtensionName(file.getOriginalFilename());
        // 调用工具类
        newChatMessage.setPath(Objects.requireNonNull(file1).getPath());
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
        String fileType = getExtensionName(multipartFile.getOriginalFilename());
        if(fileType != null && !file1.contains(fileType)){
            throw new BadRequestException("文件格式错误！, 仅支持 " + file1 +" 格式");
        }
        File file = FileUtil.upload(multipartFile,  fileProperties.getPath().getPath());
        NewChatMessage newChatMessage = new NewChatMessage();
        newChatMessage.setRoomId(Integer.valueOf(roomId));
        newChatMessage.setType("FILE");
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
