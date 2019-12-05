package com.itheima.TASK;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class ClearImgTask {

    @Autowired
    private JedisPool jedisPool;

    @Scheduled(cron = "0/15 * * * * ?")
    public void clearImg(){
        System.out.println("删除垃圾图片开始......");
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.MEAL_RESOURCES, RedisConstant.MEAL_DB_RESOURCES);
        if(set != null){
            for (String fileName : set) {
                QiniuUtils.deleteFileFromQiniu(fileName);
                jedisPool.getResource().srem(RedisConstant.MEAL_RESOURCES,fileName);
            }
        }
    }
}
