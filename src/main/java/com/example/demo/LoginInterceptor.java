package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  RedisUtil redisUtil;

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
  }

  /**
   * 进入controller前
   *
   * @param request
   * @param response
   * @param handler
   * @return
   * @throws Exception
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    System.out.println("############进入用户token验证拦截器");
    String uri = request.getRequestURI().toString();
    String token = request.getHeader("token");
    String name = request.getHeader("name");

    System.out.println("token"+token);
    if (null == token){
      System.out.println("##########当前用户未登录");
      return false;
    }
    String redisToken = (String) redisUtil.get(name);

    if(redisToken == null){
      System.out.println("##########当前用户token在redis中不存在,currentUser:{}");
      return false;
    }

    Long expire = redisUtil.getExpire(name);

    if(expire <= 0){
      System.out.println("##########当前用户token已失效,currentUser:{}");
      return false;
    }

    //为当前登录用户重置登录活性
    redisUtil.set(name,token,100);

    return true;
  }
}
