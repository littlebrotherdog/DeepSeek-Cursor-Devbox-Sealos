package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = minioUtil.upload(file);
            return Result.success(url);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }
} 