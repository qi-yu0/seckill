package com.arch.seckill.rabbitmq;

import com.arch.seckill.pojo.SeckillMessage;
import com.arch.seckill.pojo.User;
import com.arch.seckill.service.IGoodsService;
import com.arch.seckill.service.IOrderService;
import com.arch.seckill.utils.JsonUtil;
import com.arch.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息接收类
 * @Author:yaomy
 * @DateTime: 2024/4/21 15:29
 **/
@Service
@Slf4j
public class MQReceiver {

//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接收消息：" + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//
//        log.info("QUEUE01接收消息：" + msg);
//    }
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("QUEUE02接收消息：" + msg);
//    }

    /*
    * 下单操作
    * */

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("QUEUE接受消息" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStrToObject(message,SeckillMessage.class);

        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount() < 1){
            return;
        }
        //判断是否重复抢购
        String seckillOrderJson =
                (String)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);

        if(seckillOrderJson != null){
            return;
        }
        //下单操作
        orderService.seckill(user,goods);
    }
}
