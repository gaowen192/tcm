package com.ride.service.impl;

import com.ride.dto.TcmUserFollowDTO;
import com.ride.entity.TcmUserFollow;
import com.ride.mapper.TcmUserFollowRepository;
import com.ride.service.TcmUserFollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医药论坛用户关注业务逻辑实现类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Service
@Transactional
public class TcmUserFollowServiceImpl implements TcmUserFollowService {
    
    private static final Logger log = LoggerFactory.getLogger(TcmUserFollowServiceImpl.class);
    
    @Autowired
    private TcmUserFollowRepository tcmUserFollowRepository;
    
    @Override
    public TcmUserFollowDTO createUserFollow(TcmUserFollow userFollow) {
        log.info("开始创建用户关注：关注者ID {} 被关注者ID {}", userFollow.getFollowerId(), userFollow.getFollowingId());
        
        // 验证是否已经关注
        if (existsByFollowerIdAndFollowingId(userFollow.getFollowerId(), userFollow.getFollowingId())) {
            throw new IllegalArgumentException("用户已经关注该用户：关注者ID " + userFollow.getFollowerId() + " 被关注者ID " + userFollow.getFollowingId());
        }
        
        // 不能关注自己
        if (userFollow.getFollowerId().equals(userFollow.getFollowingId())) {
            throw new IllegalArgumentException("不能关注自己");
        }
        
        TcmUserFollow savedUserFollow = tcmUserFollowRepository.save(userFollow);
        log.info("用户关注创建成功：{}", savedUserFollow.getId());
        
        return convertToDTO(savedUserFollow);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmUserFollowDTO getUserFollowById(Long id) {
        log.debug("查询用户关注ID：{}", id);
        
        TcmUserFollow userFollow = tcmUserFollowRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户关注不存在，ID：" + id));
        
        return convertToDTO(userFollow);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserFollowDTO> getUserFollowsByFollowerId(Long followerId) {
        log.debug("查询关注者ID的关注列表：{}", followerId);
        
        List<TcmUserFollow> userFollows = tcmUserFollowRepository.findByFollowerId(followerId);
        return userFollows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserFollowDTO> getUserFollowsByFollowingId(Long followingId) {
        log.debug("查询被关注者ID的粉丝列表：{}", followingId);
        
        List<TcmUserFollow> userFollows = tcmUserFollowRepository.findByFollowingId(followingId);
        return userFollows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserFollowDTO> getUserFollowsByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        log.debug("查询关注者ID和被关注者ID的关注记录：{} - {}", followerId, followingId);
        
        List<TcmUserFollow> userFollows = tcmUserFollowRepository.findByFollowerId(followerId).stream()
                .filter(uf -> uf.getFollowingId().equals(followingId))
                .collect(Collectors.toList());
        return userFollows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public TcmUserFollowDTO getUserFollowByFollowerAndFollowed(Long followerId, Long followingId) {
        log.debug("查询关注者ID和被关注者ID的单条关注记录：{} - {}", followerId, followingId);
        
        List<TcmUserFollow> userFollows = tcmUserFollowRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (userFollows.isEmpty()) {
            return null;
        }
        return convertToDTO(userFollows.get(0));
    }
    
    @Override
    public void deleteUserFollow(Long id) {
        log.info("开始删除用户关注ID：{}", id);
        
        // 验证用户关注是否存在
        if (!tcmUserFollowRepository.existsById(id)) {
            throw new IllegalArgumentException("用户关注不存在，ID：" + id);
        }
        
        tcmUserFollowRepository.deleteById(id);
        log.info("用户关注删除成功：{}", id);
    }
    
    @Override
    public void deleteUserFollowByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        log.info("开始删除关注者ID和被关注者ID的关注：{} - {}", followerId, followingId);
        
        tcmUserFollowRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        log.info("删除关注者ID和被关注者ID的关注完成：{} - {}", followerId, followingId);
    }
    
    @Override
    public void unfollow(Long followerId, Long followingId) {
        log.info("用户{}取消关注用户{}", followerId, followingId);
        
        deleteUserFollowByFollowerIdAndFollowingId(followerId, followingId);
        log.info("用户{}取消关注用户{}成功", followerId, followingId);
    }
    
    @Override
    public void batchDeleteUserFollows(List<Long> ids) {
        log.info("开始批量删除用户关注，数量：{}", ids.size());
        
        tcmUserFollowRepository.deleteAllById(ids);
        log.info("批量删除用户关注完成，数量：{}", ids.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return tcmUserFollowRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return tcmUserFollowRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getFollowCount(Long userId) {
        return tcmUserFollowRepository.countByFollowerId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getFollowerCount(Long userId) {
        return tcmUserFollowRepository.countByFollowingId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUserFollowCount(Long followerId) {
        return tcmUserFollowRepository.countByFollowerId(followerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUserFansCount(Long followingId) {
        return tcmUserFollowRepository.countByFollowingId(followingId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TcmUserFollowDTO> getAllUserFollows() {
        log.debug("查询所有用户关注记录");
        
        List<TcmUserFollow> userFollows = tcmUserFollowRepository.findAll();
        return userFollows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将TcmUserFollow实体转换为TcmUserFollowDTO
     * 
     * @param userFollow TcmUserFollow实体
     * @return TcmUserFollowDTO
     */
    private TcmUserFollowDTO convertToDTO(TcmUserFollow userFollow) {
        TcmUserFollowDTO dto = new TcmUserFollowDTO();
        dto.setId(userFollow.getId());
        dto.setFollowerId(userFollow.getFollowerId());
        dto.setFollowingId(userFollow.getFollowingId());
        dto.setCreatedAt(userFollow.getCreatedAt());
        return dto;
    }
}
