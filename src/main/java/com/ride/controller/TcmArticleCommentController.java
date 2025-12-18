package com.ride.controller;

import com.ride.dto.TcmArticleCommentDTO;
import com.ride.entity.TcmArticleComment;
import com.ride.service.TcmArticleCommentService;
import com.ride.common.Result;
import com.ride.util.JwtTokenUtil;
import org.springframework.web.bind.annotation.RequestHeader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文章评论控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/article/comments")
@Tag(name = "文章评论管理", description = "文章评论相关的CRUD操作和业务逻辑")
public class TcmArticleCommentController {
    
    private static final Logger logger = LoggerFactory.getLogger(TcmArticleCommentController.class);
    
    @Autowired
    private TcmArticleCommentService tcmArticleCommentService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Operation(summary = "创建评论", description = "创建新的文章评论")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "评论创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmArticleComment> createComment(
            @Parameter(description = "评论信息", required = true)
            @RequestBody TcmArticleComment comment,
            @Parameter(description = "Authorization header with JWT token", hidden = true)
            @RequestHeader(value = "Authorization", required = true) String authorization) {
        logger.info("===============Received request to create comment for article: {}", comment.getArticleId());
        try {
            // Extract userId from JWT token
            Long userId = extractUserIdFromAuthorization(authorization);
            comment.setUserId(userId);
            
            TcmArticleComment createdComment = tcmArticleCommentService.createComment(comment);
            return Result.success("Comment created successfully", createdComment);
        } catch (Exception e) {
            logger.error("===============Error creating comment", e);
            return Result.error("Failed to create comment: " + e.getMessage());
        }
    }
    
    /**
     * Extracts userId from Authorization header
     * @param authorization Authorization header
     * @return userId
     */
    private Long extractUserIdFromAuthorization(String authorization) {
        String jwtToken = authorization.replace("Bearer ", "");
        return jwtTokenUtil.getUserIdFromToken(jwtToken);
    }
    
    @Operation(summary = "获取评论详情", description = "根据评论ID获取详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论获取成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmArticleCommentDTO> getCommentById(
            @Parameter(description = "评论ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("===============Received request to get comment by id: {}", id);
        try {
            TcmArticleCommentDTO comment = tcmArticleCommentService.getCommentDTOById(id);
            return Result.success("Comment retrieved successfully", comment);
        } catch (Exception e) {
            logger.error("===============Error getting comment by id: {}", id, e);
            return Result.error("Failed to get comment: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新评论", description = "根据评论ID更新评论信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public Result<TcmArticleComment> updateComment(
            @Parameter(description = "评论ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "评论信息", required = true)
            @RequestBody TcmArticleComment comment) {
        logger.info("===============Received request to update comment: {}", id);
        try {
            comment.setId(id); // 确保ID一致
            TcmArticleComment updatedComment = tcmArticleCommentService.updateComment(comment);
            return Result.success("Comment updated successfully", updatedComment);
        } catch (Exception e) {
            logger.error("===============Error updating comment: {}", id, e);
            return Result.error("Failed to update comment: " + e.getMessage());
        }
    }
    
    @Operation(summary = "删除评论", description = "根据评论ID删除评论")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论删除成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(
            @Parameter(description = "评论ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("===============Received request to delete comment: {}", id);
        try {
            tcmArticleCommentService.deleteComment(id);
            return Result.success("Comment deleted successfully", null);
        } catch (Exception e) {
            logger.error("===============Error deleting comment: {}", id, e);
            return Result.error("Failed to delete comment: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取文章评论列表", description = "根据文章ID获取评论列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论列表获取成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}")
    public Result<Map<String, Object>> getCommentsByArticleId(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId,
            @Parameter(description = "状态：0-禁用，1-启用", example = "1")
            @RequestParam(defaultValue = "1") Integer status,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        logger.info("===============Received request to get comments by article id: {}, status: {}, page: {}, pageSize: {}", articleId, status, page, pageSize);
        try {
            Map<String, Object> comments = tcmArticleCommentService.getCommentsByArticleIdWithPage(articleId, status, page, pageSize);
            return Result.success("Comments retrieved successfully", comments);
        } catch (Exception e) {
            logger.error("===============Error getting comments by article id: {}", articleId, e);
            return Result.error("Failed to get comments: " + e.getMessage());
        }
    }
    
    @Operation(summary = "分页获取文章评论", description = "根据文章ID分页获取评论列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论列表获取成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}/page")
    public Result<Map<String, Object>> getCommentsByArticleIdWithPage(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId,
            @Parameter(description = "状态：0-禁用，1-启用", example = "1")
            @RequestParam(defaultValue = "1") Integer status,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        logger.info("===============Received request to get comments by article id: {}, status: {}, page: {}, pageSize: {}", 
                articleId, status, page, pageSize);
        try {
            Map<String, Object> result = tcmArticleCommentService.getCommentsByArticleIdWithPage(articleId, status, page, pageSize);
            return Result.success("Comments retrieved successfully", result);
        } catch (Exception e) {
            logger.error("===============Error getting comments by article id with page: {}", articleId, e);
            return Result.error("Failed to get comments: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新评论状态", description = "根据评论ID更新评论状态")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论状态更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}/status")
    public Result<TcmArticleComment> updateCommentStatus(
            @Parameter(description = "评论ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "状态：0-禁用，1-启用", required = true, example = "1")
            @RequestParam Integer status) {
        logger.info("===============Received request to update comment status: {}, new status: {}", id, status);
        try {
            TcmArticleComment updatedComment = tcmArticleCommentService.updateCommentStatus(id, status);
            return Result.success("Comment status updated successfully", updatedComment);
        } catch (Exception e) {
            logger.error("===============Error updating comment status: {}", id, e);
            return Result.error("Failed to update comment status: " + e.getMessage());
        }
    }
    
    @Operation(summary = "增加评论点赞数", description = "根据评论ID增加点赞数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "点赞数增加成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}/like")
    public Result<Long> incrementCommentLikeCount(
            @Parameter(description = "评论ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("===============Received request to increment comment like count: {}", id);
        try {
            Long likeCount = tcmArticleCommentService.incrementCommentLikeCount(id);
            return Result.success("Comment like count incremented successfully", likeCount);
        } catch (Exception e) {
            logger.error("===============Error incrementing comment like count: {}", id, e);
            return Result.error("Failed to increment like count: " + e.getMessage());
        }
    }
    
    @Operation(summary = "减少评论点赞数", description = "根据评论ID减少点赞数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "点赞数减少成功"),
        @ApiResponse(responseCode = "404", description = "评论不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}/unlike")
    public Result<Long> decrementCommentLikeCount(
            @Parameter(description = "评论ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("===============Received request to decrement comment like count: {}", id);
        try {
            Long likeCount = tcmArticleCommentService.decrementCommentLikeCount(id);
            return Result.success("Comment like count decremented successfully", likeCount);
        } catch (Exception e) {
            logger.error("===============Error decrementing comment like count: {}", id, e);
            return Result.error("Failed to decrement like count: " + e.getMessage());
        }
    }
    
    @Operation(summary = "统计文章评论数", description = "根据文章ID统计评论数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "评论数统计成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}/count")
    public Result<Long> countCommentsByArticleId(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId,
            @Parameter(description = "状态：0-禁用，1-启用", example = "1")
            @RequestParam(defaultValue = "1") Integer status) {
        logger.info("===============Received request to count comments by article id: {}, status: {}", articleId, status);
        try {
            Long count = tcmArticleCommentService.countCommentsByArticleId(articleId, status);
            return Result.success("Comments counted successfully", count);
        } catch (Exception e) {
            logger.error("===============Error counting comments by article id: {}", articleId, e);
            return Result.error("Failed to count comments: " + e.getMessage());
        }
    }
}