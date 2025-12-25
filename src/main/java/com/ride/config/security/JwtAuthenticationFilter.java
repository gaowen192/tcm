package com.ride.config.security;

import com.ride.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ride.common.Result;

/**
 * JWT认证过滤器
 * 拦截请求并验证JWT令牌
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("=============== JwtAuthenticationFilter processing request: " + requestURI);
        
        try {
            String authHeader = request.getHeader("Authorization");
            String username = null;
            String jwtToken = null;
            logger.info("========= JWT token header ====== {}", authHeader);
            
            // 从请求头中获取并验证token
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } else {
                logger.warn("=============== JWT token does not begin with Bearer String");
            }

            // 如果找到了用户名且当前安全上下文中没有认证信息
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 验证token是否有效
                if (jwtTokenUtil.validateToken(jwtToken)) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 设置认证信息到安全上下文
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("=============== User {} authenticated successfully", username);
                }
            }
            
            // 继续过滤链
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            logger.error("=============== JWT token expired: {}", e.getMessage());
            handleJwtException(response, Result.unauthorized("JWT token expired"));
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            logger.error("=============== Invalid JWT token: {}", e.getMessage());
            handleJwtException(response, Result.unauthorized("Invalid JWT token"));
        } catch (IllegalArgumentException e) {
            logger.error("=============== JWT claims string is empty: {}", e.getMessage());
            handleJwtException(response, Result.unauthorized("JWT token is empty"));
        }
    }

    /**
     * 处理JWT异常，返回标准化的错误响应
     */
    private void handleJwtException(HttpServletResponse response, Result<?> result) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
