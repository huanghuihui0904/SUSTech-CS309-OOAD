package com.example.demo.controller;


import com.example.demo.RedisUtil;
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
    CommentRepository commentRepository;

@Autowired
    RedisUtil redisUtil;
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
    public List<OrdersInfoJ> findbyparameters(@RequestParam(required = false,value = "customer") String customerName, @RequestParam(required = false,value = "hotel") String hotelName,
                                              @RequestParam(required = false,value = "city") String cityName, @RequestParam(required = false,value = "telephone") String telephone){

        if ((customerName==null||customerName.equals("") )&&(hotelName==null||hotelName.equals("") )&&
                (telephone==null||telephone.equals("") )&& (cityName==null||cityName.equals("") )){
            return null;
        }
        List<OrdersInfoJ> ordersInfoAS=findAll();

        if (customerName!=null && !customerName.equals("")) {
            List<OrdersInfoJ> ordersInfoAS1=new ArrayList<>();
            List<Customer> customerList = customerRepository.findCustomersByName(customerName);
            for (Customer c : customerList) {
                Integer customerID = c.getCustomerid();
                List<Orders> orders=ordersRepository.findOrdersByCustomerid(customerID);
                for (Orders o:orders){
                    OrdersInfoJ ordersInfoA=new OrdersInfoJ(o);
                    ordersInfoAS1.add(ordersInfoA);
                }

            }
            ordersInfoAS= (List<OrdersInfoJ>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS1);

        }

        if (hotelName!=null && !hotelName.equals("")){
            List<OrdersInfoJ> ordersInfoAS2=new ArrayList<>();
            List<Hotel> hotelListName=hotelRepository.findHotelsByHotelname(hotelName);
            for (Hotel h:hotelListName){
                Integer hotelID=h.getHotelid();
                List<Orders> orders=ordersRepository.findOrdersByHotelid(hotelID);
                for (Orders o:orders){
                    OrdersInfoJ ordersInfoA=new OrdersInfoJ(o);
                    ordersInfoAS2.add(ordersInfoA);
                }
            }
            ordersInfoAS= (List<OrdersInfoJ>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS2);

        }

        if (telephone!=null && !telephone.equals("")){
            List<OrdersInfoJ> ordersInfoAS3=new ArrayList<>();
            Customer customerTelephone=customerRepository.findCustomerByTelephone(telephone);
            Integer customerID=customerTelephone.getCustomerid();
            List<Orders> orders=ordersRepository.findOrdersByCustomerid(customerID);

            for (Orders o:orders){
                OrdersInfoJ ordersInfoA=new OrdersInfoJ(o);
                ordersInfoAS3.add(ordersInfoA);
            }
            ordersInfoAS= (List<OrdersInfoJ>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS3);
        }

        if ( cityName!=null && !cityName.equals("")){
            List<OrdersInfoJ> ordersInfoAS4=new ArrayList<>();
            List<Hotel> hotelListCity=hotelRepository.findHotelsByCityname(cityName);
            for (Hotel h:hotelListCity){
                Integer hotelID=h.getHotelid();
                List<Orders> orders=ordersRepository.findOrdersByHotelid(hotelID);
                for (Orders o:orders){
                    OrdersInfoJ ordersInfoA=new OrdersInfoJ(o);
                    ordersInfoAS4.add(ordersInfoA);
                }
            }
            ordersInfoAS= (List<OrdersInfoJ>) CollectionUtils.intersection(ordersInfoAS,ordersInfoAS4);

        }

        return  ordersInfoAS;







//        return  ordersInfoAS1.retainAll(ordersInfoAS2);

    }



    @RequestMapping(value = "/findAllA",method = RequestMethod.GET)
    public List<OrdersInfoJ> findAllA(){
        List<Orders> orders= ordersRepository.findAll();
        List<OrdersInfoJ> ordersInfoAList=new ArrayList<>();
        for (Orders order:orders){
            OrdersInfoJ ordersInfoa=new OrdersInfoJ(order);
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
    public Orders deleteOrder(@RequestParam Integer id) throws ParseException {
        // 删除语句
        Orders orders=ordersRepository.findOrderByOrderid(id);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Integer cost=orders.getAmountpaid();
        String checkintime=orders.getCheckintime();
        String checkouttime=orders.getCheckouttime();

        Integer roomid=orders.getRoomid();
        Integer commentid=orders.getCommentid();
        Integer customerid=orders.getCustomerid();
        Room room=roomRepository.findRoomByRoomid(roomid);
        Integer roomtypeid=room.getRoomtypeid();
        RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeid);
        Comment comment=null;
        if (commentid!=null){
            comment=commentRepository.findAllByCommentid(commentid);
        }
        Customer customer=customerRepository.findByCustomerid(customerid);


        Date checkin=format.parse(checkintime);
        Date checkout=format.parse(checkouttime);


        Date now1=new Date();
        String now1String=format.format(now1);
        String nowString=now1String.substring(0,11)+"00:00:00";
        Date now=format.parse(nowString);
        int startIndex= (int) ((checkin.getTime()-now.getTime())/(1000*60*60*24));
        int endIndex=(int) ((checkout.getTime()-now.getTime())/(1000*60*60*24))-1;


        String remain=roomType.getRemain();
        String[] remains=remain.split(",");
        String isOrdered=room.getIsordered();



        StringBuilder currentIsordered= new StringBuilder();
        StringBuilder currentRemain= new StringBuilder();


        for (int i = startIndex; i <=endIndex ; i++) {
            currentIsordered.append("0,");
            int cur=Integer.parseInt(remains[i])+1;
            currentRemain.append(cur).append(",");
        }
        String remainFin="";
        String isOrderedFin="";

        if (endIndex>=remain.length()) {
            remainFin = remain.substring(0, startIndex*2) + currentRemain.substring(0,currentRemain.length()-1) ;
            isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered.substring(0,currentIsordered.length()-1);
        }else {

            remainFin = remain.substring(0, startIndex*2) + currentRemain + remain.substring(2*endIndex+2 );
            isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered+isOrdered.substring(2*endIndex+2);

            System.out.println("remain.substring(0, startIndex*2) "+remain.substring(0, startIndex*2) );
            System.out.println("currentRemain "+currentRemain);
            System.out.println("remain.substring(2*endIndex+1) "+remain.substring(2*endIndex+2));
            System.out.println("isOrdered.substring(0, startIndex*2) "+isOrdered.substring(0, startIndex*2) );
            System.out.println("currentisOrdered "+currentIsordered);
            System.out.println("isOrdered.substring(2*endIndex+1) "+isOrdered.substring(2*endIndex+2));
            System.out.println(remainFin);
            System.out.println(isOrderedFin);
        }

        //取消订单后 remain加回来
        String sql1 = "update roomtype set remain=? where roomtypeid=?";
        jdbcTemplate.update(sql1,remainFin,roomtypeid );

        //取消订单后，isordered改回来
        String sql2 = "update room set isOrdered=? where roomid=?";
        jdbcTemplate.update(sql2,isOrderedFin,roomid);

        //取消订单后，customer积分减回去
        String sql3 = "update customer set credits=? where customerid=?";
        jdbcTemplate.update(sql3,customer.getCredits()-cost,customerid);

        //取消订单后，customer钱加回去
        String sql4 = "update customer set money=? where customerid=?";
        jdbcTemplate.update(sql4,customer.getMoney()+cost,customerid);

        //取消订单后，如果有评论删除评论
        if (comment!=null){
            String sql5 = "delete from comment where commentid=?";
            jdbcTemplate.update(sql5,commentid);
        }



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



    @PostMapping("/bookroom")
    public boolean bookroom(@RequestBody BookroomInfo bookroomInfo) throws ParseException {
        Integer maxId=jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
        if (maxId==null)maxId=0;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String roomTypeName= bookroomInfo.getRoomType();
        String hotelName=bookroomInfo.getHotelName();
        String userName=bookroomInfo.getUsername();
        Integer cost=bookroomInfo.getCost();
        String roomlocation=bookroomInfo.getLocation();

        Customer customer=customerRepository.findByName(userName);
        Hotel hotel=hotelRepository.findHotelByHotelname(hotelName);

        Integer customerid=customer.getCustomerid();
        Integer hotelid=hotel.getHotelid();
        List<RoomType> roomTypeList=roomTypeRepository.findRoomTypesByHotelid(hotelid);
        if (roomTypeList == null){
            return false;
        }
        RoomType roomtype = null;
        for (RoomType r: roomTypeList) {
            if (Objects.equals(r.getRoomname(), roomTypeName)){
                roomtype=r;
                break;
            }
        }
        if ( roomtype==null){
            return false;
        }

        Integer roomtypeid=roomtype.getRoomtypeid();

        String startDate=bookroomInfo.getStartDate();

        String endDate=bookroomInfo.getEndDate();
        Date checkin=format.parse(startDate);
        Date checkout=format.parse(endDate);
        Date now1=new Date();
        String now1String=format.format(now1);
        String nowString=now1String.substring(0,11)+"00:00:00";
        Date now=format.parse(nowString);

        int startIndex= (int) ((checkin.getTime()-now.getTime())/(1000*60*60*24));
        int endIndex=(int) ((checkout.getTime()-now.getTime())/(1000*60*60*24))-1;

        Room room=roomRepository.findRoomByLocation(roomlocation);
        if (room==null|| !Objects.equals(roomtypeid, room.getRoomtypeid())){
            System.out.println("No room or Room error");
            return false;
        }
        String remain=roomtype.getRemain();
        System.out.println("Remain: "+remain);
        String[] remains=remain.split(",");
        String isOrdered=room.getIsordered();


        if (isOrdered.substring(startIndex*2, 2*endIndex+2).contains("1")){
            System.out.println("No room");
            return false;
        }


        StringBuilder currentIsordered= new StringBuilder();
        StringBuilder currentRemain= new StringBuilder();


        for (int i = startIndex; i <=endIndex ; i++) {
            currentIsordered.append("1,");
            int cur=Integer.parseInt(remains[i])-1;
            currentRemain.append(cur).append(",");
        }
        String remainFin="";
        String isOrderedFin="";

        if (endIndex>=remain.length()) {

            remainFin = remain.substring(0, startIndex*2) + currentRemain.substring(0,currentRemain.length()-1) ;
            isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered.substring(0,currentIsordered.length()-1);
        }else {
            System.out.println("remain.substring(0, startIndex*2) "+remain.substring(0, startIndex*2) );
            System.out.println("currentRemain"+currentRemain);
            System.out.println("remain.substring(2*endIndex+1)"+remain.substring(2*endIndex+2));
            remainFin = remain.substring(0, startIndex*2) + currentRemain + remain.substring(2*endIndex+2);
            System.out.println("isOrdered.substring(0, startIndex*2) "+isOrdered.substring(0, startIndex*2) );
            System.out.println("currentisOrdered"+currentIsordered);
            System.out.println("isOrdered.substring(2*endIndex+1)"+isOrdered.substring(2*endIndex+2));
            isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered+isOrdered.substring(2*endIndex+2);
        }

        System.out.println(remainFin);
        System.out.println(isOrderedFin);


        if (customer.getMoney()<cost){
            return false;
        }
        //订房扣钱
        String sql1 = "update customer set money=? where customerid=?";
        jdbcTemplate.update(sql1,customer.getMoney()-cost,customerid);

        //订房加积分
        String sql2 = "update customer set credits=? where customerid=?";
        jdbcTemplate.update(sql2,customer.getCredits()+cost,customerid);

        //订房，修改remain
        String sql3 = "update roomtype set remain=? where roomtypeid=?";
        jdbcTemplate.update(sql3,remainFin,roomtypeid);

        //订房，修改isordered
        String sql4 = "update room set isordered=? where roomid=?";
        jdbcTemplate.update(sql4,isOrderedFin,room.getRoomid());



        Orders orders=new Orders();
        orders.setOrderid(maxId+1);
        orders.setCustomerid(customerid);
        orders.setHotelid(hotelid);
        orders.setRoomtypeid(roomtypeid);
        orders.setRoomid(room.getRoomid());
        String ordertime=format.format(now);
        orders.setOrdertime(ordertime);
        orders.setCheckintime(bookroomInfo.getStartDate());
        orders.setCheckouttime(bookroomInfo.getEndDate());
        orders.setAmountpaid(cost);

        //订房增加order
        ordersRepository.save(orders);

        return true;
    }



    @PostMapping("/booking")
    public boolean booking(@RequestBody BookInfo bookInfo) throws ParseException {

        RoomType rt=roomTypeRepository.findRoomTypeByRoomname(bookInfo.roomType);
        if(redisUtil.hasKey(rt.getRoomtypeid()+"")){
            if(redisUtil.get(rt.getRoomtypeid()+"")!=null){
                if(Integer.parseInt(redisUtil.get(rt.getRoomtypeid()+"").toString())>0){
                    redisUtil.decrBy(rt.getRoomtypeid());
                }else {
                    return false;
                }
            }else {

            }
        }else {

        }


        Integer maxId=jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
        if (maxId==null)maxId=0;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String roomTypeName= bookInfo.getRoomType();
        String hotelName=bookInfo.getHotelName();
        String userName=bookInfo.getUsername();
        Integer cost=bookInfo.getCost();

        Customer customer=customerRepository.findByName(userName);
        Hotel hotel=hotelRepository.findHotelByHotelname(hotelName);

        Integer customerid=customer.getCustomerid();
        Integer hotelid=hotel.getHotelid();
        List<RoomType> roomTypeList=roomTypeRepository.findRoomTypesByHotelid(hotelid);
        if (roomTypeList == null){
            return false;
        }
        RoomType roomtype = null;
        for (RoomType r: roomTypeList) {
            if (Objects.equals(r.getRoomname(), roomTypeName)){
                roomtype=r;
                break;
            }
        }
        if ( roomtype==null){
            return false;
        }

        Integer roomtypeid=roomtype.getRoomtypeid();

        String startDate=bookInfo.getStartDate();

        String endDate=bookInfo.getEndDate();
        Date checkin=format.parse(startDate);
        Date checkout=format.parse(endDate);
        Date now1=new Date();
        String now1String=format.format(now1);
        String nowString=now1String.substring(0,11)+"00:00:00";
        Date now=format.parse(nowString);

        int startIndex= (int) ((checkin.getTime()-now.getTime())/(1000*60*60*24));
        int endIndex=(int) ((checkout.getTime()-now.getTime())/(1000*60*60*24))-1;


        List<Room> roomList=roomRepository.findRoomsByRoomtypeid(roomtypeid);
        Room room=null;
        for (Room r:roomList) {
            String isOrderedInterval=r.getIsordered().substring(startIndex*2,2*endIndex+2);
            if (!isOrderedInterval.contains("1")){
                System.out.println("R:"+isOrderedInterval);
                room=r;
                break;
            }
        }
        if (room==null){
            System.out.println("No room");
            return false;
        }
        String remain=roomtype.getRemain();
        System.out.println("Remain: "+remain);
        String[] remains=remain.split(",");
        String isOrdered=room.getIsordered();

        if (remain.substring(startIndex*2, 2*endIndex+2).contains("0")){
            System.out.println("No room");
            return false;
        }

        StringBuilder currentIsordered= new StringBuilder();
        StringBuilder currentRemain= new StringBuilder();


        for (int i = startIndex; i <=endIndex ; i++) {
            currentIsordered.append("1,");
            int cur=Integer.parseInt(remains[i])-1;
            currentRemain.append(cur).append(",");
        }
        String remainFin="";
        String isOrderedFin="";

        if (endIndex>=remain.length()) {

            remainFin = remain.substring(0, startIndex*2) + currentRemain.substring(0,currentRemain.length()-1) ;
            isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered.substring(0,currentIsordered.length()-1);
        }else {
            System.out.println("remain.substring(0, startIndex*2) "+remain.substring(0, startIndex*2) );
            System.out.println("currentRemain"+currentRemain);
            System.out.println("remain.substring(2*endIndex+1)"+remain.substring(2*endIndex+2));
            remainFin = remain.substring(0, startIndex*2) + currentRemain + remain.substring(2*endIndex+2);
            System.out.println("isOrdered.substring(0, startIndex*2) "+isOrdered.substring(0, startIndex*2) );
            System.out.println("currentisOrdered"+currentIsordered);
            System.out.println("isOrdered.substring(2*endIndex+1)"+isOrdered.substring(2*endIndex+2));
            isOrderedFin=isOrdered.substring(0,startIndex*2)+currentIsordered+isOrdered.substring(2*endIndex+2);
        }

        System.out.println(remainFin);
        System.out.println(isOrderedFin);


        if (customer.getMoney()<cost){
            return false;
        }
        //订房扣钱
        String sql1 = "update customer set money=? where customerid=?";
        jdbcTemplate.update(sql1,customer.getMoney()-cost,customerid);

        //订房加积分
        String sql2 = "update customer set credits=? where customerid=?";
        jdbcTemplate.update(sql2,customer.getCredits()+cost,customerid);

        //订房，修改remain
        String sql3 = "update roomtype set remain=? where roomtypeid=?";
        jdbcTemplate.update(sql3,remainFin,roomtypeid);

        //订房，修改isordered
        String sql4 = "update room set isordered=? where roomid=?";
        jdbcTemplate.update(sql4,isOrderedFin,room.getRoomid());


        addOrders(bookInfo,room.getRoomid(),roomtypeid);

        return true;
    }




    public void addOrders(BookInfo bookInfo, Integer roomid, Integer roomtypeid) throws ParseException {
        Integer maxId=jdbcTemplate.queryForObject("select MAX(orderid) from orders", Integer.class);
        if (maxId==null)maxId=0;

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now1=new Date();
        String now1String=format.format(now1);
        String nowString=now1String.substring(0,11)+"00:00:00";
        Date now=format.parse(nowString);
        String hotelName=bookInfo.getHotelName();
        String userName=bookInfo.getUsername();
        Integer cost=bookInfo.getCost();

        Customer customer=customerRepository.findByName(userName);
        Hotel hotel=hotelRepository.findHotelByHotelname(hotelName);

        Integer customerid=customer.getCustomerid();
        Integer hotelid=hotel.getHotelid();



        Orders orders=new Orders();
        orders.setOrderid(maxId+1);
        orders.setCustomerid(customerid);
        orders.setHotelid(hotelid);
        orders.setRoomtypeid(roomtypeid);
        orders.setRoomid(roomid);
        String ordertime=format.format(now);
        orders.setOrdertime(ordertime);
        orders.setCheckintime(bookInfo.getStartDate());
        orders.setCheckouttime(bookInfo.getEndDate());
        orders.setAmountpaid(cost);
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
        Integer roomid=orders.getRoomid();
        Room room=roomRepository.findRoomByRoomid(roomid);

        RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeId);
        Customer customer=customerRepository.findByCustomerid(customerId);

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ckin=orders.getCheckintime();
        String ckout=orders.getCheckouttime();
        Date ckind=format.parse(ckin);
        Date checkout=format.parse(checkouttime);
        Date checkin=format.parse(checkintime);
        Date ckoutd=format.parse(ckout);

        int dif= (int) ((ckoutd.getTime()-ckind.getTime())/(1000*60*60*24));
        int currentBack=dif*roomType.getPrice();


        Date now1=new Date();
        String now1String=format.format(now1);
        String nowString=now1String.substring(0,11)+"00:00:00";
        Date now=format.parse(nowString);



        int startIndex=(int) ((checkin.getTime()-now.getTime())/(1000*60*60*24));
        int endIndex=(int) ((checkout.getTime()-now.getTime())/(1000*60*60*24))-1;
        int oldStart=(int) ((ckind.getTime()-now.getTime())/(1000*60*60*24));
        int oldEnd=(int) ((ckoutd.getTime()-now.getTime())/(1000*60*60*24))-1;

        String remain=roomType.getRemain();
        String isOrdered=room.getIsordered();
//        String remainInterval=remain.substring(startIndex,endIndex);
//        String isOrderedInterval=isOrdered.substring(startIndex,endIndex);

        String[] oldremains=remain.split(",");
        String[] oldisOrdereds=isOrdered.split(",");
        StringBuilder r=new StringBuilder();
        StringBuilder o=new StringBuilder();
        for (int i = oldStart; i <=oldEnd ; i++) {
            r.append(Integer.parseInt(oldremains[i])+1).append(",");
            o.append("0,");
        }

        if (oldEnd>=remain.length()) {
            remain = remain.substring(0, 2*oldStart) + r.substring(0,r.length()-1) ;
            isOrdered=isOrdered.substring(0,2*oldStart)+o.substring(0,o.length()-1);
        }else {
            remain = remain.substring(0, 2*oldStart) +r+  remain.substring(2*oldEnd+2);
            isOrdered=isOrdered.substring(0,2*oldStart)+o+isOrdered.substring(2*oldEnd+2);
        }

        String[] remains=remain.split(",");
        String[] isOrdereds=isOrdered.split(",");


        for (int i = startIndex; i <=endIndex ; i++) {
            if (Integer.parseInt(remains[i])==0|| Objects.equals(isOrdereds[i], "1")){
                returnInfo.setMoneyChange(currentBack);
                returnInfo.setModifySucceeded(false);
                returnInfo.setNoRoom(1);
                return  returnInfo;
            }
        }

        int difference= (int) ((checkout.getTime()-checkin.getTime())/(1000*60*60*24));
        System.out.println(difference);
        int currentPaid=difference*roomType.getPrice();
        if (currentPaid>customer.getMoney()+currentBack){
            returnInfo.setMoneyChange(-currentPaid+currentBack);
            returnInfo.setModifySucceeded(false);
            returnInfo.setNoRoom(0);
            return returnInfo;
        }else {
            returnInfo.setMoneyChange(-currentPaid+currentBack);
            returnInfo.setModifySucceeded(true);
            returnInfo.setNoRoom(0);

            //修改订单时间
            String sql = "update orders set checkintime=? , checkouttime=? where orderid=?";
            jdbcTemplate.update(sql, checkintime, checkouttime, id);

            StringBuilder currentRemain= new StringBuilder();
            StringBuilder currentIsOrdered=new StringBuilder();

            for (int i = startIndex; i <= endIndex; i++) {
                int fin=Integer.parseInt(remains[i])-1;
                currentRemain.append(fin).append(",");
                currentIsOrdered.append("1,");
            }

            System.out.println("------------------------");
            System.out.println(startIndex);
            System.out.println(endIndex);
            System.out.println("------------------------");

            String remainFin="";
            String isOrderedFin="";

            if (endIndex>=remain.length()) {
                remainFin = remain.substring(0, 2*startIndex) + currentRemain.substring(0,currentRemain.length()-1) ;
                isOrderedFin=isOrdered.substring(0,2*startIndex)+currentIsOrdered.substring(0,currentIsOrdered.length()-1);
            }else {
                remainFin = remain.substring(0, 2*startIndex) + currentRemain + remain.substring(2*endIndex+2);
                isOrderedFin=isOrdered.substring(0,2*startIndex)+currentIsOrdered+isOrdered.substring(2*endIndex+2);
                System.out.println("remain.substring(0, startIndex*2) "+remain.substring(0, startIndex*2) );
                System.out.println("currentRemain "+currentRemain);
                System.out.println("remain.substring(2*endIndex+1) "+remain.substring(2*endIndex+2));
                System.out.println("isOrdered.substring(0, startIndex*2) "+isOrdered.substring(0, startIndex*2) );
                System.out.println("currentisOrdered "+currentIsOrdered);
                System.out.println("isOrdered.substring(2*endIndex+1) "+isOrdered.substring(2*endIndex+2));
                System.out.println(remainFin);
                System.out.println(isOrderedFin);
            }

            //修改订单时间后，修改customer的钱
            String sql2 = "update customer set money=? where customerid=?";
            jdbcTemplate.update(sql2,customer.getMoney()-currentPaid+currentBack,customerId);

            //修改订单时间后，修改customer的积分
            String sql3 = "update customer set credits=? where customerid=?";
            jdbcTemplate.update(sql3,customer.getCredits()+currentPaid-currentBack,customerId);

            //修改订单时间后，修改remain
            String sql4 = "update roomtype set remain=? where roomtypeid=?";
            jdbcTemplate.update(sql4,remainFin,roomtypeId );


            //修改订单时间后，修改isordered
            String sql5 = "update room set isordered=? where roomid=?";
            jdbcTemplate.update(sql5,isOrderedFin,roomid );





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
        Integer roomid;
        String rommTypeName;
        Integer roomID;
        String checkintime;
        String checkouttime;
        Integer pay;
        public OrdersInfo(Orders orders) {
            this.orderID=orders.getOrderid();
            Hotel hotel= hotelRepository.findHotelByHotelid(orders.getHotelid());
            this.hotelName=hotel.getHotelname();
            this.roomTypeID=orders.getRoomtypeid();
            RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(this.roomTypeID);
            this.rommTypeName=roomType.getRoomname();
            this.roomID=orders.getRoomid();
            this.checkintime=orders.getCheckintime();
            this.checkouttime=orders.getCheckouttime();
            this.roomid=orders.getRoomid();
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

//    @Data
//    class OrdersInfoJ {
//        Integer orderID;
//        String hotelName;
//        String city;
//        String roomTypeName;
//        Integer customerID;
//        String customerName;
//        String telephone;
//
//        String ordertime;
//
//        String checkintime;
//
//        String checkouttime;
//
//        public OrdersInfoJ(Orders order) {
//
//            this.orderID = order.getOrderid();
//            Hotel hotel=hotelRepository.findHotelByHotelid(order.getHotelid());
//            RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(order.getRoomtypeid());
//
//            this.hotelName = hotel.getHotelname();
//            this.city = hotel.getCityname();
//            this.roomTypeName = roomType.getRoomname();
//            this.customerID = order.getCustomerid();
//            Customer customer=customerRepository.findByCustomerid(customerID);
//            this.customerName = customer.getName();
//            this.telephone = customer.getTelephone();
//
//            this.ordertime=order.getOrdertime();
//            this.checkintime=order.getCheckintime();
//            this.checkouttime=order.getCheckouttime();
//        }
//
//    }


    @Data
    static
    class ReturnInfo{
        boolean modifySucceeded;
        Integer moneyChange;
        Integer noRoom;
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
    class BookInfo {
        String startDate;
        String endDate;
        String roomType;
        String hotelName;
        Integer cost;
        String username;

    }

    @Data
    static
    class BookroomInfo {
        String startDate;
        String endDate;
        String roomType;
        String hotelName;
        Integer cost;
        String username;
        String location;

    }




}