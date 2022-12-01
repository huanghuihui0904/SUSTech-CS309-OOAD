package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
import javax.swing.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/manager")
public class ManagerHandler {
  @Autowired
  ManagerRepository managerRepository;
  @Autowired
  OrdersRepository ordersRepository;
@Autowired
  RoomRepository roomRepository;
  @Autowired
  RoomTypeRepository roomTypeRepository;

  @Autowired
  HotelRepository hotelRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;


  @GetMapping("/orderedRoomNums")
  public int getOrderedRoomNums() {

    List<Room> rooms=roomRepository.findAll();
    int num=0;
    for (int i = 0; i < rooms.size(); i++) {
      String isorder=rooms.get(i).getIsordered();
      String[] orders=isorder.split(",");
      for (int j = 0; j <orders.length ; j++) {
        if(orders[j].equals("1")){
          num++;
        }
      }
    }
    return num;
  }

  @GetMapping("/currentCustomer")
  public int getCurrentCustomer() {
    Integer nums = jdbcTemplate.queryForObject("select count(*) from orders where checkouttime is null;", Integer.class);
    return nums;
  }

  @GetMapping("/hotRoomType")
  public String getHotRoomTypeName() {
    String roomName = jdbcTemplate.queryForObject("with result as (select roomtypeid ri, count(*) num from orders group by roomtypeid order by num desc limit 1)\n" +
        "select roomname\n" +
        "from roomtype r\n" +
        "         join result on roomtypeid = result.ri;", String.class);
    return roomName;
  }

  @GetMapping("/hotHotel")
  public String getHotHotel() {
    String hotelName = jdbcTemplate.queryForObject("with result as (select hotelid hi, count(*) num from orders group by hotelid order by num desc limit 1)\n" +
        "select hotelname\n" +
        "from hotel r\n" +
        "         join result on hotelid = result.hi;", String.class);
    return hotelName;
  }

  @GetMapping("/hotCity")
  public String getHotCity() {
    String cityName = jdbcTemplate.queryForObject("with result as (select hotelid hi, count(*) num from orders group by hotelid )\n" +
        "select cityname from(\n" +
        "select cityname,sum(num) from(\n" +
        "select *\n" +
        "from hotel r\n" +
        "         join result on hotelid = result.hi)a group by cityname order by sum desc limit 1)b;", String.class);
    return cityName;
  }



  //class customerTimeInfo{
//
//    String time;
//    Integer y;
//
//    public customerTimeInfo(String time, Integer y) {
//        this.time = time;
//        this.y = y;
//    }
//
//    public customerTimeInfo() {
//    }
//}
  @GetMapping("/customer-time")
  public List<xy> customerTime(@RequestParam("city") String city) {
    Date date = new Date();
    Calendar c = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String cur = formatter.format(c.getTime());
    System.out.println(formatter.format(c.getTime()));
    List<Orders> orders = ordersRepository.findAll();
    List<String[]> orderArray = new ArrayList();
    int lastday = 0;
    int[][] ymd = new int[5][373];
    for (int i = 0; i < orders.size(); i++) {
      String ordertime = orders.get(i).getOrdertime().substring(0, 10);

      String[] time = ordertime.split("-");
      int y = Integer.parseInt(time[0]) - 2020;
      int m = Integer.parseInt(time[1]);
      int d = Integer.parseInt(time[2]);
      int whichday = outDay(y, m, d);

      int hotelid = orders.get(i).getHotelid();
      Hotel hotel = hotelRepository.findHotelByHotelid(hotelid);
      System.out.println(hotel.getCityname());
      System.out.println(city);
      if(city.equals("")){
        int roomtypeid = orders.get(i).getRoomtypeid();
        RoomType rt = roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeid);
        int guestnum = rt.getNumber();
        ymd[y][whichday] = ymd[y][whichday] + guestnum;
        if (i + 2020 == 2022 && ymd[y][whichday] != 0 && whichday > lastday) {
          lastday = whichday;

        }
      }
      else {
        if (hotel.getCityname().equals(city)) {

          int roomtypeid = orders.get(i).getRoomtypeid();
          RoomType rt = roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeid);
          int guestnum = rt.getNumber();
          ymd[y][whichday] = ymd[y][whichday] + guestnum;
          if (i + 2020 == 2022 && ymd[y][whichday] != 0 && whichday > lastday) {
            lastday = whichday;

          }

        }
      }


    }


    String lastdate = outDate(2022, lastday);
    if (lastdate.compareTo(cur) > 0) {

    } else {
      lastdate = cur;
    }
    System.out.println("lastday" + lastdate);
    List<xy> result = new ArrayList<>();
    boolean br = false;
    for (int i = 0; i < ymd.length; i++) {
      for (int j = 1; j < ymd[i].length; j++) {
        String time = outDate(2020 + i, j);
        if (!time.equals("-1")) {
          System.out.println(time);
          xy re = new xy(time, ymd[i][j]);
          if (time.equals(lastdate)) {
            result.add(re);
            br = true;

          } else {
            result.add(re);
          }
        } else {
          break;
        }

        if (br) {
          break;
        }


      }
      if (br) {
        break;
      }
    }


    return result;
  }


  public static String fillZero(int t) {
    if (t < 10) {
      return "0" + t;
    } else {
      return "" + t;
    }
  }




  @GetMapping("/sales")
  public List<xy> sales(@RequestParam("city") String city) {
    Date date = new Date();
    Calendar c = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String cur = formatter.format(c.getTime());
    System.out.println(formatter.format(c.getTime()));
    List<Orders> orders = ordersRepository.findAll();
    List<String[]> orderArray = new ArrayList();
    int lastday = 0;
    int[][] ymd = new int[5][373];
    for (int i = 0; i < orders.size(); i++) {
      String ordertime = orders.get(i).getOrdertime().substring(0, 10);

      String[] time = ordertime.split("-");
      int y = Integer.parseInt(time[0]) - 2020;
      int m = Integer.parseInt(time[1]);
      int d = Integer.parseInt(time[2]);
      int whichday = outDay(y, m, d);

      int hotelid = orders.get(i).getHotelid();
      Hotel hotel = hotelRepository.findHotelByHotelid(hotelid);
      System.out.println(hotel.getCityname());
      System.out.println(city);
      if(city.equals("")){
        int price = orders.get(i).getAmountpaid();
        ymd[y][whichday] = ymd[y][whichday] + price;
        if (i + 2020 == 2022 && ymd[y][whichday] != 0 && whichday > lastday) {
          lastday = whichday;

        }
      }else {
        if (hotel.getCityname().equals(city)) {

          int price = orders.get(i).getAmountpaid();
          ymd[y][whichday] = ymd[y][whichday] + price;
          if (i + 2020 == 2022 && ymd[y][whichday] != 0 && whichday > lastday) {
            lastday = whichday;

          }

        }
      }



    }


    String lastdate = outDate(2022, lastday);
    if (lastdate.compareTo(cur) > 0) {

    } else {
      lastdate = cur;
    }
    System.out.println("lastday" + lastdate);
    List<xy> result = new ArrayList<>();
    boolean br = false;
    for (int i = 0; i < ymd.length; i++) {
      for (int j = 1; j < ymd[i].length; j++) {
        String time = outDate(2020 + i, j);
        if (!time.equals("-1")) {
          System.out.println(time);
          xy re = new xy(time, ymd[i][j]);
          if (time.equals(lastdate)) {
            result.add(re);
            br = true;

          } else {
            result.add(re);
          }
        } else {
          break;
        }

        if (br) {
          break;
        }


      }
      if (br) {
        break;
      }
    }


    return result;
  }
