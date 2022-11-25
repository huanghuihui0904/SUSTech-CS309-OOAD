package com.example.demo.controller;


import com.example.demo.NonStaticResourceHttpRequestHandler;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Orders;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.OrdersRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentHandler {
  @Autowired
  CommentRepository commentRepository;

  @Autowired
  HotelRepository hotelRepository;

  @Autowired
  OrdersRepository ordersRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;



  @GetMapping( "/{id}")
  public List<Comment> getbyid(@PathVariable("id") int id){
    Hotel hotel=hotelRepository.findHotelByHotelid(id);
    List<Orders> orders=ordersRepository.findOrdersByHotelid(id);
    List<Comment>commentList=new ArrayList<>();
    for (Orders o:
            orders) {
      Integer commentid=o.getCommentid();
      if (commentid!=null){
        Comment comment=commentRepository.findAllByCommentid(commentid);
        if (comment!=null && !commentList.contains(comment)){
          commentList.add(comment);
        }

      }

    }
    return commentList;
  }


  @GetMapping( "getvideo/{id}")
  public void getStreamData(HttpServletRequest request, HttpServletResponse response,@PathVariable("id") int id) throws IOException, ServletException {
    Comment comment=commentRepository.findAllByCommentid(id);
    String realPath="D:\\OOADLab\\comment\\"+comment.getVideo();
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




  @GetMapping("/getphoto/{id}/{pic}")
  //path 为图片在服务器的绝对路径
  public  void getPhoto(HttpServletResponse response,@PathVariable("id") int id,@PathVariable("pic") int pic) throws Exception {
    Comment comment=commentRepository.findAllByCommentid(id);
    File file = null;
    if (pic==1){
      file=new File("D:\\OOADLab\\comment\\"+comment.getPicture1());
    }else if (pic==2){
      file=new File("D:\\OOADLab\\comment\\"+comment.getPicture2());
    }else if (pic==3){
      file=new File("D:\\OOADLab\\comment\\"+comment.getPicture3());
    }

    FileInputStream fis;
    assert file != null;
    fis = new FileInputStream(file);

    long size = file.length();
    byte[] temp = new byte[(int) size];
    fis.read(temp, 0, (int) size);
    fis.close();
    byte[] data = temp;
    response.setContentType("image/png");
    OutputStream out = response.getOutputStream();
    out.write(data);
    out.flush();
    out.close();

  }




  @GetMapping("/findAll")
  public List findAll(){
    List<Comment> comments= commentRepository.findAll();
    return comments;
  }

  @Delete("/deletebyid")
  public String deletebyid(@RequestParam("id") int id){
    // 删除语句
    String sql = "delete from comment where commentid=?";
    jdbcTemplate.update(sql,id);
    // 查询
    return "delete by id Ok";

  }



  @PostMapping("/insert")
  public String addComment(@RequestBody CommentInfo commentInfo) throws IOException {


    Integer maxId = jdbcTemplate.queryForObject("select MAX(commentid) from comment", Integer.class);
    if (maxId==null)maxId=0;

    Integer commentid =maxId+1;
    Integer score=commentInfo.getScore();
    String word=commentInfo.getWord();
    Date now=new Date();
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String commenttime=format.format(now);

    String pic1=null;
    String pic2=null;
    String pic3=null;
    String vid=null;


    Integer orderid=commentInfo.getOrderid();
    String sql = "update orders set commentid=? where orderid=?";
    jdbcTemplate.update(sql,commentid,orderid);


    if (commentInfo.getPicture1()!=null&&commentInfo.getPicture1().length()>0) {
      String path=commentInfo.getPicture1();
      int index=path.lastIndexOf(".");
      String suffix=path.substring(index);
      start(commentInfo.getPicture1(), commentid +"_picture1"+suffix);
//      comment.setPicture1(id+"_picture1"+suffix);
      pic1= commentid +"_picture1"+suffix;
    }
    if (commentInfo.getPicture2()!=null&&commentInfo.getPicture2().length()>0){
      String path=commentInfo.getPicture2();
      int index=path.lastIndexOf(".");
      String suffix=path.substring(index);
      start(commentInfo.getPicture2(), commentid +"_picture2"+suffix);
//      comment.setPicture2(id+"_picture2"+suffix);
      pic2= commentid +"_picture2"+suffix;
    }if (commentInfo.getPicture3()!=null&&commentInfo.getPicture3().length()>0){
      String path=commentInfo.getPicture3();
      int index=path.lastIndexOf(".");
      String suffix=path.substring(index);
      start(commentInfo.getPicture3(), commentid +"_picture3"+suffix);
//      comment.setPicture3(id+"_picture3"+suffix);
      pic3= commentid +"_picture3"+suffix;
    }if (commentInfo.getVideo()!=null&&commentInfo.getVideo().length()>0){
      String path=commentInfo.getVideo();
      int index=path.lastIndexOf(".");
      String suffix=path.substring(index);
      start(commentInfo.getVideo(), commentid +"_video"+suffix);
//      comment.setVideo(id+"_video"+suffix);
      vid= commentid +"_video"+suffix;
    }
    Comment comment=new Comment(commentid,score,commenttime,word,pic1,pic2,pic3,vid);
    commentRepository.save(comment);
//    Comment result = commentRepository.save(comment);
    return "insert ok";
  }


  public void start(String path,String fileName) throws IOException {
//        创建本地流来读取本地文
    FileInputStream fis = new FileInputStream(path);
//    int index=path.lastIndexOf("\\");
//    String fileName=path.substring(index+1,path.length());
    //文件路径自己可以改，注意IDEA中Windows为双斜杠
    //创建socket
    Socket socket = new Socket("10.26.111.227",9999);//127.0.0.1为本机ip地址（默认）
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
  }

  @Data
  static
  class CommentInfo{
    private Integer orderid;
    private Integer score;
    private String word;
    private String picture1;
    private String picture2;
    private String picture3;
    private String video;
  }


}