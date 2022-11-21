package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/customer")
public class CustomerHandler {
  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  HotelRepository hotelRepository;

  @Autowired
  HotelWishListRepository hotelWishListRepository;

  @Autowired
  RoomTypeRepository roomTypeRepository;

  @Autowired
  RoomTypeWishListRepository roomTypeWishListRepository;

  @Autowired
  OrdersRepository ordersRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
  public Customer getbyid(@RequestParam("id") int id){
    Customer customer=  customerRepository.findByCustomerid(id);
    return customer;
  }



  @RequestMapping(value = "/getbyparameters",method = RequestMethod.GET)
  public List<CustomerInfoA> getByParameters(@RequestParam(required = false,value = "id") Integer id, @RequestParam(required = false,value = "customerName")String customerName, @RequestParam(required = false,value = "telephone") String telephone){
    if (id==null&&(customerName==null||customerName.equals(""))&&(telephone==null||telephone.equals(""))){
      return null;
    }
    List<Customer> customers=customerRepository.findAll();
    if (id!=null){
      List<Customer> customerbyid=  customerRepository.findCustomersByCustomerid(id);
      customers= (List<Customer>) CollectionUtils.union(customers,customerbyid);
      customers=customerbyid;

    }
    if (customerName!=null && !customerName.equals("")){
      List<Customer> customerbyname=customerRepository.findCustomersByName(customerName);
      customers= (List<Customer>) CollectionUtils.intersection(customerbyname,customers);
    }
    if (telephone!=null && !telephone.equals("")){
      List<Customer> customerbytelephone=customerRepository.findCustomersByTelephone(telephone);
      customers= (List<Customer>) CollectionUtils.intersection(customerbytelephone,customers);
    }
    List<CustomerInfoA> fin=new ArrayList<>();
    for (Customer c:
        customers) {
      fin.add(new CustomerInfoA(c));
    }
    return fin;

  }


  @GetMapping("/findAll")
  public List<Customer> findAll(){
    return customerRepository.findAll();
  }

  @PostMapping("/deletebyid")
  public String deletebyid(@RequestParam("id") int id){
    // 删除语句
    String sql = "delete from customer where customerid=?";
    jdbcTemplate.update(sql,id);
    // 查询
    return "delete by id Ok";
  }

  @PostMapping("/money")
  public boolean modifyMoney(@RequestParam("id") int id,@RequestParam("money")int money){
    // 删除语句
    Customer customer=customerRepository.findByCustomerid(id);
    Integer currentMoney=customer.getMoney();
    if (currentMoney+money>=0){
      String sql = "update customer set money=?  where customerid=?";
      jdbcTemplate.update(sql,(money+currentMoney),id);
      return true;

    }

    // 查询
    return false;
  }

  @PostMapping("/credits")
  public boolean addCredits(@RequestParam("id") int id,@RequestParam("credits")int credits){
    // 删除语句
    Customer customer=customerRepository.findByCustomerid(id);
    Integer currentCredits=customer.getCredits();
    if (currentCredits+credits>=0){
      String sql = "update customer set credits=?  where customerid=?";
      jdbcTemplate.update(sql,(credits+currentCredits),id);
      return true;
    }

    // 查询
    return false;
  }






    @PostMapping("/createcustomer")
    public boolean createCustomer(@RequestBody CustomerInfo customerInfo){

        Integer maxId = jdbcTemplate.queryForObject("select MAX(customerid) from customer", Integer.class);
        if (maxId==null)maxId=0;
        Customer customer=new Customer(0,0,customerInfo.getName(),customerInfo.getLoginpassword(),customerInfo.getTelephone(),0,0);
        customer.setCustomerid(maxId+1);
        List<Customer> customer1=customerRepository.findCustomersByName(customerInfo.getName());
        List<Customer> customer2=customerRepository.findCustomersByTelephone(customerInfo.getTelephone());
        Collection<Customer>customers=CollectionUtils.intersection(customer1,customer2);
        if ( customers.size()==0){
            Customer result = customerRepository.save(customer);
            return true;
        }else {
            return false;
        }

    }

  @Data
  static
  class CustomerInfo{
    private String name;
    private String loginpassword;
    private String telephone;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getLoginpassword() {
      return loginpassword;
    }

    public void setLoginpassword(String loginpassword) {
      this.loginpassword = loginpassword;
    }

    public String getTelephone() {
      return telephone;
    }

    public void setTelephone(String telephone) {
      this.telephone = telephone;
    }
  }

  @Data
  class CustomerInfoA{
    Integer customerid;
    String customerName;
    String telephone;
    String favoriteRoomType;
    String favoriteHotel;
    List<Orders> orders;


    public CustomerInfoA(Customer customer) {
      customerid=customer.getCustomerid();
      customerName=customer.getName();
      telephone=customer.getTelephone();
      RoomTypeWishList roomTypeWishList=roomTypeWishListRepository.findRoomTypeWishListByRoomtypewishlistid(customer.getRoomtypewishlistid());
      if ( roomTypeWishList!=null){
        favoriteRoomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomTypeWishList.getRoomtypeid()).getRoomname();
      }
      HotelWishList hotelWishList=hotelWishListRepository.findHotelWishListByHotelwishlistid(customer.getHotelwishlistid());
      if (hotelWishList!=null){
        favoriteHotel=hotelRepository.findHotelByHotelid(hotelWishList.getHotelid()).getHotelname();
      }

      orders=ordersRepository.findOrdersByCustomerid(customer.getCustomerid());

    }
  }


}
