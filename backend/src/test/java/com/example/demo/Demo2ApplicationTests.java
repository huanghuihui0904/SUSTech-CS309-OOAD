package com.example.demo;

import com.example.demo.entity.Orders;
import com.example.demo.repository.GiftRepository;
import com.example.demo.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class Demo2ApplicationTests {
    OrdersRepository ordersRepository;

    @Test
    @GetMapping("/sales")
    public List sales() {
        List<Orders> orders = ordersRepository.findAll();
        List<String[]>orderArray=new ArrayList();
        int[] year=new int[373];
        for (int i = 0; i < orders.size(); i++) {
            orderArray.add(orders.get(i).toString().split(","));
            for (int j = 0; j < orderArray.get(i).length; j++) {
//        System.out.println(orderArray.get(i)[j]);
                if(orderArray.get(i)[j].contains("ordertime"))
                    System.out.println(orderArray.get(i)[j].substring(11,21));
                String []time=orderArray.get(i)[j].substring(11,21).split("-");
                int m=Integer.parseInt(time[1]);
                int d=Integer.parseInt(time[2]);
                int arr=(m-1)*30+d;
                year[arr]++;
            }
        }

        return orders;
    }

}
