package com.arch.seckill.service.impl;

import com.arch.seckill.exception.GlobalException;
import com.arch.seckill.mapper.OrderMapper;
import com.arch.seckill.pojo.Order;
import com.arch.seckill.pojo.SeckillGoods;
import com.arch.seckill.pojo.SeckillOrder;
import com.arch.seckill.pojo.User;
import com.arch.seckill.service.IGoodsService;
import com.arch.seckill.service.IOrderService;
import com.arch.seckill.service.ISeckillGoodsService;
import com.arch.seckill.service.ISeckillOrderService;
import com.arch.seckill.vo.GoodsVo;
import com.arch.seckill.vo.RespBeanEnum;
import com.arch.seckill.vo.OrderDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaomy
 * @since 2024-04-11
 */
@Transactional
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 订单服务
    *  windows优化后QPS 1078
    * */

    @Override
    public Order seckill(User user, GoodsVo goods) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                .eq("goods_id",goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);

        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().
                setSql("stock_count = stock_count-1").eq("goods_id",goods.getId()).
                gt("stock_count",0));
//        if(!result){
//            return null;
//        }
        if(seckillGoods.getStockCount() < 1){
            valueOperations.set("isStockEmpty:"+goods.getId(),"0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(),seckillOrder);
        return order;

    }
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (null==orderId){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setGoodsVo(goodsVo);
        detail.setOrder(order);
        return detail;
    }
}
