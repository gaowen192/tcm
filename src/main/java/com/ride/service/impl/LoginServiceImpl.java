package com.ride.service.impl;

import com.ride.dto.LoginRequest;
import com.ride.dto.LoginResponse;
import com.ride.dto.TcmUserDTO;
import com.ride.entity.TcmUser;
import com.ride.mapper.TcmUserRepository;
import com.ride.service.LoginService;
import com.ride.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 登录服务实现类
 * 处理用户登录、注销等认证相关业务逻辑
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private TcmUserRepository tcmUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("=============== Login attempt for user: {}", loginRequest.getUsername());
        
        // 1. 根据用户名查找用户
        TcmUser user = tcmUserRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));
        
        // 2. 检查用户状态
        if (user.getStatus() != null && user.getStatus() != 1) {
            log.warn("=============== Login failed for user {}: account disabled", loginRequest.getUsername());
            throw new IllegalArgumentException("账户已被禁用，请联系管理员");
        }
        
        // 3. 验证密码

        // 3. 验证密码

    log.warn("==========1111111111111===== Login failed for user {}: invalid password", loginRequest.getPassword(), user.getPassword());
         


        if (!loginRequest.getPassword().equals(user.getPassword())) {
            log.warn("=============== Login failed for user {}: invalid password", loginRequest.getUsername());
            throw new IllegalArgumentException("用户名或密码错误");
        }
        
        // 4. 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        user = tcmUserRepository.save(user);
        
        // 5. 生成访问令牌（实际项目中应该使用JWT或其他安全的令牌机制）
        String accessToken = generateAccessToken(user);
        
        // 6. 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setUserType(user.getUserType());
        response.setAccessToken(accessToken);
        response.setTokenExpireTime(LocalDateTime.now().plusHours(24)); // 24小时过期
        
        log.info("=============== Login successful for user: {}", loginRequest.getUsername());
        return response;
    }

    @Override
    public void logout(String userId) {
        log.info("=============== User logout: {}", userId);
        // TODO: 实际项目中应该将token加入黑名单或缓存
        // 这里可以实现token失效的逻辑
    }

    @Override
    public String refreshToken(String oldToken) {
        log.info("=============== Refresh token request received");
        // TODO: 实现token刷新逻辑
        // 验证旧token，生成新token
        return generateAccessToken(null); // 简化实现
    }

    @Override
    @Transactional(readOnly = true)
    public boolean authenticate(TcmUserDTO userDTO, String password) {
        // 根据用户ID查找用户
        TcmUser user = tcmUserRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        
        // 验证密码
        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * 生成访问令牌
     * 使用JWT机制生成安全的访问令牌
     */
    private String generateAccessToken(TcmUser user) {
        if (user == null) {
            return UUID.randomUUID().toString(); // 为refreshToken方法保留的空值处理
        }
        
        // 添加额外的声明信息
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("realName", user.getRealName());
        claims.put("status", user.getStatus());
        
        // 使用JWT工具类生成token
        System.out.println("=============== Generating JWT token for user: " + user.getUsername());
        return jwtTokenUtil.generateToken(user.getUsername(), claims);
    }
}