//package com.example.demo.controller;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.example.demo.entity.Customer;
//import com.example.demo.repository.CustomerRepository;
//import org.apache.logging.log4j.util.Strings;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping(value = "/login")
//public class CustomerLoginHandler {
//
//  @Autowired
//  CustomerRepository customerRepository;
//
//  @Autowired
//  JdbcTemplate jdbcTemplate;
//
//  @RequestMapping(method = RequestMethod.GET)
//  public String loginValidate(@RequestParam("name") String name,@RequestParam("loginpassword") String loginpassword){
//    String sql = "select loginpassword from customer where name=\'"+name+"\'";
//    jdbcTemplate.queryForObject(sql,String.class);
//
//    return jwtToken(name);
//  }
//
//  private String jwtToken( String name) {
//    int expireTime=3;
//    String secretMethod="1234";
//    return JWT.create()
//        .withClaim("name", name)
//        .sign(Algorithm.HMAC256(secretMethod));
//  }
//
//
//  @GetMapping("/login-user-info")
//  public String loginUserInfo(HttpServletRequest request) {
//    String token = request.getHeader(jwtHeader);
//    if (Strings.isNullOrEmpty(token)) {
//      return ReturnMsg.defaultSuccessResult();
//    }
//    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
//    try {
//      DecodedJWT verify = jwtVerifier.verify(token);
//      String username = verify.getClaim("username").asString();
//      String role = verify.getClaim("role").asString();
//      Long expireAt = verify.getClaim("expireAt").asLong();
//      //token参数不对
//      if (!Strings.isNullOrEmpty(username)
//          && !Strings.isNullOrEmpty(role) && expireAt != null
//          && expireAt > System.currentTimeMillis()) {
//        Optional<BuildingManagerBO> bm = buildingManagerService.findByUsername(username);
//        LoginUserInfoVO loginUserInfoVO = bm.map(bo -> new LoginUserInfoVO(token, role, bo.getUserId(), bo.getUsername(), bo.getLicence(),
//            bo.getBuildCount())).orElse(null);
//        return ReturnMsg.wrapSuccessfulResult(loginUserInfoVO);
//      }
//
//    } catch (JWTVerificationException ignore) {
//      //验证失败
//    }
//
//    return ReturnMsg.defaultSuccessResult();
//  }
//
//
//}
