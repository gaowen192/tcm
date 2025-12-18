package com.ride.test;

import com.ride.dto.RegistrationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 注册请求DTO测试类
 * 验证RegistrationRequest的字段验证功能
 */
class RegistrationRequestTest {
    
    private Validator validator;
    private RegistrationRequest request;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        request = new RegistrationRequest();
    }
    
    @Test
    void testValidRegistrationRequest() {
        // 创建一个有效的注册请求
        request.setUsername("testuser123");
        request.setPassword("TestPassword1");
        request.setConfirmPassword("TestPassword1");
        request.setPhone("13800138000");
        request.setPhoneVerificationCode("123456");
        request.setEmail("test@example.com");
        request.setEmailVerificationCode("123456");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        // 验证没有验证错误
        assertTrue(violations.isEmpty(), "有效的注册请求不应该有验证错误");
    }
    
    @Test
    void testUsernameValidation() {
        // 测试用户名验证
        request.setUsername(""); // 空用户名
        assertSingleViolation("username", "用户名不能为空");
        
        request.setUsername("ab"); // 太短
        assertSingleViolation("username", "用户名长度必须在3-20个字符之间");
        
        request.setUsername("user@name!"); // 包含特殊字符
        assertSingleViolation("username", "用户名只能包含字母、数字和下划线");
    }
    
    @Test
    void testPasswordValidation() {
        // 测试密码验证
        request.setPassword(""); // 空密码
        assertSingleViolation("password", "密码不能为空");
        
        request.setPassword("123"); // 太短且无字母
        assertSingleViolation("password", "密码长度必须在6-20个字符之间");
        
        request.setPassword("password"); // 只有小写字母
        assertSingleViolation("password", "密码必须包含大小写字母和数字");
        
        request.setPassword("PASSWORD"); // 只有大写字母
        assertSingleViolation("password", "密码必须包含大小写字母和数字");
        
        request.setPassword("123456"); // 只有数字
        assertSingleViolation("password", "密码必须包含大小写字母和数字");
    }
    
    @Test
    void testPhoneValidation() {
        // 测试手机号验证
        request.setPhone(""); // 空手机号
        assertSingleViolation("phone", "手机号不能为空");
        
        request.setPhone("123"); // 太短
        assertSingleViolation("phone", "请输入正确的手机号格式");
        
        request.setPhone("1380013800"); // 少一位
        assertSingleViolation("phone", "请输入正确的手机号格式");
        
        request.setPhone("138001380000"); // 多一位
        assertSingleViolation("phone", "请输入正确的手机号格式");
        
        request.setPhone("12800138000"); // 不是以13-9开头
        assertSingleViolation("phone", "请输入正确的手机号格式");
    }
    
    @Test
    void testEmailValidation() {
        // 测试邮箱验证
        request.setEmail(""); // 空邮箱
        assertSingleViolation("email", "邮箱地址不能为空");
        
        request.setEmail("invalid-email"); // 无效邮箱格式
        assertSingleViolation("email", "请输入正确的邮箱格式");
        
        request.setEmail("test@"); // 无效邮箱格式
        assertSingleViolation("email", "请输入正确的邮箱格式");
        
        request.setEmail("@example.com"); // 无效邮箱格式
        assertSingleViolation("email", "请输入正确的邮箱格式");
    }
    
    @Test
    void testVerificationCodeValidation() {
        // 设置其他有效字段
        setOtherValidFields();
        
        // 测试手机验证码验证
        request.setPhoneVerificationCode(""); // 空验证码
        assertSingleViolation("phoneVerificationCode", "手机验证码不能为空");
        
        request.setPhoneVerificationCode("12345"); // 5位数字
        assertSingleViolation("phoneVerificationCode", "手机验证码必须是6位数字");
        
        request.setPhoneVerificationCode("1234567"); // 7位数字
        assertSingleViolation("phoneVerificationCode", "手机验证码必须是6位数字");
        
        request.setPhoneVerificationCode("123abc"); // 包含字母
        assertSingleViolation("phoneVerificationCode", "手机验证码必须是6位数字");
        
        // 测试邮箱验证码验证
        request.setEmailVerificationCode(""); // 空验证码
        assertSingleViolation("emailVerificationCode", "邮箱验证码不能为空");
        
        request.setEmailVerificationCode("12345"); // 5位数字
        assertSingleViolation("emailVerificationCode", "邮箱验证码必须是6位数字");
    }
    
    /**
     * 设置其他有效字段
     */
    private void setOtherValidFields() {
        request.setUsername("testuser123");
        request.setPassword("TestPassword1");
        request.setConfirmPassword("TestPassword1");
        request.setPhone("13800138000");
        request.setEmail("test@example.com");
    }
    
    /**
     * 断言特定字段有且仅有一个验证错误
     */
    private void assertSingleViolation(String fieldName, String expectedMessage) {
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertEquals(1, violations.size(), "应该有且仅有一个验证错误");
        
        ConstraintViolation<RegistrationRequest> violation = violations.iterator().next();
        assertEquals(fieldName, violation.getPropertyPath().toString());
        assertEquals(expectedMessage, violation.getMessage());
    }
}