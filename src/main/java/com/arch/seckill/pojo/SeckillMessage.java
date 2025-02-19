package com.arch.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 秒杀信息类
 * @Author:yaomy
 * @DateTime: 2024/4/23 22:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
    private User user;
    private  Long goodsId;
}
