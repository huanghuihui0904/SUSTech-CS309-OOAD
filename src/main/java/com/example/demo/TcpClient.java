package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

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

