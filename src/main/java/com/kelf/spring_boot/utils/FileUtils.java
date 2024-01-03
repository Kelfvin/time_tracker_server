package com.kelf.spring_boot.utils;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import   java.awt.Color;



public class FileUtils {

    // 图片文件格式转换，全部转换成JPG格式
    static public String convertToJpeg(String path, String fileName) throws  IOException{

        // 检测如果是jpg格式，直接返回
        if (fileName.endsWith(".jpg")) {
            return fileName;
        }

        // 如果不是jpg格式，转换成jpg格式
        String targetFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";
        // 使用ImageIO读取图片，然后保存到新的文件
        File rawFile = new File(path, fileName);
        BufferedImage bufferedImage = ImageIO.read(rawFile);
            // 创建一个jpeg格式的图片文件


        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        //由于png格式是32位，jpg格式是24位，因此要进行位数转换
        //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

        // 目标文件
        //        创建一个jpeg格式的图片文件
        File targetFile = new File(path, targetFileName);

        // 写入JPEG格式的图片
        ImageIO.write(newBufferedImage, "jpg", targetFile);
        // 删除
        rawFile.delete();

            return targetFileName;
    }


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

    static public String saveAvatar(MultipartFile file, String path, String fileName) throws IOException{
        saveFile(file, path, fileName);
        String targetName =  convertToJpeg(path, fileName);
        return targetName;
    }
}
