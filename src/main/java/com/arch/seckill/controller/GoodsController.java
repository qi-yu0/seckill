package com.arch.seckill.controller;

import com.arch.seckill.pojo.User;
import com.arch.seckill.service.IGoodsService;
import com.arch.seckill.vo.DetailVo;
import com.arch.seckill.vo.GoodsVo;
import com.arch.seckill.vo.RespBean;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author:yaomy
 * @DateTime: 2024/4/9 11:09
 **/
@Controller
@RequestMapping("goods")

public class GoodsController {

    public GoodsController() {
    }

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /*
    * Windows优化前QPS 208（线程数5000，循环次数10）
    * 页面缓存后QPS 1435
    * */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(HttpServletRequest request,HttpServletResponse response, Model model, User user){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //Redis中获取页面，如果不为空，直接返回页面

        String html = (String)valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        //return "goodsList";
        //如果为空，手动渲染，存入Redis并返回

// 修改前 Spring Boot2.7：
//        final IWebExchange webExchange = this.application.buildExchange(request, response);
//        IWebContext context = new WebContext(webExchange,request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);

// 修改后 Spring Boot3.0：
        JakartaServletWebApplication jakartaServletWebApplication = JakartaServletWebApplication.buildApplication(request.getServletContext());
        WebContext webContext = new WebContext(jakartaServletWebApplication.buildExchange(request, response), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }
    @RequestMapping( "/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model,User user,@PathVariable Long goodsId){

        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //距离秒杀开始时间
        int remainSeconds = 0;
        if(nowDate.before(startDate)){
            //秒杀还未开始
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        }else if (nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        }else {
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }
//        model.addAttribute("secKillStatus",secKillStatus);
//        model.addAttribute("remainSeconds",remainSeconds);
////        return "goodsDetail";
//        //如果为空，手动渲染，存入Redis并返回
//        WebContext context = new WebContext();
//
//        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail:",context);
//
//        if(StringUtils.isEmpty(html){
//            valueOperations.set("goodsDetail:" + goodsId,html,60,TimeUnit.SECONDS);
//        }

        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goods);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(secKillStatus);
        return RespBean.success(detailVo);
    }
}
