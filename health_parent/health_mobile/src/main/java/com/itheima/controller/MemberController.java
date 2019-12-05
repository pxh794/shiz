package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result check(HttpServletResponse resp,@RequestBody Map map) {
        try {
            String validateCode = (String) map.get("validateCode");
            String telephone = (String) map.get("telephone");
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            if (code != null && validateCode != null && code.equals(validateCode)) {
                Result result = memberService.check(telephone);
                Cookie cookie=new Cookie("login_member_telephone",telephone);
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24*30);
                resp.addCookie(cookie);
                return result;
            } else {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
