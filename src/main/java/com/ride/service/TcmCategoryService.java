package com.ride.service;

import com.ride.dto.TcmCategoryDTO;
import com.ride.entity.TcmCategory;

import java.util.List;

/**
 * 医药论坛板块业务逻辑接口
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public interface TcmCategoryService {
    
    /**
     * 创建板块
     * 
     * @param category 板块实体
     * @return 板块信息
     */
    TcmCategoryDTO createCategory(TcmCategory category);
    
    /**
     * 根据ID查询板块
     * 
     * @param id 板块ID
     * @return 板块信息
     */
    TcmCategoryDTO getCategoryById(Long id);
    
    /**
     * 根据名称查询板块
     * 
     * @param name 板块名称
     * @return 板块信息
     */
    TcmCategoryDTO getCategoryByName(String name);
    
    /**
     * 查询所有板块
     * 
     * @return 板块列表
     */
    List<TcmCategoryDTO> getAllCategories();
    
    /**
     * 根据状态查询板块
     * 
     * @param status 状态
     * @return 板块列表
     */
    List<TcmCategoryDTO> getCategoriesByStatus(Integer status);
    
    /**
     * 查询已启用的板块（按排序）
     * 
     * @param status 状态
     * @return 板块列表
     */
    List<TcmCategoryDTO> getEnabledCategories(Integer status);
    
    /**
     * 更新板块信息
     * 
     * @param id 板块ID
     * @param category 板块信息
     * @return 更新后的板块信息
     */
    TcmCategoryDTO updateCategory(Long id, TcmCategory category);
    
    /**
     * 删除板块
     * 
     * @param id 板块ID
     */
    void deleteCategory(Long id);
    
    /**
     * 批量删除板块
     * 
     * @param ids 板块ID列表
     */
    void batchDeleteCategories(List<Long> ids);
    
    /**
     * 检查板块名称是否存在
     * 
     * @param name 板块名称
     * @return 是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 获取板块的帖子数量
     * 
     * @param categoryId 板块ID
     * @return 帖子数量
     */
    long getPostCountByCategory(Long categoryId);
    
    /**
     * 获取已启用板块统计
     * 
     * @param status 状态
     * @return 板块数量
     */
    long getEnabledCategoryCount(Integer status);
}
