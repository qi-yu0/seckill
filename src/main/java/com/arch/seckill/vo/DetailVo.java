package com.arch.seckill.vo;

import com.arch.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author:yaomy
 * @DateTime: 2024/4/19 10:39
 **/

//Lombook代码模版，生成了无参和全参构造函数，生成了每个字段的getter和setter方法！
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private User user;
    private GoodsVo goodsVo;
    private int secKillStatus;
    private int remainSeconds;
}