package com.ride.config.security;

import com.ride.entity.TcmUser;
import com.ride.mapper.TcmUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * UserDetailsService实现类
 * 用于加载用户信息，支持Spring Security认证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private TcmUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=============== Loading user by username: " + username);
        
        // 根据用户名查找用户
        TcmUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("=============== User not found: {}", username);
                    return new UsernameNotFoundException("用户不存在: " + username);
                });

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() != 1) {
            logger.warn("=============== User account disabled: {}", username);
            throw new UsernameNotFoundException("账户已被禁用: " + username);
        }

        logger.info("=============== User {} loaded successfully", username);
        
        // 返回Spring Security的UserDetails对象
        // 注意：这里暂时返回空权限列表，实际项目中应该返回用户的权限
        return new User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // 权限列表，后续可以根据实际需求添加
        );
    }
}
