package com.ride.exception;

import com.ride.common.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // 手动创建Logger
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
   
    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {
        log.warn("参数校验失败：{}", ex.getMessage());
        
        Map<String, Object> result = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        result.put("code", 400);
        result.put("message", "参数校验失败");
        result.put("errors", errors);
        result.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.badRequest().body(result);
    }
    
    /**
     * 处理数据绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBindException(BindException ex) {
        log.warn("数据绑定失败：{}", ex.getMessage());
        
        Map<String, Object> result = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        result.put("code", 400);
        result.put("message", "数据绑定失败");
        result.put("errors", errors);
        result.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.badRequest().body(result);
    }
    
    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        log.warn("非法参数：{}", ex.getMessage());
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("message", ex.getMessage());
        result.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.badRequest().body(result);
    }
    
    /**
     * 处理JWT过期异常
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public Result<?> handleExpiredJwtException(ExpiredJwtException ex) {
        log.warn("JWT token expired: {}", ex.getMessage());
        return Result.unauthorized("JWT token expired");
    }
    
    /**
     * 处理其他JWT相关异常
     */
    @ExceptionHandler({UnsupportedJwtException.class, MalformedJwtException.class, 
                     SignatureException.class})
    public Result<?> handleJwtException(Exception ex) {
        log.warn("Invalid JWT token: {}", ex.getMessage());
        return Result.unauthorized("Invalid JWT token");
    }
    
    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        log.error("系统内部错误：", ex);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "系统内部错误，请稍后重试");
        result.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}