package com.ride.config;

import com.ride.config.security.JwtAuthenticationFilter;
import com.ride.config.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置类
 * 配置密码加密器和Web安全规则
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 创建BCrypt密码加密器
     * 用于密码的加密和验证
     *
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 强度设为12，可以根据需要调整
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 配置AuthenticationManager
     * 用于处理身份认证请求
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        System.out.println("=============== Configuring AuthenticationManager");
        return authConfig.getAuthenticationManager();
    }

    /**
     * 配置SecurityFilterChain
     * 设置URL访问权限规则和过滤器链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("=============== Configuring Spring Security Filter Chain");
        
        http
            // 禁用CSRF保护（开发环境）
            .csrf(AbstractHttpConfigurer::disable)
            // 添加JWT认证过滤器到过滤链中，放在UsernamePasswordAuthenticationFilter之前
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // 配置请求授权规则
            .authorizeHttpRequests(authorize -> authorize
                // 允许Swagger相关路径的访问
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/webjars/**"
                ).permitAll()
                // 允许登录接口和静态资源的访问
                .requestMatchers("/auth/**", "/tcm/posts/category/**","/tcm/videos", "/tcm/videos/*/thumbnail",
                "/tcm/videos/*/thumbnail-url","tcm/posts/hot", "/uploads/**","tcm/posts","tcm/videos/*/comments",
                        "tcm/posts/all","/tcm/replies/post/*","/tcm/posts/*",
                        "/tcm/videos/*","/tcm/articles/list","tcm/articles/*"
                )
                .permitAll()
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            );
            
        return http.build();
    }
}