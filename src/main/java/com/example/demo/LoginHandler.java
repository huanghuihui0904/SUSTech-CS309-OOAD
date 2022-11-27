package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Login;
import com.example.demo.entity.Manager;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.ManagerRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
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
@Autowired
  ManagerRepository managerRepository;

@Data
class loginInfo implements Serializable {
  String token;
  int id;

  public loginInfo(String token, int id) {
    this.token = token;
    this.id = id;
  }
}
  @Data
  class managerloginInfo implements Serializable {
    String token;
    int id;
    String name;
    int hotelID;

    public managerloginInfo(int id,String name,int hotelID,String token) {
      this.name=name;
      this.hotelID=hotelID;
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
      redisUtil.set(login.getName(), token, 1000);
      loginInfo re=new loginInfo(token,c.get(0).getCustomerid());
      return re;
    } else {
      loginInfo re=new loginInfo(null,0);
      return re;
    }

  }
  @PostMapping("/manager")
  public managerloginInfo loginmanager(@RequestBody Login login) {
    String password = loginRepository.getManagerPassword(login.getName());
    //去数据库找用户id
    Manager m=managerRepository.findManagerByManagername(login.getName());

    //
    if (login.getLoginpassword() != null && password != null && login.getLoginpassword().equals(password)) {
      String token = jwtToken(login.getName());
      //将token放入redis缓存
      redisUtil.set(login.getName(), token, 1000);
      managerloginInfo re=new managerloginInfo(m.getManagerid(),m.getManagername(),m.getHotelid(),token);
      return re;
    } else {
      managerloginInfo re=new managerloginInfo(0,null,0,null);

      return re;
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
