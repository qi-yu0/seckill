package com.arch.seckill.controller;

import com.arch.seckill.service.IUserService;
import com.arch.seckill.vo.LoginVo;
import com.arch.seckill.vo.RespBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/login")
@Slf4j
public class loginController {

    @Autowired
    private IUserService userService;

    /*
    * 页面跳转
    * */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /*
    * 登录功能
    * */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        return userService.doLogin(loginVo,request,response);
    }
}
