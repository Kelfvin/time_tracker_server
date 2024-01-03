package com.kelf.spring_boot.utils;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    static public void saveFile(MultipartFile file, String path, String fileName) throws IOException {
        // 判断存储文件的文件夹是否存在，不存在则创建
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 保存文件
        // 如果原始文件名存在是否会覆盖？
        // 会覆盖
        try {
            file.transferTo(new File(path, fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
