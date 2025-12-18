package com.ride.service.impl;

import com.ride.dto.TcmVideoWatchRecordDTO;
import com.ride.entity.TcmVideoWatchRecord;
import com.ride.mapper.TcmVideoWatchRecordRepository;
import com.ride.service.TcmVideoWatchRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * TCM Video watch record service implementation
 */
@Service
public class TcmVideoWatchRecordServiceImpl implements TcmVideoWatchRecordService {

    private static final Logger log = LoggerFactory.getLogger(TcmVideoWatchRecordServiceImpl.class);

    @Autowired
    private TcmVideoWatchRecordRepository tcmVideoWatchRecordRepository;

    @Override
    @Transactional
    public TcmVideoWatchRecord saveOrUpdateRecord(TcmVideoWatchRecord record) {
        log.debug("=============== Saving or updating watch record for user {}, video {}, type: {}", record.getUserId(), record.getVideoId(), record.getRecodeType());
        
        // Ensure recodeType has default value if not provided
        if (record.getRecodeType() == null) {
            record.setRecodeType("watch");
        }
        
        // Try to find existing record with same user, video and recodeType
        Optional<TcmVideoWatchRecord> existingRecord = tcmVideoWatchRecordRepository.findByUserIdAndVideoIdAndRecodeType(
                record.getUserId(), record.getVideoId(), record.getRecodeType());
        
        if (existingRecord.isPresent()) {
            // Update existing record's watch date
            TcmVideoWatchRecord updatedRecord = existingRecord.get();
            updatedRecord.setWatchDate(record.getWatchDate());
            log.debug("=============== Updated existing watch record with ID {}", updatedRecord.getId());
            return tcmVideoWatchRecordRepository.save(updatedRecord);
        } else {
            // Create new record for this type
            log.debug("=============== Created new watch record for type: {}", record.getRecodeType());
            return tcmVideoWatchRecordRepository.save(record);
        }
    }

    @Override
    @Transactional
    public TcmVideoWatchRecordDTO saveOrUpdateRecord(TcmVideoWatchRecordDTO dto) {
        log.debug("=============== Saving or updating watch record DTO for user {} and video {}", dto.getUserId(), dto.getVideoId());
        
        // Convert DTO to entity
        TcmVideoWatchRecord record = convertToEntity(dto);
        
        // Save or update record
        TcmVideoWatchRecord savedRecord = saveOrUpdateRecord(record);
        
        // Convert back to DTO and return
        return convertToDTO(savedRecord);
    }

    @Override
    public Optional<TcmVideoWatchRecord> getRecordById(Long id) {
        log.debug("=============== Getting watch record by ID: {}", id);
        return tcmVideoWatchRecordRepository.findById(id);
    }

