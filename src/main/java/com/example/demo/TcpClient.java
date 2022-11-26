package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/upload")
@AllArgsConstructor
public class TcpClient {

    @Autowired
    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @GetMapping("/video/1")
    public void videoPreview2(HttpServletRequest request, HttpServletResponse response) throws Exception {





        //假如我把视频1.mp4放在了static下的video文件夹里面
        //sourcePath 是获取resources文件夹的绝对地址
        //realPath 即是视频所在的磁盘地址
        String sourcePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String realPath = "C:\\Users\\pc\\Desktop\\test11\\CKplayer\\CKplayer\\ckplayer\\video\\1_0.mp4";


        Path filePath = Paths.get(realPath );
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }



    @RequestMapping("/stream")
    public void getStreamData(HttpServletResponse response) {
        File file=new File("C:\\Users\\pc\\Desktop\\test11\\CKplayer\\CKplayer\\ckplayer\\video\\1_0.mp4");
        ServletOutputStream out=null;
        try {
            FileInputStream instream=new FileInputStream(file);
            byte[] b=new byte[1024];
            int length=0;
            BufferedInputStream buf=new BufferedInputStream(instream);
            out=response.getOutputStream();
            BufferedOutputStream bot=new BufferedOutputStream(out);
            while((length=buf.read(b))!=-1) {
                bot.write(b,0, b.length);
            }
        } catch (Exception  e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Data
    static
    class Picture{
        String path;

    }


}

