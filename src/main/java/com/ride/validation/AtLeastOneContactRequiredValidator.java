package com.ride.validation;

import com.ride.dto.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * 自定义验证器
 * 验证注册请求中至少提供一种联系方式（手机号或邮箱）
 */
public class AtLeastOneContactRequiredValidator implements 
        ConstraintValidator<AtLeastOneContactRequired, RegistrationRequest> {

    @Override
    public void initialize(AtLeastOneContactRequired constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true; // null验证应该在其他注解中处理
        }
        
        boolean hasPhone = StringUtils.hasText(request.getPhone()) && 
                          StringUtils.hasText(request.getPhoneVerificationCode());
        boolean hasEmail = StringUtils.hasText(request.getEmail()) && 
                          StringUtils.hasText(request.getEmailVerificationCode());
        
        return hasPhone || hasEmail;
    }
}