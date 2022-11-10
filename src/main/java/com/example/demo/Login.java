package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
  public String loginValidate(@RequestParam("name") String name, @RequestParam("loginpassword") String loginpassword) {
    String password = loginRepository.getPassword(name);
    if (loginpassword != null && password != null && loginpassword.equals(password)) {
      String token = jwtToken(name);
      //将token放入redis缓存
      redisUtil.set(name, token, 100);
      return token;
    } else {
      return "login error";
    }

  }

  private String jwtToken(String name) {
    String secretMethod = "1234";
    String JwtString = JWT.create()
        .withClaim("name", name)
        .sign(Algorithm.HMAC256(secretMethod));
    System.out.println(JwtString);
    decode(JwtString);
    return JwtString;
  }
  public void decode(String json) {
    DecodedJWT decode = JWT.decode(json);
    Claim name = decode.getClaim("name");
    System.out.println(name);
  }

}
