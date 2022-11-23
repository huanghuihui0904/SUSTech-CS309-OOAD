package com.example.demo;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
@RestController
@RequestMapping(value = "/upload")
public class TcpClient {

    @PostMapping("/picture")
    public String start(@RequestBody Picture picture) throws IOException {
//        创建本地流来读取本地文
        String path= picture.getPath();
        FileInputStream fis = new FileInputStream(path);
        int index=path.lastIndexOf("\\");
        String fileName=path.substring(index+1,path.length());
        //文件路径自己可以改，注意IDEA中Windows为双斜杠
        //创建socket
        Socket socket = new Socket("127.0.0.1",8080);//127.0.0.1为本机ip地址（默认）
        //获取网络输出流 发送数据
        OutputStream os = socket.getOutputStream();
        byte[]head=new byte[1024];
        byte[] temp=fileName.getBytes();
        for (int i = 0; i < temp.length; i++) {
            head[i]=temp[i];
        }
        for (int i = temp.length; i <1024 ; i++) {
            head[i]=(byte)32;
        }
        os.write(head);
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes))!= -1){
            os.write(bytes);
        }

        socket.shutdownOutput(); //关闭输出流，否则会堵塞输入流接受
        // 获取网络输入流接受数据
        InputStream in = socket.getInputStream();
        while ((len = in.read(bytes))!= -1){
            System.out.println(new String(bytes,0,len));
        }

        fis.close();
        socket.close();
        return path;
    }


    @Data
    static
    class Picture{
        String path;

    }


}

