package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrdersettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersettingDao ordersettingDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Result order(Map map) throws Exception {
        // 1.检查用户所选日期是否已经有预约设置
        String order_Date = (String) map.get("orderDate");//前端传来的日期
        Date orderDate = DateUtils.parseString2Date(order_Date);//转格式
        //查询预约日期是否有
        OrderSetting orderSetting = ordersettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.检查用户所选预约日期是否约满
        int reservations = orderSetting.getReservations();//已预约人数
        int number = orderSetting.getNumber();//可预约人数
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        String telephone = (String) map.get("telephone");//前端传的电话
        String idCard = (String) map.get("idCard");
        String name = (String) map.get("name");
        String sex = (String) map.get("sex");
        Member byTelephone = memberDao.findByTelephone(telephone);//根据电话查用户
        //3.查询会员是否为空
        String setmealId = (String) map.get("setmealId");
        if (byTelephone != null) {
            Integer id = byTelephone.getId();

            Order order = new Order(id, orderDate, Integer.parseInt(setmealId));
            //根据用户id，预约时间，套餐id查询用户是否重复预约
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //4.注册会员
            byTelephone = new Member(name, sex, idCard, telephone, new Date());
            memberDao.add(byTelephone);
        }
        String orderType = (String) map.get("orderType");
        Integer id = byTelephone.getId();
        //新增预约        用户id 预约时间 预约类型    是否到诊             套餐id
        Order o = new Order(id, orderDate, orderType, Order.ORDERSTATUS_NO, Integer.parseInt(setmealId));
        orderDao.add(o);
        //5.更新已预约人数
        orderSetting.setReservations(orderSetting.getReservations()+1);
        ordersettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,o.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map byId4Detail = orderDao.findById4Detail(id);
        if (byId4Detail!=null){
            Date orderDate = (Date) byId4Detail.get("orderDate");
            String date = DateUtils.parseDate2String(orderDate);
            byId4Detail.put("orderDate",date);
        }
        return byId4Detail;
    }
}
