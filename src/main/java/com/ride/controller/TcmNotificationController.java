package com.ride.controller;

import com.ride.common.Result;
import com.ride.entity.TcmNotification;
import com.ride.service.TcmNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * 系统通知管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/notifications")
@Tag(name = "系统通知管理", description = "系统通知相关接口")
public class TcmNotificationController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmNotificationController.class);
    
    @Autowired
    private TcmNotificationService tcmNotificationService;
    
    /**
     * 获取用户通知列表（分页）
     * 查询出来的未读通知会自动标记为已读
     * 
     * @param userId 用户ID
     * @param page 页码，从1开始
     * @param pageSize 每页数量
     * @return 分页通知列表
     */
    @Operation(summary = "获取用户通知列表", description = "获取用户的通知列表，支持分页，查询后未读通知自动标记为已读")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}")
    public Result<Page<TcmNotification>> getNotificationsByUserId(
            @PathVariable Long userId,
            @Parameter(description = "页码，从1开始", required = false, example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", required = false, example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        log.debug("=============== Received get notifications request for user: {}, page: {}, pageSize: {}", userId, page, pageSize);
        
        // 创建分页参数，页码从0开始
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        
        // 查询通知列表
        Page<TcmNotification> notifications = tcmNotificationService.getNotificationsByUserId(userId, pageable);
        
        // 标记未读通知为已读（系统消息除外）
        List<Long> unreadNotificationIds = notifications.getContent().stream()
                .filter(n -> !"system".equals(n.getType()) && !Boolean.TRUE.equals(n.getIsRead()))
                .map(TcmNotification::getId)
                .toList();
        
        if (!unreadNotificationIds.isEmpty()) {
            tcmNotificationService.batchMarkAsRead(unreadNotificationIds);
            log.debug("=============== Marked {} notifications as read", unreadNotificationIds.size());
        }
        
        return Result.success("Query successful", notifications);
    }
    
    /**
     * 获取用户未读通知数量
     * 
     * @param userId 用户ID
     * @return 未读通知数量
     */
    @Operation(summary = "获取未读通知数量", description = "获取用户的未读通知数量，系统消息不计算在内")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/unread-count")
    public Result<Long> getUnreadNotificationCount(@PathVariable Long userId) {
        
        log.debug("=============== Received get unread notification count request for user: {}", userId);
        
        long count = tcmNotificationService.getUnreadNotificationCount(userId);
        return Result.success("Query successful", count);
    }
    
    /**
     * 标记单个通知为已读
     * 
     * @param id 通知ID
     * @return 操作结果
     */
    @Operation(summary = "标记通知为已读", description = "标记单个通知为已读，系统消息不处理")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "404", description = "通知不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}/read")
    public Result<TcmNotification> markAsRead(@PathVariable Long id) {
        
        log.debug("=============== Received mark notification as read request: {}", id);
        
        TcmNotification notification = tcmNotificationService.markAsRead(id);
        return Result.success("Marked as read", notification);
    }
    
    /**
     * 标记所有通知为已读
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "标记所有通知为已读", description = "标记用户的所有通知为已读，系统消息不处理")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/user/{userId}/read-all")
    public Result<String> markAllAsRead(@PathVariable Long userId) {
        
        log.debug("=============== Received mark all notifications as read request for user: {}", userId);
        
        int count = tcmNotificationService.markAllAsRead(userId);
        return Result.success("Marked all notifications as read", "Successfully marked " + count + " notifications as read");
    }
}
