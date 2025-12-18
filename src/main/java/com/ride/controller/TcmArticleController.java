package com.ride.controller;

import com.ride.dto.TcmArticleDTO;
import com.ride.entity.TcmArticle;
import com.ride.service.TcmArticleService;
import com.ride.service.TcmArticleInteractionService;
import com.ride.common.Result;
import com.ride.util.JwtTokenUtil;
import com.ride.mapper.TcmArticleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 医药论坛文章控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/articles")
@Tag(name = "文章管理", description = "文章相关的CRUD操作和业务逻辑")
public class TcmArticleController {
    
    private static final Logger logger = LoggerFactory.getLogger(TcmArticleController.class);
    
    @Autowired
    private TcmArticleService tcmArticleService;
    
    @Autowired
    private TcmArticleInteractionService tcmArticleInteractionService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private TcmArticleRepository tcmArticleRepository;
    
    @Operation(summary = "创建文章", description = "创建新的文章记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "文章创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmArticle> createArticle(
            @Parameter(description = "文章信息", required = true)
            @RequestBody TcmArticleDTO articleDTO) {
        logger.info("===============Received request to create article: {}", articleDTO.getTitle());
        try {
            TcmArticle article = tcmArticleService.createArticle(articleDTO);
            return Result.success("Article created successfully", article);
        } catch (Exception e) {
            logger.error("===============Error creating article", e);
            return Result.error("Failed to create article: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取文章详情", description = "根据文章ID获取详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "文章获取成功"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmArticle> getArticleById(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        logger.info("===============Received request to get article by id: {}", id);
        try {
            TcmArticle article = tcmArticleService.getArticleById(id);
            // 增加阅读次数
            tcmArticleService.incrementViewCount(id);
            
            // 如果用户已登录，记录观看记录
            Long userId = extractUserIdFromAuthorization(authorization);
            if (userId != null) {
                logger.info("===============Recording view for user: {} on article: {}", userId, id);
                tcmArticleInteractionService.recordView(id, userId);
            }
            
            return Result.success("Article retrieved successfully", article);
        } catch (Exception e) {
            logger.error("===============Error getting article by id: {}", id, e);
            return Result.error("Failed to get article: " + e.getMessage());
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
    
    @Operation(summary = "更新文章", description = "根据文章ID更新文章信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "文章更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public Result<TcmArticle> updateArticle(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "文章信息", required = true)
            @RequestBody TcmArticleDTO articleDTO) {
        logger.info("===============Received request to update article: {}", id);
        try {
            TcmArticle article = tcmArticleService.updateArticle(id, articleDTO);
            return Result.success("Article updated successfully", article);
        } catch (Exception e) {
            logger.error("===============Error updating article: {}", id, e);
            return Result.error("Failed to update article: " + e.getMessage());
        }
    }
    
    @Operation(summary = "删除文章", description = "根据文章ID删除文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "文章删除成功"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteArticle(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("===============Received request to delete article: {}", id);
        try {
            boolean result = tcmArticleService.deleteArticle(id);
            if (result) {
                return Result.success("Article deleted successfully", true);
            } else {
                return Result.error("Article not found");
            }
        } catch (Exception e) {
            logger.error("===============Error deleting article: {}", id, e);
            return Result.error("Failed to delete article: " + e.getMessage());
        }
    }
    
    @Operation(summary = "根据用户ID获取文章列表", description = "分页获取指定用户发布的所有文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}")
    public Result<Page<TcmArticle>> getArticlesByUserId(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "页码（从1开始，默认1）", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量（默认10）", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        logger.info("===============Received request to get articles by user id with pagination: userId={}, page={}, pageSize={}", 
                userId, page, pageSize);
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<TcmArticle> articlePage = tcmArticleRepository.findByUserId(userId, pageable);
            return Result.success("Articles retrieved successfully", articlePage);
        } catch (Exception e) {
            logger.error("===============Error getting articles by user id with pagination: userId={}", userId, e);
            return Result.error("Failed to get articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "根据用户ID获取文章列表（分页）", description = "分页获取指定用户发布的所有文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "分页文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/pagination")
    public Result<Map<String, Object>> getArticlesByUserIdWithPagination(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "页码（从1开始，默认1）", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量（默认10）", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        
        logger.info("===============Received request to get articles by user id with pagination: userId={}, page={}, pageSize={}", 
                userId, page, pageSize);
        
        try {
            Map<String, Object> result = tcmArticleService.getArticlesByUserIdWithPagination(userId, page, pageSize);
            return Result.success("Pagination articles retrieved successfully", result);
        } catch (Exception e) {
            logger.error("===============Error getting articles by user id with pagination: {}", userId, e);
            return Result.error("Failed to get pagination articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "根据板块ID获取文章列表", description = "获取指定板块下的所有文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "板块ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/category/{categoryId}")
    public Result<List<TcmArticle>> getArticlesByCategoryId(
            @Parameter(description = "板块ID", required = true, example = "1")
            @PathVariable Long categoryId) {
        logger.info("===============Received request to get articles by category id: {}", categoryId);
        try {
            List<TcmArticle> articles = tcmArticleService.getArticlesByCategoryId(categoryId);
            return Result.success("Articles retrieved successfully", articles);
        } catch (Exception e) {
            logger.error("===============Error getting articles by category id: {}", categoryId, e);
            return Result.error("Failed to get articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "根据板块ID获取文章列表（分页）", description = "分页获取指定板块下的所有文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "分页文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/category/{categoryId}/pagination")
    public Result<Map<String, Object>> getArticlesByCategoryIdWithPagination(
            @Parameter(description = "板块ID", required = true, example = "1")
            @PathVariable Long categoryId,
            @Parameter(description = "页码（从1开始，默认1）")
            @PageableDefault(page = 1, size = 10) Pageable pageable) {
        
        logger.info("===============Received request to get articles by category id with pagination: categoryId={}, page={}, pageSize={}", 
                categoryId, pageable.getPageNumber() + 1, pageable.getPageSize());
        
        try {
            Map<String, Object> result = tcmArticleService.getArticlesByCategoryIdWithPagination(categoryId, pageable);
            return Result.success("Pagination articles retrieved successfully", result);
        } catch (Exception e) {
            logger.error("===============Error getting articles by category id with pagination: {}", categoryId, e);
            return Result.error("Failed to get pagination articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "根据状态获取文章列表", description = "获取指定状态的所有文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "状态参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/status/{status}")
    public Result<List<TcmArticle>> getArticlesByStatus(
            @Parameter(description = "文章状态，1-启用，0-禁用，2-审核中", required = true, example = "1")
            @PathVariable Integer status) {
        logger.info("===============Received request to get articles by status: {}", status);
        try {
            List<TcmArticle> articles = tcmArticleService.getArticlesByStatus(status);
            return Result.success("Articles retrieved successfully", articles);
        } catch (Exception e) {
            logger.error("===============Error getting articles by status: {}", status, e);
            return Result.error("Failed to get articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "根据状态获取文章列表（分页）", description = "分页获取指定状态的所有文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "分页文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/status/{status}/pagination")
    public Result<Map<String, Object>> getArticlesByStatusWithPagination(
            @Parameter(description = "文章状态，1-启用，0-禁用，2-审核中", required = true, example = "1")
            @PathVariable Integer status,
            @Parameter(description = "页码（从1开始，默认1）", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量（默认10）", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        
        logger.info("===============Received request to get articles by status with pagination: status={}, page={}, pageSize={}", 
                status, page, pageSize);
        
        try {
            Map<String, Object> result = tcmArticleService.getArticlesByStatusWithPagination(status, page, pageSize);
            return Result.success("Pagination articles retrieved successfully", result);
        } catch (Exception e) {
            logger.error("===============Error getting articles by status with pagination: {}", status, e);
            return Result.error("Failed to get pagination articles: " + e.getMessage());
        }
    }
    
    /**
     * 获取文章列表（支持分页和搜索）
     * 
     * @param page 当前页码
     * @param pageSize 每页数量
     * @param title 标题关键字（可选）
     * @return 文章列表
     */
    @Operation(summary = "获取文章列表（支持分页和搜索）", description = "获取所有文章的分页列表，支持按标题搜索")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "获取成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/list")
    public Result getArticlesList(
            @Parameter(description = "当前页码", required = true, example = "1") @RequestParam int page,
            @Parameter(description = "每页数量", required = true, example = "10") @RequestParam int pageSize,
            @Parameter(description = "标题关键字", required = false, example = "中医") @RequestParam(required = false) String title) {
        try {
            Map<String, Object> result = tcmArticleService.searchArticles(title, page, pageSize);
            return Result.success("获取文章列表成功", result);
        } catch (Exception e) {
            logger.error("===============Error getting articles list: {}", e.getMessage());
            return Result.error("获取文章列表失败");
        }
    }
    
    @Operation(summary = "获取热门文章列表", description = "获取浏览量最多的热门文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "热门文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/hot")
    public Result<List<TcmArticle>> getHotArticles(
            @Parameter(description = "返回数量限制（默认10）", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("===============Received request to get hot articles with limit: {}", limit);
        try {
            List<TcmArticle> articles = tcmArticleService.getHotArticles(limit);
            return Result.success("Hot articles retrieved successfully", articles);
        } catch (Exception e) {
            logger.error("===============Error getting hot articles", e);
            return Result.error("Failed to get hot articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取推荐文章列表", description = "获取推荐的文章列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "推荐文章列表获取成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/recommended")
    public Result<List<TcmArticle>> getRecommendedArticles() {
        logger.info("===============Received request to get recommended articles");
        try {
            List<TcmArticle> articles = tcmArticleService.getRecommendedArticles();
            return Result.success("Recommended articles retrieved successfully", articles);
        } catch (Exception e) {
            logger.error("===============Error getting recommended articles", e);
            return Result.error("Failed to get recommended articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取最新文章列表", description = "获取最新发布的文章")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "最新文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/latest")
    public Result<List<TcmArticle>> getLatestArticles(
            @Parameter(description = "返回数量限制（默认10）", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("===============Received request to get latest articles with limit: {}", limit);
        try {
            List<TcmArticle> articles = tcmArticleService.getLatestArticles(limit);
            return Result.success("Latest articles retrieved successfully", articles);
        } catch (Exception e) {
            logger.error("===============Error getting latest articles", e);
            return Result.error("Failed to get latest articles: " + e.getMessage());
        }
    }
    
    @Operation(summary = "增加文章点赞次数", description = "增加指定文章的点赞次数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "点赞成功"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/{id}/like")
    public Result<Boolean> incrementLikeCount(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        logger.info("===============Received request to increment like count for article: {}", id);
        try {
            // 获取当前登录用户ID
            Long userId = extractUserIdFromAuthorization(authorization);
            if (userId == null) {
                // 如果用户未登录，只增加文章点赞数
                boolean result = tcmArticleService.incrementLikeCount(id);
                if (result) {
                    return Result.success("Like count incremented successfully", true);
                } else {
                    return Result.error("Failed to increment like count");
                }
            } else {
                // 如果用户已登录，同时增加文章点赞数和保存点赞记录
                boolean result = tcmArticleService.incrementLikeCount(id);
                if (result) {
                    // 保存点赞记录到tcm_article_interaction表
                    tcmArticleInteractionService.toggleLike(id, userId, true);
                    logger.info("===============Saved like record for user: {} on article: {}", userId, id);
                    return Result.success("Like count incremented successfully and like record saved", true);
                } else {
                    return Result.error("Failed to increment like count");
                }
            }
        } catch (Exception e) {
            logger.error("===============Error incrementing like count for article: {}", id, e);
            return Result.error("Failed to increment like count: " + e.getMessage());
        }
    }
    
    @Operation(summary = "减少文章点赞次数", description = "减少指定文章的点赞次数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消点赞成功"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}/like")
    public Result<Boolean> decrementLikeCount(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("===============Received request to decrement like count for article: {}", id);
        try {
            boolean result = tcmArticleService.decrementLikeCount(id);
            if (result) {
                return Result.success("Like count decremented successfully", true);
            } else {
                return Result.error("Failed to decrement like count");
            }
        } catch (Exception e) {
            logger.error("===============Error decrementing like count for article: {}", id, e);
            return Result.error("Failed to decrement like count: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新文章热门状态", description = "更新指定文章的热门状态")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "热门状态更新成功"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}/hot")
    public Result<Boolean> updateHotStatus(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "是否热门，true-热门，false-非热门", required = true, example = "true")
            @RequestParam Boolean isHot) {
        logger.info("===============Received request to update hot status for article: {}, isHot: {}", id, isHot);
        try {
            boolean result = tcmArticleService.updateHotStatus(id, isHot);
            if (result) {
                return Result.success("Hot status updated successfully", true);
            } else {
                return Result.error("Failed to update hot status");
            }
        } catch (Exception e) {
            logger.error("===============Error updating hot status for article: {}", id, e);
            return Result.error("Failed to update hot status: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新文章推荐状态", description = "更新指定文章的推荐状态")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "推荐状态更新成功"),
        @ApiResponse(responseCode = "404", description = "文章不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}/recommended")
    public Result<Boolean> updateRecommendedStatus(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "是否推荐，true-推荐，false-非推荐", required = true, example = "true")
            @RequestParam Boolean isRecommended) {
        logger.info("===============Received request to update recommended status for article: {}, isRecommended: {}", id, isRecommended);
        try {
            boolean result = tcmArticleService.updateRecommendedStatus(id, isRecommended);
            if (result) {
                return Result.success("Recommended status updated successfully", true);
            } else {
                return Result.error("Failed to update recommended status");
            }
        } catch (Exception e) {
            logger.error("===============Error updating recommended status for article: {}", id, e);
            return Result.error("Failed to update recommended status: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取带用户名的文章列表", description = "获取指定板块下带用户名的文章列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "带用户名的文章列表获取成功"),
        @ApiResponse(responseCode = "400", description = "板块ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/with-username/category/{categoryId}")
    public Result<List<Map<String, Object>>> getArticlesWithUserNameByCategoryId(
            @Parameter(description = "板块ID", required = true, example = "1")
            @PathVariable Long categoryId) {
        logger.info("===============Received request to get articles with user name by category id: {}", categoryId);
        try {
            List<Map<String, Object>> articles = tcmArticleService.getArticlesWithUserNameByCategoryId(categoryId);
            return Result.success("Articles with user name retrieved successfully", articles);
        } catch (Exception e) {
            logger.error("===============Error getting articles with user name by category id: {}", categoryId, e);
            return Result.error("Failed to get articles with user name: " + e.getMessage());
        }
    }
}