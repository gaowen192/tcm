package com.ride.controller;

import com.ride.entity.TcmPost;
import com.ride.entity.TcmPostHistory;
import com.ride.service.TcmPostHistoryService;
import com.ride.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RestController
@RequestMapping("/tcm/post-histories")
@Tag(name = "帖子修改历史管理", description = "帖子修改历史的增删改查操作")
public class TcmPostHistoryController {

    private static final Logger log = LoggerFactory.getLogger(TcmPostHistoryController.class);

    @Autowired
    private TcmPostHistoryService tcmPostHistoryService;

    /**
     * 获取帖子的所有修改历史
     */
    @Operation(summary = "获取帖子的所有修改历史", description = "根据帖子ID获取所有修改历史记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}")
    public Result<List<TcmPostHistory>> getPostHistories(@Parameter(description = "帖子ID", required = true, example = "1")
                                                         @PathVariable Long postId) {
        log.info("=============== Received get post histories request, post ID: {}", postId);
        List<TcmPostHistory> histories = tcmPostHistoryService.getPostHistoriesByPostId(postId);
        return Result.success("获取帖子修改历史成功", histories);
    }

    /**
     * 分页获取帖子的修改历史
     */
    @Operation(summary = "分页获取帖子的修改历史", description = "根据帖子ID分页获取修改历史记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}/page")
    public Result<Page<TcmPostHistory>> getPostHistoriesPage(@Parameter(description = "帖子ID", required = true, example = "1")
                                                              @PathVariable Long postId,
                                                              @Parameter(description = "页码，从1开始", required = true, example = "1")
                                                              @RequestParam(defaultValue = "1") Integer page,
                                                              @Parameter(description = "每页数量", required = true, example = "10")
                                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("=============== Received get post histories page request, post ID: {}, page: {}, size: {}", postId, page, size);
        // 页码从1开始转换为从0开始
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TcmPostHistory> historiesPage = tcmPostHistoryService.getPostHistoriesByPostId(postId, pageable);
        return Result.success("分页获取帖子修改历史成功", historiesPage);
    }

    /**
     * 根据版本号获取帖子的修改历史
     */
    @Operation(summary = "根据版本号获取帖子的修改历史", description = "根据帖子ID和版本号获取特定版本的修改历史记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "404", description = "历史记录不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}/version/{version}")
    public Result<TcmPostHistory> getPostHistoryByVersion(@Parameter(description = "帖子ID", required = true, example = "1")
                                                          @PathVariable Long postId,
                                                          @Parameter(description = "版本号", required = true, example = "1")
                                                          @PathVariable Integer version) {
        log.info("=============== Received get post history by version request, post ID: {}, version: {}", postId, version);
        TcmPostHistory history = tcmPostHistoryService.getPostHistoryByPostIdAndVersion(postId, version);
        return Result.success("获取特定版本帖子修改历史成功", history);
    }

    /**
     * 获取帖子的最新修改历史
     */
    @Operation(summary = "获取帖子的最新修改历史", description = "根据帖子ID获取最新版本的修改历史记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "404", description = "历史记录不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/post/{postId}/latest")
    public Result<TcmPostHistory> getLatestPostHistory(@Parameter(description = "帖子ID", required = true, example = "1")
                                                      @PathVariable Long postId) {
        log.info("=============== Received get latest post history request, post ID: {}", postId);
        TcmPostHistory history = tcmPostHistoryService.getLatestPostHistory(postId);
        return Result.success("获取最新版本帖子修改历史成功", history);
    }

    /**
     * 恢复帖子到指定版本
     */
    @Operation(summary = "恢复帖子到指定版本", description = "根据帖子ID和版本号将帖子恢复到指定版本")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "恢复成功"),
            @ApiResponse(responseCode = "404", description = "历史记录不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/post/{postId}/restore/{version}")
    public Result<TcmPost> restorePost(@Parameter(description = "帖子ID", required = true, example = "1")
                                       @PathVariable Long postId,
                                       @Parameter(description = "版本号", required = true, example = "1")
                                       @PathVariable Integer version) {
        log.info("=============== Received restore post request, post ID: {}, version: {}", postId, version);
        TcmPost restoredPost = tcmPostHistoryService.restorePostFromHistory(postId, version);
        return Result.success("帖子恢复成功", restoredPost);
    }

    /**
     * 删除帖子的所有修改历史
     */
    @Operation(summary = "删除帖子的所有修改历史", description = "根据帖子ID删除所有修改历史记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/post/{postId}")
    public Result<Void> deletePostHistories(@Parameter(description = "帖子ID", required = true, example = "1")
                                            @PathVariable Long postId) {
        log.info("=============== Received delete post histories request, post ID: {}", postId);
        tcmPostHistoryService.deletePostHistoriesByPostId(postId);
        return Result.success("删除帖子修改历史成功");
    }
}