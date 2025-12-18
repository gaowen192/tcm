package com.ride.mapper;

import com.ride.entity.TcmUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 医药论坛用户角色数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmUserRoleRepository extends JpaRepository<TcmUserRole, Long> {
    
    /**
     * 根据用户ID查找角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<TcmUserRole> findByUserId(Long userId);
    
    /**
     * 根据角色类型查找用户列表
     * 
     * @param roleType 角色类型
     * @return 用户角色列表
     */
    List<TcmUserRole> findByRoleType(String roleType);
    
    /**
     * 根据角色类型和板块ID查找用户角色列表
     * 
     * @param roleType 角色类型
     * @param boardId 板块ID
     * @return 用户角色列表
     */
    List<TcmUserRole> findByRoleTypeAndBoardId(String roleType, Long boardId);
    
    /**
     * 根据用户ID和角色类型查找
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     * @return 用户角色列表
     */
    List<TcmUserRole> findByUserIdAndRoleType(Long userId, String roleType);
    
    /**
     * 根据用户ID和角色类型查找单个记录
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     * @return 用户角色
     */
    Optional<TcmUserRole> findFirstByUserIdAndRoleType(Long userId, String roleType);
    
    /**
     * 根据板块ID查找版主列表
     * 
     * @param boardId 板块ID
     * @return 版主列表
     */
    List<TcmUserRole> findByBoardId(Long boardId);
    
    /**
     * 根据板块ID和角色类型查找用户角色列表
     * 
     * @param boardId 板块ID
     * @param roleType 角色类型
     * @return 用户角色列表
     */
    List<TcmUserRole> findByBoardIdAndRoleType(Long boardId, String roleType);
    
    /**
     * 根据用户ID和板块ID查找
     * 
     * @param userId 用户ID
     * @param boardId 板块ID
     * @return 用户角色
     */
    Optional<TcmUserRole> findByUserIdAndBoardId(Long userId, Long boardId);
    
    /**
     * 根据用户ID和角色类型删除
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     */
    void deleteByUserIdAndRoleType(Long userId, String roleType);
    
    /**
     * 根据用户ID删除所有角色
     * 
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);
    
    /**
     * 统计特定角色的用户数量
     * 
     * @param roleType 角色类型
     * @return 用户数量
     */
    long countByRoleType(String roleType);
    
    /**
     * 统计特定板块的用户数量
     * 
     * @param boardId 板块ID
     * @return 用户数量
     */
    long countByBoardId(Long boardId);
    
    /**
     * 统计特定板块的版主数量
     * 
     * @param boardId 板块ID
     * @return 版主数量
     */
    @Query("SELECT COUNT(ur) FROM TcmUserRole ur WHERE ur.boardId = :boardId AND ur.roleType = 'MODERATOR'")
    long countModeratorsByBoardId(@Param("boardId") Long boardId);
}
