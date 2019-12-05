package com.itheima.controller;

import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    //获得当前登录用户的用户名
    @RequestMapping("/getUsername")
    @CrossOrigin
    public Result getUsername(HttpServletResponse response, HttpServletRequest request) {
        //当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            String username = user.getUsername();
            Cookie cookie = new Cookie("user", JSON.toJSONString(user));
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}