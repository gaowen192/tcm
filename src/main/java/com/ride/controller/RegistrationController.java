package com.ride.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ride.common.Result;
import com.ride.dto.RegistrationRequest;
import com.ride.service.RegistrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
/**
 * 用户注册控制器
 * 处理用户注册相关的HTTP请求
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "用户注册管理", description = "用户注册、验证相关接口")
public class RegistrationController {
    
    // 手动创建Logger
    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    
    @Autowired
    private RegistrationService registrationService;
    
    /**
     * 用户注册接口
     * 
     * @param request 注册请求
     * @return 注册结果
     */
    @Operation(summary = "用户注册", description = "根据注册请求信息创建新用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "注册成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或业务验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegistrationRequest request) {
        // Log registration request with contact information type
        String contactInfo = StringUtils.hasText(request.getPhone()) ? "phone: " + request.getPhone() : "";        
        if (StringUtils.hasText(request.getEmail())) {
            contactInfo += (StringUtils.hasText(contactInfo) ? " and " : "") + "email: " + request.getEmail();
        }
        log.info("=============== Received registration request: {}", contactInfo);
        
        try {
            RegistrationService.RegistrationResult result = registrationService.register(request);
            
            if (result.isSuccess()) {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", result.getUserId());
                
                return Result.success("注册成功", data);
            } else {
                return Result.badRequest(result.getMessage());
            }
            
        } catch (IllegalArgumentException e) {
            log.warn("=============== Registration validation failed: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("=============== Registration processing exception", e);
            return Result.error("服务器内部错误，请稍后重试");
        }
    }
    
    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        // 将错误信息封装到数据中返回
        Map<String, String> data = new HashMap<>();
        data.put("message", "参数验证失败");
        data.putAll(errors);
        
        // 使用badRequest方法返回400状态码的失败响应
        return Result.badRequest("参数验证失败");
    }
    
    /**
     * 获取手机验证码
     * 随机生成6位数验证码并返回给前端
     * 
     * @param phone 手机号码
     * @return 验证码生成结果
     */
    @Operation(summary = "获取手机验证码", description = "根据手机号码生成6位数字验证码")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "验证码生成成功"),
        @ApiResponse(responseCode = "400", description = "手机号码格式不正确或为空"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/phone/verification-code")
    public Result<Map<String, String>> getPhoneVerificationCode(
            @Parameter(description = "手机号码", required = true, example = "13800138000") 
            @RequestParam String phone) {
        log.info("=============== Received phone verification code request: {}", phone);
        
        try {
            // 参数验证
            if (phone == null || phone.trim().isEmpty()) {
                return Result.badRequest("Phone number cannot be empty");
            }
            
            // 简单手机号格式验证（11位数字，以1开头）
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                return Result.badRequest("Invalid phone number format");
            }
            
            // 生成6位随机验证码
            String verificationCode = generateVerificationCode();
            
            // TODO: 这里可以添加发送短信的逻辑
            
            log.info("=============== Generated verification code for phone {}: {}", phone, verificationCode);
            
            Map<String, String> data = new HashMap<>();
            data.put("phone", phone);
            data.put("verificationCode", verificationCode);
            
            return Result.success("验证码生成成功", data);
            
        } catch (Exception e) {
            log.error("=============== Phone verification code generation exception", e);
            return Result.error("服务器内部错误，请稍后重试");
        }
    }
    
    /**
     * 获取邮箱验证码
     * 随机生成6位数验证码并返回给前端
     * 
     * @param email 邮箱地址
     * @return 验证码生成结果
     */
    @Operation(summary = "获取邮箱验证码", description = "根据邮箱地址生成6位数字验证码")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "验证码生成成功"),
        @ApiResponse(responseCode = "400", description = "邮箱地址格式不正确或为空"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/email/verification-code")
    public Result<Map<String, String>> getEmailVerificationCode(
            @Parameter(description = "邮箱地址", required = true, example = "user@example.com") 
            @RequestParam String email) {
        log.info("=============== Received email verification code request: {}", email);
        
        try {
            // 参数验证
            if (email == null || email.trim().isEmpty()) {
                return Result.badRequest("Email address cannot be empty");
            }
            
            // 简单邮箱格式验证
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return Result.badRequest("Invalid email address format");
            }
            
            // 生成6位随机验证码
            String verificationCode = generateVerificationCode();
            
            // TODO: 这里可以添加发送邮件的逻辑
            
            log.info("=============== Generated verification code for email {}: {}", email, verificationCode);
            
            Map<String, String> data = new HashMap<>();
            data.put("email", email);
            data.put("verificationCode", verificationCode);
            
            return Result.success("验证码生成成功", data);
            
        } catch (Exception e) {
            log.error("=============== Email verification code generation exception", e);
            return Result.error("服务器内部错误，请稍后重试");
        }
    }
    
    /**
     * 生成6位随机数字验证码
     * 
     * @return 6位数字字符串
     */
    private String generateVerificationCode() {
        // 生成100000-999999之间的随机数
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        
        return Result.badRequest(ex.getMessage());
    }
}