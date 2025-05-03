package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@RequiredArgsConstructor
public class CommonController {
    
    private final MinioUtil minioUtil;
    
    /**
     * 文件上传到minio
     *
     * @param file 这个是SpringMVC提供的
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        
        minioUtil.uploadFile(fileName, file.getInputStream(), file.getContentType());
        return Result.success(minioUtil.getFileUrl(fileName));
    }
}
