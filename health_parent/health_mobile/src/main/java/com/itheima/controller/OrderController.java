package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;

import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;


import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;


    //预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String jedistelephone = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        if (jedistelephone != null && validateCode != null && validateCode.equals(jedistelephone)) {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                try {
                    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    //预约成功后回显用户对应的信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Map map=null;
        try {
            map=orderService.findById(id);

        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }
}

