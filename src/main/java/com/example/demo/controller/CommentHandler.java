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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
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



  @GetMapping( "/{hotelname}")
  public List<Comment> getbyid(@PathVariable("hotelname") String hotelname){
    Hotel hotel=hotelRepository.findHotelByHotelname(hotelname);
    Integer id=hotel.getHotelid();
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


  @GetMapping( "getvideo/{vid}")
  public void getStreamData(HttpServletRequest request, HttpServletResponse response,@PathVariable("vid") String vid) throws IOException, ServletException {

    String realPath="D:\\OOADLab\\comment\\"+vid;
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




  @GetMapping("/getphoto/{picname}")
  //path 为图片在服务器的绝对路径
  public  void getPhoto(HttpServletResponse response,@PathVariable("picname") String picname) throws Exception {


    File file=new File("D:\\OOADLab\\comment\\"+picname);


    FileInputStream fis;
    assert file != null;
    fis = new FileInputStream(file);

    long size = file.length();
    byte[] temp = new byte[(int) size];
    fis.read(temp, 0, (int) size);
    fis.close();
    byte[] data = temp;
//    response.setContentType("image/png");
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



  @PostMapping(value = "/insert")
  @ResponseBody
  public String addComment(CommentInfo commentInfo) throws IOException {


    Integer maxId = jdbcTemplate.queryForObject("select MAX(commentid) from comment", Integer.class);
    if (maxId==null)maxId=0;

    Integer commentid =maxId+1;
    Integer score=commentInfo.getScore();
    String word=commentInfo.getWords();
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


    if (commentInfo.getPicture1()!=null) {
      String fileName=commentInfo.getPicture1().getOriginalFilename();
//      int index=fileName.lastIndexOf(".");
//      String suffix=fileName.substring(index);
      String suffix=".png";

      String path="D:\\OOADLab\\comment\\"+commentid +fileName+suffix;
      File dest = new File(path);
      //检测是否存在该目录
//      if (!dest.getParentFile().exists()){
//        dest.getParentFile().mkdirs();
//      }
      //写入文件
      commentInfo.getPicture1().transferTo(dest);


      pic1= "http://10.26.111.227:8888/comment/getphoto/"+commentid +fileName+suffix;
    }
    if (commentInfo.getPicture2()!=null){
      String fileName=commentInfo.getPicture2().getOriginalFilename();
//      int index=fileName.lastIndexOf(".");
//      String suffix=fileName.substring(index);
      String suffix=".png";
      String path="D:\\OOADLab\\comment\\"+commentid +fileName+suffix;
      File dest = new File(path);

      commentInfo.getPicture2().transferTo(dest);
      pic2="http://10.26.111.227:8888/comment/getphoto/"+commentid +fileName+suffix;

    }if (commentInfo.getPicture3()!=null){
      String fileName=commentInfo.getPicture3().getOriginalFilename();
//      int index=fileName.lastIndexOf(".");
//      String suffix=fileName.substring(index);
      String suffix=".png";
      String path="D:\\OOADLab\\comment\\"+commentid +fileName+suffix;
      File dest = new File(path);

      commentInfo.getPicture3().transferTo(dest);
      pic3="http://10.26.111.227:8888/comment/getphoto/"+commentid +fileName+suffix;

    }if (commentInfo.getVideo()!=null){
      String fileName=commentInfo.getVideo().getOriginalFilename();
//      int index=fileName.lastIndexOf(".");
//      String suffix=fileName.substring(index);
      String suffix=".mp4";
      String path="D:\\OOADLab\\comment\\"+commentid +fileName+suffix;
      File dest = new File(path);

      commentInfo.getVideo().transferTo(dest);
      vid="http://10.26.111.227:8888/comment/getvideo/"+commentid +fileName+suffix;

    }
    Comment comment=new Comment(commentid,score,commenttime,word,pic1,pic2,pic3,vid);
    commentRepository.save(comment);
//    Comment result = commentRepository.save(comment);
    return "insert ok";

  }

  @PostMapping(value = "/test")
  @ResponseBody
  public String test(UserFileReq req) {

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
    private String words;
    private MultipartFile picture1;
    private MultipartFile picture2;
    private MultipartFile picture3;
    private MultipartFile video;
  }

  @Data
  static
  class UserFileReq {
    // 参数
    private String username;
    // 文件
    private MultipartFile file;
    // 省略get\set
  }


}