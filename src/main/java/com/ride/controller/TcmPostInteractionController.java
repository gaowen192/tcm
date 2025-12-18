package com.ride.controller;

import com.ride.common.Result;
import com.ride.service.TcmPostInteractionService;
import com.ride.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 帖子互动记录Controller
 */
@RestController
@RequestMapping("/post/interaction")
@Tag(name = "帖子互动管理", description = "用户对帖子的点赞和观看记录管理")
public class TcmPostInteractionController {

    private static final Logger log = LoggerFactory.getLogger(TcmPostInteractionController.class);

    @Autowired
    private TcmPostInteractionService tcmPostInteractionService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 记录帖子点赞
     * @param authorization 请求头中的认证信息
     * @param postId 帖子ID
     * @return 操作结果
     */
    @PostMapping("/like/{postId}")
    @Operation(summary = "记录帖子点赞", description = "记录用户对帖子的点赞行为")
    public Result<?> recordPostLike(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        log.debug("=============== Received request to record post like: postId={}", postId);
        
        try {
            // 从Authorization头中获取用户ID（实际项目中应使用JWT解析）
            Long userId = extractUserIdFromAuthorization(authorization);
            
            tcmPostInteractionService.recordPostInteraction(userId, postId, "LIKE");
            return Result.success("点赞成功");
        } catch (Exception e) {
            log.error("=============== Failed to record post like: {}", e.getMessage(), e);
            return Result.error("点赞失败");
        }
    }

    /**
     * 记录帖子观看
     * @param authorization 请求头中的认证信息
     * @param postId 帖子ID
     * @return 操作结果
     */
    @PostMapping("/watch/{postId}")
    @Operation(summary = "记录帖子观看", description = "记录用户对帖子的观看行为")
    public Result<?> recordPostWatch(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        log.debug("=============== Received request to record post watch: postId={}", postId);
        
        try {
            // 从Authorization头中获取用户ID（实际项目中应使用JWT解析）
            Long userId = extractUserIdFromAuthorization(authorization);
            
            tcmPostInteractionService.recordPostInteraction(userId, postId, "WATCH");
            return Result.success("观看记录成功");
        } catch (Exception e) {
            log.error("=============== Failed to record post watch: {}", e.getMessage(), e);
            return Result.error("观看记录失败");
        }
    }

    /**
     * 获取用户帖子互动历史
     * @param authorization 请求头中的认证信息
     * @param page 页码（从1开始，默认1）
     * @param pageSize 每页数量（默认10）
     * @return 互动历史列表
     */
    @GetMapping("/history")
    @Operation(summary = "获取用户帖子互动历史", description = "获取用户对帖子的点赞和观看历史记录")
    public Result<Map<String, Object>> getUserPostHistory(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "页码（从1开始，默认1）", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量（默认10）", example = "10") @RequestParam(defaultValue = "10") int pageSize) {
        log.debug("=============== Received request to get user post interaction history with page={}, pageSize={}", page, pageSize);
        
        try {
            // 从Authorization头中获取用户ID（实际项目中应使用JWT解析）
            Long userId = extractUserIdFromAuthorization(authorization);
            
            Map<String, Object> history = tcmPostInteractionService.getUserPostHistoryWithPagination(userId, page, pageSize);
            return Result.success(history);
        } catch (Exception e) {
            log.error("=============== Failed to get user post interaction history: {}", e.getMessage(), e);
            return Result.error("获取互动历史失败");
        }
    }

    /**
     * 从Authorization头中提取用户ID
     * @param authorization 认证头
     * @return 用户ID
     */
    private Long extractUserIdFromAuthorization(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String jwtToken = authorization.substring(7); // 去除Bearer前缀
            return jwtTokenUtil.getUserIdFromToken(jwtToken);
        }
        return null;
    }
}
