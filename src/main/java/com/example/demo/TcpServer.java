package com.example.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void start() throws IOException {
        //创建服务器
        ServerSocket server = new ServerSocket(8080);
        //获取服务器id、
        InetAddress address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        System.out.println("服务端ip地址: " + ip );
        while(true) {
            // 创建Socket 接受网络请求
            Socket socket = server.accept();
            // 多线程，提高效率
            new  Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //获取网络输入流，读取网络数据
                        InputStream in = socket.getInputStream();
                        //j建立本地文件
                        File file = new File("src/main/java/com/example/demo/received");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        // 自定义一个接受文件，命名
                        // 域名 + 时间 +随机数
//                        String fileName = "ns.code" + System.currentTimeMillis()
//                                + new Random().nextInt(999) + ".png";
                        // 本地输出流，保存数据
                        String fileName = null;
                        FileOutputStream fos = null;
                        int len = 0;
                        byte[] bytes = new byte[1024];
                        int count=0;
                        
                        while ((len = in.read(bytes)) != -1) {
                            if ( count==0){
                                fileName=new String(bytes);
                                System.out.println(fileName);
                                fos= new FileOutputStream(file + "\\" + fileName);
                                count++;
                            }else {
                                fos.write(bytes);
                            }
                        }

                        // 网络输出流，回复客户端
                        socket.getOutputStream().write("上传成功！".getBytes());
                        System.out.println("接收到文件"+fileName);


                        byte[] bs = new byte[1024];
                        while ((len = in.read(bs)) != -1) {
                            System.out.println(new String(bs));
                        }


                        fos.close();
                        socket.close();
                    } catch (IOException e){
                        System.out.println(e);
                    }
                }
            }).start();
        }
    }
}
