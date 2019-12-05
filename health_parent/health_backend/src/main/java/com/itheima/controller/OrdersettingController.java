package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    private OrdersettingService ordersettingService;
    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            //解析exce
            List<String[]> strings = POIUtils.readExcel(excelFile);
            ordersettingService.upload(strings);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/findByMonth")
    public Result findByMonth(String date) {
        List<Map> list = null;
        try {
            list = ordersettingService.findByMonth(date);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
    }

    @RequestMapping("/uploadByDay")
    public Result uploadByDay(@RequestBody OrderSetting orderSetting) {
        try {
            ordersettingService.uploadByDay(orderSetting);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
