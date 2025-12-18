package com.ride.service.impl;

import com.ride.dto.TcmVideoDTO;
import com.ride.entity.TcmVideo;
import com.ride.mapper.TcmVideoRepository;
import com.ride.service.TcmVideoService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 医药论坛视频服务实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
public class TcmVideoServiceImpl implements TcmVideoService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmVideoServiceImpl.class);
    
    @Autowired
    private TcmVideoRepository tcmVideoRepository;
    
    // 从配置文件读取视频存储路径
    @Value("${video.storage.path}")
    private String videoStoragePath;
    
    // 从配置文件读取支持的视频格式
    @Value("${video.formats}")
    private String supportedFormatsString;
    
    // 支持的视频格式列表
    private List<String> supportedFormats;
    
    // 从配置文件读取视频最大大小
    @Value("${video.max-size}")
    private long maxVideoSize;
    
    // 从配置文件读取缩略图最大大小
    @Value("${video.thumbnail.max-size}")
    private long maxThumbnailSize;
    
    // 初始化支持的视频格式列表
    @PostConstruct
    public void init() {
        this.supportedFormats = Arrays.asList(supportedFormatsString.split(","));
        log.debug("=============== Video configuration initialized: storage path={}, formats={}, max video size={}, max thumbnail size={}",
                videoStoragePath, supportedFormatsString, maxVideoSize, maxThumbnailSize);
    }
    
    @Override
    @Transactional
    public TcmVideo saveVideo(TcmVideo video) {
        log.debug("=============== Saving video: {}", video.getTitle());
        return tcmVideoRepository.save(video);
    }
    
    @Override
    @Transactional
    public TcmVideo uploadVideo(TcmVideo video, MultipartFile file) throws IOException {
        // 调用带封面图片的重载方法，但封面图片为null
        return uploadVideo(video, file, null);
    }
    
    @Override
    @Transactional
    public TcmVideo uploadVideo(TcmVideo video, MultipartFile file, MultipartFile thumbnail) throws IOException {
        log.debug("=============== Processing single video upload for title: {}", video.getTitle());
        
        // 1. 验证文件存在
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file cannot be empty");
        }
        
        // 2. 明确记录只处理单个文件
        log.debug("=============== Single file upload confirmed, original filename: {}", file.getOriginalFilename());
        
        // 3. 验证文件
        validateVideoFile(file);
        
        // 4. 创建上传目录（按日期组织）
        String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 结合当前项目路径和配置的存储路径
        String uploadDir = System.getProperty("user.dir") + File.separator + videoStoragePath.replace("./", "");
        Path uploadPath = Paths.get(uploadDir, dateDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.debug("=============== Created upload directory: {}", uploadPath);
        }
        
        // 5. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;
        
        // 6. 保存视频文件
        Path filePath = uploadPath.resolve(uniqueFilename);
        file.transferTo(filePath.toFile());
        log.debug("=============== Single video file saved to: {}", filePath);
        
        // 7. 处理封面图片
        if (thumbnail != null && !thumbnail.isEmpty()) {
            log.debug("=============== Processing thumbnail image");
            
            // 验证缩略图文件
            validateThumbnailFile(thumbnail);
            
            // 使用与视频相同的目录结构存储封面图片
            // 结合当前项目路径和配置的存储路径
            uploadDir = System.getProperty("user.dir") + File.separator + videoStoragePath.replace("./", "");
            Path thumbnailPath = Paths.get(uploadDir, dateDir);
            // 目录已经在视频处理部分创建，无需重复创建
            
            // 生成缩略图文件名，添加thumbnail前缀以区分视频文件
            String thumbnailExtension = getFileExtension(thumbnail.getOriginalFilename());
            String thumbnailFilename = "thumbnail_" + UUID.randomUUID().toString() + "." + thumbnailExtension;
            Path thumbnailFilePath = thumbnailPath.resolve(thumbnailFilename);
            
            // 保存缩略图
            thumbnail.transferTo(thumbnailFilePath.toFile());
            log.debug("=============== Thumbnail saved to: {}", thumbnailFilePath);
            
            // 设置缩略图相对路径
            String relativeThumbnailPath = videoStoragePath.replace("./", "") + "/" + dateDir + "/" + thumbnailFilename;
            video.setThumbnailPath(relativeThumbnailPath.replace("\\", "/"));
            log.debug("=============== Storing relative thumbnail path: {}", relativeThumbnailPath);
        }
        
        // 8. 更新视频信息，只存储相对路径
        String relativeVideoPath = videoStoragePath.replace("./", "") + "/" + dateDir + "/" + uniqueFilename;
        video.setFilePath(relativeVideoPath.replace("\\", "/"));
        log.debug("=============== Storing relative video path: {}", relativeVideoPath);
        video.setFileSize(file.getSize());
        video.setFormat(fileExtension); // 修正：使用正确的setFormat方法
        // 不需要设置uploadDate，实体类会自动处理createdAt
        video.setStatus(1); // 默认状态为审核中
        
        // 9. 保存到数据库
        return saveVideo(video);
    }
    
    /**
     * Validate video file
     */
    private void validateVideoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file cannot be empty");
        }
        
        // Check file size using configured value
        if (file.getSize() > maxVideoSize) {
            throw new IllegalArgumentException("Video file size cannot exceed " + (maxVideoSize / (1024 * 1024)) + "MB");
        }
        
        // Check file format
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Invalid file name");
        }
        
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!supportedFormats.contains(fileExtension)) {
            throw new IllegalArgumentException("Unsupported video format. Supported formats: " + String.join(", ", supportedFormats));
        }
    }
    
    /**
     * Validate thumbnail file
     */
    private void validateThumbnailFile(MultipartFile thumbnail) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            return; // Skip validation if thumbnail is not provided
        }
        
        // Check file size using configured value
        if (thumbnail.getSize() > maxThumbnailSize) {
            throw new IllegalArgumentException("Thumbnail file size cannot exceed " + (maxThumbnailSize / (1024 * 1024)) + "MB");
        }
        
        // Check file format (common image formats)
        String originalFilename = thumbnail.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Invalid thumbnail file name");
        }
        
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        List<String> supportedImageFormats = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
        if (!supportedImageFormats.contains(fileExtension)) {
            throw new IllegalArgumentException("Unsupported thumbnail format. Supported formats: " + String.join(", ", supportedImageFormats));
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }
    
    @Override
    public TcmVideo getVideoById(Long id) {
        log.debug("=============== Getting video by id: {}", id);
        return tcmVideoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found: " + id));
    }
    
    @Override
    @Transactional
    public TcmVideo updateVideo(TcmVideo video) {
        log.debug("=============== Updating video, id: {}", video.getId());
        return tcmVideoRepository.save(video);
    }
    
    @Override
    @Transactional
    public void deleteVideo(Long id) {
        log.debug("=============== Deleting video, id: {}", id);
        tcmVideoRepository.deleteById(id);
    }
    
    @Override
    public List<TcmVideo> getVideosByUserId(Long userId) {
        log.debug("=============== Getting videos by user id: {}", userId);
        return tcmVideoRepository.findByUserId(userId);
    }
    
    @Override
    public Map<String, Object> getVideosByUserIdWithPagination(Long userId, int page, int pageSize) {
        log.debug("=============== Getting videos by user id with pagination - userId: {}, page: {}, pageSize: {}", userId, page, pageSize);
        Map<String, Object> result = new HashMap<>();
        
        // 创建分页请求对象，将页码从1开始转换为0开始
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // 执行分页查询
        Page<TcmVideo> videoPage = tcmVideoRepository.findByUserId(userId, pageable);
        
        // 构建返回结果，将当前页码转换为从1开始
        result.put("videos", videoPage.getContent());
        result.put("totalElements", videoPage.getTotalElements());
        result.put("totalPages", videoPage.getTotalPages());
        result.put("currentPage", videoPage.getNumber() + 1);
        result.put("pageSize", videoPage.getSize());
        
        return result;
    }
    
    @Override
    public List<TcmVideo> getVideosByCategoryId(Long categoryId) {
        log.debug("=============== Getting videos by category id: {}", categoryId);
        // 调用新增的关联查询方法，获取视频和用户信息
        List<Object[]> results = tcmVideoRepository.findVideosWithUserNameByCategoryId(categoryId);
        
        // 将结果转换为DTO对象列表
        List<TcmVideoDTO> dtoList = new ArrayList<>();
        for (Object[] result : results) {
            // 第一个对象是TcmVideo实体
            TcmVideo video = (TcmVideo) result[0];
            // 第二个对象是用户名
            String userName = (String) result[1];
            
            // 创建DTO并设置属性
            TcmVideoDTO dto = new TcmVideoDTO();
            dto.setId(video.getId());
            dto.setTitle(video.getTitle());
            dto.setDescription(video.getDescription());
            dto.setUserId(video.getUserId());
            dto.setUserName(userName);
            dto.setCategoryId(video.getCategoryId());
            dto.setFilePath(video.getFilePath());
            dto.setThumbnailPath(video.getThumbnailPath());
            dto.setDuration(video.getDuration());
            dto.setFileSize(video.getFileSize());
            dto.setFormat(video.getFormat());
            dto.setResolution(video.getResolution());
            dto.setBitrate(video.getBitrate());
            dto.setCodec(video.getCodec());
            dto.setTags(video.getTags());
            dto.setViewCount(video.getViewCount());
            dto.setLikeCount(video.getLikeCount());
            dto.setCommentCount(video.getCommentCount());
            dto.setDownloadCount(video.getDownloadCount());
            dto.setUploadIp(video.getUploadIp());
            dto.setStatus(video.getStatus());
            dto.setIsHot(video.getIsHot());
            dto.setIsRecommended(video.getIsRecommended());
            dto.setPlaybackUrl(video.getPlaybackUrl());
            dto.setCreatedAt(video.getCreatedAt());
            dto.setUpdatedAt(video.getUpdatedAt());
            dto.setPublishedAt(video.getPublishedAt());
            
            dtoList.add(dto);
        }
        
        // 为了保持兼容性，将DTO列表转换回实体列表
        // 注意：这种方式会丢失userName信息，但保证接口返回类型一致
        // 实际项目中应该修改接口返回类型为DTO
        List<TcmVideo> videoList = new ArrayList<>();
        for (TcmVideoDTO dto : dtoList) {
            TcmVideo video = new TcmVideo();
            video.setId(dto.getId());
            video.setTitle(dto.getTitle());
            video.setDescription(dto.getDescription());
            video.setUserId(dto.getUserId());
            video.setCategoryId(dto.getCategoryId());
            video.setFilePath(dto.getFilePath());
            video.setThumbnailPath(dto.getThumbnailPath());
            video.setDuration(dto.getDuration());
            video.setFileSize(dto.getFileSize());
            video.setFormat(dto.getFormat());
            video.setResolution(dto.getResolution());
            video.setBitrate(dto.getBitrate());
            video.setCodec(dto.getCodec());
            video.setTags(dto.getTags());
            video.setViewCount(dto.getViewCount());
            video.setLikeCount(dto.getLikeCount());
            video.setCommentCount(dto.getCommentCount());
            video.setDownloadCount(dto.getDownloadCount());
            video.setUploadIp(dto.getUploadIp());
            video.setStatus(dto.getStatus());
            video.setIsHot(dto.getIsHot());
            video.setIsRecommended(dto.getIsRecommended());
            video.setPlaybackUrl(dto.getPlaybackUrl());
            video.setCreatedAt(dto.getCreatedAt());
            video.setUpdatedAt(dto.getUpdatedAt());
            video.setPublishedAt(dto.getPublishedAt());
            videoList.add(video);
        }
        
        return videoList;
    }
    
    @Override
    public List<TcmVideo> getVideosByStatus(Integer status) {
        log.debug("=============== Getting videos by status: {}", status);
        return tcmVideoRepository.findByStatus(status);
    }
    
    @Override
    public Page<TcmVideo> getVideosWithPagination(int page, int pageSize, Integer status) {
        log.debug("=============== Getting videos with pagination - page: {}, pageSize: {}, status: {}", page, pageSize, status);
        
        // 创建分页请求对象，将页码从1开始转换为0开始
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // 执行分页查询
        if (status != null) {
            // 根据状态查询
            return tcmVideoRepository.findByStatus(status, pageable);
        } else {
            // 查询所有
            return tcmVideoRepository.findAll(pageable);
        }
    }
    
    @Override
    public List<TcmVideo> searchVideos(String keyword) {
        log.debug("=============== Searching videos by keyword: {}", keyword);
        return tcmVideoRepository.findByTitleLike(keyword);
    }
    
    @Override
    public List<TcmVideo> getHotVideos(int limit) {
        log.debug("=============== Getting hot videos, limit: {}", limit);
        return tcmVideoRepository.findHotVideos(limit);
    }
    
    @Override
    public List<TcmVideo> getRecommendedVideos() {
        log.debug("=============== Getting recommended videos");
        return tcmVideoRepository.findByIsRecommendedTrueAndStatus(1);
    }
    
    @Override
    public List<TcmVideo> getLatestVideos(int limit) {
        log.debug("=============== Getting latest videos, limit: {}", limit);
        return tcmVideoRepository.findLatestVideos(limit);
    }
    
    @Override
    @Transactional
    public void incrementViewCount(Long videoId) {
        log.debug("=============== Incrementing view count for video: {}", videoId);
        tcmVideoRepository.incrementViewCount(videoId);
    }
    
    @Override
    @Transactional
    public void incrementLikeCount(Long videoId) {
        log.debug("=============== Incrementing like count for video: {}", videoId);
        tcmVideoRepository.incrementLikeCount(videoId);
    }
    
    @Override
    @Transactional
    public void decrementLikeCount(Long videoId) {
        log.debug("=============== Decrementing like count for video: {}", videoId);
        tcmVideoRepository.decrementLikeCount(videoId);
    }
    
    @Override
    @Transactional
    public void incrementCommentCount(Long videoId) {
        log.debug("=============== Incrementing comment count for video: {}", videoId);
        tcmVideoRepository.incrementCommentCount(videoId);
    }
    
    @Override
    @Transactional
    public void decrementCommentCount(Long videoId) {
        log.debug("=============== Decrementing comment count for video: {}", videoId);
        tcmVideoRepository.decrementCommentCount(videoId);
    }
    
    @Override
    @Transactional
    public void incrementDownloadCount(Long videoId) {
        log.debug("=============== Incrementing download count for video: {}", videoId);
        tcmVideoRepository.incrementDownloadCount(videoId);
    }
    
    @Override
    @Transactional
    public void updateVideoStatus(Long id, Integer status) {
        log.debug("=============== Updating video status, id: {}, status: {}", id, status);
        TcmVideo video = getVideoById(id);
        video.setStatus(status);
        tcmVideoRepository.save(video);
    }
    
    @Override
    @Transactional
    public void updateHotStatus(Long id, Boolean isHot) {
        log.debug("=============== Updating hot status, id: {}, isHot: {}", id, isHot);
        TcmVideo video = getVideoById(id);
        video.setIsHot(isHot);
        tcmVideoRepository.save(video);
    }
    
    @Override
    @Transactional
    public void updateRecommendedStatus(Long id, Boolean isRecommended) {
        log.debug("=============== Updating recommended status, id: {}, isRecommended: {}", id, isRecommended);
        TcmVideo video = getVideoById(id);
        video.setIsRecommended(isRecommended);
        tcmVideoRepository.save(video);
    }
    
    @Override
    @Transactional
    public void batchUpdateHotStatus(List<Long> ids, Boolean isHot) {
        log.debug("=============== Batch updating hot status, ids: {}, isHot: {}", ids, isHot);
        tcmVideoRepository.updateHotStatus(ids, isHot);
    }
    
    @Override
    @Transactional
    public void batchUpdateRecommendedStatus(List<Long> ids, Boolean isRecommended) {
        log.debug("=============== Batch updating recommended status, ids: {}, isRecommended: {}", ids, isRecommended);
        tcmVideoRepository.updateRecommendedStatus(ids, isRecommended);
    }
    
    @Override
    public long countVideos() {
        log.debug("=============== Counting all videos");
        return tcmVideoRepository.count();
    }
    
    @Override
    public long countVideosByUserId(Long userId) {
        log.debug("=============== Counting videos by user id: {}", userId);
        return tcmVideoRepository.countByUserId(userId);
    }
    
    @Override
    public long countVideosByCategoryId(Long categoryId) {
        log.debug("=============== Counting videos by category id: {}", categoryId);
        return tcmVideoRepository.countByCategoryId(categoryId);
    }
    
    @Override
    public Map<String, Object> getVideoStatistics() {
        log.debug("=============== Getting video statistics");
        Map<String, Object> stats = new HashMap<>();
        
        // 统计总视频数
        stats.put("totalCount", countVideos());
        
        // 统计启用视频数
        stats.put("enabledCount", tcmVideoRepository.findByStatus(1).size());
        
        // 统计禁用视频数
        stats.put("disabledCount", tcmVideoRepository.findByStatus(0).size());
        
        // 统计审核中视频数
        stats.put("pendingCount", tcmVideoRepository.findByStatus(2).size());
        
        // 统计总文件大小
        Long totalSize = tcmVideoRepository.getTotalSize();
        stats.put("totalSize", totalSize);
        
        // 统计格式分布
        List<Object[]> formatStats = tcmVideoRepository.countByFormat();
        stats.put("formatDistribution", formatStats);
        
        // 统计分辨率分布
        List<Object[]> resolutionStats = tcmVideoRepository.countByResolution();
        stats.put("resolutionDistribution", resolutionStats);
        
        return stats;
    }
    
    @Override
    public List<TcmVideo> getVideosByDurationRange(Integer minDuration, Integer maxDuration) {
        log.debug("=============== Getting videos by duration range: {}-{}", minDuration, maxDuration);
        return tcmVideoRepository.findByDurationBetweenAndStatusOrderByCreatedAtDesc(minDuration, maxDuration, 1);
    }
    
    @Override
    public List<TcmVideo> getVideosByTag(String tag) {
        log.debug("=============== Getting videos by tag: {}", tag);
        return tcmVideoRepository.findByTag(tag);
    }
    
    @Override
    @Transactional
    public void updateVideoFileInfo(Long videoId, Map<String, Object> fileInfo) {
        log.debug("=============== Updating video file info, id: {}", videoId);
        TcmVideo video = getVideoById(videoId);
        
        if (fileInfo.containsKey("filePath")) {
            video.setFilePath((String) fileInfo.get("filePath"));
        }
        if (fileInfo.containsKey("thumbnailPath")) {
            video.setThumbnailPath((String) fileInfo.get("thumbnailPath"));
        }
        if (fileInfo.containsKey("duration")) {
            video.setDuration((Integer) fileInfo.get("duration"));
        }
        if (fileInfo.containsKey("fileSize")) {
            video.setFileSize((Long) fileInfo.get("fileSize"));
        }
        if (fileInfo.containsKey("format")) {
            video.setFormat((String) fileInfo.get("format"));
        }
        if (fileInfo.containsKey("resolution")) {
            video.setResolution((String) fileInfo.get("resolution"));
        }
        if (fileInfo.containsKey("bitrate")) {
            video.setBitrate((Integer) fileInfo.get("bitrate"));
        }
        if (fileInfo.containsKey("codec")) {
            video.setCodec((String) fileInfo.get("codec"));
        }
        if (fileInfo.containsKey("playbackUrl")) {
            video.setPlaybackUrl((String) fileInfo.get("playbackUrl"));
        }
        
        tcmVideoRepository.save(video);
    }
    
    @Override
    public byte[] getVideoThumbnail(Long videoId) throws IOException {
        log.debug("=============== Getting video thumbnail for videoId: {}", videoId);
        
        try {
            // 1. 获取视频信息
            TcmVideo video = getVideoById(videoId);
            
            // 2. 检查是否有封面图片
            String thumbnailPath = video.getThumbnailPath();
            if (thumbnailPath == null || thumbnailPath.isEmpty()) {
                log.warn("=============== Video has no thumbnail: {}", videoId);
                return null;
            }
            
            // 3. 构建完整的封面图片路径
            String fullPath = System.getProperty("user.dir") + File.separator + thumbnailPath;
            File thumbnailFile = new File(fullPath);
            
            // 4. 检查文件是否存在
            if (!thumbnailFile.exists() || !thumbnailFile.isFile()) {
                log.error("=============== Thumbnail file not found: {}", fullPath);
                return null;
            }
            
            // 5. 读取文件内容并返回
            log.debug("=============== Successfully retrieved thumbnail for videoId: {}", videoId);
            return Files.readAllBytes(thumbnailFile.toPath());
        } catch (RuntimeException e) {
            // 捕获视频不存在的异常，返回null而不是报错
            log.error("=============== Error getting video thumbnail: {}", e.getMessage());
            return null;
        }
    }
    
    @Override
    public String getVideoThumbnailUrl(Long videoId) {
        log.debug("=============== Getting video thumbnail URL for videoId: {}", videoId);
        
        try {
            // 1. 获取视频信息
            TcmVideo video = getVideoById(videoId);
            
            // 2. 返回存储的封面路径（可以根据需要格式化为完整URL）
            String thumbnailPath = video.getThumbnailPath();
            if (thumbnailPath == null || thumbnailPath.isEmpty()) {
                log.warn("=============== Video has no thumbnail: {}", videoId);
                return null;
            }
            
            // 注意：这里返回的是相对路径，可以根据实际需求调整为绝对URL
            log.debug("=============== Successfully retrieved thumbnail URL for videoId: {}", videoId);
            return thumbnailPath;
        } catch (RuntimeException e) {
            // 捕获视频不存在的异常，返回null而不是报错
            log.error("=============== Error getting video thumbnail URL: {}", e.getMessage());
            return null;
        }
    }
}