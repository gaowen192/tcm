package com.ride.mapper;

import com.ride.entity.TcmUserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医药论坛用户关注数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmUserFollowRepository extends JpaRepository<TcmUserFollow, Long> {
    
    /**
     * 根据关注者ID和被关注者ID查找关注记录
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 关注记录列表
     */
    List<TcmUserFollow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 根据关注者ID查找关注列表
     * 
     * @param followerId 关注者ID
     * @return 关注列表
     */
    List<TcmUserFollow> findByFollowerId(Long followerId);
    
    /**
     * 根据被关注者ID查找粉丝列表
     * 
     * @param followingId 被关注者ID
     * @return 粉丝列表
     */
    List<TcmUserFollow> findByFollowingId(Long followingId);
    
    /**
     * 检查用户是否关注了另一个用户
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否关注
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 取消关注
     * 
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     */
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * 统计用户的关注数量
     * 
     * @param followerId 关注者ID
     * @return 关注数量
     */
    @Query("SELECT COUNT(uf) FROM TcmUserFollow uf WHERE uf.followerId = :followerId")
    long countByFollowerId(@Param("followerId") Long followerId);
    
    /**
     * 统计用户的粉丝数量
     * 
     * @param followingId 被关注者ID
     * @return 粉丝数量
     */
    @Query("SELECT COUNT(uf) FROM TcmUserFollow uf WHERE uf.followingId = :followingId")
    long countByFollowingId(@Param("followingId") Long followingId);
}
