package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.UserToken;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.LoginRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/login")
public class Login {

  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  LoginRepository loginRepository;

  @Autowired
  RedisUtil redisUtil;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(method = RequestMethod.GET)
  public String loginValidate(@RequestParam("name") String name,@RequestParam("loginpassword") String loginpassword){
    String password=loginRepository.getPassword(name);
if(loginpassword!=null&&password!=null&&loginpassword.equals(password)){
 String token=jwtToken(name);

 //将token放入redis缓存
redisUtil.set(name,token,100);

  return token;





}else {
  return "login error";
}

  }

  private String jwtToken(String name ) {

    int expireTime=3;
    String secretMethod="1234";
    String JwtString= JWT.create()
        .withClaim("name", name)
        .sign(Algorithm.HMAC256(secretMethod));
    System.out.println(JwtString);
    decode(JwtString);
return JwtString;
  }


  @GetMapping("/login-user-info")
  public String loginUserInfo(HttpServletRequest request) {
return "a";
  }

  public void decode(String json){
    DecodedJWT decode=JWT.decode(json);
    Claim name=decode.getClaim("name");
    System.out.println(name);
  }

}
