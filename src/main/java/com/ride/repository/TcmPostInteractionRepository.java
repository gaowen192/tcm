package com.ride.repository;

import com.ride.entity.TcmPostInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 帖子互动记录Repository接口
 */
public interface TcmPostInteractionRepository extends JpaRepository<TcmPostInteraction, Long> {

    /**
     * 根据用户ID、帖子ID和互动类型查询记录
     * @param userId 用户ID
     * @param postId 帖子ID
     * @param interactionType 互动类型
     * @return 互动记录
     */
    Optional<TcmPostInteraction> findByUserIdAndPostIdAndInteractionTypeAndIsDeleted(Long userId, 
                                                                                    Long postId, 
                                                                                    String interactionType, 
                                                                                    Integer isDeleted);

    /**
     * 获取用户帖子互动历史
     * @param userId 用户ID
     * @return 互动历史列表
     */
    @Query(value = "SELECT tpi.id, tpi.user_id, tpi.post_id, tpi.interaction_type, tpi.interaction_date, " +
            "tp.title AS post_title, tp.content AS post_content, tp.created_at AS post_create_time " +
            "FROM tcm_post_interaction tpi " +
            "LEFT JOIN tcm_posts tp ON tpi.post_id = tp.id " +
            "WHERE tpi.user_id = :userId AND tpi.is_deleted = 0 " +
            "ORDER BY tpi.interaction_date DESC", 
            nativeQuery = true)
    List<Map<String, Object>> findUserPostHistory(@Param("userId") Long userId);
}