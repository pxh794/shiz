package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;


public interface CheckItemDao {
    //新增
    public void add(CheckItem checkItem);

    //分页查询
    public Page<CheckItem> findPage(String queryString);

    //检查中间表
    long findCountByCheckItemId(int id);

    //根据ID删除检查项
    void deleteById(Integer id);

    //修改
    void edit(CheckItem checkItem);

    //数据回显
    CheckItem findById(Integer id);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemById(int id);
}
