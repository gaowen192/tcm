package com.ride.service;

import com.ride.dto.TcmArticleDTO;
import com.ride.entity.TcmArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.io.IOException;

/**
 * 医药论坛文章服务层接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmArticleService {
    
    /**
     * 创建文章
     * 
     * @param articleDTO 文章DTO
     * @return 创建的文章
     */
    TcmArticle createArticle(TcmArticleDTO articleDTO);
    
    /**
     * 创建文章并上传封面图片
     * 
     * @param articleDTO 文章DTO
     * @param file 封面图片文件
     * @return 创建的文章
     * @throws IOException 文件处理异常
     */
    TcmArticle createArticle(TcmArticleDTO articleDTO, MultipartFile file) throws IOException;
    
    /**
     * 根据ID获取文章
     * 
     * @param id 文章ID
     * @return 文章
     */
    TcmArticle getArticleById(Long id);
    
    /**
     * 更新文章
     * 
     * @param id 文章ID
     * @param articleDTO 文章DTO
     * @return 更新后的文章
     */
    TcmArticle updateArticle(Long id, TcmArticleDTO articleDTO);
    
    /**
     * 更新文章并上传封面图片
     * 
     * @param id 文章ID
     * @param articleDTO 文章DTO
     * @param file 封面图片文件
     * @return 更新后的文章
     * @throws IOException 文件处理异常
     */
    TcmArticle updateArticle(Long id, TcmArticleDTO articleDTO, MultipartFile file) throws IOException;
    
    /**
     * 删除文章
     * 
     * @param id 文章ID
     * @return 删除结果
     */
    boolean deleteArticle(Long id);
    
    /**
     * 根据用户ID获取文章列表
     * 
     * @param userId 用户ID
     * @return 文章列表
     */
    List<TcmArticle> getArticlesByUserId(Long userId);
    
    /**
     * 根据用户ID获取文章列表（分页）
     * 
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页文章结果
     */
    Map<String, Object> getArticlesByUserIdWithPagination(Long userId, int page, int pageSize);
    
    /**
     * 根据板块ID获取文章列表
     * 
     * @param categoryId 板块ID
     * @return 文章列表
     */
    List<TcmArticle> getArticlesByCategoryId(Long categoryId);
    
    /**
     * 根据板块ID获取文章列表（分页）
     * 
     * @param categoryId 板块ID
     * @param pageable 分页参数
     * @return 分页文章结果
     */
    Map<String, Object> getArticlesByCategoryIdWithPagination(Long categoryId, Pageable pageable);
    
    /**
     * 根据状态获取文章列表
     * 
     * @param status 状态
     * @return 文章列表
     */
    List<TcmArticle> getArticlesByStatus(Integer status);
    
    /**
     * 根据状态获取文章列表（分页）
     * 
     * @param status 状态
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页文章结果
     */
    Map<String, Object> getArticlesByStatusWithPagination(Integer status, int page, int pageSize);
    
    /**
     * 根据用户ID和状态获取文章列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 文章列表
     */
    List<TcmArticle> getArticlesByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 获取所有文章列表（分页）
     * 
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页文章结果
     */
    Map<String, Object> getAllArticlesWithPagination(int page, int pageSize);
    
    /**
     * 获取文章列表（支持搜索和分页）
     * 
     * @param title 标题关键字（可选）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页文章结果
     */
    Map<String, Object> searchArticles(String title, int page, int pageSize);
    
    /**
     * 根据标题搜索文章
     * 
     * @param title 标题关键字
     * @return 文章列表
     */
    List<TcmArticle> searchArticlesByTitle(String title);
    
    /**
     * 根据标签搜索文章
     * 
     * @param tag 标签
     * @return 文章列表
     */
    List<TcmArticle> searchArticlesByTag(String tag);
    
    /**
     * 获取热门文章列表
     * 
     * @param limit 限制数量
     * @return 热门文章列表
     */
    List<TcmArticle> getHotArticles(int limit);
    
    /**
     * 获取推荐文章列表
     * 
     * @return 推荐文章列表
     */
    List<TcmArticle> getRecommendedArticles();
    
    /**
     * 获取最新文章列表
     * 
     * @param limit 限制数量
     * @return 最新文章列表
     */
    List<TcmArticle> getLatestArticles(int limit);
    
    /**
     * 增加文章阅读次数
     * 
     * @param id 文章ID
     * @return 增加结果
     */
    boolean incrementViewCount(Long id);
    
    /**
     * 增加文章点赞次数
     * 
     * @param id 文章ID
     * @return 增加结果
     */
    boolean incrementLikeCount(Long id);
    
    /**
     * 减少文章点赞次数
     * 
     * @param id 文章ID
     * @return 减少结果
     */
    boolean decrementLikeCount(Long id);
    
    /**
     * 增加文章评论次数
     * 
     * @param id 文章ID
     * @return 增加结果
     */
    boolean incrementCommentCount(Long id);
    
    /**
     * 减少文章评论次数
     * 
     * @param id 文章ID
     * @return 减少结果
     */
    boolean decrementCommentCount(Long id);
    
    /**
     * 增加文章收藏次数
     * 
     * @param id 文章ID
     * @return 增加结果
     */
    boolean incrementCollectCount(Long id);
    
    /**
     * 减少文章收藏次数
     * 
     * @param id 文章ID
     * @return 减少结果
     */
    boolean decrementCollectCount(Long id);
    
    /**
     * 更新文章热门状态
     * 
     * @param id 文章ID
     * @param isHot 是否热门
     * @return 更新结果
     */
    boolean updateHotStatus(Long id, Boolean isHot);
    
    /**
     * 批量更新文章热门状态
     * 
     * @param ids 文章ID列表
     * @param isHot 是否热门
     * @return 更新结果
     */
    boolean batchUpdateHotStatus(List<Long> ids, Boolean isHot);
    
    /**
     * 更新文章推荐状态
     * 
     * @param id 文章ID
     * @param isRecommended 是否推荐
     * @return 更新结果
     */
    boolean updateRecommendedStatus(Long id, Boolean isRecommended);
    
    /**
     * 批量更新文章推荐状态
     * 
     * @param ids 文章ID列表
     * @param isRecommended 是否推荐
     * @return 更新结果
     */
    boolean batchUpdateRecommendedStatus(List<Long> ids, Boolean isRecommended);
    
    /**
     * 统计用户文章数量
     * 
     * @param userId 用户ID
     * @return 文章数量
     */
    long countArticlesByUserId(Long userId);
    
    /**
     * 统计板块文章数量
     * 
     * @param categoryId 板块ID
     * @return 文章数量
     */
    long countArticlesByCategoryId(Long categoryId);
    
    /**
     * 获取带用户名的文章列表
     * 
     * @param categoryId 板块ID
     * @return 带用户名的文章列表
     */
    List<Map<String, Object>> getArticlesWithUserNameByCategoryId(Long categoryId);
}