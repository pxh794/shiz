package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckgroupDao {
    void add(CheckGroup checkGroup);

    void addCheckitmeAndCheckgroup(Map<String, Integer> map);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findById(Integer id);

    Integer[] findCheckGroupId(Integer id);

    void deleteCheckgroupAndCheckItem(Integer id);

    void editCheckgroup(CheckGroup checkGroup);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupById(int id);
}
