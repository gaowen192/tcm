package com.ride.service;

import com.ride.dto.TcmUserDTO;
import com.ride.entity.TcmUser;

import java.util.List;
import java.util.Map;

/**
 * 医药论坛用户业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmUserService {
    
    /**
     * 创建用户
     * 
     * @param user 用户实体
     * @return 用户信息
     */
    TcmUserDTO createUser(TcmUser user);
    
    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    TcmUserDTO getUserById(Long id);
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    TcmUserDTO getUserByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    TcmUserDTO getUserByEmail(String email);
    
    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    List<TcmUserDTO> getAllUsers();
    
    /**
     * 根据状态查询用户
     * 
     * @param status 状态
     * @return 用户列表
     */
    List<TcmUserDTO> getUsersByStatus(Integer status);
    
    /**
     * 根据专业查询用户
     * 
     * @param profession 专业
     * @return 用户列表
     */
    List<TcmUserDTO> getUsersByProfession(String profession);
    
    /**
     * 根据医院查询用户
     * 
     * @param hospital 医院
     * @return 用户列表
     */
    List<TcmUserDTO> getUsersByHospital(String hospital);
    
    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    TcmUserDTO updateUser(Long id, TcmUser user);
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     */
    void deleteUser(Long id);
    
    /**
     * 批量删除用户
     * 
     * @param ids 用户ID列表
     */
    void batchDeleteUsers(List<Long> ids);
    
    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 获取活跃用户统计
     * 
     * @return 活跃用户数量
     */
    long getActiveUserCount();
    
    /**
     * 根据专业统计用户数量
     * 
     * @param profession 专业
     * @return 用户数量
     */
    long getUserCountByProfession(String profession);
    
    /**
     * 获取发帖数量最多的5个用户信息
     * 
     * @return 包含用户信息和发帖数量的列表
     */
    List<Map<String, Object>> getTop5UsersByPostCount();
}
