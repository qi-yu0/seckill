package com.arch.seckill.service;

import com.arch.seckill.pojo.Goods;
import com.arch.seckill.vo.GoodsVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaomy
 * @since 2024-04-11
 */
public interface IGoodsService extends IService<Goods> {

    /*
    * @Description: 获取商品列表
    * @Author: yaomy
    * */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
