package com.ride.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义验证注解
 * 确保注册请求中至少提供一种联系方式（手机号或邮箱）
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneContactRequiredValidator.class)
@Documented
public @interface AtLeastOneContactRequired {
    
    String message() default "至少需要提供一种联系方式（手机号或邮箱）";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}