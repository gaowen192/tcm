package com.ride.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ride.common.Result;
import com.ride.dto.TcmPostLikeDTO;
import com.ride.entity.TcmPostLike;
import com.ride.service.TcmPostLikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛帖子点赞管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/post-likes")
@Validated
@Tag(name = "医药论坛帖子点赞管理", description = "医药论坛帖子点赞CRUD操作相关接口")
public class TcmPostLikeController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmPostLikeController.class);
    
    @Autowired
    private TcmPostLikeService tcmPostLikeService;
    
    /**
     * 创建医药论坛帖子点赞
     */
    @Operation(summary = "创建医药论坛帖子点赞", description = "根据帖子点赞信息创建新的医药论坛帖子点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "帖子点赞创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户不存在或帖子不存在或点赞已存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmPostLikeDTO> createPostLike(@RequestBody TcmPostLike postLike) {
        log.info("接收到创建医药论坛帖子点赞请求，用户ID：{}", postLike.getUserId());
        
        TcmPostLikeDTO result = tcmPostLikeService.createPostLike(postLike);
        return Result.success("帖子点赞创建成功", result);
    }
    
    /**
     * 根据ID查询医药论坛帖子点赞
     */
    @Operation(summary = "根据ID查询医药论坛帖子点赞", description = "根据帖子点赞ID查询医药论坛帖子点赞详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "帖子点赞不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmPostLikeDTO> getPostLikeById(@Parameter(description = "帖子点赞ID", required = true, example = "1") 
                                                @PathVariable Long id) {
        log.debug("接收到查询医药论坛帖子点赞请求，ID：{}", id);
        
        TcmPostLikeDTO result = tcmPostLikeService.getPostLikeById(id);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据用户ID查询医药论坛帖子点赞
     */
    @Operation(summary = "根据用户ID查询医药论坛帖子点赞", description = "根据用户ID查询该用户点赞的所有帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}")
    public Result<List<TcmPostLikeDTO>> getPostLikesByUserId(@Parameter(description = "用户ID", required = true, example = "1") 
                                                           @PathVariable Long userId) {
        log.debug("接收到按用户查询医药论坛帖子点赞请求，用户ID：{}", userId);
        
        List<TcmPostLikeDTO> result = tcmPostLikeService.getPostLikesByUserId(userId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据帖子ID查询医药论坛帖子点赞
     */
    @Operation(summary = "根据帖子ID查询医药论坛帖子点赞", description = "根据帖子ID查询该帖子被点赞的信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "帖子ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}")
    public Result<List<TcmPostLikeDTO>> getPostLikesByPostId(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                           @PathVariable Long postId) {
        log.debug("接收到按帖子查询医药论坛帖子点赞请求，帖子ID：{}", postId);
        
        List<TcmPostLikeDTO> result = tcmPostLikeService.getPostLikesByPostId(postId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 查询用户对帖子的点赞
     */
    @Operation(summary = "查询用户对帖子的点赞", description = "查询指定用户对指定帖子的点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/post/{postId}")
    public Result<TcmPostLikeDTO> getPostLikeByUserAndPost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                          @PathVariable Long userId,
                                                          @Parameter(description = "帖子ID", required = true, example = "1") 
                                                          @PathVariable Long postId) {
        log.debug("接收到查询用户对帖子点赞请求，用户ID：{}, 帖子ID：{}", userId, postId);
        
        TcmPostLikeDTO result = tcmPostLikeService.getPostLikeByUserAndPost(userId, postId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 删除医药论坛帖子点赞
     */
    @Operation(summary = "删除医药论坛帖子点赞", description = "根据帖子点赞ID删除医药论坛帖子点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "帖子点赞删除成功"),
        @ApiResponse(responseCode = "404", description = "帖子点赞不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deletePostLike(@Parameter(description = "帖子点赞ID", required = true, example = "1") 
                                                    @PathVariable Long id) {
        log.info("接收到删除医药论坛帖子点赞请求，ID：{}", id);
        
        tcmPostLikeService.deletePostLike(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "帖子点赞删除成功");
        data.put("id", id);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 根据用户和帖子删除帖子点赞
     */
    @Operation(summary = "根据用户和帖子删除帖子点赞", description = "删除指定用户对指定帖子的点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消点赞成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/user/{userId}/post/{postId}")
    public Result<Map<String, Object>> deletePostLikeByUserAndPost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                                  @PathVariable Long userId,
                                                                  @Parameter(description = "帖子ID", required = true, example = "1") 
                                                                  @PathVariable Long postId) {
        log.info("接收到根据用户和帖子删除请求，用户ID：{}, 帖子ID：{}", userId, postId);
        
        tcmPostLikeService.deletePostLikeByUserAndPost(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "取消点赞成功");
        data.put("userId", userId);
        data.put("postId", postId);
        
        return Result.success("取消点赞成功", data);
    }
    
    /**
     * 批量删除医药论坛帖子点赞
     */
    @Operation(summary = "批量删除医药论坛帖子点赞", description = "根据ID列表批量删除医药论坛帖子点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或帖子点赞不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeletePostLikes(@Parameter(description = "帖子点赞ID列表", required = true, example = "[1, 2, 3]") 
                                                           @RequestBody List<Long> ids) {
        log.info("接收到批量删除医药论坛帖子点赞请求，数量：{}", ids.size());
        
        tcmPostLikeService.batchDeletePostLikes(ids);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "批量删除帖子点赞成功");
        data.put("count", ids.size());
        
        return Result.success("批量删除成功", data);
    }
    
    /**
     * 删除帖子的所有点赞
     */
    @Operation(summary = "删除帖子的所有点赞", description = "删除指定帖子下的所有点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "帖子ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/post/{postId}")
    public Result<Map<String, Object>> deletePostLikesByPostId(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                             @PathVariable Long postId) {
        log.info("接收到删除帖子所有点赞请求，帖子ID：{}", postId);
        
        tcmPostLikeService.deletePostLikesByPostId(postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "帖子所有点赞删除成功");
        data.put("postId", postId);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 取消点赞医药论坛帖子
     */
    @Operation(summary = "取消点赞医药论坛帖子", description = "对指定的医药论坛帖子取消点赞（简化接口）")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消点赞成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或点赞记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/user/{userId}/post/{postId}/unlike")
    public Result<Map<String, Object>> unlikePost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                @PathVariable Long userId,
                                                @Parameter(description = "帖子ID", required = true, example = "1") 
                                                @PathVariable Long postId) {
        log.info("接收到取消点赞医药论坛帖子请求，用户ID：{}, 帖子ID：{}", userId, postId);
        
        tcmPostLikeService.deletePostLikeByUserAndPost(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "取消点赞成功");
        data.put("userId", userId);
        data.put("postId", postId);
        
        return Result.success("取消点赞成功", data);
    }
    
    /**
     * 检查帖子是否被用户点赞
     */
    @Operation(summary = "检查帖子是否被用户点赞", description = "检查指定帖子是否被指定用户点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/user-post")
    public Result<Map<String, Object>> checkPostLikeExists(@Parameter(description = "用户ID", required = true, example = "1") 
                                                         @RequestParam Long userId,
                                                         @Parameter(description = "帖子ID", required = true, example = "1") 
                                                         @RequestParam Long postId) {
        log.debug("接收到检查帖子点赞状态请求，用户ID：{}, 帖子ID：{}", userId, postId);
        
        boolean exists = tcmPostLikeService.existsByUserIdAndPostId(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("postId", postId);
        data.put("exists", exists);
        data.put("message", exists ? "帖子已被用户点赞" : "帖子未被用户点赞");
        
        return Result.success("检查成功", data);
    }
    
    /**
     * 获取用户点赞数量统计
     */
    @Operation(summary = "获取用户点赞数量统计", description = "获取指定用户点赞的帖子数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/count")
    public Result<Map<String, Object>> getUserLikeCount(@Parameter(description = "用户ID", required = true, example = "1") 
                                                      @PathVariable Long userId) {
        log.debug("接收到获取用户点赞数量统计请求，用户ID：{}", userId);
        
        long count = tcmPostLikeService.getUserLikeCount(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("likeCount", count);
        data.put("message", "用户点赞数量统计获取成功");
        
        return Result.success("统计成功", data);
    }
    
    /**
     * 获取帖子点赞数量统计
     */
    @Operation(summary = "获取帖子点赞数量统计", description = "获取指定帖子的点赞数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}/count")
    public Result<Map<String, Object>> getPostLikeCount(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                       @PathVariable Long postId) {
        log.debug("接收到获取帖子点赞数量统计请求，帖子ID：{}", postId);
        
        long count = tcmPostLikeService.getPostLikeCount(postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("postId", postId);
        data.put("likeCount", count);
        data.put("message", "帖子点赞数量统计获取成功");
        
        return Result.success("统计成功", data);
    }
    
    /**
     * 获取系统点赞总数统计
     */
    @Operation(summary = "获取系统点赞总数统计", description = "获取系统中所有帖子点赞的数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/total")
    public Result<Map<String, Object>> getTotalLikeCount() {
        log.debug("接收到获取系统点赞总数统计请求");
        
        long count = tcmPostLikeService.getTotalLikeCount();
        
        Map<String, Object> data = new HashMap<>();
        data.put("totalLikeCount", count);
        data.put("message", "系统点赞总数统计获取成功");
        
        return Result.success("统计成功", data);
    }
}
