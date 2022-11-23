package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
 public static List<String[]> decodeJSON(String str){
   String cur=str.replace("{","").replace("}","");
   String[] arr=cur.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);
   List<String[]> re=new ArrayList<>();
   for (int i = 0; i <arr.length ; i++) {
     String pairstr=arr[i].trim().replace("\"","");
     String[] pair=pairstr.split(":");
     re.add(pair);
   }
return re;
 }}
