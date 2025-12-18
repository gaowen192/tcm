package com.ride.mapper;

import com.ride.entity.TcmCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 医药论坛板块数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmCategoryRepository extends JpaRepository<TcmCategory, Long> {
    
    /**
     * 根据板块名称查找
     * 
     * @param name 板块名称
     * @return 板块信息
     */
    Optional<TcmCategory> findByName(String name);
    
    /**
     * 根据状态查找板块列表
     * 
     * @param status 状态
     * @return 板块列表
     */
    List<TcmCategory> findByStatus(Integer status);
    
    /**
     * 根据排序查找已启用板块列表
     * 
     * @param status 状态
     * @return 板块列表
     */
    List<TcmCategory> findByStatusOrderBySortOrder(Integer status);
    
    /**
     * 根据描述模糊查询
     * 
     * @param description 描述关键字
     * @return 板块列表
     */
    @Query("SELECT c FROM TcmCategory c WHERE c.description LIKE CONCAT('%', :description, '%') AND c.status = 1")
    List<TcmCategory> findByDescriptionLike(@Param("description") String description);
    
    /**
     * 统计板块下的帖子数量
     * 
     * @param categoryId 板块ID
     * @return 帖子数量
     */
    @Query("SELECT COUNT(p) FROM TcmPost p WHERE p.categoryId = :categoryId AND p.status = 1")
    long countPostsByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * 统计已启用的板块数量
     * 
     * @param status 状态
     * @return 板块数量
     */
    @Query("SELECT COUNT(c) FROM TcmCategory c WHERE c.status = :status")
    long countByStatus(@Param("status") Integer status);
}
