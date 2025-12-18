package com.ride.service.impl;

import com.ride.dto.TcmCategoryDTO;
import com.ride.entity.TcmCategory;
import com.ride.mapper.TcmCategoryRepository;
import com.ride.service.TcmCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医药论坛板块业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmCategoryServiceImpl implements TcmCategoryService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmCategoryServiceImpl.class);
    
    @Autowired
    private TcmCategoryRepository tcmCategoryRepository;
    
    @Override
    public TcmCategoryDTO createCategory(TcmCategory category) {
        log.info("开始创建医药论坛板块：{}", category.getName());
        
        // 验证板块名称是否已存在
        if (existsByName(category.getName())) {
            throw new IllegalArgumentException("板块名称已存在：" + category.getName());
        }
        
        TcmCategory savedCategory = tcmCategoryRepository.save(category);
        log.info("医药论坛板块创建成功：{}", savedCategory.getName());
        
        return convertToDTO(savedCategory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmCategoryDTO getCategoryById(Long id) {
        log.debug("查询医药论坛板块ID：{}", id);
        
        TcmCategory category = tcmCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("板块不存在，ID：" + id));
        
        return convertToDTO(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmCategoryDTO getCategoryByName(String name) {
        log.debug("查询医药论坛板块名称：{}", name);
        
        TcmCategory category = tcmCategoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("板块不存在，名称：" + name));
        
        return convertToDTO(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmCategoryDTO> getAllCategories() {
        log.debug("查询所有医药论坛板块");
        
        List<TcmCategory> categories = tcmCategoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmCategoryDTO> getCategoriesByStatus(Integer status) {
        log.debug("查询状态为{}的医药论坛板块", status);
        
        List<TcmCategory> categories = tcmCategoryRepository.findByStatus(status);
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmCategoryDTO> getEnabledCategories(Integer status) {
        log.debug("查询已启用的医药论坛板块");
        
        List<TcmCategory> categories = tcmCategoryRepository.findByStatusOrderBySortOrder(status);
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TcmCategoryDTO updateCategory(Long id, TcmCategory category) {
        log.info("开始更新医药论坛板块ID：{}", id);
        
        // 验证板块是否存在
        TcmCategory existingCategory = tcmCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("板块不存在，ID：" + id));
        
        // 验证板块名称是否冲突
        if (!existingCategory.getName().equals(category.getName()) && 
            existsByName(category.getName())) {
            throw new IllegalArgumentException("板块名称已存在：" + category.getName());
        }
        
        // 设置ID并更新
        category.setId(id);
        category.setCreatedAt(existingCategory.getCreatedAt());
        TcmCategory updatedCategory = tcmCategoryRepository.save(category);
        
        log.info("医药论坛板块更新成功：{}", updatedCategory.getName());
        return convertToDTO(updatedCategory);
    }
    
    @Override
    public void deleteCategory(Long id) {
        log.info("开始删除医药论坛板块ID：{}", id);
        
        // 验证板块是否存在
        if (!tcmCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("板块不存在，ID：" + id);
        }
        
        tcmCategoryRepository.deleteById(id);
        log.info("医药论坛板块删除成功：{}", id);
    }
    
    @Override
    public void batchDeleteCategories(List<Long> ids) {
        log.info("开始批量删除医药论坛板块，数量：{}", ids.size());
        
        tcmCategoryRepository.deleteAllById(ids);
        log.info("批量删除医药论坛板块完成，数量：{}", ids.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return tcmCategoryRepository.findByName(name).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPostCountByCategory(Long categoryId) {
        return tcmCategoryRepository.countPostsByCategory(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getEnabledCategoryCount(Integer status) {
        return tcmCategoryRepository.countByStatus(status);
    }
    
    /**
     * 将TcmCategory实体转换为TcmCategoryDTO
     * 
     * @param category TcmCategory实体
     * @return TcmCategoryDTO
     */
    private TcmCategoryDTO convertToDTO(TcmCategory category) {
        TcmCategoryDTO dto = new TcmCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setIcon(category.getIcon());
        dto.setSortOrder(category.getSortOrder());
        dto.setStatus(category.getStatus());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}
