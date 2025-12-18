package com.ride.controller;

import com.ride.entity.TcmVideo;
import com.ride.entity.TcmVideoWatchRecord;
import com.ride.service.TcmVideoService;
import com.ride.service.TcmVideoWatchRecordService;
import com.ride.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ride.common.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;

/**
 * 医药论坛视频控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm")
@Tag(name = "视频管理", description = "视频相关的CRUD操作和业务逻辑")
public class TcmVideoController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmVideoController.class);
    
    @Autowired
    private TcmVideoService tcmVideoService;
    
    @Autowired
    private TcmVideoWatchRecordService tcmVideoWatchRecordService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    /**
     * 视频文件上传
     */
    @Operation(summary = "单个视频文件上传", description = "一次只能上传一个视频文件并创建视频记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "视频上传成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效或文件格式不支持"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/videos/upload")
    public Result<TcmVideo> uploadVideo(
            @Parameter(description = "视频文件", required = true)
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "视频标题", required = true, example = "中医养生基础教程")
            @RequestParam String title,
            @Parameter(description = "视频描述", example = "本视频介绍中医养生的基础知识")
            @RequestParam(required = false) String description,
            @Parameter(description = "用户ID", required = true, example = "1")
            @RequestParam Long userId,
            @Parameter(description = "板块ID", required = true, example = "1")
            @RequestParam Long categoryId,
            @Parameter(description = "视频标签", example = "中医,养生")
            @RequestParam(required = false) String tags,
            @Parameter(description = "视频封面图片", example = "封面图片文件")
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {
        log.debug("=============== Received single video upload request, file name: {}, title: {}", file.getOriginalFilename(), title);
        try {
            // 验证只上传了一个视频文件
            if (file == null || file.isEmpty()) {
                log.error("=============== Video file is required");
                return Result.badRequest("视频文件不能为空");
            }
            
            TcmVideo video = new TcmVideo();
            video.setTitle(title);
            video.setDescription(description);
            video.setUserId(userId);
            video.setCategoryId(categoryId);
            video.setTags(tags);
            
            TcmVideo uploadedVideo = tcmVideoService.uploadVideo(video, file, thumbnail);
            return Result.success("视频上传成功", uploadedVideo);
        } catch (IOException e) {
            log.error("=============== Single video upload failed: {}", e.getMessage());
            return Result.error("视频上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建视频记录(仅创建记录，不上传文件)
     */
    @Operation(summary = "创建视频记录", description = "仅创建视频记录，不包含文件上传")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "视频记录创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/videos/create")
    public Result<TcmVideo> createVideo(@RequestBody TcmVideo video) {
        log.debug("=============== Received create video request, title: {}", video.getTitle());
        try {
            TcmVideo createdVideo = tcmVideoService.saveVideo(video);
            return Result.success("视频记录创建成功", createdVideo);
        } catch (Exception e) {
            log.error("=============== Create video failed: {}", e.getMessage());
            return Result.error("视频记录创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取视频
     */
    @Operation(summary = "根据ID获取视频", description = "根据视频ID获取详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频查询成功"),
        @ApiResponse(responseCode = "404", description = "视频不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/{id}")
    public Result<TcmVideo> getVideoById(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.debug("=============== Received get video request, id: {}", id);
        try {
            TcmVideo video = tcmVideoService.getVideoById(id);
            if (video == null) {
                return Result.notFound("视频不存在");
            }
            // 增加观看次数
            tcmVideoService.incrementViewCount(id);
            return Result.success("视频查询成功", video);
        } catch (Exception e) {
            log.error("=============== Get video failed: {}", e.getMessage());
            return Result.error("视频查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新视频
     */
    @Operation(summary = "更新视频", description = "更新视频信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "404", description = "视频不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/videos/{id}")
    public Result<TcmVideo> updateVideo(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long id,
            @RequestBody TcmVideo video) {
        log.debug("=============== Received update video request, id: {}", id);
        try {
            video.setId(id);
            TcmVideo updatedVideo = tcmVideoService.updateVideo(video);
            if (updatedVideo == null) {
                return Result.notFound("视频不存在");
            }
            return Result.success("视频更新成功", updatedVideo);
        } catch (Exception e) {
            log.error("=============== Update video failed: {}", e.getMessage());
            return Result.error("视频更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除视频
     */
    @Operation(summary = "删除视频", description = "根据ID删除视频")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "视频删除成功"),
        @ApiResponse(responseCode = "404", description = "视频不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/videos/{id}")
    public Result<?> deleteVideo(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.debug("=============== Received delete video request, id: {}", id);
        try {
            tcmVideoService.deleteVideo(id);
            return Result.success("视频删除成功");
        } catch (Exception e) {
            log.error("=============== Delete video failed: {}", e.getMessage());
            return Result.error("视频删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有视频（支持分页）
     */
    @Operation(summary = "获取所有视频", description = "分页获取视频列表，包含视频封面信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频列表查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos")
    public Result<Page<TcmVideo>> getAllVideos(
            @Parameter(description = "页码（从1开始，默认1）", example = "1") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量（默认10）", example = "10") 
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "状态筛选（可选），1-启用，0-禁用，2-审核中", example = "1") 
            @RequestParam(required = false) Integer status) {
        log.debug("=============== Received get all videos request with pagination - page: {}, pageSize: {}, status: {}", page, pageSize, status);
        try {
            // 默认查询启用状态的视频
            if (status == null) {
                status = 1;
            }
            
            Page<TcmVideo> result = tcmVideoService.getVideosWithPagination(page, pageSize, status);
            return Result.success("视频列表查询成功", result);
        } catch (Exception e) {
            log.error("=============== Get all videos failed: {}", e.getMessage());
            return Result.error("视频列表查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据用户ID获取视频列表（分页）
     */
    @Operation(summary = "根据用户ID获取视频列表", description = "分页获取指定用户上传的所有视频")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频列表查询成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/user/{userId}")
    public Result<Map<String, Object>> getVideosByUserId(
            @Parameter(description = "用户ID", required = true, example = "1") 
            @PathVariable Long userId,
            @Parameter(description = "页码（从1开始，默认1）", example = "1") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量（默认10）", example = "10") 
            @RequestParam(defaultValue = "10") int pageSize) {
        log.debug("=============== Received get videos by user id request, userId: {}, page: {}, pageSize: {}", userId, page, pageSize);
        try {
            if (userId == null || userId <= 0) {
                return Result.badRequest("用户ID无效");
            }
            Map<String, Object> result = tcmVideoService.getVideosByUserIdWithPagination(userId, page, pageSize);
            return Result.success("用户视频列表查询成功", result);
        } catch (Exception e) {
            log.error("=============== Get videos by user id failed: {}", e.getMessage());
            return Result.error("用户视频列表查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据板块ID获取视频列表
     */
    @Operation(summary = "根据板块ID获取视频列表", description = "获取指定板块下的所有视频")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频列表查询成功"),
        @ApiResponse(responseCode = "400", description = "板块ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/category/{categoryId}")
    public Result<List<TcmVideo>> getVideosByCategoryId(
            @Parameter(description = "板块ID", required = true, example = "1") 
            @PathVariable Long categoryId) {
        log.debug("=============== Received get videos by category id request, categoryId: {}", categoryId);
        try {
            if (categoryId == null || categoryId <= 0) {
                return Result.badRequest("板块ID无效");
            }
            List<TcmVideo> videos = tcmVideoService.getVideosByCategoryId(categoryId);
            return Result.success("板块视频列表查询成功", videos);
        } catch (Exception e) {
            log.error("=============== Get videos by category id failed: {}", e.getMessage());
            return Result.error("板块视频列表查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 搜索视频
     */
    @Operation(summary = "搜索视频", description = "根据关键词搜索视频")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频搜索成功"),
        @ApiResponse(responseCode = "400", description = "搜索关键词无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/search")
    public Result<List<TcmVideo>> searchVideos(
            @Parameter(description = "搜索关键词", required = true, example = "中医") 
            @RequestParam String keyword) {
        log.debug("=============== Received search videos request, keyword: {}", keyword);
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return Result.badRequest("搜索关键词不能为空");
            }
            List<TcmVideo> videos = tcmVideoService.searchVideos(keyword);
            return Result.success("视频搜索成功", videos);
        } catch (Exception e) {
            log.error("=============== Search videos failed: {}", e.getMessage());
            return Result.error("视频搜索失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取热门视频
     */
    @Operation(summary = "获取热门视频", description = "获取观看次数最多的视频")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "热门视频查询成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/hot")
    public Result<List<TcmVideo>> getHotVideos(
            @Parameter(description = "返回数量限制", required = false, example = "10") 
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("=============== Received get hot videos request, limit: {}", limit);
        try {
            if (limit <= 0) {
                limit = 10; // 确保limit为正数
            }
            List<TcmVideo> videos = tcmVideoService.getHotVideos(limit);
            return Result.success("热门视频查询成功", videos);
        } catch (Exception e) {
            log.error("=============== Get hot videos failed: {}", e.getMessage());
            return Result.error("热门视频查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取推荐视频
     */
    @Operation(summary = "获取推荐视频", description = "获取推荐的视频列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "推荐视频查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/recommended")
    public Result<List<TcmVideo>> getRecommendedVideos() {
        log.debug("=============== Received get recommended videos request");
        try {
            List<TcmVideo> videos = tcmVideoService.getRecommendedVideos();
            return Result.success("推荐视频查询成功", videos);
        } catch (Exception e) {
            log.error("=============== Get recommended videos failed: {}", e.getMessage());
            return Result.error("推荐视频查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取最新视频
     */
    @Operation(summary = "获取最新视频", description = "获取最新上传的视频")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "最新视频查询成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/latest")
    public Result<List<TcmVideo>> getLatestVideos(
            @Parameter(description = "返回数量限制", required = false, example = "10") 
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("=============== Received get latest videos request, limit: {}", limit);
        try {
            if (limit <= 0) {
                limit = 10; // 确保limit为正数
            }
            List<TcmVideo> videos = tcmVideoService.getLatestVideos(limit);
            return Result.success("最新视频查询成功", videos);
        } catch (Exception e) {
            log.error("=============== Get latest videos failed: {}", e.getMessage());
            return Result.error("最新视频查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 增加视频点赞
     */
    @Operation(summary = "增加视频点赞", description = "增加视频的点赞次数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "点赞成功"),
        @ApiResponse(responseCode = "404", description = "视频不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/videos/{id}/like")
     public Result<Map<String, String>> likeVideo( 
             @Parameter(description = "视频ID", required = true, example = "1") 
             @PathVariable Long id,
             @RequestHeader("Authorization") String authorization) { 
         log.debug("=============== Received like video request, id: {}", id); 
         try {
             if (id == null || id <= 0) {
                 log.warn("=============== Invalid video id: {}", id);
                 return Result.badRequest("Invalid video id");
             }
              
             tcmVideoService.incrementLikeCount(id); 
              
             // 从JWT中获取userId
             String jwtToken = authorization.substring(7); // 去除Bearer前缀
             Long userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
              
             // 创建点赞记录
             TcmVideoWatchRecord watchRecord = new TcmVideoWatchRecord();
             watchRecord.setVideoId(id);
             watchRecord.setUserId(userId);
             watchRecord.setRecodeType("like");
             watchRecord.setWatchDate(java.time.LocalDate.now());
             
             // 保存点赞记录
             tcmVideoWatchRecordService.saveOrUpdateRecord(watchRecord);
             log.debug("=============== Created like record for video {} by user {}", id, userId);
              
             Map<String, String> response = Map.of("message", "Video liked successfully"); 
             return Result.success(response);
         } catch (Exception e) {
             log.error("=============== Error liking video, id: {}, error: {}", id, e.getMessage());
             return Result.error("Failed to like video");
         }
     }
    
    /**
      * 获取用户视频历史 
      */ 
     @Operation(summary = "获取用户视频历史", description = "获取用户观看和点赞的视频历史列表") 
      @ApiResponses(value = { 
          @ApiResponse(responseCode = "200", description = "获取视频历史成功"), 
          @ApiResponse(responseCode = "401", description = "未授权"), 
          @ApiResponse(responseCode = "500", description = "服务器内部错误") 
      }) 
     @GetMapping("/videos/likeAndWatchHistory") 
       public Result<Map<String, Object>> likeAndWatchHistory( 
               @RequestHeader("Authorization") String authorization,
               @Parameter(description = "页码（从1开始，默认1）", example = "1") 
               @RequestParam(defaultValue = "1") int page,
               @Parameter(description = "每页数量（默认10）", example = "10") 
               @RequestParam(defaultValue = "10") int pageSize) { 
          log.debug("=============== Received request to get user's like and watch video history with page={}, pageSize={}", page, pageSize); 
          try { 
              // 从JWT中获取userId 
              String jwtToken = authorization.substring(7); // 去除Bearer前缀 
              Long userId = jwtTokenUtil.getUserIdFromToken(jwtToken); 
              
              // 获取用户的观看和点赞视频历史（分页）
              Map<String, Object> result = tcmVideoWatchRecordService.getUserVideoHistoryWithDetails(userId, page, pageSize); 
              
              log.debug("=============== Retrieved {} records from user's video history", result.get("totalElements")); 
              return Result.success(result); 
          } catch (Exception e) { 
              log.error("=============== Error retrieving user's video history: {}", e.getMessage()); 
              return Result.error("Failed to retrieve video history"); 
          } 
      }
     
     /**
      * 增加视频观看次数
      */
     @Operation(summary = "增加视频观看次数", description = "每次调用增加视频的观看次数+1")
     @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "增加观看次数成功"),
         @ApiResponse(responseCode = "400", description = "参数错误"),
         @ApiResponse(responseCode = "404", description = "视频不存在"),
         @ApiResponse(responseCode = "500", description = "服务器内部错误")
     })
     @PostMapping("/videos/{id}/view")
     public Result<Map<String, Object>> incrementViewCount(
             @Parameter(description = "视频ID", required = true, example = "1")
             @PathVariable Long id) {
         log.debug("=============== Received increment video view count request, video ID: {}", id);
         try {
             if (id == null || id <= 0) {
                 log.warn("=============== Invalid video id: {}", id);
                 return Result.badRequest("Invalid video id");
             }
             
             // 检查视频是否存在
             TcmVideo video = tcmVideoService.getVideoById(id);
             if (video == null) {
                 log.warn("=============== Video not found, id: {}", id);
                 return Result.notFound("Video not found");
             }
             
             // 增加观看次数
             tcmVideoService.incrementViewCount(id);
             
             // 获取更新后的观看次数
             long updatedViewCount = video.getViewCount() + 1; // 这里使用+1是因为我们刚刚更新了
             
             Map<String, Object> data = new HashMap<>();
             data.put("message", "View count increment successful");
             data.put("videoId", id);
             data.put("viewCount", updatedViewCount);
             
             return Result.success("View count increment successful", data);
         } catch (Exception e) {
             log.error("=============== Error incrementing video view count, id: {}, error: {}", id, e.getMessage());
             return Result.error("Failed to increment view count");
         }
     }
    
    /**
     * 更新视频状态
     */
    @Operation(summary = "更新视频状态", description = "更新视频的状态")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "状态更新成功"),
        @ApiResponse(responseCode = "400", description = "参数无效"),
        @ApiResponse(responseCode = "404", description = "视频不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/videos/{id}/status")
    public Result<Map<String, Object>> updateVideoStatus(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long id,
            @Parameter(description = "视频状态(0-禁用, 1-启用, 2-审核中)", required = true, example = "1") 
            @RequestParam Integer status) {
        log.debug("=============== Received update video status request, id: {}, status: {}", id, status);
        try {
            if (id == null || id <= 0) {
                return Result.badRequest("视频ID无效");
            }
            if (status == null || status < 0 || status > 2) {
                return Result.badRequest("状态值无效");
            }
            tcmVideoService.updateVideoStatus(id, status);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "视频状态更新成功");
            return Result.success("视频状态更新成功", result);
        } catch (Exception e) {
            log.error("=============== Update video status failed: {}", e.getMessage());
            return Result.error("视频状态更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取视频统计信息
     */
    @Operation(summary = "获取视频统计信息", description = "获取视频相关的统计数据")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计信息查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/statistics")
    public ResponseEntity<Map<String, Object>> getVideoStatistics() {
        log.debug("=============== Received get video statistics request");
        return ResponseEntity.ok(tcmVideoService.getVideoStatistics());
    }
    
    /**
     * 增加视频观看次数
     */
    @Operation(summary = "增加视频观看次数", description = "增加指定视频的观看次数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频观看数增加成功"),
        @ApiResponse(responseCode = "400", description = "视频ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/videos/view/{videoId}")
    public Result<Map<String, Object>> increaseVideoView(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long videoId) {
        log.debug("=============== Received increase video view request, videoId: {}", videoId);
        try {
            if (videoId == null || videoId <= 0) {
                return Result.badRequest("视频ID无效");
            }
            tcmVideoService.incrementViewCount(videoId);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "视频观看数增加成功");
            return Result.success("视频观看数增加成功", result);
        } catch (Exception e) {
            log.error("=============== Increase video view failed: {}", e.getMessage());
            return Result.error("视频观看数增加失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据标签获取视频
     */
    @Operation(summary = "根据标签获取视频", description = "根据标签查询视频列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "视频查询成功"),
        @ApiResponse(responseCode = "400", description = "标签无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/tag/{tag}")
    public Result<List<TcmVideo>> getVideosByTag(
            @Parameter(description = "视频标签", required = true, example = "中医") 
            @PathVariable String tag) {
        log.debug("=============== Received get videos by tag request, tag: {}", tag);
        try {
            if (tag == null || tag.trim().isEmpty()) {
                return Result.badRequest("标签不能为空");
            }
            List<TcmVideo> videos = tcmVideoService.getVideosByTag(tag);
            return Result.success("根据标签查询视频成功", videos);
        } catch (Exception e) {
            log.error("=============== Get videos by tag failed: {}", e.getMessage());
            return Result.error("根据标签查询视频失败：" + e.getMessage());
        }
    }
    
    /**
     * 下载视频
     */
    @Operation(summary = "下载视频", description = "下载视频文件，同时增加下载次数")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "下载成功")
    })
    @GetMapping("/videos/{id}/download")
    public Result<Map<String, String>> downloadVideo(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.debug("=============== Received download video request, id: {}", id);
        try {
            if (id == null || id <= 0) {
                return Result.badRequest("视频ID无效");
            }
            // 增加下载次数
            tcmVideoService.incrementDownloadCount(id);
            // 获取视频信息
            TcmVideo video = tcmVideoService.getVideoById(id);
            
            if (video == null) {
                return Result.notFound("视频不存在");
            }
            
            Map<String, String> response = Map.of(
                    "message", "Download count incremented",
                    "filePath", video.getFilePath(),
                    "fileName", video.getTitle()
            );
            return Result.success("视频下载信息获取成功", response);
        } catch (Exception e) {
            log.error("=============== Download video failed: {}", e.getMessage());
            return Result.error("视频下载信息获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取视频封面图片
     */
    @Operation(summary = "获取视频封面图片", description = "根据视频ID获取封面图片数据")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "封面图片获取成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/videos/{id}/thumbnail")
    public Result<byte[]> getVideoThumbnail(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.debug("=============== Received get video thumbnail request, id: {}", id);
        try {
            if (id == null || id <= 0) {
                return Result.badRequest("视频ID无效");
            }
            
            byte[] thumbnailData = tcmVideoService.getVideoThumbnail(id);
            if (thumbnailData == null) {
                log.debug("=============== No thumbnail found for video id: {}", id);
                return Result.success("无缩略图", null);
            }
            
            // 简单判断文件类型，实际应用中可能需要更复杂的类型检测
            TcmVideo video = tcmVideoService.getVideoById(id);
            String thumbnailPath = video != null ? video.getThumbnailPath() : null;
            String contentType = "image/jpeg"; // 默认类型
            
            if (thumbnailPath != null) {
                if (thumbnailPath.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                } else if (thumbnailPath.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (thumbnailPath.toLowerCase().endsWith(".webp")) {
                    contentType = "image/webp";
                }
            }
            
            return Result.success("视频缩略图获取成功", thumbnailData);
        } catch (IOException e) {
            log.error("=============== Error retrieving video thumbnail: {}", e.getMessage());
            return Result.error("视频缩略图获取失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("=============== Get video thumbnail failed: {}", e.getMessage());
            return Result.error("视频缩略图获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取视频封面URL
     */
    @Operation(summary = "获取视频封面URL", description = "根据视频ID获取封面图片的URL地址")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "封面URL获取成功")
    })
    @GetMapping("/videos/{id}/thumbnail-url")
    public Result<Map<String, String>> getVideoThumbnailUrl(
            @Parameter(description = "视频ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.debug("=============== Received get video thumbnail URL request, id: {}", id);
        try {
            if (id == null || id <= 0) {
                return Result.badRequest("视频ID无效");
            }
            
            String thumbnailUrl = tcmVideoService.getVideoThumbnailUrl(id);
            Map<String, String> response;
            
            if (thumbnailUrl == null) {
                log.debug("=============== No thumbnail URL found for video id: {}", id);
                response = Map.of(
                        "thumbnailUrl", null,
                        "message", "No thumbnail available"
                );
                return Result.success("无缩略图URL", response);
            } else {
                response = Map.of(
                        "thumbnailUrl", thumbnailUrl,
                        "message", "Thumbnail URL retrieved successfully"
                );
                return Result.success("视频缩略图URL获取成功", response);
            }
        } catch (Exception e) {
            log.error("=============== Get video thumbnail URL failed: {}", e.getMessage());
            return Result.error("视频缩略图URL获取失败：" + e.getMessage());
        }
    }
}