package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



@RestController
@RequestMapping(value = "/orders")
public class OrdersHandler {
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RoomRepository roomRepository;


    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getbyid",method = RequestMethod.GET)
    public Orders getbyid(@RequestParam("id") int id){
        Orders order=  ordersRepository.findOrderByOrderid(id);
        return order;
    }


    @GetMapping(value = "/numberofguest")
    public int NumberOfGuest() throws ParseException {
        int numbers=0;
        List<Orders> orders=ordersRepository.findAll();
        for (Orders order:orders){
            String checkouttime=order.getCheckouttime();
            String checkintime=order.getCheckintime();
            Date now=new Date();
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date checkout=format.parse(checkouttime);
            Date checkin=format.parse(checkintime);
            if (checkout.getTime()>now.getTime() && checkin.getTime()<=now.getTime()){
                Integer roomtypeid=order.getRoomtypeid();
                RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeid);
                numbers+=roomType.getNumber();
            }


        }
        return numbers;
    }


    @GetMapping(value = "/popularroomtype")
    public  List<String> PopularRoomType()  {

        List<Map<String,Object>> mapList= jdbcTemplate.queryForList("select count(*)as count,roomtypeid from orders group by roomtypeid order by count desc");
        int count=0;
        List<Integer> roomtypeList=new ArrayList<>();
        List<String> roomtypeListName=new ArrayList<>();
        for(Map<String,Object> map:mapList){
            Iterator<Object> it=map.values().iterator();
            int currentCount= Integer.parseInt(String.valueOf(it.next()));
            Integer roomtypeid= Integer.valueOf(String.valueOf(it.next()));
            if (currentCount>=count){
                roomtypeList.add(roomtypeid);
                count=currentCount;
            }
            if (currentCount<count){
                break;
            }
        }
        for (Integer i:roomtypeList){
            RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(i);
            roomtypeListName.add(roomType.getRoomname());
        }
        return roomtypeListName;

    }


    @GetMapping(value = "/popularbranch")
    public  List<String> popularBranch()  {

        List<Map<String,Object>> mapList= jdbcTemplate.queryForList("select count(*)as count,hotelid from orders group by hotelid order by count desc");
        int count=0;
        List<Integer> HotelList=new ArrayList<>();
        List<String> HotelListName=new ArrayList<>();
        for(Map<String,Object> map:mapList){
            Iterator<Object> it=map.values().iterator();
            int currentCount= Integer.parseInt(String.valueOf(it.next()));
            Integer hotelid= Integer.valueOf(String.valueOf(it.next()));
            if (currentCount>=count){
                HotelList.add(hotelid);
                count=currentCount;
            }
            if (currentCount<count){
                break;
            }
        }
        for (Integer i:HotelList){
            Hotel hotel=hotelRepository.findHotelByHotelid(i);
            HotelListName.add(hotel.getHotelname());
        }
        return HotelListName;

    }

    @GetMapping(value = "/popularcity")
    public  List<String> popularCity() {

        List<Map<String,Object>> mapList= jdbcTemplate.queryForList("select count(orderid)as count,cityname from orders,hotel where hotel.hotelid=orders.hotelid group by cityname order by count desc");
        int count=0;
        List<String> CityList=new ArrayList<>();
        for(Map<String,Object> map:mapList){
            Iterator<Object> it=map.values().iterator();
            int currentCount= Integer.parseInt(String.valueOf(it.next()));
            String city= String.valueOf(it.next());
            if (currentCount>=count){
                CityList.add(city);
                count=currentCount;
            }
            if (currentCount<count){
                break;
            }
        }
        return CityList;

    }


    @RequestMapping(value = "/findbyparameters",method = RequestMethod.GET)
    public List<OrdersInfoA> findbyparameters(@RequestParam(required = false,value = "customer") String customerName, @RequestParam(required = false,value = "hotel") String hotelName,
                                              @RequestParam(required = false,value = "city") String cityName, @RequestParam(required = false,value = "telephone") String telephone){






        if ((customerName==null||customerName.equals("") )&&(hotelName==null||hotelName.equals("") )&&
                (telephone==null||telephone.equals("") )&& (cityName==null||cityName.equals("") )){
            return null;
        }
        List<OrdersInfoA> ordersInfoAS=findAllA();

        if (customerName!=null && !customerName.equals("")) {
            List<OrdersInfoA> ordersInfoAS1=new ArrayList<>();
            List<Customer> customerList = customerRepository.findCustomersByName(customerName);
            for (Customer c : customerList) {
                Integer customerID = c.getCustomerid();
                List<Orders> orders=ordersRepository.findOrdersByCustomerid(customerID);
                for (Orders o:orders){
                    OrdersInfoA ordersInfoA=new OrdersInfoA(o);
                    ordersInfoAS1.add(ordersInfoA);
                }

            }
            ordersInfoAS= (List<OrdersInfoA>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS1);

        }

        if (hotelName!=null && !hotelName.equals("")){
            List<OrdersInfoA> ordersInfoAS2=new ArrayList<>();
            List<Hotel> hotelListName=hotelRepository.findHotelsByHotelname(hotelName);
            for (Hotel h:hotelListName){
                Integer hotelID=h.getHotelid();
                List<Orders> orders=ordersRepository.findOrdersByHotelid(hotelID);
                for (Orders o:orders){
                    OrdersInfoA ordersInfoA=new OrdersInfoA(o);
                    ordersInfoAS2.add(ordersInfoA);
                }
            }
            ordersInfoAS= (List<OrdersInfoA>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS2);

        }

        if (telephone!=null && !telephone.equals("")){
            List<OrdersInfoA> ordersInfoAS3=new ArrayList<>();
            Customer customerTelephone=customerRepository.findCustomerByTelephone(telephone);
            Integer customerID=customerTelephone.getCustomerid();
            List<Orders> orders=ordersRepository.findOrdersByCustomerid(customerID);

            for (Orders o:orders){
                OrdersInfoA ordersInfoA=new OrdersInfoA(o);
                ordersInfoAS3.add(ordersInfoA);
            }
            ordersInfoAS= (List<OrdersInfoA>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS3);
        }

        if ( cityName!=null && !cityName.equals("")){
            List<OrdersInfoA> ordersInfoAS4=new ArrayList<>();
            List<Hotel> hotelListCity=hotelRepository.findHotelsByCityname(cityName);
            for (Hotel h:hotelListCity){
                Integer hotelID=h.getHotelid();
                List<Orders> orders=ordersRepository.findOrdersByHotelid(hotelID);
                for (Orders o:orders){
                    OrdersInfoA ordersInfoA=new OrdersInfoA(o);
                    ordersInfoAS4.add(ordersInfoA);
                }
            }
            ordersInfoAS= (List<OrdersInfoA>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS4);

        }

        return  ordersInfoAS;







//        return  ordersInfoAS1.retainAll(ordersInfoAS2);

    }



    @RequestMapping(value = "/findAllA",method = RequestMethod.GET)
    public List<OrdersInfoA> findAllA(){
        List<Orders> orders= ordersRepository.findAll();
        List<OrdersInfoA> ordersInfoAList=new ArrayList<>();
        for (Orders order:orders){
            OrdersInfoA ordersInfoa=new OrdersInfoA(order);
            ordersInfoAList.add(ordersInfoa);
        }
        return ordersInfoAList;
//        return "nxjkqwnx";
    }




    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public List<OrdersInfoJ> findAll(){
        List<Orders> orders= ordersRepository.findAll();
        List<OrdersInfoJ> ordersInfoJList=new ArrayList<>();
        for (Orders order:orders){
            OrdersInfoJ ordersInfoj=new OrdersInfoJ(order);
            ordersInfoJList.add(ordersInfoj);
        }
        return ordersInfoJList;
//        return "nxjkqwnx";
//        return orders;
    }

    @RequestMapping(value = "/ongoing-orders",method = RequestMethod.GET)
    public List<OrdersInfo> getOngoingOrders(@RequestParam("userID") int id) throws ParseException {
        List<Orders> orderList=  ordersRepository.findOrdersByCustomerid(id);
        List<OrdersInfo> ordersInfoList = new ArrayList<>();
        for (Orders order:orderList) {
            String checkouttime=order.getCheckouttime();
            Date now=new Date();
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date checkout=format.parse(checkouttime);
            if (checkout.getTime()>=now.getTime()){
                ordersInfoList.add(new OrdersInfo(order));
            }
        }

        return ordersInfoList;
    }



    @RequestMapping(value = "/finished-orders",method = RequestMethod.GET)
    public List<OrdersInfo> getFinishedOrders(@RequestParam("userID") int id) throws ParseException {
        List<Orders> orderList=  ordersRepository.findOrdersByCustomerid(id);
        List<OrdersInfo> ordersInfoList = new ArrayList<>();
        for (Orders order:orderList) {
            String checkouttime=order.getCheckouttime();
            Date now=new Date();
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date checkout=format.parse(checkouttime);
            if (checkout.getTime()<now.getTime()){
                ordersInfoList.add(new OrdersInfo(order));
            }
        }

        return ordersInfoList;
    }





    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Orders deleteOrder(@RequestParam Integer id){
        // 删除语句
        Orders orders=ordersRepository.findOrderByOrderid(id);
        if (orders==null)return null;
        String sql = "delete from orders where orderid=?";
        jdbcTemplate.update(sql,id);
        // 查询
        return orders;
//        return "xna";
    }

    @GetMapping(value = "/count")
    public long count(){
        return ordersRepository.count();
    }




    @PostMapping("/booking")
    public boolean booking(@RequestBody bookInfo bookInfo){
        Integer maxId=jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
        if (maxId==null)maxId=0;

        String roomType= bookInfo.getRoomType();
        String hotelName=bookInfo.getHotelName();
        String userName=bookInfo.getUsername();
        Integer cost=bookInfo.getCost();
//        return  userName +hotelName+ cost;

        Customer customer=customerRepository.findByName(userName);
        Hotel hotel=hotelRepository.findHotelByHotelname(hotelName);
        Integer customerid=customer.getCustomerid();
        Integer hotelid=hotel.getHotelid();
        List<RoomType> roomTypeList=roomTypeRepository.findRoomTypesByHotelid(hotelid);
        if (roomTypeList == null){
            return false;
        }
        RoomType type = null;
        for (RoomType r: roomTypeList) {
            if (Objects.equals(r.getRoomname(), roomType)){
                type=r;
                break;
            }
        }
        if ( type==null){
            return false;
        }
        Integer roomtypeid=type.getRoomtypeid();
        List<Room> roomList=roomRepository.findRoomsByRoomtypeid(roomtypeid);
        Room room=null;
        for (Room r: roomList) {
            if (r.getIsordered()==0){
                room=r;
                break;
            }
        }
        if (room==null){
            return  false;
        }
        Integer roomid=room.getRoomid();




        if (customer.getMoney()<cost){
            return false;
        }
        String sql1 = "update customer set money=? where customerid=?";
        jdbcTemplate.update(sql1,customer.getMoney()-cost,customerid);
        String sql2 = "update customer set credits=? where customerid=?";
        jdbcTemplate.update(sql2,customer.getCredits()+cost,customerid);


        Orders orders=new Orders();
        orders.setOrderid(maxId+1);
        orders.setCustomerid(customerid);
        orders.setHotelid(hotelid);
        orders.setRoomtypeid(roomtypeid);
        orders.setRoomid(roomid);
        Date now=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ordertime=format.format(now);
        orders.setOrdertime(ordertime);
        orders.setCheckintime(bookInfo.getStartDate());
        orders.setCheckouttime(bookInfo.getEndDate());
        orders.setAmountpaid(cost);

        ordersRepository.save(orders);

        return true;
    }


    @PutMapping("/modifyordertime")
    public ReturnInfo modifyOrderTime(@RequestBody ModifyInfo modifyInfo) throws ParseException {
        Integer id=modifyInfo.getOrderID();

        String checkintime= modifyInfo.getCheckinTime();
        String checkouttime= modifyInfo.getCheckoutTime();
        ReturnInfo returnInfo=new ReturnInfo();
        Orders orders=ordersRepository.findOrderByOrderid(id);
        Integer customerId=orders.getCustomerid();
        Integer roomtypeId=orders.getRoomtypeid();
        RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeId);
        Customer customer=customerRepository.findByCustomerid(customerId);

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ckin=orders.getCheckintime();
        String ckout=orders.getCheckouttime();
        Date ckind=format.parse(ckin);
        Date ckoutd=format.parse(ckout);
        int dif= (int) ((ckoutd.getTime()-ckind.getTime())/(1000*60*60*24));
        int currentBack=dif*roomType.getPrice();

        Date checkout=format.parse(checkouttime);
        Date checkin=format.parse(checkintime);
        int difference= (int) ((checkout.getTime()-checkin.getTime())/(1000*60*60*24));
        System.out.println(difference);
        int currentPaid=difference*roomType.getPrice();
        if (currentPaid>customer.getMoney()+currentBack){
            returnInfo.setMoneyChange(currentPaid-currentBack);
            returnInfo.setModifySucceeded(false);
        }else {
            returnInfo.setMoneyChange(currentPaid-currentBack);
            returnInfo.setModifySucceeded(true);


            String sql = "update orders set checkintime=? , checkouttime=? where orderid=?";

            jdbcTemplate.update(sql, checkintime, checkouttime, id);

            String sql2 = "update customer set money=? where customerid=?";

            jdbcTemplate.update(sql2,customer.getMoney()-currentPaid+currentBack,customerId);

            String sql3 = "update customer set credits=? where customerid=?";

            jdbcTemplate.update(sql3,customer.getCredits()+currentPaid-currentBack,customerId);
        }

        return  returnInfo;
    }

    //todo test 高并发
    @PostMapping("/test")
    public int orderRoom(){
        String sql = "update roomtype set remain=remain-1 where roomtypeid=9";
        int result=jdbcTemplate.update(sql);

        return result;

    }






    @Data
    static
    class ID{
        Integer id;
    }

    @Data
    class OrdersInfo{
        Integer orderID;
        String hotelName;
        Integer roomTypeID;
        String rommTypeName;
        Integer roomID;
        String time;
        Integer pay;
        public OrdersInfo(Orders orders) {
            this.orderID=orders.getOrderid();
            Hotel hotel= hotelRepository.findHotelByHotelid(orders.getHotelid());
            this.hotelName=hotel.getHotelname();
            this.roomTypeID=orders.getRoomtypeid();
            RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(this.roomTypeID);
            this.rommTypeName=roomType.getRoomname();
            this.roomID=orders.getRoomid();
            this.time=orders.getCheckintime();
            this.pay=orders.getAmountpaid();
        }

    }


    @Data
    class OrdersInfoJ {


        String customerName;
        String telephone;
        String roomTypeName;
        String hotelName;
        String orderTime;
        String checkInTime;
        String checkOutTime;

        String cityName;
        public OrdersInfoJ(Orders orders) {
            Integer roomTypeID=orders.getRoomtypeid();
            Integer customerID=orders.getCustomerid();
            Integer hotelID=orders.getHotelid();
            Customer customer=customerRepository.findByCustomerid(customerID);
            this.customerName=customer.getName();
            this.telephone=customer.getTelephone();
            RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomTypeID);
            if (roomType==null){
                this.roomTypeName="null";
            }else {
                this.roomTypeName = roomType.getRoomname();

            }
            Hotel hotel=hotelRepository.findHotelByHotelid(hotelID);
            if (hotel==null){
                this.hotelName="null";
                this.cityName="null";
            }else {
                this.hotelName=hotelRepository.findHotelByHotelid(hotelID).getHotelname();
                this.cityName=hotel.getCityname();
            }
//            this.hotelName=hotelRepository.findHotelByHotelid(hotelID).getHotelname();
            this.orderTime=orders.getOrdertime();
            this.checkOutTime=orders.getCheckouttime();
            this.checkInTime=orders.getCheckintime();

        }
