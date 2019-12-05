package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService {

    @Autowired
    private CheckgroupDao checkgroupDao;

    //新增检查组
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkgroupDao.add(checkGroup);
        //添加中间表
        Integer id = checkGroup.getId();
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", id);
                map.put("checkitemId", checkitemId);
                checkgroupDao.addCheckitmeAndCheckgroup(map);
            }
        }

    }

    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        if (queryString != null) {
            currentPage = 1;
        }
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkgroupDao.findPage(queryString);
        long total = page.getTotal();
        List<CheckGroup> result = page.getResult();
        return new PageResult(total, result);
    }

    //数据回显
    public CheckGroup findById(Integer id) {
        return checkgroupDao.findById(id);
    }

    @Override
    public Integer[] findCheckGroupId(Integer id) {

        return checkgroupDao.findCheckGroupId(id);
    }

    @Override
    public void editCheckgroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        Integer id = checkGroup.getId();
        checkgroupDao.deleteCheckgroupAndCheckItem(id);
        checkgroupDao.editCheckgroup(checkGroup);
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", id);
                map.put("checkitemId", checkitemId);
                checkgroupDao.addCheckitmeAndCheckgroup(map);
            }
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkgroupDao.findAll();
    }
}
