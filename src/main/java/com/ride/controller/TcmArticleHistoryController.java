package com.ride.controller;

import com.ride.entity.TcmArticleHistory;
import com.ride.mapper.TcmArticleHistoryRepository;
import com.ride.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 医药论坛文章修改历史控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/article-history")
@Tag(name = "文章历史管理", description = "文章修改历史相关的查询操作")
public class TcmArticleHistoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(TcmArticleHistoryController.class);
    
    @Autowired
    private TcmArticleHistoryRepository tcmArticleHistoryRepository;
    
    @Operation(summary = "获取文章所有历史记录", description = "根据文章ID获取所有修改历史记录，按版本号降序排列")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "历史记录获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}")
    public Result<List<TcmArticleHistory>> getArticleHistories(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId) {
        logger.info("===============Received request to get all article histories for article: {}", articleId);
        try {
            List<TcmArticleHistory> histories = tcmArticleHistoryRepository.findByArticleIdOrderByVersionDesc(articleId);
            return Result.success("Article histories retrieved successfully", histories);
        } catch (Exception e) {
            logger.error("===============Error getting article histories for article: {}", articleId, e);
            return Result.error("Failed to get article histories: " + e.getMessage());
        }
    }
    
    @Operation(summary = "分页获取文章历史记录", description = "根据文章ID分页获取修改历史记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "分页历史记录获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}/pagination")
    public Result<Page<TcmArticleHistory>> getArticleHistoriesWithPagination(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId,
            @Parameter(description = "页码（从1开始，默认1）", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量（默认10）", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        logger.info("===============Received request to get article histories with pagination: articleId={}, page={}, pageSize={}", 
                articleId, page, pageSize);
        try {
            // 调整页码，因为Spring Data PageRequest从0开始
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<TcmArticleHistory> historiesPage = tcmArticleHistoryRepository.findByArticleId(articleId, pageable);
            return Result.success("Article histories retrieved successfully", historiesPage);
        } catch (Exception e) {
            logger.error("===============Error getting article histories with pagination: articleId={}", articleId, e);
            return Result.error("Failed to get article histories: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取文章特定版本历史记录", description = "根据文章ID和版本号获取特定版本的历史记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "特定版本历史记录获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "404", description = "历史记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}/version/{version}")
    public Result<TcmArticleHistory> getArticleHistoryByVersion(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId,
            @Parameter(description = "版本号", required = true, example = "1")
            @PathVariable Integer version) {
        logger.info("===============Received request to get article history by version: articleId={}, version={}", articleId, version);
        try {
            TcmArticleHistory history = tcmArticleHistoryRepository.findByArticleIdAndVersion(articleId, version);
            if (history == null) {
                return Result.notFound("Article history not found for version: " + version);
            }
            return Result.success("Article history retrieved successfully", history);
        } catch (Exception e) {
            logger.error("===============Error getting article history by version: articleId={}, version={}", articleId, version, e);
            return Result.error("Failed to get article history: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取文章最新版本号", description = "根据文章ID获取最新的历史版本号")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "最新版本号获取成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}/latest-version")
    public Result<Map<String, Object>> getLatestArticleVersion(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId) {
        logger.info("===============Received request to get latest article version: articleId={}", articleId);
        try {
            Integer latestVersion = tcmArticleHistoryRepository.findLatestVersionByArticleId(articleId);
            Map<String, Object> response = new HashMap<>();
            response.put("articleId", articleId);
            response.put("latestVersion", latestVersion);
            response.put("hasHistory", latestVersion != null);
            return Result.success("Latest article version retrieved successfully", response);
        } catch (Exception e) {
            logger.error("===============Error getting latest article version: articleId={}", articleId, e);
            return Result.error("Failed to get latest article version: " + e.getMessage());
        }
    }
    
    @Operation(summary = "统计文章历史记录数量", description = "根据文章ID统计历史记录的总数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "历史记录数量统计成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/article/{articleId}/count")
    public Result<Map<String, Object>> countArticleHistories(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable Long articleId) {
        logger.info("===============Received request to count article histories: articleId={}", articleId);
        try {
            long count = tcmArticleHistoryRepository.countByArticleId(articleId);
            Map<String, Object> response = new HashMap<>();
            response.put("articleId", articleId);
            response.put("historyCount", count);
            return Result.success("Article history count retrieved successfully", response);
        } catch (Exception e) {
            logger.error("===============Error counting article histories: articleId={}", articleId, e);
            return Result.error("Failed to count article histories: " + e.getMessage());
        }
    }
}
