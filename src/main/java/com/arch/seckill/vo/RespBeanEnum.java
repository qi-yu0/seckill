package com.arch.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    //通用状态码
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    //登录模块5002xx
    SESSION_ERROR(500210,"session不存在或者已经失效"),
    LOGIN_ERROR(500211,"用户名或者密码错误"),
    MOBILE_ERROR(500212,"手机号码格式错误"),
    BIND_ERROR(500223,"异常"),

    MOBILE_NOT_EXIST(500213,"手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214,"密码更新失败"),
    //秒杀模块
    EMPTY_STOCK(500500,"库存不足"),
    REPEATE_ERROR(500505,"重复抢购"),
    PROGRAM_ERROR(500216,"商品被同一用户重复抢购成功"),
    ORDER_NOT_EXIST(500506,"订单不存在" );
    private final Integer code;
    private final String message;
}
