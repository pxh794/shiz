package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //预约验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        try {
            Integer integer = ValidateCodeUtils.generateValidateCode(4);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, integer.toString());
            Jedis resource = jedisPool.getResource();
            resource.setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 300, integer.toString());
        } catch (Exception e) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //登陆验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        try {
            Integer integer = ValidateCodeUtils.generateValidateCode(6);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, integer.toString());
            Jedis resource = jedisPool.getResource();
            //jedis自动清理  秒为单位
            resource.setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 300, integer.toString());
        } catch (Exception e) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
