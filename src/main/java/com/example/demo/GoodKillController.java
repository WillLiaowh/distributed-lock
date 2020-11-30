package com.example.demo;

import com.example.demo.mapper.GoodMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Good;
import com.example.demo.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.redisson.Redisson;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class GoodKillController {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    GoodMapper goodMapper;

    @Value("${server.port}")
    String port;

//    @Autowired
//    RedisTemplate redisTemplate;

//    @RequestMapping(value = "/kill",method = RequestMethod.GET)
//    public String kill(Long goodId){
//        String uuid = UUID.randomUUID().toString().replaceAll("-","");
//        if(redisTemplate.opsForValue().setIfAbsent(goodId,uuid,10, TimeUnit.SECONDS)) {
//            try {
//                //下单
//                Order order = new Order();
//                order.setGood_id(goodId);
//                order.setUser_id(123l);
//                orderMapper.insert(order);
//
//                //库存-1
//                Good good = goodMapper.selectById(goodId);
//                good.setStock(good.getStock() - 1);
//                log.info("库存量" + good.getStock() + "，端口" + port);
//                goodMapper.updateById(good);
//            }finally {
//                if(uuid.equals(redisTemplate.opsForValue().get(goodId))) {
//                    redisTemplate.opsForValue().getOperations().delete(goodId);
//                }
//            }
//            return "你抢到了";
//        }else{
//            return "你没抢到了";
//        }
//    }

    //    @Autowired
    //    RedissonClient redissonClient;

//    @RequestMapping(value = "/kill",method = RequestMethod.GET)
//    public String kill(Long goodId) throws InterruptedException {
//        RLock rLock = redissonClient.getLock("lock");
//        //rLock.lock();
//        boolean res = rLock.tryLock(100, 10, TimeUnit.SECONDS);
//        if(res) {
//            try {
//                //下单
//                Order order = new Order();
//                order.setGood_id(goodId);
//                order.setUser_id(123l);
//                orderMapper.insert(order);
//
//                //库存-1
//                Good good = goodMapper.selectById(goodId);
//                good.setStock(good.getStock() - 1);
//                log.info("库存量" + good.getStock() + "，端口" + port);
//                goodMapper.updateById(good);
//            } finally {
//                rLock.unlock();
//            }
//            return "你抢到了";
//        }else{
//            return "你没抢到了";
//        }
//
//    }

    @Autowired
    private CuratorFramework curatorFramework;

    @RequestMapping(value = "/kill", method = RequestMethod.GET)
    public String kill(Long goodId) throws Exception {
        String lockNode = "/lock_node";
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, lockNode);

        boolean res = lock.acquire(2, TimeUnit.SECONDS);
        if (res) {
            try {
                //下单
                Order order = new Order();
                order.setGood_id(goodId);
                order.setUser_id(123L);
                orderMapper.insert(order);

                //库存-1
                Good good = goodMapper.selectById(goodId);
                good.setStock(good.getStock() - 1);
                log.info("库存量" + good.getStock() + "，端口" + port);
                goodMapper.updateById(good);
            } finally {
                lock.release();
            }
            return "你抢到了";
        } else {
            return "你没抢到了";
        }

    }
}
