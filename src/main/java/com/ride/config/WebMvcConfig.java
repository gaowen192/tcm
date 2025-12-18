package com.ride.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Spring MVC配置类
 * 调整请求处理顺序，确保控制器优先于静态资源处理器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("=============== Configuring resource handlers with lower priority");
        
        // 确保uploads和videos目录存在
        String userDir = System.getProperty("user.dir");
        log.info("=============== Current working directory: " + userDir);
        
        // 构建绝对路径，使用File.separator确保跨平台兼容性
        String uploadsDir = userDir + File.separator + "uploads";
        String videosDir = uploadsDir + File.separator + "videos";
        
        // 创建目录（如果不存在）
        File uploadsFile = new File(uploadsDir);
        File videosFile = new File(videosDir);
        
        if (!uploadsFile.exists() && uploadsFile.mkdirs()) {
            log.info("=============== Created uploads directory: " + uploadsDir);
        }
        if (!videosFile.exists() && videosFile.mkdirs()) {
            log.info("=============== Created videos directory: " + videosDir);
        }
        
        // 转换为URL格式的路径（使用正斜杠）
        String uploadsPath = "file:" + uploadsDir.replace("\\", "/") + "/";
        String videosPath = "file:" + videosDir.replace("\\", "/") + "/";
        
        log.info("=============== Uploads directory path (URL format): " + uploadsPath);
        log.info("=============== Videos directory path (URL format): " + videosPath);
        
        // 验证目录是否可访问
        log.info("=============== Uploads directory exists: " + uploadsFile.exists());
        log.info("=============== Videos directory exists: " + videosFile.exists());
        log.info("=============== Uploads directory is readable: " + uploadsFile.canRead());
        log.info("=============== Videos directory is readable: " + videosFile.canRead());
        
        // 配置静态资源处理器，设置较低优先级
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600)
                .resourceChain(true);
        
        // 配置视频文件的资源映射，增加优先级以确保正确处理
        log.info("=============== Configuring dedicated video file resource handlers");
        
        // 主要的视频文件映射 - 不带API前缀
        registry.addResourceHandler("/uploads/videos/**")
                .addResourceLocations(videosPath)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new org.springframework.web.servlet.resource.PathResourceResolver());
                
        // 带API前缀的视频文件映射
        registry.addResourceHandler("/api/uploads/videos/**")
                .addResourceLocations(videosPath)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new org.springframework.web.servlet.resource.PathResourceResolver());
        
        // 通用uploads映射 - 不带API前缀
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadsPath)
                .setCachePeriod(3600)
                .resourceChain(true);
        
        // 带API前缀的通用uploads映射
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations(uploadsPath)
                .setCachePeriod(3600)
                .resourceChain(true);
        
        log.info("=============== Resource handlers configuration completed");
    }

}
