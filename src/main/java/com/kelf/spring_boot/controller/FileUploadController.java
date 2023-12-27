package com.kelf.spring_boot.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
public class FileUploadController {
    @PostMapping
    @RequestMapping("/upload")
    public String upload(String username, MultipartFile avatar, HttpServletRequest request) {
        System.out.println(username);
        System.out.println(avatar.getOriginalFilename());

        String path = request.getServletContext().getRealPath("/upload");
        System.out.println(path);

        try {
            saveFile(avatar, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";

    }

    public void saveFile(MultipartFile file, String path) throws IOException {
        // 判断存储文件的文件夹是否存在，不存在则创建
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 保存文件
        try {
            file.transferTo(new File(path, file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
