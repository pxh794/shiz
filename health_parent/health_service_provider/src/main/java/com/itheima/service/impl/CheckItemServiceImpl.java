package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.dao.CheckItemDao;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    public PageResult findPage(QueryPageBean queryPageBean) {
        //当前页码
        Integer currentPage = queryPageBean.getCurrentPage();
        //每页显示条数
        Integer pageSize = queryPageBean.getPageSize();
        //条件查询
        String queryString = queryPageBean.getQueryString();
        if (queryString!=null){
            currentPage=1;
        };
        PageHelper.startPage(currentPage, pageSize);

        Page<CheckItem> page = checkItemDao.findPage(queryString);
        //总数据数
        long total = page.getTotal();
        //数据
        List<CheckItem> result = page.getResult();
        return new PageResult(total, result);
    }

    @Override
    public void deleteId(Integer id) {
        long countByCheckItemId = checkItemDao.findCountByCheckItemId(id);
        if (countByCheckItemId > 0) {
            new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
