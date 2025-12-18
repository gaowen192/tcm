package com.ride.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ride.common.Result;
import com.ride.dto.TcmReplyDTO;
import com.ride.entity.TcmReply;
import com.ride.service.TcmPostService;
import com.ride.service.TcmReplyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛回复管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/replies")
@Validated
@Tag(name = "医药论坛回复管理", description = "医药论坛回复CRUD操作相关接口")
public class TcmReplyController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmReplyController.class);
    
    @Autowired
    private TcmReplyService tcmReplyService;
    
    @Autowired
    private TcmPostService tcmPostService;
    
    
   
    
    /**
     * 创建医药论坛回复
     */
    @Operation(summary = "创建医药论坛回复", description = "根据回复信息创建新的医药论坛回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "回复创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户不存在或帖子不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmReplyDTO> createReply(@RequestBody TcmReply reply) {
        log.info("=============== 接收到创建医药论坛回复请求，帖子ID：{}", reply.getPostId());
        
        TcmReplyDTO result = tcmReplyService.createReply(reply);
        
        // 增加帖子的回复数量
        tcmPostService.increaseReplyCount(reply.getPostId());
        
        return Result.success("回复创建成功", result);
    }
    
    /**
     * 根据ID查询医药论坛回复
     */
    @Operation(summary = "根据ID查询医药论坛回复", description = "根据回复ID查询医药论坛回复详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "回复不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmReplyDTO> getReplyById(@Parameter(description = "回复ID", required = true, example = "1") 
                                          @PathVariable Long id) {
        log.debug("接收到查询医药论坛回复请求，ID：{}", id);
        
        TcmReplyDTO result = tcmReplyService.getReplyById(id);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据帖子ID分页查询医药论坛回复
     */
    @Operation(summary = "根据帖子ID分页查询医药论坛回复", description = "根据帖子ID分页查询该帖子下的所有回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "帖子ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}")
    public Result<Page<TcmReplyDTO>> getRepliesByPostId(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                      @PathVariable Long postId,
                                                      @Parameter(description = "页码，默认1", example = "1") 
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @Parameter(description = "每页数量，默认10", example = "10") 
                                                      @RequestParam(defaultValue = "10") int size) {
        log.debug("=============== 接收到按帖子查询医药论坛回复请求，帖子ID：{}, 分页参数：page={}, size={}", postId, page, size);
        
        Page<TcmReplyDTO> result = tcmReplyService.getRepliesByPostIdWithPagination(postId, page, size);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据用户ID分页查询医药论坛回复
     */
    @Operation(summary = "根据用户ID分页查询医药论坛回复", description = "根据用户ID分页查询该用户发布的所有回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}")
    public Result<Page<TcmReplyDTO>> getRepliesByUserId(@Parameter(description = "用户ID", required = true, example = "1") 
                                                       @PathVariable Long userId,
                                                       @Parameter(description = "页码，默认1", example = "1") 
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @Parameter(description = "每页数量，默认10", example = "10") 
                                                       @RequestParam(defaultValue = "10") int size) {
        log.debug("=============== Received query replies by user request, user ID: {}, page: {}, size: {}", userId, page, size);
        
        Page<TcmReplyDTO> result = tcmReplyService.getRepliesByUserIdWithPagination(userId, page, size);
        return Result.success("Query successful", result);
    }
    
    /**
     * 根据状态查询医药论坛回复
     */
    @Operation(summary = "根据状态查询医药论坛回复", description = "根据回复状态查询医药论坛回复列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "状态参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/status/{status}")
    public Result<List<TcmReplyDTO>> getRepliesByStatus(@Parameter(description = "回复状态（1:正常, 0:禁用）", required = true, example = "1") 
                                                     @PathVariable Integer status) {
        log.debug("接收到按状态查询医药论坛回复请求，状态：{}", status);
        
        List<TcmReplyDTO> result = tcmReplyService.getRepliesByStatus(status);
        return Result.success("查询成功", result);
    }
    
    /**
     * 查询用户对帖子的回复
     */
    @Operation(summary = "查询用户对帖子的回复", description = "查询指定用户对指定帖子的回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/post/{postId}")
    public Result<List<TcmReplyDTO>> getRepliesByUserAndPost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                            @PathVariable Long userId,
                                                            @Parameter(description = "帖子ID", required = true, example = "1") 
                                                            @PathVariable Long postId) {
        log.debug("接收到查询用户对帖子回复请求，用户ID：{}, 帖子ID：{}", userId, postId);
        
        List<TcmReplyDTO> result = tcmReplyService.getRepliesByUserIdAndPostId(userId, postId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 更新医药论坛回复
     */
    @Operation(summary = "更新医药论坛回复", description = "根据回复ID更新医药论坛回复信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "回复更新成功"),
        @ApiResponse(responseCode = "404", description = "回复不存在"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public Result<TcmReplyDTO> updateReply(@Parameter(description = "回复ID", required = true, example = "1") 
                                         @PathVariable Long id, 
                                         @RequestBody TcmReply reply) {
        log.info("接收到更新医药论坛回复请求，ID：{}", id);
        
        TcmReplyDTO result = tcmReplyService.updateReply(id, reply);
        return Result.success("回复更新成功", result);
    }
    
    /**
     * 删除医药论坛回复
     */
    @Operation(summary = "删除医药论坛回复", description = "根据回复ID删除医药论坛回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "回复删除成功"),
        @ApiResponse(responseCode = "404", description = "回复不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deleteReply(@Parameter(description = "回复ID", required = true, example = "1") 
                                                  @PathVariable Long id) {
        log.info("接收到删除医药论坛回复请求，ID：{}", id);
        
        tcmReplyService.deleteReply(id);

        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "回复删除成功");
        data.put("id", id);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 批量删除医药论坛回复
     */
    @Operation(summary = "批量删除医药论坛回复", description = "根据ID列表批量删除医药论坛回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或回复不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeleteReplies(@Parameter(description = "回复ID列表", required = true, example = "[1, 2, 3]") 
                                                        @RequestBody List<Long> ids) {
        log.info("接收到批量删除医药论坛回复请求，数量：{}", ids.size());
        
        tcmReplyService.batchDeleteReplies(ids);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "批量删除回复成功");
        data.put("count", ids.size());
        
        return Result.success("批量删除成功", data);
    }
    
    /**
     * 删除帖子的所有回复
     */
    @Operation(summary = "删除帖子的所有回复", description = "删除指定帖子下的所有回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "帖子ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/post/{postId}")
    public Result<Map<String, Object>> deleteRepliesByPostId(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                           @PathVariable Long postId) {
        log.info("接收到删除帖子所有回复请求，帖子ID：{}", postId);
        
        tcmReplyService.deleteRepliesByPostId(postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "帖子所有回复删除成功");
        data.put("postId", postId);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 检查用户是否对帖子进行了回复
     */
    @Operation(summary = "检查用户是否对帖子进行了回复", description = "检查指定用户是否对指定帖子有回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/user-post")
    public Result<Map<String, Object>> hasUserRepliedToPost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                          @RequestParam Long userId,
                                                          @Parameter(description = "帖子ID", required = true, example = "1") 
                                                          @RequestParam Long postId) {
        log.debug("接收到检查用户回复状态请求，用户ID：{}, 帖子ID：{}", userId, postId);
        
        boolean hasReplied = tcmReplyService.hasUserRepliedToPost(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("postId", postId);
        data.put("hasReplied", hasReplied);
        data.put("message", hasReplied ? "用户已对帖子进行回复" : "用户未对帖子进行回复");
        
        return Result.success("检查成功", data);
    }
    
    /**
     * 获取帖子回复数量统计
     */
    @Operation(summary = "获取帖子回复数量统计", description = "获取指定帖子的回复数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}/count")
    public Result<Map<String, Object>> getReplyCountByPost(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                         @PathVariable Long postId) {
        log.debug("接收到获取帖子回复数量统计请求，帖子ID：{}", postId);
        
        long count = tcmReplyService.getReplyCountByPost(postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("postId", postId);
        data.put("replyCount", count);
        data.put("message", "帖子回复数量统计获取成功");
        
        return Result.success("统计成功", data);
    }
    
    /**
     * 获取用户回复数量统计
     */
    @Operation(summary = "获取用户回复数量统计", description = "获取指定用户的回复数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/count")
    public Result<Map<String, Object>> getReplyCountByUser(@Parameter(description = "用户ID", required = true, example = "1") 
                                                          @PathVariable Long userId) {
        log.debug("接收到获取用户回复数量统计请求，用户ID：{}", userId);
        
        long count = tcmReplyService.getReplyCountByUser(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("replyCount", count);
        data.put("message", "用户回复数量统计获取成功");
        
        return Result.success("统计成功", data);
    }
    
    /**
     * 获取系统回复总数统计
     */
    @Operation(summary = "获取系统回复总数统计", description = "获取系统中所有回复的数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/total")
    public Result<Map<String, Object>> getTotalReplyCount() {
        log.debug("接收到获取系统回复总数统计请求");
        
        long count = tcmReplyService.getTotalReplyCount();
        
        Map<String, Object> data = new HashMap<>();
        data.put("totalReplyCount", count);
        data.put("message", "系统回复总数统计获取成功");
        
        return Result.success("统计成功", data);
    }
}
