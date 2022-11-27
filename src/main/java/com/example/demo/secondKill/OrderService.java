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


  public void decrByOrder(String goodsname){
    System.out.println("decrby");
    int roomtypeid=Integer.parseInt(goodsname);
    RoomType roomType=roomTypeRepository.findRoomTypeByRoomtypeid(roomtypeid);
    int remain=roomType.getRemain();
    if(remain>0){
      roomType.setRemain(remain-1);
      roomTypeRepository.save(roomType);
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


