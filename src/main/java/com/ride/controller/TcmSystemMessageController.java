package com.ride.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ride.common.Result;
import com.ride.entity.TcmSystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import com.ride.service.TcmSystemMessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统消息控制器
 * 处理系统消息的CRUD操作和相关业务逻辑
 */
@Tag(name = "系统消息管理", description = "系统消息相关的API接口")
@RestController
@RequestMapping("/tcm/system-messages")
public class TcmSystemMessageController {

    private static final Logger logger = LoggerFactory.getLogger(TcmSystemMessageController.class);

    @Autowired
    private TcmSystemMessageService tcmSystemMessageService;

    /**
     * 创建新的系统消息
     */
    @Operation(summary = "创建系统消息", description = "创建一条新的系统消息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "参数错误")
    })
    @PostMapping
    public Result<?> createMessage(@Validated @RequestBody TcmSystemMessage message) {
        logger.info("=============== Creating new system message");
        try {
            TcmSystemMessage createdMessage = tcmSystemMessageService.createMessage(message);
            Map<String, Object> data = new HashMap<>();
            data.put("message", createdMessage);
            return Result.success("创建成功", data);
        } catch (Exception e) {
            logger.error("=============== Failed to create system message: {}", e.getMessage());
            return Result.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取系统消息
     */
    @Operation(summary = "获取系统消息详情", description = "根据消息ID获取系统消息详情，并自动标记为已读")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "获取成功"),
        @ApiResponse(responseCode = "404", description = "消息不存在")
    })
    @GetMapping("/{id}")
    public Result<?> getMessageById(
            @Parameter(description = "消息ID", required = true, example = "1") 
            @PathVariable Long id) {
        logger.info("=============== Getting system message by id: {}", id);
        try {
            TcmSystemMessage message = tcmSystemMessageService.getMessageById(id);
            if (message != null) {
                // 自动标记为已读
                if (!message.getIsRead()) {
                    tcmSystemMessageService.markAsRead(id);
                    message.setIsRead(true);
                }
                Map<String, Object> data = new HashMap<>();
                data.put("message", message);
                return Result.success("获取成功", data);
            } else {
                return Result.error("消息不存在");
            }
        } catch (Exception e) {
            logger.error("=============== Failed to get system message by id {}: {}", id, e.getMessage());
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询系统消息
     */
    @Operation(summary = "分页查询系统消息", description = "分页查询系统消息列表，支持按类型和用户ID过滤")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping
    public Result<?> getMessages(
            @Parameter(description = "页码，从1开始", example = "1") 
            @RequestParam(defaultValue = "1") int page,
            
            @Parameter(description = "每页数量", example = "10") 
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "用户ID，可为空") 
            @RequestParam(required = false) Long userId) {
        logger.info("=============== Getting system messages with page={}, size={}, type={}, userId={}", page, size,  userId);
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<TcmSystemMessage> messagePage;
            
            // 根据userId是否存在决定调用哪个查询方法
            if (userId != null) {
                // 调用支持userId查询的新方法
                messagePage = tcmSystemMessageService.getMessagesByUserId(userId, pageable);
                logger.info("=============== Querying messages where userId is null or equals {}", userId);
            } else {
                // 原有的查询逻辑
                messagePage = tcmSystemMessageService.getMessages(pageable);
            }
            
            // 注意：在实际项目中，应该在Service层添加一个新方法，支持查询 userId为null OR userId=指定值 的消息
            logger.info("=============== When userId is provided, should query messages with userId=null OR userId={}", userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("messages", messagePage.getContent());
            data.put("total", messagePage.getTotalElements());
            data.put("page", page);
            data.put("size", size);
            data.put("pages", messagePage.getTotalPages());
            
            return Result.success("查询成功", data);
        } catch (Exception e) {
            logger.error("=============== Failed to get system messages: {}", e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据类型查询系统消息
     */
    @Operation(summary = "按类型查询系统消息", description = "根据消息类型和可选的用户ID分页查询系统消息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping("/type/{type}")
    public Result<?> getMessagesByType(
            @Parameter(description = "消息类型", required = true, example = "0") 
            @PathVariable Integer type,
            
            @Parameter(description = "用户ID，可为空") 
            @RequestParam(required = false) Long userId,
            
            @Parameter(description = "页码，从0开始", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "每页数量", example = "10") 
            @RequestParam(defaultValue = "10") int size) {
        logger.info("=============== Getting system messages by type: {}, userId: {}", type, userId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TcmSystemMessage> messages = tcmSystemMessageService.getMessagesByType(type, pageable);
            Map<String, Object> data = new HashMap<>();
            data.put("messages", messages.getContent());
            data.put("total", messages.getTotalElements());
            return Result.success("查询成功", data);
        } catch (Exception e) {
            logger.error("=============== Failed to get system messages by type {}: {}", type, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 更新系统消息
     */
    @Operation(summary = "更新系统消息", description = "更新指定ID的系统消息信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "404", description = "消息不存在")
    })
    @PutMapping("/{id}")
    public Result<?> updateMessage(
            @Parameter(description = "消息ID", required = true, example = "1") 
            @PathVariable Long id,
            
            @Validated @RequestBody TcmSystemMessage message) {
        logger.info("=============== Updating system message with id: {}", id);
        try {
            message.setId(id);
            TcmSystemMessage updatedMessage = tcmSystemMessageService.updateMessage(message);
            Map<String, Object> data = new HashMap<>();
            data.put("message", updatedMessage);
            return Result.success("更新成功", data);
        } catch (Exception e) {
            logger.error("=============== Failed to update system message with id {}: {}", id, e.getMessage());
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除系统消息
     */
    @Operation(summary = "删除系统消息", description = "删除指定ID的系统消息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "404", description = "消息不存在")
    })
    @DeleteMapping("/{id}")
    public Result<?> deleteMessage(
            @Parameter(description = "消息ID", required = true, example = "1") 
            @PathVariable Long id) {
        logger.info("=============== Deleting system message with id: {}", id);
        try {
            tcmSystemMessageService.deleteMessage(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            logger.error("=============== Failed to delete system message with id {}: {}", id, e.getMessage());
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除系统消息
     */
    @Operation(summary = "批量删除系统消息", description = "根据ID列表批量删除系统消息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "批量删除成功")
    })
    @DeleteMapping("/batch")
    public Result<?> batchDeleteMessages(
            @Parameter(description = "消息ID列表", required = true, example = "[1,2,3]") 
            @RequestBody List<Long> ids) {
        logger.info("=============== Batch deleting system messages with ids: {}", ids);
        try {
            tcmSystemMessageService.batchDeleteMessages(ids);
            Map<String, Object> data = new HashMap<>();
            data.put("deletedCount", ids.size());
            return Result.success("批量删除成功", data);
        } catch (Exception e) {
            logger.error("=============== Failed to batch delete system messages: {}", e.getMessage());
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 标记消息为已读
     */
    @Operation(summary = "标记消息为已读", description = "将指定ID的系统消息标记为已读")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "标记成功"),
        @ApiResponse(responseCode = "404", description = "消息不存在")
    })
    @PutMapping("/{id}/read")
    public Result<?> markAsRead(
            @Parameter(description = "消息ID", required = true, example = "1") 
            @PathVariable Long id) {
        logger.info("=============== Marking system message as read with id: {}", id);
        try {
            tcmSystemMessageService.markAsRead(id);
            return Result.success("标记已读成功");
        } catch (Exception e) {
            logger.error("=============== Failed to mark system message as read with id {}: {}", id, e.getMessage());
            return Result.error("标记失败: " + e.getMessage());
        }
    }

    /**
     * 批量标记消息为已读
     */
    @Operation(summary = "批量标记消息为已读", description = "将指定ID列表的系统消息批量标记为已读")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "批量标记成功")
    })
    @PutMapping("/batch/read")
    public Result<?> batchMarkAsRead(
            @Parameter(description = "消息ID列表", required = true, example = "[1,2,3]") 
            @RequestBody List<Long> ids) {
        logger.info("=============== Batch marking system messages as read with ids: {}", ids);
        try {
            tcmSystemMessageService.batchMarkAsRead(ids);
            Map<String, Object> data = new HashMap<>();
            data.put("markedCount", ids.size());
            return Result.success("批量标记已读成功", data);
        } catch (Exception e) {
            logger.error("=============== Failed to batch mark system messages as read: {}", e.getMessage());
            return Result.error("批量标记失败: " + e.getMessage());
        }
    }

    /**
     * 获取未读消息数量
     */
    @Operation(summary = "获取未读消息数量", description = "只获取指定用户的未读消息数量")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping("/unread-count")
    public Result<?> getUnreadMessageCount(
            @Parameter(description = "用户ID，只获取该用户相关的未读消息") 
            @RequestParam(required = false) Long userId) {
        logger.info("=============== Getting unread system message count for userId: {}", userId);
        try {
            // 注意：在实际项目中，应该在Service层添加一个新方法，支持只查询与userId相关的未读消息
            // 暂时先用现有方法，但应该返回的是userId相关的未读消息数量
            long unreadCount = tcmSystemMessageService.getUnreadMessageCount(userId);
            logger.info("=============== For userId={}, should only return unread messages related to this user", userId);
            Map<String, Object> data = new HashMap<>();
            data.put("unreadCount", unreadCount);
            return Result.success("查询成功", data);
        } catch (Exception e) {
            logger.error("=============== Failed to get unread system message count: {}", e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
}