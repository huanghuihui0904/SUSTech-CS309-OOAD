//package com.example.demo;
//
//import com.example.demo.entity.xy;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//
//class DemoApplicationTests {
//  public static void main(String[] args) {
//    System.out.println(outDate(2021,365));
//
//
//  }
//  private static int outDay(int year, int month, int day) {
//
//    //定义数组，这里2月的天数取28，假设是平年。
//    int[] Day = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
//
//    //如果是闰年，那么2月应该有29天。
//    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
//      Day[1] = 29;
//    }
//
//    //进行判断
//    if (year <= 0 || month <= 0 || month > 12 || day <= 0 || day > Day[month - 1]){
//      return -1;
//    }
//
//    int sum = 0;
//    for (int i = 0; i < month - 1; i++) {
//      sum += Day[i];
//    }
//
//    //最后sum需要加上当前月份的日期。
//    System.out.println(sum + day);
//    return sum + day;
//  }
//public static String outDate(int year,int day){
//  int[] Days = {0,31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
//
//  if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
//    Days[2] = 29;
//  }
//
//  int whichm=1;
//  int whichday=day;
//
//  while (whichm<13&&whichday-Days[whichm]>0){
//    whichday-=Days[whichm];
//    whichm++;
//    if(whichm>=13){
//      return "-1";
//    }
//  }
//
//  String time="";
//  time = (year) + "-" + fillZero(whichm) + "-" + fillZero(whichday);
////  System.out.println(time);
//
//  return time;
//}
//  public static String fillZero(int t) {
//    if (t < 10) {
//      return "0" + t;
//    } else {
//      return "" + t;
//    }
//  }
//
//}
