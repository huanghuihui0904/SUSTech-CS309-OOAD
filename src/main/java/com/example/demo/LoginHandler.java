package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Login;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.LoginRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/login")
public class LoginHandler {

  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  LoginRepository loginRepository;
  @Autowired
  RedisUtil redisUtil;

  @Autowired
  JdbcTemplate jdbcTemplate;


@Data
class loginInfo{
  String token;
  int id;

  public loginInfo(String token, int id) {
    this.token = token;
    this.id = id;
  }
}
  @PostMapping
  public loginInfo loginValidate(@RequestBody Login login) {
    String password = loginRepository.getPassword(login.getName());
    //去数据库找用户id
List<Customer> c=customerRepository.findCustomersByName(login.getName());

    //
    if (login.getLoginpassword() != null && password != null && login.getLoginpassword().equals(password)) {
      String token = jwtToken(login.getName());
      //将token放入redis缓存
      redisUtil.set(login.getName(), token, 100);
      loginInfo re=new loginInfo(token,c.get(0).getCustomerid());
      return re;
    } else {
      return null;
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
