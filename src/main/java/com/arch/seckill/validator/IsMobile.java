package com.arch.seckill.validator;

import com.arch.seckill.vo.IsMobileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@Target 注解指定了 @IsMobile 注解可以应用的位置
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })

//@Retention 注解定义了注解的生命周期。RUNTIME 表示注解在运行时是可用的。即，注解信息会保留在运行时，允许反射机制读取并进行处理。
@Retention(RUNTIME)

//@Documented 注解表示该注解会出现在 Javadoc 中，意味着生成的文档会包括这个注解的定义
@Documented

@Constraint(validatedBy = {IsMobileValidator.class })
public @interface IsMobile {

    boolean required() default true;
    String message() default "手机号码格式错误";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
