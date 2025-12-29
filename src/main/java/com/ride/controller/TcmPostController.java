package com.ride.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ride.common.Result;
import com.ride.dto.TcmPostDTO;
import com.ride.entity.TcmPost;
import com.ride.service.TcmPostInteractionService;
import com.ride.service.TcmPostService;
import com.ride.util.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛帖子管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/posts")
@Validated
@Tag(name = "医药论坛帖子管理", description = "医药论坛帖子CRUD操作相关接口")
public class TcmPostController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmPostController.class);
    
    @Autowired
    private TcmPostService tcmPostService;
    
    @Autowired
    private TcmPostInteractionService tcmPostInteractionService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    /**
     * 创建医药论坛帖子
     */
    @Operation(summary = "创建医药论坛帖子", description = "根据帖子信息创建新的医药论坛帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "帖子创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户不存在或板块不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmPostDTO> createPost(@RequestBody TcmPost post) {
        log.info("=============== Received create TCM forum post request: {}", post.getTitle());
        
        TcmPostDTO result = tcmPostService.createPost(post);
        return Result.success("Post creation successful", result);
    }
    
    /**
     * 根据ID查询医药论坛帖子
     */
    @Operation(summary = "根据ID查询医药论坛帖子", description = "根据帖子ID查询医药论坛帖子详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "帖子不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmPostDTO> getPostById(@Parameter(description = "帖子ID", required = true, example = "1") 
                                        @PathVariable Long id,
                                        @RequestHeader(value = "Authorization", required = false) String authorization) {
        log.debug("=============== Received query TCM forum post request, ID: {}", id);
        
        TcmPostDTO result = tcmPostService.getPostById(id);
        
        // Increase view count for the post
        tcmPostService.incrementViewCount(id);
        
        // Record view interaction to tcm_post_interaction table if userId is available
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String jwtToken = authorization.substring(7); // 去除Bearer前缀
                Long userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
                if (userId != null) {
                    tcmPostInteractionService.recordPostInteraction(userId, id, "WATCH");
                    log.debug("=============== Recorded post watch for user: {}, post: {}", userId, id);
                }
            } catch (Exception e) {
                log.warn("=============== Failed to extract userId from JWT: {}", e.getMessage());
                // 不影响正常查询，仅记录警告日志
            }
        }
        
        return Result.success("Query successful", result);
    }
    
    /** 
      * 根据用户ID查询医药论坛帖子 
      */ 
     @Operation(summary = "根据用户ID查询医药论坛帖子", description = "根据用户ID查询该用户发布的所有帖子") 
     @ApiResponses(value = { 
         @ApiResponse(responseCode = "200", description = "查询成功"), 
         @ApiResponse(responseCode = "400", description = "用户ID无效"), 
         @ApiResponse(responseCode = "500", description = "服务器内部错误") 
     }) 
     @GetMapping("/user/{userId}") 
     public Result<Page<TcmPostDTO>> getPostsByUserId(@Parameter(description = "用户ID", required = true, example = "1") 
                                                    @PathVariable Long userId, 
                                                    @Parameter(description = "页码，从1开始", required = false, example = "1") 
                                                    @RequestParam(defaultValue = "1") Integer page, 
                                                    @Parameter(description = "每页数量", required = false, example = "10") 
                                                    @RequestParam(defaultValue = "10") Integer pageSize) { 
         log.debug("=============== Received query TCM forum posts by user request, user ID: {}, page: {}, pageSize: {}", userId, page, pageSize); 
         
         Pageable pageable = PageRequest.of(page - 1, pageSize);
         Page<TcmPostDTO> result = tcmPostService.getPostsByUserId(userId, pageable); 
         return Result.success("Query successful", result); 
     }
    
    /**
     * 根据板块ID查询医药论坛帖子
     */
    @Operation(summary = "根据板块ID查询医药论坛帖子", description = "根据板块ID查询该板块下的所有帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "板块ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/category/{categoryId}")
    public Result<List<TcmPostDTO>> getPostsByCategoryId(@Parameter(description = "板块ID", required = true, example = "1") 
                                                       @PathVariable Long categoryId) {
        log.debug("=============== Received query TCM forum posts by category request, category ID: {}", categoryId);
        
        List<TcmPostDTO> result = tcmPostService.getPostsByCategoryId(categoryId);
        return Result.success("Query successful", result);
    }
    
    /**
     * 根据状态查询医药论坛帖子
     */
    @Operation(summary = "根据状态查询医药论坛帖子", description = "根据帖子状态查询医药论坛帖子列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "状态参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/status/{status}")
    public Result<List<TcmPostDTO>> getPostsByStatus(@Parameter(description = "帖子状态（1:正常, 0:禁用）", required = true, example = "1") 
                                                    @PathVariable Integer status) {
        log.debug("=============== Received query TCM forum posts by status request, status: {}", status);
        
        List<TcmPostDTO> result = tcmPostService.getPostsByStatus(status);
        return Result.success("Query successful", result);
    }
    
    /**
     * 查询热门医药论坛帖子
     */
    @Operation(summary = "查询热门医药论坛帖子", description = "查询系统中所有热门帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/hot")
    public Result<List<TcmPostDTO>> getHotPosts() {
        log.debug("=============== Received query hot TCM forum posts request");
        
        List<TcmPostDTO> result = tcmPostService.getHotPosts(1, 5);
        return Result.success("Query successful", result);
    }
    
    /**
     * 查询最新医药论坛帖子
     */
    @Operation(summary = "查询最新医药论坛帖子", description = "查询系统中最新发布的帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/latest")
    public Result<List<TcmPostDTO>> getLatestPosts() {
        log.debug("=============== Received query latest TCM forum posts request");
        
        List<TcmPostDTO> result = tcmPostService.getLatestPosts(1, 10);
        return Result.success("Query successful", result);
    }
    
    /**
     * 搜索医药论坛帖子
     */
    @Operation(summary = "搜索医药论坛帖子", description = "根据关键词搜索医药论坛帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "搜索成功"),
        @ApiResponse(responseCode = "400", description = "搜索关键词不能为空"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/search")
    public Result<Page<TcmPostDTO>> searchPosts(@Parameter(description = "搜索关键词", required = true, example = "中医") 
                                               @RequestParam String keyword,
                                               Pageable pageable) {
        log.debug("=============== Received search TCM forum posts request, keyword: {}", keyword);
        
        Page<TcmPostDTO> result = tcmPostService.searchPosts(keyword, pageable);
        return Result.success("Search successful", result);
    }
    
    /**
     * 更新医药论坛帖子
     */
    @Operation(summary = "更新医药论坛帖子", description = "根据帖子ID更新医药论坛帖子信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "帖子更新成功"),
        @ApiResponse(responseCode = "404", description = "帖子不存在"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public Result<TcmPostDTO> updatePost(@Parameter(description = "帖子ID", required = true, example = "1") 
                                        @PathVariable Long id, 
                                        @RequestBody TcmPost post) {
        log.info("=============== Received update TCM forum post request, ID: {}", id);
        
        TcmPostDTO result = tcmPostService.updatePost(id, post);
        return Result.success("Post update successful", result);
    }
    
    /**
     * 删除医药论坛帖子
     */
    @Operation(summary = "删除医药论坛帖子", description = "根据帖子ID删除医药论坛帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "帖子删除成功"),
        @ApiResponse(responseCode = "404", description = "帖子不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deletePost(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                 @PathVariable Long id) {
        log.info("=============== Received delete TCM forum post request, ID: {}", id);
        
        tcmPostService.deletePost(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Post deletion successful");
        data.put("id", id);
        
        return Result.success("Deletion successful", data);
    }
    
    /**
     * 批量删除医药论坛帖子
     */
    @Operation(summary = "批量删除医药论坛帖子", description = "根据ID列表批量删除医药论坛帖子")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或帖子不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeletePosts(@Parameter(description = "帖子ID列表", required = true, example = "[1, 2, 3]") 
                                                       @RequestBody List<Long> ids) {
        log.info("=============== Received batch delete TCM forum posts request, count: {}", ids.size());
        
        tcmPostService.batchDeletePosts(ids);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Batch deletion of posts successful");
        data.put("count", ids.size());
        
        return Result.success("Batch deletion successful", data);
    }
    
    /**
     * 点赞医药论坛帖子
     */
    @Operation(summary = "点赞医药论坛帖子", description = "对指定的医药论坛帖子进行点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "点赞成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户不存在或帖子不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/{id}/like")
    public Result<Map<String, Object>> likePost(@Parameter(description = "帖子ID", required = true, example = "1") 
                                               @PathVariable Long id,
                                               @Parameter(description = "用户ID", required = true, example = "1") 
                                               @RequestParam Long userId) {
        log.info("=============== Received like TCM forum post request, post ID: {}, user ID: {}", id, userId);
        
        tcmPostService.likePost(id, userId);
        
        // 记录点赞互动到tcm_post_interaction表
        tcmPostInteractionService.recordPostInteraction(userId, id, "LIKE");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Like successful");
        data.put("postId", id);
        data.put("userId", userId);
        
        return Result.success("Like successful", data);
    }
    
    /**
     * 取消点赞医药论坛帖子
     */
    @Operation(summary = "取消点赞医药论坛帖子", description = "对指定的医药论坛帖子取消点赞")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消点赞成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或点赞记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}/like")
    public Result<Map<String, Object>> unlikePost(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                 @PathVariable Long id,
                                                 @Parameter(description = "用户ID", required = true, example = "1") 
                                                 @RequestParam Long userId) {
        log.info("=============== Received unlike TCM forum post request, post ID: {}, user ID: {}", id, userId);
        
        tcmPostService.unlikePost(id, userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Unlike successful");
        data.put("postId", id);
        data.put("userId", userId);
        
        return Result.success("Unlike successful", data);
    }
    
    /**
     * 增加帖子浏览量
     */
    @Operation(summary = "增加帖子浏览量", description = "增加指定医药论坛帖子的浏览量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "浏览量增加成功"),
        @ApiResponse(responseCode = "404", description = "帖子不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/{id}/view")
    public Result<Map<String, Object>> incrementViewCount(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                        @PathVariable Long id) {
        log.debug("=============== Received increment post view count request, post ID: {}", id);
        
        long viewCount = tcmPostService.incrementViewCount(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "View count increment successful");
        data.put("postId", id);
        data.put("viewCount", viewCount);
        
        return Result.success("View count increment successful", data);
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
    @GetMapping("/{id}/is-liked")
    public Result<Map<String, Object>> isPostLikedByUser(@Parameter(description = "帖子ID", required = true, example = "1") 
                                                        @PathVariable Long id,
                                                        @Parameter(description = "用户ID", required = true, example = "1") 
                                                        @RequestParam Long userId) {
        log.debug("=============== Received check post like status request, post ID: {}, user ID: {}", id, userId);
        
        boolean isLiked = tcmPostService.isPostLikedByUser(id, userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("postId", id);
        data.put("userId", userId);
        data.put("isLiked", isLiked);
        data.put("message", isLiked ? "Post has been liked by user" : "Post has not been liked by user");
        
        return Result.success("Check successful", data);
    }
    
    /**
     * 获取用户帖子数量统计
     */
    @Operation(summary = "获取用户帖子数量统计", description = "获取指定用户发布的帖子数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/count")
    public Result<Map<String, Object>> getPostCountByUser(@Parameter(description = "用户ID", required = true, example = "1") 
                                                        @PathVariable Long userId) {
        log.debug("=============== Received get user post count statistics request, user ID: {}", userId);
        
        long count = tcmPostService.getPostCountByUser(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("postCount", count);
        data.put("message", "User post count statistics obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
    
    /**
     * 获取板块帖子数量统计
     */
    @Operation(summary = "获取板块帖子数量统计", description = "获取指定板块下的帖子数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/category/{categoryId}/count")
    public Result<Map<String, Object>> getPostCountByCategory(@Parameter(description = "板块ID", required = true, example = "1") 
                                                            @PathVariable Long categoryId) {
        log.debug("=============== Received get category post count statistics request, category ID: {}", categoryId);
        
        long count = tcmPostService.getPostCountByCategory(categoryId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("categoryId", categoryId);
        data.put("postCount", count);
        data.put("message", "Category post count statistics obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
    
    /**
     * 获取系统帖子总数统计
     */
    @Operation(summary = "获取系统帖子总数统计", description = "获取系统中所有帖子的数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/total")
    public Result<Map<String, Object>> getTotalPostCount() {
        log.debug("=============== Received get system total post count statistics request");
        
        long count = tcmPostService.getTotalPostCount();
        
        Map<String, Object> data = new HashMap<>();
        data.put("totalPostCount", count);
        data.put("message", "System total post count statistics obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
    
    /**
      * 分页查询所有帖子
      */
     @Operation(summary = "分页查询所有帖子", description = "分页获取系统中所有帖子，支持分页参数设置、搜索和高级筛选条件")
     @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "查询成功"),
         @ApiResponse(responseCode = "400", description = "分页参数无效"),
         @ApiResponse(responseCode = "500", description = "服务器内部错误")
     })
     @GetMapping("/all")
     public Result<Page<TcmPostDTO>> getAllPostsWithPagination(
             @Parameter(description = "页码（从1开始）", required = false, example = "1")
             @RequestParam(defaultValue = "1") Integer page,
             @Parameter(description = "每页数量", required = false, example = "10")
             @RequestParam(defaultValue = "10") Integer pageSize,
             @Parameter(description = "热门帖子筛选（不为空则查询浏览量>10的帖子）", required = false)
             @RequestParam(required = false) String hotpost,
             @Parameter(description = "最新排序（不为空则按创建时间降序排序）", required = false)
             @RequestParam(required = false) String isNew,
             @Parameter(description = "搜索关键词（用于搜索帖子标题或内容）", required = false)
             @RequestParam(required = false) String keyword) {
         log.debug("=============== Received get all posts with pagination request, page: {}, pageSize: {}, hotpost: {}, isNew: {}, keyword: {}",
                 page, pageSize, hotpost, isNew, keyword);
         
         // 创建分页参数，页码从0开始（page-1），但返回给前端的仍然是从1开始的页码
         Pageable pageable = PageRequest.of(page - 1, pageSize);
         
         // 获取分页数据，传入筛选条件
         Page<TcmPostDTO> postsPage = tcmPostService.getAllPosts(pageable, hotpost, isNew, keyword);
         
         return Result.success("Query successful", postsPage);
     }
}