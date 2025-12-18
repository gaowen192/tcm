package com.ride.service;

import com.ride.dto.LoginRequest;
import com.ride.dto.LoginResponse;
import com.ride.dto.TcmUserDTO;

/**
 * 登录服务接口
 * 处理用户登录、注销等认证相关业务逻辑
 */
public interface LoginService {

    /**
     * 用户登录
     * 根据用户名和密码进行身份验证
     * 
     * @param loginRequest 登录请求参数
     * @return 登录响应结果
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 用户注销
     * 
     * @param userId 用户ID
     */
    void logout(String userId);
    
    /**
     * 刷新访问令牌
     * 
     * @param oldToken 旧的访问令牌
     * @return 新的访问令牌
     */
    String refreshToken(String oldToken);
    
    /**
     * 验证用户身份
     * 
     * @param userDTO 用户信息
     * @param password 密码
     * @return 验证结果
     */
    boolean authenticate(TcmUserDTO userDTO, String password);
}
