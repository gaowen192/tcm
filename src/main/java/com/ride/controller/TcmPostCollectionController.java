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
import com.ride.dto.TcmPostCollectionDTO;
import com.ride.entity.TcmPostCollection;
import com.ride.service.TcmPostCollectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛帖子收藏管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/post-collections")
@Validated
@Tag(name = "医药论坛帖子收藏管理", description = "医药论坛帖子收藏CRUD操作相关接口")
public class TcmPostCollectionController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmPostCollectionController.class);
    
    @Autowired
    private TcmPostCollectionService tcmPostCollectionService;
    
    /**
     * 创建医药论坛帖子收藏
     */
    @Operation(summary = "创建医药论坛帖子收藏", description = "根据帖子收藏信息创建新的医药论坛帖子收藏")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "帖子收藏创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户不存在或帖子不存在或收藏已存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmPostCollectionDTO> createPostCollection(@RequestBody TcmPostCollection postCollection) {
        log.info("=============== Received create TCM forum post collection request, user ID: {}", postCollection.getUserId());
        
        TcmPostCollectionDTO result = tcmPostCollectionService.createPostCollection(postCollection);
        return Result.success("Post collection created successfully", result);
    }
    
    /**
     * 根据ID查询医药论坛帖子收藏
     */
    @Operation(summary = "根据ID查询医药论坛帖子收藏", description = "根据帖子收藏ID查询医药论坛帖子收藏详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "帖子收藏不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmPostCollectionDTO> getPostCollectionById(@Parameter(description = "帖子收藏ID", required = true, example = "1") 
                                                             @PathVariable Long id) {
        log.debug("=============== Received query TCM forum post collection request, ID: {}", id);
        
        TcmPostCollectionDTO result = tcmPostCollectionService.getPostCollectionById(id);
        return Result.success("Query successful", result);
    }
    
    /**
     * 根据用户ID查询医药论坛帖子收藏
     */
    @Operation(summary = "根据用户ID查询医药论坛帖子收藏", description = "根据用户ID查询该用户收藏的所有帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}")
    public Result<List<TcmPostCollectionDTO>> getPostCollectionsByUserId(@Parameter(description = "用户ID", required = true, example = "1") 
                                                                        @PathVariable Long userId) {
        log.debug("=============== Received query TCM forum post collection by user request, user ID: {}", userId);
        
        List<TcmPostCollectionDTO> result = tcmPostCollectionService.getPostCollectionsByUserId(userId);
        return Result.success("Query successful", result);
    }
    
    /**
     * 根据帖子ID查询医药论坛帖子收藏
     */
    @Operation(summary = "根据帖子ID查询医药论坛帖子收藏", description = "根据帖子ID查询该帖子被收藏的信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "帖子ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}")
    public Result<List<TcmPostCollectionDTO>> getPostCollectionsByPostId(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                                        @PathVariable Long postId) {
        log.debug("=============== Received query TCM forum post collection by post request, post ID: {}", postId);
        
        List<TcmPostCollectionDTO> result = tcmPostCollectionService.getPostCollectionsByPostId(postId);
        return Result.success("Query successful", result);
    }
    
    /**
     * 查询用户对帖子的收藏
     */
    @Operation(summary = "查询用户对帖子的收藏", description = "查询指定用户对指定帖子的收藏")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/post/{postId}")
    public Result<TcmPostCollectionDTO> getPostCollectionByUserAndPost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                                      @PathVariable Long userId,
                                                                      @Parameter(description = "帖子ID", required = true, example = "1") 
                                                                      @PathVariable Long postId) {
        log.debug("=============== Received query user post collection request, user ID: {}, post ID: {}", userId, postId);
        
        TcmPostCollectionDTO result = tcmPostCollectionService.getPostCollectionByUserAndPost(userId, postId);
        return Result.success("Query successful", result);
    }
    
    /**
     * 删除医药论坛帖子收藏
     */
    @Operation(summary = "删除医药论坛帖子收藏", description = "根据帖子收藏ID删除医药论坛帖子收藏")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "帖子收藏删除成功"),
        @ApiResponse(responseCode = "404", description = "帖子收藏不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deletePostCollection(@Parameter(description = "帖子收藏ID", required = true, example = "1") 
                                                          @PathVariable Long id) {
        log.info("=============== Received delete TCM forum post collection request, ID: {}", id);
        
        tcmPostCollectionService.deletePostCollection(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Post collection deleted successfully");
        data.put("id", id);
        
        return Result.success("Deletion successful", data);
    }
    
    /**
     * 根据用户和帖子删除帖子收藏
     */
    @Operation(summary = "根据用户和帖子删除帖子收藏", description = "删除指定用户对指定帖子的收藏")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消收藏成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/user/{userId}/post/{postId}")
    public Result<Map<String, Object>> deletePostCollectionByUserAndPost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                                        @PathVariable Long userId,
                                                                        @Parameter(description = "帖子ID", required = true, example = "1") 
                                                                        @PathVariable Long postId) {
        log.info("=============== Received delete by user and post request, user ID: {}, post ID: {}", userId, postId);
        
        tcmPostCollectionService.deletePostCollectionByUserAndPost(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Unfavorite successful");
        data.put("userId", userId);
        data.put("postId", postId);
        
        return Result.success("Unfavorite successful", data);
    }
    
    /**
     * 批量删除医药论坛帖子收藏
     */
    @Operation(summary = "批量删除医药论坛帖子收藏", description = "根据ID列表批量删除医药论坛帖子收藏")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或帖子收藏不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeletePostCollections(@Parameter(description = "帖子收藏ID列表", required = true, example = "[1, 2, 3]") 
                                                                 @RequestBody List<Long> ids) {
        log.info("=============== Received batch delete TCM forum post collection request, count: {}", ids.size());
        
        tcmPostCollectionService.batchDeletePostCollections(ids);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Batch deletion of post collections successful");
        data.put("count", ids.size());
        
        return Result.success("Batch deletion successful", data);
    }
    
    /**
     * 删除帖子的所有收藏
     */
    @Operation(summary = "删除帖子的所有收藏", description = "删除指定帖子下的所有收藏")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "帖子ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/post/{postId}")
    public Result<Map<String, Object>> deletePostCollectionsByPostId(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                                     @PathVariable Long postId) {
        log.info("=============== Received delete all post collections request, post ID: {}", postId);
        
        tcmPostCollectionService.deletePostCollectionsByPostId(postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "All post collections deleted successfully");
        data.put("postId", postId);
        
        return Result.success("Deletion successful", data);
    }
    
    /**
     * 取消收藏医药论坛帖子
     */
    @Operation(summary = "取消收藏医药论坛帖子", description = "对指定的医药论坛帖子取消收藏（简化接口）")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消收藏成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或收藏记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/user/{userId}/post/{postId}/unfavorite")
    public Result<Map<String, Object>> unfavoritePost(@Parameter(description = "用户ID", required = true, example = "1") 
                                                    @PathVariable Long userId,
                                                    @Parameter(description = "帖子ID", required = true, example = "1") 
                                                    @PathVariable Long postId) {
        log.info("=============== Received unfavorite TCM forum post request, user ID: {}, post ID: {}", userId, postId);
        
        tcmPostCollectionService.deletePostCollectionByUserAndPost(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Unfavorite successful");
        data.put("userId", userId);
        data.put("postId", postId);
        
        return Result.success("Unfavorite successful", data);
    }
    
    /**
     * 检查帖子是否被用户收藏
     */
    @Operation(summary = "检查帖子是否被用户收藏", description = "检查指定帖子是否被指定用户收藏")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/user-post")
    public Result<Map<String, Object>> checkPostCollectionExists(@Parameter(description = "用户ID", required = true, example = "1") 
                                                                @RequestParam Long userId,
                                                                @Parameter(description = "帖子ID", required = true, example = "1") 
                                                                @RequestParam Long postId) {
        log.debug("=============== Received check post collection status request, user ID: {}, post ID: {}", userId, postId);
        
        boolean exists = tcmPostCollectionService.existsByUserIdAndPostId(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("postId", postId);
        data.put("exists", exists);
        data.put("message", exists ? "Post has been favorited by user" : "Post has not been favorited by user");
        
        return Result.success("Check successful", data);
    }
    
    /**
     * 获取用户收藏数量统计
     */
    @Operation(summary = "获取用户收藏数量统计", description = "获取指定用户收藏的帖子数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/count")
    public Result<Map<String, Object>> getUserCollectionCount(@Parameter(description = "用户ID", required = true, example = "1") 
                                                            @PathVariable Long userId) {
        log.debug("=============== Received get user collection count statistics request, user ID: {}", userId);
        
        long count = tcmPostCollectionService.getUserCollectionCount(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("collectionCount", count);
        data.put("message", "User collection count statistics obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
    
    /**
     * 获取帖子收藏数量统计
     */
    @Operation(summary = "获取帖子收藏数量统计", description = "获取指定帖子的收藏数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}/count")
    public Result<Map<String, Object>> getPostCollectionCount(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                            @PathVariable Long postId) {
        log.debug("=============== Received get post collection count statistics request, post ID: {}", postId);
        
        long count = tcmPostCollectionService.getPostCollectionCount(postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("postId", postId);
        data.put("collectionCount", count);
        data.put("message", "Post collection count statistics obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
    
    /**
     * 获取系统收藏总数统计
     */
    @Operation(summary = "获取系统收藏总数统计", description = "获取系统中所有帖子收藏的数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/total")
    public Result<Map<String, Object>> getTotalCollectionCount() {
        log.debug("=============== Received get system total collection count statistics request");
        
        long count = tcmPostCollectionService.getTotalCollectionCount();
        
        Map<String, Object> data = new HashMap<>();
        data.put("totalCollectionCount", count);
        data.put("message", "System total collection count statistics obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
}
