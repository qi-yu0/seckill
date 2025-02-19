package com.arch.seckill.controller;

import com.arch.seckill.pojo.User;
import com.arch.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 用户控制类
 * @author yaomy
 * @since 2024-04-05
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /*
    * 用户信息
    * */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }
}
