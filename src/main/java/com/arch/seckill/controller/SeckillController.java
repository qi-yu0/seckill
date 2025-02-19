package com.arch.seckill.controller;
import com.arch.seckill.pojo.SeckillMessage;
import com.arch.seckill.pojo.SeckillOrder;
import com.arch.seckill.pojo.User;
import com.arch.seckill.rabbitmq.MQSender;
import com.arch.seckill.service.IGoodsService;
import com.arch.seckill.service.ISeckillOrderService;
import com.arch.seckill.utils.JsonUtil;
import com.arch.seckill.vo.GoodsVo;
import com.arch.seckill.vo.RespBean;
import com.arch.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Description: 秒杀控制类
 * @Author:yaomy
 * @DateTime: 2024/4/12 15:08
 **/
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript script;

    //标记商品是否还有库存
    private Map<Long,Boolean> EmptyStockMap = new HashMap<>(0);

    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(User user, Long goodsId){
        //判断用户是否为空
         if(user == null){
             return RespBean.error((RespBeanEnum.SESSION_ERROR));
         }

        ValueOperations valueOperations = redisTemplate.opsForValue();

         //判断是否重复抢购
        SeckillOrder seckillOrder =
                (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);

        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        /*
        方案一：直接减库存（原子操作）
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        */

        //方案二：分布式锁
        Long stock = (Long) redisTemplate.execute(script,
                Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if(stock < 0){
            //通过内存标记，减少redis访问
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user,goodsId);
        mqSender.sendSeckillMessage(JsonUtil.objectToJsonStr(seckillMessage));
        return RespBean.success(0);
     }

     @RequestMapping(value = "/result",method = RequestMethod.GET)
     @ResponseBody
     public RespBean getResult(User user,Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
     }
     /*
     * 系统初始化，把商品库存数量加载到Redis
     * */
    @Override
    public void afterPropertiesSet() throws Exception {

        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo ->{
                    redisTemplate.opsForValue().set("seckillGoods:"+ goodsVo.getId(),goodsVo.getStockCount());
                    EmptyStockMap.put(goodsVo.getId(),false);
                }
        );
    }
}