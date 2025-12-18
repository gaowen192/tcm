package com.ride.service;

import com.ride.dto.TcmPostDTO;
import com.ride.entity.TcmPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 医药论坛帖子业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmPostService {
    
    /**
     * 创建帖子
     * 
     * @param post 帖子实体
     * @return 帖子信息
     */
    TcmPostDTO createPost(TcmPost post);
    
    /**
     * 根据ID查询帖子
     * 
     * @param id 帖子ID
     * @return 帖子信息
     */
    TcmPostDTO getPostById(Long id);
    
    /**
     * 根据用户ID查询医药论坛帖子
     * 
     * @param userId 用户ID
     * @return 帖子列表
     */
    List<TcmPostDTO> getPostsByUserId(Long userId);
    
    /**
     * 根据用户ID查询医药论坛帖子（分页）
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    Page<TcmPostDTO> getPostsByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和板块ID查询帖子
     * 
     * @param userId 用户ID
     * @param categoryId 板块ID
     * @return 帖子列表
     */
    List<TcmPostDTO> getPostsByUserIdAndCategoryId(Long userId, Long categoryId);
    
    /**
     * 根据板块ID查询帖子
     * 
     * @param categoryId 板块ID
     * @return 帖子列表
     */
    List<TcmPostDTO> getPostsByCategoryId(Long categoryId);
    
    /**
     * 查询所有帖子
     * 
     * @return 帖子列表
     */
    List<TcmPostDTO> getAllPosts();
    
    /**
     * 分页获取所有帖子，支持高级筛选条件
     * 
     * @param pageable 分页参数
     * @param hotpost 是否筛选热门帖子（不为空则查询浏览量>10的帖子）
     * @param isNew 是否按最新排序（不为空则按创建时间降序排序）
     * @param keyword 搜索关键词（用于搜索帖子标题或内容）
     * @return 帖子分页数据
     */
    Page<TcmPostDTO> getAllPosts(Pageable pageable, String hotpost, String isNew, String keyword);
    
    /**
     * 根据状态查询帖子
     * 
     * @param status 状态
     * @return 帖子列表
     */
    List<TcmPostDTO> getPostsByStatus(Integer status);
    
    /**
     * 模糊查询帖子标题
     * 
     * @param title 标题关键字
     * @return 帖子列表
     */
    List<TcmPostDTO> getPostsByTitleContaining(String title);
    
    /**
     * 根据标题或内容搜索帖子
     * 
     * @param keyword 搜索关键字
     * @return 帖子列表
     */
    List<TcmPostDTO> searchPostsByTitleOrContent(String keyword);
    
    /**
     * 搜索帖子
     * 
     * @param keyword 搜索关键字
     * @return 帖子列表
     */
    List<TcmPostDTO> searchPosts(String keyword);
    
    /**
     * 搜索帖子（分页）
     * 
     * @param keyword 搜索关键字
     * @param pageable 分页参数
     * @return 分页帖子列表
     */
    Page<TcmPostDTO> searchPosts(String keyword, Pageable pageable);
    
    /**
     * 模糊查询帖子标题
     * 
     * @param title 标题关键字
     * @return 帖子列表
     */
    List<TcmPostDTO> searchPostsByTitle(String title);
    
    /**
     * 更新帖子信息
     * 
     * @param id 帖子ID
     * @param post 帖子信息
     * @return 更新后的帖子信息
     */
    TcmPostDTO updatePost(Long id, TcmPost post);
    
    /**
     * 删除帖子
     * 
     * @param id 帖子ID
     */
    void deletePost(Long id);
    
    /**
     * 批量删除帖子
     * 
     * @param ids 帖子ID列表
     */
    void batchDeletePosts(List<Long> ids);
    
    /**
     * 点赞帖子
     * 
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    void likePost(Long postId, Long userId);
    
    /**
     * 取消点赞帖子
     * 
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    void unlikePost(Long postId, Long userId);
    
    /**
     * 增加帖子浏览量
     * 
     * @param id 帖子ID
     * @return 增加后的浏览量
     */
    long incrementViewCount(Long id);
    
    /**
     * 获取热门帖子列表
     *
     * @param status 帖子状态
     * @param limit 返回数量限制
     * @return 帖子列表
     */
    List<TcmPostDTO> getHotPosts(Integer status, Integer limit);

    /**
     * 获取最新帖子列表
     *
     * @param status 帖子状态
     * @param limit 返回数量限制
     * @return 帖子列表
     */
    List<TcmPostDTO> getLatestPosts(Integer status, Integer limit);
    
    /**
     * 检查用户是否点赞过帖子
     * 
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 是否点赞
     */
    boolean isPostLikedByUser(Long postId, Long userId);
    
    /**
     * 获取用户帖子统计
     * 
     * @param userId 用户ID
     * @return 帖子数量
     */
    long getPostCountByUser(Long userId);
    
    /**
     * 获取板块帖子统计
     * 
     * @param categoryId 板块ID
     * @return 帖子数量
     */
    long getPostCountByCategory(Long categoryId);
    
    /**
     * 获取总帖子数
     * 
     * @return 总帖子数
     */
    long getTotalPostCount();
    
    /**
     * 减少帖子回复数量
     * 
     * @param postId 帖子ID
     */
    void decreaseReplyCount(Long postId);
    
    /**
     * 增加帖子回复数量
     * 
     * @param postId 帖子ID
     */
    void increaseReplyCount(Long postId);
}
