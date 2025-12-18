package com.ride.controller;

import com.ride.entity.TcmVideoComment;
import com.ride.service.TcmVideoCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.ride.common.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频评论控制器
 * 提供视频评论相关的REST API接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm")
@Tag(name = "视频评论管理", description = "视频评论相关的CRUD操作和业务逻辑")
public class TcmVideoCommentController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmVideoCommentController.class);
    
    @Autowired
    private TcmVideoCommentService tcmVideoCommentService;
    
    /**
     * 创建视频评论
     */
    @Operation(summary = "创建视频评论", description = "创建新的视频评论，支持回复其他评论")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "评论创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/video-comments")
    public Result<TcmVideoComment> createComment(@RequestBody TcmVideoComment comment) {
        log.debug("=============== Received comment creation request: videoId={}, userId={}", comment.getVideoId(), comment.getUserId());
        try {
            // 验证必要参数
            if (comment.getVideoId() == null || comment.getUserId() == null || comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                log.error("=============== Invalid comment parameters: videoId or userId or content is missing");
                return Result.badRequest("视频ID、用户ID和评论内容不能为空");
            }
            
            TcmVideoComment createdComment = tcmVideoCommentService.createComment(comment);
            log.info("=============== Comment created successfully: id={}", createdComment.getId());
            return Result.success("评论创建成功", createdComment);
        } catch (Exception e) {
            log.error("=============== Comment creation failed: {}", e.getMessage());
            return Result.error("评论创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取评论详情
     */
    @Operation(summary = "获取评论详情", description = "根据评论ID获取评论详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论查询成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/video-comments/{id}")
    public Result<TcmVideoComment> getCommentById(@PathVariable Long id) {
        log.debug("=============== Received comment query request: id={}", id);
        try {
            TcmVideoComment comment = tcmVideoCommentService.getCommentById(id);
            if (comment == null) {
                return Result.notFound("评论不存在");
            }
            return Result.success("评论查询成功", comment);
        } catch (Exception e) {
            log.error("=============== Comment query failed: {}", e.getMessage());
            return Result.error("评论查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新评论内容
     */
    @Operation(summary = "更新评论内容", description = "更新指定评论的内容")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/video-comments/{id}")
    public Result<TcmVideoComment> updateComment(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        log.debug("=============== Received comment update request: id={}", id);
        try {
            String content = requestBody.get("content");
            if (content == null || content.trim().isEmpty()) {
                return Result.badRequest("评论内容不能为空");
            }
            
            TcmVideoComment updatedComment = tcmVideoCommentService.updateComment(id, content);
            if (updatedComment == null) {
                return Result.notFound("评论不存在");
            }
            
            log.info("=============== Comment updated successfully: id={}", id);
            return Result.success("评论更新成功", updatedComment);
        } catch (Exception e) {
            log.error("=============== Comment update failed: {}", e.getMessage());
            return Result.error("评论更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除评论
     */
    @Operation(summary = "删除评论", description = "删除指定的评论")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "评论删除成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/video-comments/{id}")
    public Result<?> deleteComment(@PathVariable Long id) {
        log.debug("=============== Received comment deletion request: id={}", id);
        try {
            // 检查评论是否存在
            TcmVideoComment comment = tcmVideoCommentService.getCommentById(id);
            if (comment == null) {
                return Result.notFound("评论不存在");
            }
            
            tcmVideoCommentService.deleteComment(id);
            log.info("=============== Comment deleted successfully: id={}", id);
            return Result.success("评论删除成功");
        } catch (Exception e) {
            log.error("=============== Comment deletion failed: {}", e.getMessage());
            return Result.error("评论删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据视频ID获取评论列表（分页）
     */
    @Operation(summary = "获取视频评论列表", description = "分页获取指定视频的评论列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论列表查询成功"),
        @ApiResponse(responseCode = "400", description = "视频ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/{videoId}/comments")
    public Result<Map<String, Object>> getCommentsByVideoId(
            @PathVariable Long videoId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.debug("=============== Received video comments query request: videoId={}, page={}, pageSize={}", videoId, page, pageSize);
        try {
            // 将page减1，因为Spring Data PageRequest从0开始
            Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<TcmVideoComment> commentsPage = tcmVideoCommentService.getCommentsByVideoId(videoId, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", commentsPage.getContent());
            response.put("totalElements", commentsPage.getTotalElements());
            response.put("totalPages", commentsPage.getTotalPages());
            response.put("page", page); // 保持返回给前端的page是从1开始的
            response.put("pageSize", pageSize);
            
            return Result.success("评论列表查询成功", response);
        } catch (Exception e) {
            log.error("=============== Video comments query failed: {}", e.getMessage());
            return Result.error("评论列表查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据用户ID获取评论列表（分页）
     */
    @Operation(summary = "获取用户评论列表", description = "分页获取指定用户发表的评论列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户评论列表查询成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/users/{userId}/comments")
    public Result<Map<String, Object>> getCommentsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.debug("=============== Received user comments query request: userId={}, page={}, pageSize={}", userId, page, pageSize);
        try {
            // 将page减1，因为Spring Data PageRequest从0开始
            Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<TcmVideoComment> commentsPage = tcmVideoCommentService.getCommentsByUserId(userId, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", commentsPage.getContent());
            response.put("totalElements", commentsPage.getTotalElements());
            response.put("totalPages", commentsPage.getTotalPages());
            response.put("page", page); // 保持返回给前端的page是从1开始的
            response.put("pageSize", pageSize);
            
            return Result.success("用户评论列表查询成功", response);
        } catch (Exception e) {
            log.error("=============== User comments query failed: {}", e.getMessage());
            return Result.error("用户评论列表查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取评论的回复列表
     */
    @Operation(summary = "获取评论回复列表", description = "获取指定评论的所有回复")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "回复列表查询成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/video-comments/{commentId}/replies")
    public Result<List<TcmVideoComment>> getRepliesByCommentId(@PathVariable Long commentId) {
        log.debug("=============== Received comment replies query request: commentId={}", commentId);
        try {
            List<TcmVideoComment> replies = tcmVideoCommentService.getRepliesByCommentId(commentId);
            return Result.success("回复列表查询成功", replies);
        } catch (Exception e) {
            log.error("=============== Comment replies query failed: {}", e.getMessage());
            return Result.error("回复列表查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 评论点赞
     */
    @Operation(summary = "评论点赞", description = "增加评论的点赞次数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "点赞成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/video-comments/{id}/like")
    public Result<Map<String, String>> likeComment(@PathVariable Long id) {
        log.debug("=============== Received comment like request: id={}", id);
        try {
            // 检查评论是否存在
            TcmVideoComment comment = tcmVideoCommentService.getCommentById(id);
            if (comment == null) {
                return Result.notFound("评论不存在");
            }
            
            tcmVideoCommentService.likeComment(id);
            log.info("=============== Comment liked successfully: id={}", id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "点赞成功");
            return Result.success(response);
        } catch (Exception e) {
            log.error("=============== Comment like failed: {}", e.getMessage());
            return Result.error("点赞失败：" + e.getMessage());
        }
    }
    
    /**
     * 取消评论点赞
     */
    @Operation(summary = "取消评论点赞", description = "减少评论的点赞次数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消点赞成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/video-comments/{id}/like")
    public Result<Map<String, String>> unlikeComment(@PathVariable Long id) {
        log.debug("=============== Received comment unlike request: id={}", id);
        try {
            // 检查评论是否存在
            TcmVideoComment comment = tcmVideoCommentService.getCommentById(id);
            if (comment == null) {
                return Result.notFound("评论不存在");
            }
            
            tcmVideoCommentService.unlikeComment(id);
            log.info("=============== Comment unliked successfully: id={}", id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "取消点赞成功");
            return Result.success(response);
        } catch (Exception e) {
            log.error("=============== Comment unlike failed: {}", e.getMessage());
            return Result.error("取消点赞失败：" + e.getMessage());
        }
    }
    
    /**
     * 统计视频评论数量
     */
    @Operation(summary = "统计视频评论数量", description = "获取指定视频的评论总数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论数量统计成功"),
        @ApiResponse(responseCode = "400", description = "视频ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/{videoId}/comments/count")
    public Result<Map<String, Long>> countCommentsByVideoId(@PathVariable Long videoId) {
        log.debug("=============== Received comment count request: videoId={}", videoId);
        try {
            long count = tcmVideoCommentService.countCommentsByVideoId(videoId);
            
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            
            return Result.success("评论数量统计成功", response);
        } catch (Exception e) {
            log.error("=============== Comment count failed: {}", e.getMessage());
            return Result.error("评论数量统计失败：" + e.getMessage());
        }
    }
    
    /**
     * 验证评论是否属于指定用户
     */
    @Operation(summary = "验证评论归属", description = "验证评论是否属于指定用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "验证成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/video-comments/{commentId}/verify-owner")
    public Result<Map<String, Boolean>> verifyCommentOwner(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        log.debug("=============== Received comment ownership verification request: commentId={}, userId={}", commentId, userId);
        try {
            boolean isOwned = tcmVideoCommentService.isCommentOwnedByUser(commentId, userId);
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("isOwned", isOwned);
            
            return Result.success("验证成功", response);
        } catch (Exception e) {
            log.error("=============== Comment ownership verification failed: {}", e.getMessage());
            return Result.error("验证失败：" + e.getMessage());
        }
    }
}