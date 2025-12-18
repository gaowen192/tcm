package com.ride.service;

import com.ride.dto.RegistrationRequest;
import com.ride.entity.TcmUser;
import com.ride.mapper.TcmUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

/**
 * 用户注册服务
 * 处理用户注册逻辑、验证码验证、数据保存等
 */
@Service
@Validated
public class RegistrationService {
    
    // 手动创建Logger
    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
    
    @Autowired
    private TcmUserRepository tcmUserRepository;
    
    @Autowired
    private Validator validator;
    
    /**
     * 用户注册
     * 
     * @param request 注册请求
     * @return 注册结果
     */
    @Transactional
    public RegistrationResult register(@Valid RegistrationRequest request) {
        log.info("=============== Start processing user registration request: {}", request.getUsername());
        
        // 1. 基础验证
        validateRegistrationRequest(request);
   
        // 2. 检查用户名是否已存在
        if (tcmUserRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("=============== Username already exists: {}", request.getUsername());
            return RegistrationResult.failure("Username already exists, please choose another username");
        }
        
        // 3. 检查手机号是否已存在（如果提供了手机号）
        if (StringUtils.hasText(request.getPhone())) {
            if (tcmUserRepository.findByPhone(request.getPhone()).isPresent()) {
                log.warn("=============== Phone number already registered: {}", request.getPhone());
                return RegistrationResult.failure("This phone number has already been registered");
            }
        }
        
        // 4. 检查邮箱是否已存在（如果提供了邮箱）
        if (StringUtils.hasText(request.getEmail())) {
            if (tcmUserRepository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("=============== Email already registered: {}", request.getEmail());
                return RegistrationResult.failure("This email has already been registered");
            }
        }
        
        // 5. 验证手机验证码（如果提供了手机号）
        if (StringUtils.hasText(request.getPhone())) {
            if (!verifyPhoneCode(request.getPhone(), request.getPhoneVerificationCode())) {
                log.warn("=============== Phone verification code error: {}", request.getPhone());
                return RegistrationResult.failure("Phone verification code is incorrect or expired");
            }
        }
        
        // 6. 验证邮箱验证码（如果提供了邮箱）
        if (StringUtils.hasText(request.getEmail())) {
            if (!verifyEmailCode(request.getEmail(), request.getEmailVerificationCode())) {
                log.warn("=============== Email verification code error: {}", request.getEmail());
                return RegistrationResult.failure("Email verification code is incorrect or expired");
            }
        }
        
        // 7. 创建用户并保存
        TcmUser user = createUserFromRequest(request);
        tcmUserRepository.save(user);
        
        log.info("=============== User registration successful: {}", request.getUsername());
        return RegistrationResult.success("Registration successful", user.getId());
    }
    
    /**
     * 验证注册请求的基础信息
     */
    private void validateRegistrationRequest(RegistrationRequest request) {
        // 使用Bean Validation验证
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            throw new IllegalArgumentException("Registration information validation failed: " + errorMessage);
        }
        
        // 密码一致性验证
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }
        
        log.debug("=============== Registration information validation passed");
    }
    
    /**
     * 模拟手机验证码验证
     */
    private boolean verifyPhoneCode(String phone, String code) {
        // 这里应该调用实际的验证码服务
        // 模拟验证逻辑：6位数字验证码
        System.out.println(11111);
        return true;
       // return StringUtils.hasText(code) && code.matches("\\d{6}");
    }
    
    /**
     * 模拟邮箱验证码验证
     */
    private boolean verifyEmailCode(String email, String code) {
        // 这里应该调用实际的验证码服务
        // 模拟验证逻辑：6位数字验证码
        return true; 
       // return StringUtils.hasText(code) && code.matches("\\d{6}");
    }
    
    /**
     * 从注册请求创建用户实体
     */
    private TcmUser createUserFromRequest(RegistrationRequest request) {
        TcmUser user = new TcmUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // 注意：实际生产中应该加密
        
        // 只有当手机号存在时才设置
        if (StringUtils.hasText(request.getPhone())) {
            user.setPhone(request.getPhone());
        }
        
        // 只有当邮箱存在时才设置
        if (StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        
        // 设置用户类型
        user.setUserType(request.getUserType());
        
        user.setStatus(1); // 1表示活跃状态
        
        return user;
    }
    
    /**
     * 注册结果
     */
    public static class RegistrationResult {
        private boolean success;
        private String message;
        private Long userId;
        
        public RegistrationResult(boolean success, String message, Long userId) {
            this.success = success;
            this.message = message;
            this.userId = userId;
        }
        
        public static RegistrationResult success(String message, Long userId) {
            return new RegistrationResult(true, message, userId);
        }
        
        public static RegistrationResult failure(String message) {
            return new RegistrationResult(false, message, null);
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Long getUserId() { return userId; }
    }
}