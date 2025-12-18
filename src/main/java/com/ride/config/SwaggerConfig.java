package com.ride.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * Swagger配置类
 * 用于配置API文档的显示信息和请求头
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
    
    @Bean
    public OpenAPI customOpenAPI() {
        System.out.println("=============== Configuring Swagger with standard JWT authentication");
        logger.info("=============== Configuring Swagger with standard JWT authentication");
        
        // 使用更标准的JWT认证配置方式
        // 1. 创建Bearer认证安全方案
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization")
                .description("JWT认证令牌");
        
        // 2. 创建组件并添加安全方案
        Components components = new Components();
        components.addSecuritySchemes("jwtAuth", securityScheme);
        
        // 3. 创建安全需求
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("jwtAuth");
        
        System.out.println("=============== Standard JWT authentication configuration completed");
        logger.info("=============== Standard JWT authentication configuration completed");
        
        // 4. 创建并返回完整的OpenAPI对象
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(apiInfo());
        openAPI.servers(List.of(
            new Server().url("http://localhost:8080/api").description("本地开发环境"),
            new Server().url("https://api.ride.com/api").description("生产环境")
        ));
        openAPI.components(components);
        openAPI.security(Collections.singletonList(securityRequirement));
        
        return openAPI;
    }
    
    private Info apiInfo() {
        return new Info()
                .title("千行服务API文档")
                .description("千行服务微服务项目API接口文档，支持用户注册、用户管理等功能的RESTful API")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("千行团队")
                        .email("support@ride.com")
                        .url("https://ride.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org")
                        );
    }
}