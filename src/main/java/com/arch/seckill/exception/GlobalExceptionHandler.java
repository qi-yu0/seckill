package com.arch.seckill.exception;

import com.arch.seckill.vo.RespBean;
import com.arch.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException ex){
            return RespBean.error(ex.getRespBeanEnum());
        }else if(e instanceof BindException ex){
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常："+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        System.out.println(e);
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
