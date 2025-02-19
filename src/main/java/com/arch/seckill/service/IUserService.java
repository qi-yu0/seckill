package com.arch.seckill.service;

import com.arch.seckill.pojo.User;
import com.arch.seckill.vo.LoginVo;
import com.arch.seckill.vo.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 *  服务类
 * @author yaomy
 * @since 2024-04-05
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    RespBean updatePassword(String userTicket,Long id,String password);
}