//
//        public OrdersInfoJ() {
//        }
    }

    @Data
    class OrdersInfoA{
        Integer orderID;
        String hotelName;
        String city;
        String roomTypeName;
        Integer customerID;
        String customerName;
        String telephone;

        String ordertime;

        String checkintime;

        String checkouttime;

        public OrdersInfoA(Orders order) {

            this.orderID = order.getOrderid();
            Hotel hotel=hotelRepository.findHotelByHotelid(order.getHotelid());
            RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(order.getRoomtypeid());

            this.hotelName = hotel.getHotelname();
            this.city = hotel.getCityname();
            this.roomTypeName = roomType.getRoomname();
            this.customerID = order.getCustomerid();
            Customer customer=customerRepository.findByCustomerid(customerID);
            this.customerName = customer.getName();
            this.telephone = customer.getTelephone();

            this.ordertime=order.getOrdertime();
            this.checkintime=order.getCheckintime();
            this.checkouttime=order.getCheckouttime();
        }

    }


    @Data
    static
    class ReturnInfo{
        boolean modifySucceeded;
        Integer moneyChange;
    }

    @Data
    static
    class ModifyInfo{
        Integer orderID;
        String checkinTime;
        String checkoutTime;
    }


    @Data
    static
    class bookInfo{
        String startDate;
        String endDate;
        String roomType;
        String hotelName;
        Integer cost;
        String username;

    }




}