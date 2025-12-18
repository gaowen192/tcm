package com.ride.controller;

import com.ride.dto.TcmVideoWatchRecordDTO;
import com.ride.service.TcmVideoWatchRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ride.common.Result;

import java.util.List;
import java.util.Optional;

/**
 * 视频观看记录控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/watch-records")
@Tag(name = "视频观看记录管理", description = "视频观看记录相关的CRUD操作")
public class TcmVideoWatchRecordController {

    private static final Logger log = LoggerFactory.getLogger(TcmVideoWatchRecordController.class);

    @Autowired
    private TcmVideoWatchRecordService tcmVideoWatchRecordService;

    /**
     * 保存或更新观看记录
     */
    @Operation(summary = "保存或更新观看记录", description = "保存或更新用户的视频观看记录，相同用户和视频的记录只保留最新的一条")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "记录保存或更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmVideoWatchRecordDTO> saveOrUpdateRecord(
            @Parameter(description = "观看记录信息", required = true) @RequestBody TcmVideoWatchRecordDTO recordDTO) {
        log.debug("=============== Received save or update watch record request for user {} and video {}", 
                recordDTO.getUserId(), recordDTO.getVideoId());
        
        try {
            // 验证必要参数
            if (recordDTO.getUserId() == null || recordDTO.getVideoId() == null) {
                log.error("=============== User ID and Video ID are required");
                return Result.badRequest("用户ID和视频ID不能为空");
            }
            
            TcmVideoWatchRecordDTO savedDTO = tcmVideoWatchRecordService.saveOrUpdateRecord(recordDTO);
            return Result.success("观看记录保存成功", savedDTO);
        } catch (Exception e) {
            log.error("=============== Failed to save watch record: {}", e.getMessage());
            return Result.error("保存观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取观看记录
     */
    @Operation(summary = "根据ID获取观看记录", description = "根据ID获取详细的观看记录信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录查询成功"),
        @ApiResponse(responseCode = "404", description = "记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmVideoWatchRecordDTO> getRecordById(
            @Parameter(description = "记录ID", required = true, example = "1") @PathVariable Long id) {
        log.debug("=============== Received get watch record by ID request: {}", id);
        
        try {
            Optional<TcmVideoWatchRecordDTO> record = tcmVideoWatchRecordService.getRecordDTOById(id);
            return record.map(dto -> Result.success("查询成功", dto))
                    .orElse(Result.notFound("观看记录不存在"));
        } catch (Exception e) {
            log.error("=============== Failed to get watch record: {}", e.getMessage());
            return Result.error("查询观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID获取所有观看记录
     */
    @Operation(summary = "根据用户ID获取所有观看记录", description = "获取指定用户的所有视频观看记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录查询成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}")
    public Result<List<TcmVideoWatchRecordDTO>> getRecordsByUserId(
            @Parameter(description = "用户ID", required = true, example = "1") @PathVariable Long userId) {
        log.debug("=============== Received get watch records by user ID request: {}", userId);
        
        try {
            List<TcmVideoWatchRecordDTO> records = tcmVideoWatchRecordService.getRecordDTOsByUserId(userId);
            return Result.success("查询成功", records);
        } catch (Exception e) {
            log.error("=============== Failed to get watch records by user ID: {}", e.getMessage());
            return Result.error("查询观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据视频ID获取所有观看记录
     */
    @Operation(summary = "根据视频ID获取所有观看记录", description = "获取指定视频的所有用户观看记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录查询成功"),
        @ApiResponse(responseCode = "400", description = "视频ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/video/{videoId}")
    public Result<List<TcmVideoWatchRecordDTO>> getRecordsByVideoId(
            @Parameter(description = "视频ID", required = true, example = "1") @PathVariable Long videoId) {
        log.debug("=============== Received get watch records by video ID request: {}", videoId);
        
        try {
            List<TcmVideoWatchRecordDTO> records = tcmVideoWatchRecordService.getRecordDTOsByVideoId(videoId);
            return Result.success("查询成功", records);
        } catch (Exception e) {
            log.error("=============== Failed to get watch records by video ID: {}", e.getMessage());
            return Result.error("查询观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID和视频ID获取观看记录
     */
    @Operation(summary = "根据用户ID和视频ID获取观看记录", description = "获取指定用户对指定视频的最新观看记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录查询成功"),
        @ApiResponse(responseCode = "404", description = "记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}/video/{videoId}")
    public Result<TcmVideoWatchRecordDTO> getRecordByUserIdAndVideoId(
            @Parameter(description = "用户ID", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "视频ID", required = true, example = "1") @PathVariable Long videoId) {
        log.debug("=============== Received get watch record by user ID {} and video ID {} request", userId, videoId);
        
        try {
            Optional<TcmVideoWatchRecordDTO> record = tcmVideoWatchRecordService.getRecordDTOByUserIdAndVideoId(userId, videoId);
            return record.map(dto -> Result.success("查询成功", dto))
                    .orElse(Result.notFound("观看记录不存在"));
        } catch (Exception e) {
            log.error("=============== Failed to get watch record by user ID and video ID: {}", e.getMessage());
            return Result.error("查询观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID删除观看记录
     */
    @Operation(summary = "根据ID删除观看记录", description = "根据ID删除指定的观看记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录删除成功"),
        @ApiResponse(responseCode = "404", description = "记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<?> deleteRecordById(
            @Parameter(description = "记录ID", required = true, example = "1") @PathVariable Long id) {
        log.debug("=============== Received delete watch record by ID request: {}", id);
        
        try {
            tcmVideoWatchRecordService.deleteRecordById(id);
            return Result.success("观看记录删除成功");
        } catch (Exception e) {
            log.error("=============== Failed to delete watch record: {}", e.getMessage());
            return Result.error("删除观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID删除所有观看记录
     */
    @Operation(summary = "根据用户ID删除所有观看记录", description = "删除指定用户的所有观看记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录删除成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/user/{userId}")
    public Result<?> deleteRecordsByUserId(
            @Parameter(description = "用户ID", required = true, example = "1") @PathVariable Long userId) {
        log.debug("=============== Received delete watch records by user ID request: {}", userId);
        
        try {
            int deletedCount = tcmVideoWatchRecordService.deleteRecordsByUserId(userId);
            return Result.success("成功删除 " + deletedCount + " 条观看记录");
        } catch (Exception e) {
            log.error("=============== Failed to delete watch records by user ID: {}", e.getMessage());
            return Result.error("删除观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据视频ID删除所有观看记录
     */
    @Operation(summary = "根据视频ID删除所有观看记录", description = "删除指定视频的所有观看记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录删除成功"),
        @ApiResponse(responseCode = "400", description = "视频ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/video/{videoId}")
    public Result<?> deleteRecordsByVideoId(
            @Parameter(description = "视频ID", required = true, example = "1") @PathVariable Long videoId) {
        log.debug("=============== Received delete watch records by video ID request: {}", videoId);
        
        try {
            int deletedCount = tcmVideoWatchRecordService.deleteRecordsByVideoId(videoId);
            return Result.success("成功删除 " + deletedCount + " 条观看记录");
        } catch (Exception e) {
            log.error("=============== Failed to delete watch records by video ID: {}", e.getMessage());
            return Result.error("删除观看记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID和视频ID删除观看记录
     */
    @Operation(summary = "根据用户ID和视频ID删除观看记录", description = "删除指定用户对指定视频的观看记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "记录删除成功"),
        @ApiResponse(responseCode = "404", description = "记录不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/user/{userId}/video/{videoId}")
    public Result<?> deleteRecordByUserIdAndVideoId(
            @Parameter(description = "用户ID", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "视频ID", required = true, example = "1") @PathVariable Long videoId) {
        log.debug("=============== Received delete watch record by user ID {} and video ID {} request", userId, videoId);
        
        try {
            int deletedCount = tcmVideoWatchRecordService.deleteRecordByUserIdAndVideoId(userId, videoId);
            if (deletedCount > 0) {
                return Result.success("观看记录删除成功");
            } else {
                return Result.notFound("观看记录不存在");
            }
        } catch (Exception e) {
            log.error("=============== Failed to delete watch record by user ID and video ID: {}", e.getMessage());
            return Result.error("删除观看记录失败：" + e.getMessage());
        }
    }
}
