package com.itheima.service;

import com.itheima.entity.Result;

import java.util.List;

public interface MemberService {
    Result check(String telephone);

    List<Integer> findMemberCountByMonths(List<String> months);
}
