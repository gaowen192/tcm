package com.ride.service.impl;

import com.ride.dto.TcmUserDTO;
import com.ride.entity.TcmUser;
import com.ride.mapper.TcmUserRepository;
import com.ride.service.TcmUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 医药论坛用户业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmUserServiceImpl implements TcmUserService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmUserServiceImpl.class);
    
    @Autowired
    private TcmUserRepository tcmUserRepository;
    

    
    @Override
    public TcmUserDTO createUser(TcmUser user) {
        log.info("开始创建医药论坛用户：{}", user.getUsername());
        
        // 验证用户名是否已存在
        if (existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在：" + user.getUsername());
        }
        
        // 验证邮箱是否已存在
        if (StringUtils.hasText(user.getEmail()) && existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在：" + user.getEmail());
        }
        
        TcmUser savedUser = tcmUserRepository.save(user);
        log.info("医药论坛用户创建成功：{}", savedUser.getUsername());
        
        return convertToDTO(savedUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmUserDTO getUserById(Long id) {
        log.info("=============== Getting TCM user by ID: {}", id);
        
        // 从数据库查询用户实体，findById默认会从数据库获取最新数据
        TcmUser user = tcmUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmUserDTO getUserByUsername(String username) {
        log.debug("查询医药论坛用户名：{}", username);
        
        TcmUser user = tcmUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在，用户名：" + username));
        
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmUserDTO getUserByEmail(String email) {
        log.debug("查询医药论坛用户邮箱：{}", email);
        
        TcmUser user = tcmUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在，邮箱：" + email));
        
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserDTO> getAllUsers() {
        log.debug("查询所有医药论坛用户");
        
        List<TcmUser> users = tcmUserRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserDTO> getUsersByStatus(Integer status) {
        log.debug("查询状态为{}的医药论坛用户", status);
        
        List<TcmUser> users = tcmUserRepository.findByStatus(status);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserDTO> getUsersByProfession(String profession) {
        log.debug("查询专业为{}的医药论坛用户", profession);
        
        List<TcmUser> users = tcmUserRepository.findByProfession(profession);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserDTO> getUsersByHospital(String hospital) {
        log.debug("查询医院为{}的医药论坛用户", hospital);
        
        List<TcmUser> users = tcmUserRepository.findByHospital(hospital);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TcmUserDTO updateUser(Long id, TcmUser user) {
        log.info("开始更新医药论坛用户ID：{}", id);
        
        // 验证用户是否存在
        TcmUser existingUser = tcmUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在，ID：" + id));
        
        // 验证用户名是否冲突
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在：" + user.getUsername());
        }
        
        // 验证邮箱是否冲突
        if (StringUtils.hasText(user.getEmail()) && 
            !user.getEmail().equals(existingUser.getEmail()) && 
            existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在：" + user.getEmail());
        }
        
        // 只更新传过来的非空字段
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getStatus() != null) {
            existingUser.setStatus(user.getStatus());
        }
        if (user.getRealName() != null) {
            existingUser.setRealName(user.getRealName());
        }
        if (user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }
        if (user.getBirthday() != null) {
            existingUser.setBirthday(user.getBirthday());
        }
        if (user.getProfession() != null) {
            existingUser.setProfession(user.getProfession());
        }
        if (user.getHospital() != null) {
            existingUser.setHospital(user.getHospital());
        }
        if (user.getDepartment() != null) {
            existingUser.setDepartment(user.getDepartment());
        }
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }
        if (user.getTitle() != null) {
            existingUser.setTitle(user.getTitle());
        }
        if (user.getLicenseNumber() != null) {
            existingUser.setLicenseNumber(user.getLicenseNumber());
        }
        if (user.getQualificationLevel() != null) {
            existingUser.setQualificationLevel(user.getQualificationLevel());
        }
        if (user.getUserType() != null) {
            existingUser.setUserType(user.getUserType());
        }
        if (user.getEmailVerified() != null) {
            existingUser.setEmailVerified(user.getEmailVerified());
        }
        if (user.getLastLoginTime() != null) {
            existingUser.setLastLoginTime(user.getLastLoginTime());
        }
        if (user.getLastLoginIp() != null) {
            existingUser.setLastLoginIp(user.getLastLoginIp());
        }
        
        TcmUser updatedUser = tcmUserRepository.save(existingUser);

        log.info("=============== TCM user updated successfully: {}", updatedUser.getUsername());
        return convertToDTO(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        log.info("开始删除医药论坛用户ID：{}", id);
        
        // 验证用户是否存在
        if (!tcmUserRepository.existsById(id)) {
            throw new IllegalArgumentException("用户不存在，ID：" + id);
        }
        
        tcmUserRepository.deleteById(id);
        log.info("医药论坛用户删除成功：{}", id);
    }
    
    @Override
    public void batchDeleteUsers(List<Long> ids) {
        log.info("开始批量删除医药论坛用户，数量：{}", ids.size());
        
        tcmUserRepository.deleteAllById(ids);
        log.info("批量删除医药论坛用户完成，数量：{}", ids.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return tcmUserRepository.findByUsername(username).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return tcmUserRepository.findByEmail(email).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getActiveUserCount() {
        return tcmUserRepository.countActiveUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUserCountByProfession(String profession) {
        return tcmUserRepository.countByProfession(profession);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTop5UsersByPostCount() {
        log.info("=============== Getting top 5 users by post count");
        return tcmUserRepository.findTop5UsersByPostCount();
    }
    
    /**
     * 将TcmUser实体转换为TcmUserDTO
     * 
     * @param user TcmUser实体
     * @return TcmUserDTO
     */
    private TcmUserDTO convertToDTO(TcmUser user) {
        TcmUserDTO dto = new TcmUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRealName(user.getRealName());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setTitle(user.getTitle());
        dto.setGender(user.getGender());
        dto.setBirthday(user.getBirthday());
        dto.setProfession(user.getProfession());
        dto.setHospital(user.getHospital());
        dto.setDepartment(user.getDepartment());
        dto.setLicenseNumber(user.getLicenseNumber());
        dto.setQualificationLevel(user.getQualificationLevel());
        dto.setStatus(user.getStatus());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setUserType(user.getUserType());
        dto.setLastLoginTime(user.getLastLoginTime());
        dto.setLastLoginIp(user.getLastLoginIp());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
