package com.ride.service;

import com.ride.entity.TcmVideo;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 医药论坛视频服务接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmVideoService {
    
    /**
     * 保存视频
     * 
     * @param video 视频实体
     * @return 保存后的视频
     */
    TcmVideo saveVideo(TcmVideo video);
    
    /**
     * 上传视频文件
     * 
     * @param video 视频实体
     * @param file 视频文件
     * @return 保存后的视频
     * @throws IOException IO异常
     */
    TcmVideo uploadVideo(TcmVideo video, MultipartFile file) throws IOException;
    
    /**
     * 上传视频文件（支持封面图片）
     * 
     * @param video 视频实体
     * @param file 视频文件
     * @param thumbnail 封面图片
     * @return 保存后的视频
     * @throws IOException IO异常
     */
    TcmVideo uploadVideo(TcmVideo video, MultipartFile file, MultipartFile thumbnail) throws IOException;
    
    /**
     * 根据ID获取视频
     * 
     * @param id 视频ID
     * @return 视频实体
     */
    TcmVideo getVideoById(Long id);
    
    /**
     * 更新视频
     * 
     * @param video 视频实体
     * @return 更新后的视频
     */
    TcmVideo updateVideo(TcmVideo video);
    
    /**
     * 删除视频
     * 
     * @param id 视频ID
     */
    void deleteVideo(Long id);
    
    /**
     * 根据用户ID获取视频列表
     * 
     * @param userId 用户ID
     * @return 视频列表
     */
    List<TcmVideo> getVideosByUserId(Long userId);
    
    /**
     * 根据用户ID分页获取视频列表
     * 
     * @param userId 用户ID
     * @param page 页码（从0开始）
     * @param pageSize 每页数量
     * @return 包含视频列表和分页信息的Map
     */
    Map<String, Object> getVideosByUserIdWithPagination(Long userId, int page, int pageSize);
    
    /**
     * 根据板块ID获取视频列表
     * 
     * @param categoryId 板块ID
     * @return 视频列表
     */
    List<TcmVideo> getVideosByCategoryId(Long categoryId);
    
    /**
     * 根据状态获取视频列表
     * 
     * @param status 状态
     * @return 视频列表
     */
    List<TcmVideo> getVideosByStatus(Integer status);
    
    /**
     * 分页查询视频列表
     * 
     * @param page 页码（从0开始）
     * @param pageSize 每页数量
     * @param status 状态筛选（可选）
     * @return 视频分页结果
     */
    Page<TcmVideo> getVideosWithPagination(int page, int pageSize, Integer status);
    
    /**
     * 搜索视频
     * 
     * @param keyword 关键字
     * @return 视频列表
     */
    List<TcmVideo> searchVideos(String keyword);
    
    /**
     * 获取热门视频列表
     * 
     * @param limit 限制数量
     * @return 热门视频列表
     */
    List<TcmVideo> getHotVideos(int limit);
    
    /**
     * 获取推荐视频列表
     * 
     * @return 推荐视频列表
     */
    List<TcmVideo> getRecommendedVideos();
    
    /**
     * 获取最新视频列表
     * 
     * @param limit 限制数量
     * @return 最新视频列表
     */
    List<TcmVideo> getLatestVideos(int limit);
    
    /**
     * 增加观看次数
     * 
     * @param videoId 视频ID
     */
    void incrementViewCount(Long videoId);
    
    /**
     * 增加点赞次数
     * 
     * @param videoId 视频ID
     */
    void incrementLikeCount(Long videoId);
    
    /**
     * 减少点赞次数
     * 
     * @param videoId 视频ID
     */
    void decrementLikeCount(Long videoId);
    
    /**
     * 增加评论次数
     * 
     * @param videoId 视频ID
     */
    void incrementCommentCount(Long videoId);
    
    /**
     * 减少评论次数
     * 
     * @param videoId 视频ID
     */
    void decrementCommentCount(Long videoId);
    
    /**
     * 增加下载次数
     * 
     * @param videoId 视频ID
     */
    void incrementDownloadCount(Long videoId);
    
    /**
     * 更新视频状态
     * 
     * @param id 视频ID
     * @param status 状态
     */
    void updateVideoStatus(Long id, Integer status);
    
    /**
     * 更新热门状态
     * 
     * @param id 视频ID
     * @param isHot 是否热门
     */
    void updateHotStatus(Long id, Boolean isHot);
    
    /**
     * 更新推荐状态
     * 
     * @param id 视频ID
     * @param isRecommended 是否推荐
     */
    void updateRecommendedStatus(Long id, Boolean isRecommended);
    
    /**
     * 批量更新热门状态
     * 
     * @param ids 视频ID列表
     * @param isHot 是否热门
     */
    void batchUpdateHotStatus(List<Long> ids, Boolean isHot);
    
    /**
     * 批量更新推荐状态
     * 
     * @param ids 视频ID列表
     * @param isRecommended 是否推荐
     */
    void batchUpdateRecommendedStatus(List<Long> ids, Boolean isRecommended);
    
    /**
     * 统计视频总数
     * 
     * @return 视频总数
     */
    long countVideos();
    
    /**
     * 统计用户视频数量
     * 
     * @param userId 用户ID
     * @return 视频数量
     */
    long countVideosByUserId(Long userId);
    
    /**
     * 统计板块视频数量
     * 
     * @param categoryId 板块ID
     * @return 视频数量
     */
    long countVideosByCategoryId(Long categoryId);
    
    /**
     * 获取视频统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getVideoStatistics();
    
    /**
     * 根据视频时长范围查询
     * 
     * @param minDuration 最小时长
     * @param maxDuration 最大时长
     * @return 视频列表
     */
    List<TcmVideo> getVideosByDurationRange(Integer minDuration, Integer maxDuration);
    
    /**
     * 根据标签查询视频
     * 
     * @param tag 标签
     * @return 视频列表
     */
    List<TcmVideo> getVideosByTag(String tag);
    
    /**
     * 更新视频文件信息
     * 
     * @param videoId 视频ID
     * @param fileInfo 文件信息
     */
    void updateVideoFileInfo(Long videoId, Map<String, Object> fileInfo);
    
    /**
     * 获取视频封面
     * 
     * @param videoId 视频ID
     * @return 封面图片字节数组
     * @throws IOException IO异常
     */
    byte[] getVideoThumbnail(Long videoId) throws IOException;
    
    /**
     * 获取视频封面URL
     * 
     * @param videoId 视频ID
     * @return 封面URL
     */
    String getVideoThumbnailUrl(Long videoId);
}