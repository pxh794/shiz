package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrdersettingService {
    void upload(List<String[]> strings);

    List<Map> findByMonth(String date);

    void uploadByDay(OrderSetting orderSetting);
}
