package com.ride.service.impl;

import com.ride.dto.TcmUserRoleDTO;
import com.ride.entity.TcmUserRole;
import com.ride.mapper.TcmUserRoleRepository;
import com.ride.service.TcmUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医药论坛用户角色业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmUserRoleServiceImpl implements TcmUserRoleService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmUserRoleServiceImpl.class);
    
    @Autowired
    private TcmUserRoleRepository tcmUserRoleRepository;
    
    @Override
    public TcmUserRoleDTO createUserRole(TcmUserRole userRole) {
        log.info("开始创建用户角色：用户ID {} 角色类型 {}", userRole.getUserId(), userRole.getRoleType());
        
        // 验证用户角色是否已存在
        if (existsByUserIdAndRoleType(userRole.getUserId(), userRole.getRoleType())) {
            throw new IllegalArgumentException("用户角色已存在：用户ID " + userRole.getUserId() + " 角色类型 " + userRole.getRoleType());
        }
        
        TcmUserRole savedUserRole = tcmUserRoleRepository.save(userRole);
        log.info("用户角色创建成功：{}", savedUserRole.getId());
        
        return convertToDTO(savedUserRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmUserRoleDTO getUserRoleById(Long id) {
        log.debug("查询用户角色ID：{}", id);
        
        TcmUserRole userRole = tcmUserRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户角色不存在，ID：" + id));
        
        return convertToDTO(userRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getUserRolesByUserId(Long userId) {
        log.debug("查询用户ID的角色列表：{}", userId);
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByUserId(userId);
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getUserRolesByRoleType(String roleType) {
        log.debug("查询角色类型的用户角色列表：{}", roleType);
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByRoleType(roleType);
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getUserRolesByUserIdAndRoleType(Long userId, String roleType) {
        log.debug("查询用户ID和角色类型的用户角色列表：{} - {}", userId, roleType);
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByUserIdAndRoleType(userId, roleType);
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getUserRolesByBoardId(Long boardId) {
        log.debug("查询板块ID的用户角色列表：{}", boardId);
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByBoardId(boardId);
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getModeratorsByBoardId(Long boardId) {
        log.debug("查询板块ID的版主列表：{}", boardId);
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByBoardIdAndRoleType(boardId, "MODERATOR");
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getAdmins() {
        log.debug("查询所有管理员角色");
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByRoleType("ADMIN");
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getUserRolesByRoleTypeAndBoardId(String roleType, Long boardId) {
        log.debug("查询角色类型和板块ID的用户角色列表：{} - {}", roleType, boardId);
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByRoleTypeAndBoardId(roleType, boardId);
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TcmUserRoleDTO updateUserRole(Long id, TcmUserRole userRole) {
        log.info("开始更新用户角色ID：{}", id);
        
        // 验证用户角色是否存在
        TcmUserRole existingUserRole = tcmUserRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户角色不存在，ID：" + id));
        
        // 验证用户角色是否冲突（排除当前记录）
        if (!existingUserRole.getUserId().equals(userRole.getUserId()) || 
            !existingUserRole.getRoleType().equals(userRole.getRoleType())) {
            if (existsByUserIdAndRoleType(userRole.getUserId(), userRole.getRoleType())) {
                throw new IllegalArgumentException("用户角色已存在：用户ID " + userRole.getUserId() + " 角色类型 " + userRole.getRoleType());
            }
        }
        
        // 设置ID并更新
        userRole.setId(id);
        userRole.setCreatedAt(existingUserRole.getCreatedAt());
        TcmUserRole updatedUserRole = tcmUserRoleRepository.save(userRole);
        
        log.info("用户角色更新成功：{}", updatedUserRole.getId());
        return convertToDTO(updatedUserRole);
    }
    
    @Override
    public void deleteUserRole(Long id) {
        log.info("开始删除用户角色ID：{}", id);
        
        // 验证用户角色是否存在
        if (!tcmUserRoleRepository.existsById(id)) {
            throw new IllegalArgumentException("用户角色不存在，ID：" + id);
        }
        
        tcmUserRoleRepository.deleteById(id);
        log.info("用户角色删除成功：{}", id);
    }
    
    @Override
    public void batchDeleteUserRoles(List<Long> ids) {
        log.info("开始批量删除用户角色，数量：{}", ids.size());
        
        tcmUserRoleRepository.deleteAllById(ids);
        log.info("批量删除用户角色完成，数量：{}", ids.size());
    }
    
    @Override
    public void deleteUserRoleByUserIdAndRoleType(Long userId, String roleType) {
        log.info("开始删除用户ID和角色类型的用户角色：{} - {}", userId, roleType);
        
        tcmUserRoleRepository.deleteByUserIdAndRoleType(userId, roleType);
        log.info("删除用户ID和角色类型的用户角色完成：{} - {}", userId, roleType);
    }
    
    @Override
    public void deleteUserRoleByUserId(Long userId) {
        log.info("开始删除用户ID的所有角色：{}", userId);
        
        tcmUserRoleRepository.deleteByUserId(userId);
        log.info("删除用户ID的所有角色完成：{}", userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndRoleType(Long userId, String roleType) {
        return tcmUserRoleRepository.findFirstByUserIdAndRoleType(userId, roleType).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndBoardId(Long userId, Long boardId) {
        return tcmUserRoleRepository.findByUserIdAndBoardId(userId, boardId).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByRoleType(String roleType) {
        return tcmUserRoleRepository.countByRoleType(roleType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByBoardId(Long boardId) {
        return tcmUserRoleRepository.countByBoardId(boardId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countModeratorsByBoardId(Long boardId) {
        return tcmUserRoleRepository.countModeratorsByBoardId(boardId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserRoleDTO> getAllUserRoles() {
        log.debug("查询所有用户角色");
        
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findAll();
        return userRoles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void removeUserRole(Long userId, String roleType) {
        log.info("开始移除用户角色：用户ID {} 角色类型 {}", userId, roleType);
        
        deleteUserRoleByUserIdAndRoleType(userId, roleType);
        log.info("移除用户角色完成：用户ID {} 角色类型 {}", userId, roleType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserRole(Long userId, String roleType) {
        return existsByUserIdAndRoleType(userId, roleType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndRoleType(Long userId, String roleType, Long boardId) {
        List<TcmUserRole> userRoles = tcmUserRoleRepository.findByUserIdAndRoleType(userId, roleType);
        return userRoles.stream()
                .anyMatch(ur -> ur.getBoardId() != null && ur.getBoardId().equals(boardId));
    }
    
    /**
     * 将TcmUserRole实体转换为TcmUserRoleDTO
     * 
     * @param userRole TcmUserRole实体
     * @return TcmUserRoleDTO
     */
    private TcmUserRoleDTO convertToDTO(TcmUserRole userRole) {
        TcmUserRoleDTO dto = new TcmUserRoleDTO();
        dto.setId(userRole.getId());
        dto.setUserId(userRole.getUserId());
        dto.setRoleType(userRole.getRoleType());
        dto.setBoardId(userRole.getBoardId());
        dto.setCreatedAt(userRole.getCreatedAt());
        dto.setUpdatedAt(userRole.getUpdatedAt());
        return dto;
    }
}