    @Override
    public Optional<TcmVideoWatchRecordDTO> getRecordDTOById(Long id) {
        log.debug("=============== Getting watch record DTO by ID: {}", id);
        return tcmVideoWatchRecordRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<TcmVideoWatchRecord> getRecordsByUserId(Long userId) {
        log.debug("=============== Getting watch records by user ID: {}", userId);
        return tcmVideoWatchRecordRepository.findByUserId(userId);
    }

    @Override
    public List<TcmVideoWatchRecordDTO> getRecordDTOsByUserId(Long userId) {
        log.debug("=============== Getting watch record DTOs by user ID: {}", userId);
        return tcmVideoWatchRecordRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TcmVideoWatchRecord> getRecordsByVideoId(Long videoId) {
        log.debug("=============== Getting watch records by video ID: {}", videoId);
        return tcmVideoWatchRecordRepository.findByVideoId(videoId);
    }

    @Override
    public List<TcmVideoWatchRecordDTO> getRecordDTOsByVideoId(Long videoId) {
        log.debug("=============== Getting watch record DTOs by video ID: {}", videoId);
        return tcmVideoWatchRecordRepository.findByVideoId(videoId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TcmVideoWatchRecord> getRecordByUserIdAndVideoId(Long userId, Long videoId) {
        log.debug("=============== Getting watch record by user ID {} and video ID {}", userId, videoId);
        return tcmVideoWatchRecordRepository.findByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public Optional<TcmVideoWatchRecordDTO> getRecordDTOByUserIdAndVideoId(Long userId, Long videoId) {
        log.debug("=============== Getting watch record DTO by user ID {} and video ID {}", userId, videoId);
        return tcmVideoWatchRecordRepository.findByUserIdAndVideoId(userId, videoId)
                .map(this::convertToDTO);
    }

    @Override
    public List<TcmVideoWatchRecord> getRecordsByUserIdAndRecodeType(Long userId, String recodeType) {
        log.debug("=============== Getting records by user ID {} and record type {}", userId, recodeType);
        return tcmVideoWatchRecordRepository.findByUserIdAndRecodeType(userId, recodeType);
    }

    @Override
    public List<TcmVideoWatchRecordDTO> getRecordDTOsByUserIdAndRecodeType(Long userId, String recodeType) {
        log.debug("=============== Getting record DTOs by user ID {} and record type {}", userId, recodeType);
        return tcmVideoWatchRecordRepository.findByUserIdAndRecodeType(userId, recodeType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TcmVideoWatchRecord> getRecordsByVideoIdAndRecodeType(Long videoId, String recodeType) {
        log.debug("=============== Getting records by video ID {} and record type {}", videoId, recodeType);
        return tcmVideoWatchRecordRepository.findByVideoIdAndRecodeType(videoId, recodeType);
    }

    @Override
    public List<TcmVideoWatchRecordDTO> getRecordDTOsByVideoIdAndRecodeType(Long videoId, String recodeType) {
        log.debug("=============== Getting record DTOs by video ID {} and record type {}", videoId, recodeType);
        return tcmVideoWatchRecordRepository.findByVideoIdAndRecodeType(videoId, recodeType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TcmVideoWatchRecord> getRecordByUserIdAndVideoIdAndRecodeType(Long userId, Long videoId, String recodeType) {
        log.debug("=============== Getting record by user ID {}, video ID {} and record type {}", userId, videoId, recodeType);
        return tcmVideoWatchRecordRepository.findByUserIdAndVideoIdAndRecodeType(userId, videoId, recodeType);
    }

    @Override
    public Optional<TcmVideoWatchRecordDTO> getRecordDTOByUserIdAndVideoIdAndRecodeType(Long userId, Long videoId, String recodeType) {
        log.debug("=============== Getting record DTO by user ID {}, video ID {} and record type {}", userId, videoId, recodeType);
        return getRecordByUserIdAndVideoIdAndRecodeType(userId, videoId, recodeType).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public void deleteRecordById(Long id) {
        log.debug("=============== Deleting watch record by ID: {}", id);
        tcmVideoWatchRecordRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int deleteRecordsByUserId(Long userId) {
        log.debug("=============== Deleting watch records by user ID: {}", userId);
        return tcmVideoWatchRecordRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public int deleteRecordsByVideoId(Long videoId) {
        log.debug("=============== Deleting watch records by video ID: {}", videoId);
        return tcmVideoWatchRecordRepository.deleteByVideoId(videoId);
    }

    @Override
    @Transactional
    public int deleteRecordByUserIdAndVideoId(Long userId, Long videoId) {
        log.debug("=============== Deleting records by user ID: {} and video ID: {}", userId, videoId);
        
        // First check if record exists
        Optional<TcmVideoWatchRecord> record = tcmVideoWatchRecordRepository.findByUserIdAndVideoId(userId, videoId);
        if (record.isPresent()) {
            // Delete record
            tcmVideoWatchRecordRepository.deleteById(record.get().getId());
            log.debug("=============== Deleted record with ID: {}", record.get().getId());
            return 1;
        } else {
            // No record found
            log.debug("=============== No record found for user ID: {} and video ID: {}", userId, videoId);
            return 0;
        }
    }

    @Override
    public List<Map<String, Object>> getUserVideoHistoryWithDetails(Long userId) {
        log.debug("=============== Getting user video history with details for user ID: {}", userId);
        return tcmVideoWatchRecordRepository.findUserVideoHistoryWithDetails(userId);
    }

    @Override
    public Map<String, Object> getUserVideoHistoryWithDetails(Long userId, int page, int pageSize) {
        log.debug("=============== Getting user video history with details for user ID: {}, page: {}, pageSize: {}", userId, page, pageSize);
        
        // Convert page from 1-based to 0-based
        int adjustedPage = Math.max(0, page - 1);
        
        // Create Pageable object
        Pageable pageable = PageRequest.of(adjustedPage, pageSize);
        
        // Get paginated results from repository
        org.springframework.data.domain.Page<Map<String, Object>> paginatedResults = 
            tcmVideoWatchRecordRepository.findUserVideoHistoryWithDetails(userId, pageable);
        
        // Build response map with pagination info
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("content", paginatedResults.getContent());
        response.put("totalElements", paginatedResults.getTotalElements());
        response.put("totalPages", paginatedResults.getTotalPages());
        response.put("currentPage", paginatedResults.getNumber());
        response.put("pageSize", paginatedResults.getSize());
        response.put("hasNext", paginatedResults.hasNext());
        response.put("hasPrevious", paginatedResults.hasPrevious());
        
        return response;
    }

    /**
     * Convert entity to DTO
     */
    private TcmVideoWatchRecordDTO convertToDTO(TcmVideoWatchRecord record) {
        TcmVideoWatchRecordDTO dto = new TcmVideoWatchRecordDTO();
        dto.setId(record.getId());
        dto.setUserId(record.getUserId());
        dto.setVideoId(record.getVideoId());
        dto.setRecodeType(record.getRecodeType());
        dto.setWatchDate(record.getWatchDate());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        return dto;
    }

    /**
     * Convert DTO to entity
     */
    private TcmVideoWatchRecord convertToEntity(TcmVideoWatchRecordDTO dto) {
        TcmVideoWatchRecord record = new TcmVideoWatchRecord();
        record.setId(dto.getId());
        record.setUserId(dto.getUserId());
        record.setVideoId(dto.getVideoId());
        record.setRecodeType(dto.getRecodeType());
        record.setWatchDate(dto.getWatchDate());
        record.setCreatedAt(dto.getCreatedAt());
        record.setUpdatedAt(dto.getUpdatedAt());
        return record;
    }
}
