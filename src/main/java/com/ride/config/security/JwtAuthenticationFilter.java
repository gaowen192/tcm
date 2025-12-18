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
import java.util.Enumeration;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("=============== JwtAuthenticationFilter processing request: " + requestURI);
        
        String authHeader = request.getHeader("Authorization");


        String username = null;
        String jwtToken = null;
        logger.warn("===1111111111111=======request.getHeaderNames()===== JWT token expired",request.getHeaderNames());
        System.out.println("==============authHeaderst: " + authHeader);
        System.out.println("==============authHeaderst: " + request.getHeaderNames());
        //        Enumeration<String> h= request.getHeaders("Authorization");
//        while(h.hasMoreElements()){
//            System.out.println(h.nextElement());
//            logger.warn("=============== JWT token expired",h.nextElement());
//            authHeader=h.nextElement();
//        }
        // 从请求头中获取并验证token
        logger.error("=========11111111111====== JWT token 真正 ",authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (ExpiredJwtException e) {
                logger.warn("=============== JWT token expired");
            } catch (UnsupportedJwtException e) {
                logger.warn("=============== JWT token is unsupported");
            } catch (MalformedJwtException e) {
                logger.warn("=============== Invalid JWT token");
            } catch (SignatureException e) {
                logger.warn("=============== Invalid JWT signature");
            } catch (IllegalArgumentException e) {
                logger.warn("=============== JWT claims string is empty");
            }
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
    }
}
