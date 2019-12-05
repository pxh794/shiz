package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //查询所有套餐
    @RequestMapping("/findAll")
    public Result findAll(){

        try {
            List<Setmeal>  list=setmealService.findAll();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }

    }

    //回显套餐数据and套餐对应的检查组和检查组对应的检查项
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Setmeal setmeal=null;
        try {
            setmeal=setmealService.findById(id);
        }catch (Exception e){
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
    }

}

