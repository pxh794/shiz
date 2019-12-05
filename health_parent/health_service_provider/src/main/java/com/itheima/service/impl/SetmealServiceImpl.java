package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SetmealDao setmealDao;

    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.addSetmeal(setmeal);
        Integer id = setmeal.getId();
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map=new HashMap<>();
            map.put("sermealId",id);
            map.put("checkgroupId",checkgroupId);
            setmealDao.addSetmealAndCheckGroup(map);
        }
        jedisPool.getResource().sadd(RedisConstant.MEAL_DB_RESOURCES,setmeal.getImg());
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        if (queryString!=null){
            currentPage=1;
        }
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        long total = page.getTotal();
        List<Setmeal> result = page.getResult();
        return new PageResult(total,result);
    }

    @Override
    public void upload(String s) {
        jedisPool.getResource().sadd(RedisConstant.MEAL_RESOURCES,s);
    }

    @Override
    public List<Setmeal> findAll() {

        List<Setmeal> all = setmealDao.findAll();
        return all;
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
