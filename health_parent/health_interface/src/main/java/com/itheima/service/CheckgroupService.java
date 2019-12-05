package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckgroupService {
    void add(CheckGroup checkGroup,Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    Integer[] findCheckGroupId(Integer id);

    void editCheckgroup(CheckGroup checkGroup,Integer [] checkitemIds);

    List<CheckGroup> findAll();

}
