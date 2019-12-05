package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrdersettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrdersettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService {

    @Autowired
    private OrdersettingDao ordersettingDao;

    public void upload(List<String[]> strings) {
        List<OrderSetting> list = new ArrayList<>();
        for (String[] string : strings) {
            OrderSetting o = new OrderSetting(new Date(string[0]), Integer.parseInt(string[1]));
            list.add(o);
        }
        for (OrderSetting orderSetting : list) {
            Long i = ordersettingDao.findByData(orderSetting.getOrderDate());
            if (i > 0) {
                ordersettingDao.editorderByData(orderSetting);
            } else {
                ordersettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map> findByMonth(String date) {
        Map<String, String> map = new HashMap<>();
        String begin = date + "-1";
        String end = date + "-31";
        map.put("begin", begin);
        map.put("end", end);
        List<OrderSetting> list = ordersettingDao.findByMonth(map);
        List<Map> mapList=new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String,Object> stringObjectMap=new HashMap<>();
             //   { date: 1, number: 120, reservations: 1 },
                stringObjectMap.put("date",orderSetting.getOrderDate().getDate());
                stringObjectMap.put("number",orderSetting.getNumber());
                stringObjectMap.put("reservations",orderSetting.getReservations());

                mapList.add(stringObjectMap);
            }
        }

        return mapList;
    }

    @Override
    public void uploadByDay(OrderSetting orderSetting) {
        Long i = ordersettingDao.findByData(orderSetting.getOrderDate());
        if (i > 0) {
            ordersettingDao.editorderByData(orderSetting);
        } else {
            ordersettingDao.add(orderSetting);
        }
    }
}
