package com.arch.seckill.service;

import com.arch.seckill.pojo.SeckillOrder;
import com.arch.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaomy
 * @since 2024-04-11
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /*
    * 获取秒杀结果
    * */
    Long getResult(User user, Long goodsId);
}
