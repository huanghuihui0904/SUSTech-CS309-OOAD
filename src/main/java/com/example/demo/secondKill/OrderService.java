package com.example.demo.secondKill;



import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author code
 * @Date 2022/6/24 9:25
 * Description 订单实现
 * Version 1.0
 */
@Service
public class OrderService {
//  @Autowired
//  private StockOrderMapper stockOrderMapper;
//
  @Autowired
  private seckillOrderRepository seckillorderRepository;
@Autowired
  RoomTypeRepository roomTypeRepository;

  @Transactional(rollbackFor = Exception.class)
  public void createOrder(String username,String goodsname) {
    //校验库存

seckillOrder order=new seckillOrder(username,goodsname);
seckillorderRepository.save(order);

  }

//todo
  public void decrByOrder(String roomtypeid){
    String date="";
    System.out.println("decrby");

    RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(Integer.parseInt(roomtypeid));
    String remian=roomType.getRemain();
    String day=date.split("-")[2];
    int dayint=(Integer.parseInt(day)-1)*2;
    String daythat=remian.substring(dayint,dayint+1);
    int thatramian=Integer.parseInt(daythat);
    if(thatramian>0){
      StringBuilder strBuilder = new StringBuilder(remian);
      thatramian--;
      char re=(thatramian+"").charAt(0);
      strBuilder.setCharAt(dayint, re);
      String str= strBuilder.toString();
      roomType.setRemain(str);
      roomTypeRepository.save(roomType);
    }else {
      System.out.println("no remain");
    }

  }

//  //校验库存
//  private Stock checkStock(Integer id) {
//    return stockMapper.checkStock(id);
//  }
//
//  //扣库存
//  private int updateSale(Stock stock){
//    return stockMapper.updateSale(stock);
//  }


}


