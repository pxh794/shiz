package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private MemberDao memberDao;

    public Result check(String telephone) {
        Member member=null;
        member = memberDao.findByTelephone(telephone);
        if (member == null) {
            member=new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        String string = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone,60*30,string);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    //根据月份查询会员数量
    public List<Integer> findMemberCountByMonths(List<String> months) {//2018.05
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            String date = month + ".31";//2018.05.31
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }
        return memberCount;
    }
}
