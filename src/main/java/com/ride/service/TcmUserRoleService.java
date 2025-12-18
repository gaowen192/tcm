package com.ride.service;

import com.ride.dto.TcmUserRoleDTO;
import com.ride.entity.TcmUserRole;

import java.util.List;

/**
 * 医药论坛用户角色业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmUserRoleService {
    
    /**
     * 创建用户角色
     * 
     * @param userRole 用户角色实体
     * @return 用户角色信息
     */
    TcmUserRoleDTO createUserRole(TcmUserRole userRole);
    
    /**
     * 根据ID查询用户角色
     * 
     * @param id 角色ID
     * @return 用户角色信息
     */
    TcmUserRoleDTO getUserRoleById(Long id);
    
    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<TcmUserRoleDTO> getUserRolesByUserId(Long userId);
    
    /**
     * 根据角色类型查询用户列表
     * 
     * @param roleType 角色类型
     * @return 用户角色列表
     */
    List<TcmUserRoleDTO> getUserRolesByRoleType(String roleType);
    
    /**
     * 根据用户ID和角色类型查询用户列表
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     * @return 用户角色列表
     */
    List<TcmUserRoleDTO> getUserRolesByUserIdAndRoleType(Long userId, String roleType);
    
    /**
     * 根据板块ID查询用户角色列表
     * 
     * @param boardId 板块ID
     * @return 用户角色列表
     */
    List<TcmUserRoleDTO> getUserRolesByBoardId(Long boardId);
    
    /**
     * 根据板块ID查询版主列表
     * 
     * @param boardId 板块ID
     * @return 版主列表
     */
    List<TcmUserRoleDTO> getModeratorsByBoardId(Long boardId);
    
    /**
     * 查询所有管理员
     * 
     * @return 管理员列表
     */
    List<TcmUserRoleDTO> getAdmins();
    
    /**
     * 根据角色类型和板块ID查询用户角色列表
     * 
     * @param roleType 角色类型
     * @param boardId 板块ID
     * @return 用户角色列表
     */
    List<TcmUserRoleDTO> getUserRolesByRoleTypeAndBoardId(String roleType, Long boardId);
    
    /**
     * 查询所有用户角色
     * 
     * @return 用户角色列表
     */
    List<TcmUserRoleDTO> getAllUserRoles();
    
    /**
     * 更新用户角色
     * 
     * @param id 角色ID
     * @param userRole 用户角色信息
     * @return 更新后的用户角色信息
     */
    TcmUserRoleDTO updateUserRole(Long id, TcmUserRole userRole);
    
    /**
     * 删除用户角色
     * 
     * @param id 角色ID
     */
    void deleteUserRole(Long id);
    
    /**
     * 批量删除用户角色
     * 
     * @param ids ID列表
     */
    void batchDeleteUserRoles(List<Long> ids);
    
    /**
     * 移除用户角色
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     */
    void removeUserRole(Long userId, String roleType);
    
    /**
     * 检查用户是否具有特定角色
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     * @return 是否具有角色
     */
    boolean hasUserRole(Long userId, String roleType);
    
    /**
     * 检查用户角色是否存在
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     * @return 是否存在
     */
    boolean existsByUserIdAndRoleType(Long userId, String roleType);
    
    /**
     * 检查用户是否有特定板块的角色
     * 
     * @param userId 用户ID
     * @param boardId 板块ID
     * @return 是否存在
     */
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);
    
    /**
     * 检查用户角色是否存在（包含板块验证）
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     * @param boardId 板块ID
     * @return 是否存在
     */
    boolean existsByUserIdAndRoleType(Long userId, String roleType, Long boardId);
    
    /**
     * 根据用户ID和角色类型删除用户角色
     * 
     * @param userId 用户ID
     * @param roleType 角色类型
     */
    void deleteUserRoleByUserIdAndRoleType(Long userId, String roleType);
    
    /**
     * 删除用户的所有角色
     * 
     * @param userId 用户ID
     */
    void deleteUserRoleByUserId(Long userId);
    
    /**
     * 统计特定角色的用户数量
     * 
     * @param roleType 角色类型
     * @return 用户数量
     */
    long countByRoleType(String roleType);
    
    /**
     * 统计板块的用户数量
     * 
     * @param boardId 板块ID
     * @return 用户数量
     */
    long countByBoardId(Long boardId);
    
    /**
     * 统计板块的版主数量
     * 
     * @param boardId 板块ID
     * @return 版主数量
     */
    long countModeratorsByBoardId(Long boardId);
}
