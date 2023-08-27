package com.kece.fanta.controller;

import com.kece.fanta.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;


/*
* 文件上传和下载
*/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${picture.path}")
    private String basePath;
    @PostMapping("/upload")
    // MultipartFile file中的属性名字“file”必须和请求头的数据name=“xxx”保持一致
    public R<String> upload(MultipartFile file){ // file是一个临时文件，需要转存到指定位置，否则本次请求后就会消失
        log.info(file.toString());

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //substring(from,to) 左闭右开，to可以不写
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //  使用UUID重新生成文件名，防止文件被覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        // 创建一个目录对象
        File dir = new File(basePath);
        // 判断目录是否存在
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            // 将临时文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    //文件下载
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response  ){
        try {
            // 输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));

            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
            outputStream.flush();

            // 关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
