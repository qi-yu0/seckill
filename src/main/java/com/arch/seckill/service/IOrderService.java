package com.arch.seckill.service;

import com.arch.seckill.pojo.Order;
import com.arch.seckill.pojo.User;
import com.arch.seckill.vo.GoodsVo;
import com.arch.seckill.vo.OrderDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaomy
 * @since 2024-04-11
 */
public interface IOrderService extends IService<Order> {
    /*
    * 秒杀功能实现
    * */

    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

}
