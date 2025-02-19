package com.arch.seckill.mapper;

import com.arch.seckill.pojo.Goods;
import com.arch.seckill.vo.GoodsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yaomy
 * @since 2024-04-11
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /*
    * 获取商品列表
    * */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
