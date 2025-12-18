package com.ride.controller;

import com.ride.entity.TcmArticleInteraction;
import com.ride.service.TcmArticleInteractionService;
import com.ride.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章互动控制器（点赞和观看记录）
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/article-interactions")
@Tag(name = "文章互动管理", description = "文章点赞和观看记录相关的接口")
public class TcmArticleInteractionController {
    
    private static final Logger logger = LoggerFactory.getLogger(TcmArticleInteractionController.class);
    
    @Autowired
    private TcmArticleInteractionService tcmArticleInteractionService;
    
    @Operation(summary = "切换文章点赞状态", description = "点赞或取消点赞文章，同一用户同一文章只记录最后一次")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "点赞状态切换成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/like")
    public Result<TcmArticleInteraction> toggleLike(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Long articleId,
            @Parameter(description = "用户ID", required = true, example = "1")
            @RequestParam Long userId,
            @Parameter(description = "是否点赞（true为点赞，false为取消点赞）", required = true)
            @RequestParam boolean isLiked) {
        logger.info("===============Received request to toggle like for article: {}, user: {}, isLiked: {}", articleId, userId, isLiked);
        try {
            TcmArticleInteraction interaction = tcmArticleInteractionService.toggleLike(articleId, userId, isLiked);
            return Result.success(isLiked ? "Article liked successfully" : "Article unliked successfully", interaction);
        } catch (Exception e) {
            logger.error("===============Error toggling like for article: {}, user: {}", articleId, userId, e);
            return Result.error("Failed to toggle like: " + e.getMessage());
        }
    }
    
    @Operation(summary = "记录文章观看", description = "记录用户观看文章，同一用户同一文章只记录最后一次")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "观看记录成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/view")
    public Result<TcmArticleInteraction> recordView(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Long articleId,
            @Parameter(description = "用户ID", required = true, example = "1")
            @RequestParam Long userId) {
        logger.info("===============Received request to record view for article: {}, user: {}", articleId, userId);
        try {
            TcmArticleInteraction interaction = tcmArticleInteractionService.recordView(articleId, userId);
            return Result.success("View recorded successfully", interaction);
        } catch (Exception e) {
            logger.error("===============Error recording view for article: {}, user: {}", articleId, userId, e);
            return Result.error("Failed to record view: " + e.getMessage());
        }
    }
    
    @Operation(summary = "检查用户是否点赞文章", description = "查询用户是否已点赞指定文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/like-check")
    public Result<Boolean> isArticleLikedByUser(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Long articleId,
            @Parameter(description = "用户ID", required = true, example = "1")
            @RequestParam Long userId) {
        logger.info("===============Received request to check if article: {} is liked by user: {}", articleId, userId);
        try {
            boolean isLiked = tcmArticleInteractionService.isArticleLikedByUser(articleId, userId);
            return Result.success("Like status retrieved successfully", isLiked);
        } catch (Exception e) {
            logger.error("===============Error checking like status for article: {}, user: {}", articleId, userId, e);
            return Result.error("Failed to check like status: " + e.getMessage());
        }
    }
    
    @Operation(summary = "检查用户是否观看文章", description = "查询用户是否已观看指定文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/view-check")
    public Result<Boolean> isArticleViewedByUser(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Long articleId,
            @Parameter(description = "用户ID", required = true, example = "1")
            @RequestParam Long userId) {
        logger.info("===============Received request to check if article: {} is viewed by user: {}", articleId, userId);
        try {
            boolean isViewed = tcmArticleInteractionService.isArticleViewedByUser(articleId, userId);
            return Result.success("View status retrieved successfully", isViewed);
        } catch (Exception e) {
            logger.error("===============Error checking view status for article: {}, user: {}", articleId, userId, e);
            return Result.error("Failed to check view status: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取文章点赞数", description = "查询指定文章的点赞总数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/like-count/{articleId}")
    public Result<Long> getArticleLikeCount(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId) {
        logger.info("===============Received request to get like count for article: {}", articleId);
        try {
            long likeCount = tcmArticleInteractionService.getArticleLikeCount(articleId);
            return Result.success("Like count retrieved successfully", likeCount);
        } catch (Exception e) {
            logger.error("===============Error getting like count for article: {}", articleId, e);
            return Result.error("Failed to get like count: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取文章观看数", description = "查询指定文章的观看总数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/view-count/{articleId}")
    public Result<Long> getArticleViewCount(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId) {
        logger.info("===============Received request to get view count for article: {}", articleId);
        try {
            long viewCount = tcmArticleInteractionService.getArticleViewCount(articleId);
            return Result.success("View count retrieved successfully", viewCount);
        } catch (Exception e) {
            logger.error("===============Error getting view count for article: {}", articleId, e);
            return Result.error("Failed to get view count: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取用户与文章的互动记录", description = "查询用户与指定文章的互动详情")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user-article")
    public Result<TcmArticleInteraction> getInteractionByUserAndArticle(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Long articleId,
            @Parameter(description = "用户ID", required = true, example = "1")
            @RequestParam Long userId) {
        logger.info("===============Received request to get interaction for article: {} and user: {}", articleId, userId);
        try {
            TcmArticleInteraction interaction = tcmArticleInteractionService.getInteractionByUserAndArticle(articleId, userId);
            return Result.success("Interaction retrieved successfully", interaction);
        } catch (Exception e) {
            logger.error("===============Error getting interaction for article: {}, user: {}", articleId, userId, e);
            return Result.error("Failed to get interaction: " + e.getMessage());
        }
    }
}