@Data
  class salsebysityInfo implements Serializable {
String city;
int sales;

    public salsebysityInfo(String city, int sales) {
      this.city = city;
      this.sales = sales;
    }

    public salsebysityInfo() {
    }
  }
  @GetMapping("/salesByCity")
  public List<salsebysityInfo> salsebycity(){
    //shanghai guangzhou shzh chongqing
    String[] cityname=new String[]{"上海","广州","深圳","重庆"};
int[] sales=new int[4];
    List<Orders> orders = ordersRepository.findAll();
    for (int i = 0; i < orders.size(); i++) {
      Hotel hotel=hotelRepository.findHotelByHotelid(orders.get(i).getHotelid());
      for (int j = 0; j < cityname.length; j++) {
        if(hotel.getCityname().equals(cityname[j])){
          sales[j]+=orders.get(i).getAmountpaid();
        }
      }
    }
    List<salsebysityInfo> re=new ArrayList<>();
    for (int i = 0; i < cityname.length; i++) {
      salsebysityInfo t=new salsebysityInfo(cityname[i],sales[i]);
      re.add(t);
    }
    return re;
  }
  @GetMapping("/salesByHotel")
  public List<salsebysityInfo> salesByHotel(){
    //shanghai guangzhou shzh chongqing
    List<Hotel> hotels=hotelRepository.findAll();
    List<String>hotelnames=new ArrayList<>();
    int[] sales=new int[hotels.size()];
    for (int i = 0; i < hotels.size(); i++) {
      hotelnames.add(hotels.get(i).getHotelname());
    }

    List<Orders> orders = ordersRepository.findAll();
    for (int i = 0; i < orders.size(); i++) {
      Hotel hotel=hotelRepository.findHotelByHotelid(orders.get(i).getHotelid());
      for (int j = 0; j < hotelnames.size(); j++) {
        if(hotel.getHotelname().equals(hotelnames.get(j))){
          sales[j]+=orders.get(i).getAmountpaid();
        }
      }
    }
    List<salsebysityInfo> re=new ArrayList<>();
    for (int i = 0; i < hotelnames.size(); i++) {
      salsebysityInfo t=new salsebysityInfo(hotelnames.get(i),sales[i]);
      re.add(t);
    }
    return re;
  }

  public static int outDay(int year, int month, int day) {

    //定义数组，这里2月的天数取28，假设是平年。
    int[] Day = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    //如果是闰年，那么2月应该有29天。
    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
      Day[1] = 29;
    }

    //进行判断
    if (year <= 0 || month <= 0 || month > 12 || day <= 0 || day > Day[month - 1]) {
      return -1;
    }

    int sum = 0;
    for (int i = 0; i < month - 1; i++) {
      sum += Day[i];
    }

    //最后sum需要加上当前月份的日期。
    System.out.println(sum + day);
    return sum + day;
  }

  public static String outDate(int year, int day) {
    int[] Days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
      Days[2] = 29;
    }

    int whichm = 1;
    int whichday = day;
    while (whichm < 13 && whichday - Days[whichm] > 0) {
      whichday -= Days[whichm];
      whichm++;
      if (whichm >= 13) {
        return "-1";
      }
    }

    String time = "";
    time = (year) + "-" + fillZero(whichm) + "-" + fillZero(whichday);
    System.out.println(time);

    return time;
  }


}
