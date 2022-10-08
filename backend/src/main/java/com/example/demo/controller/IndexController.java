package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//templates目录下所有页面只能通过controller来跳转
@Controller
public class IndexController {

    @RequestMapping("/")
     public String index(Model model){
//        model.addAttribute("msg","<h1>hello,springboot</h1>");
//        model.addAttribute("users", Arrays.asList(1,2,3));
        return "index";
    }


}
