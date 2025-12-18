package com.ride.service;

import com.ride.dto.TcmVideoWatchRecordDTO;
import com.ride.entity.TcmVideoWatchRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TCM Video watch record service interface
 */
public interface TcmVideoWatchRecordService {

    /**
     * Save or update watch record
     * If a record with the same user ID and video ID exists, it will be updated
     * Otherwise, a new record will be created
     *
     * @param record TCM Video watch record
     * @return Saved or updated record
     */
    TcmVideoWatchRecord saveOrUpdateRecord(TcmVideoWatchRecord record);

    /**
     * Save or update watch record using DTO
     *
     * @param dto TCM Video watch record DTO
     * @return Saved or updated DTO
     */
    TcmVideoWatchRecordDTO saveOrUpdateRecord(TcmVideoWatchRecordDTO dto);

    /**
     * Get watch record by ID
     *
     * @param id Record ID
     * @return Optional record
     */
    Optional<TcmVideoWatchRecord> getRecordById(Long id);

    /**
     * Get watch record by ID and convert to DTO
     *
     * @param id Record ID
     * @return Optional DTO
     */
    Optional<TcmVideoWatchRecordDTO> getRecordDTOById(Long id);

    /**
     * Get watch records by user ID
     *
     * @param userId User ID
     * @return List of records
     */
    List<TcmVideoWatchRecord> getRecordsByUserId(Long userId);

    /**
     * Get watch records by user ID and convert to DTO
     *
     * @param userId User ID
     * @return List of DTOs
     */
    List<TcmVideoWatchRecordDTO> getRecordDTOsByUserId(Long userId);

    /**
     * Get watch records by video ID
     *
     * @param videoId Video ID
     * @return List of records
     */
    List<TcmVideoWatchRecord> getRecordsByVideoId(Long videoId);

    /**
     * Get watch records by video ID and convert to DTO
     *
     * @param videoId Video ID
     * @return List of DTOs
     */
    List<TcmVideoWatchRecordDTO> getRecordDTOsByVideoId(Long videoId);

    /**
     * Get watch record by user ID and video ID
     *
     * @param userId  User ID
     * @param videoId Video ID
     * @return Optional record
     */
    Optional<TcmVideoWatchRecord> getRecordByUserIdAndVideoId(Long userId, Long videoId);

    /**
     * Get watch record by user ID and video ID and convert to DTO
     *
     * @param userId  User ID
     * @param videoId Video ID
     * @return Optional DTO
     */
    Optional<TcmVideoWatchRecordDTO> getRecordDTOByUserIdAndVideoId(Long userId, Long videoId);

    /**
     * Get records by user ID and record type
     *
     * @param userId User ID
     * @param recodeType Record type
     * @return List of records
     */
    List<TcmVideoWatchRecord> getRecordsByUserIdAndRecodeType(Long userId, String recodeType);

    /**
     * Get records by user ID and record type and convert to DTO
     *
     * @param userId User ID
     * @param recodeType Record type
     * @return List of DTOs
     */
    List<TcmVideoWatchRecordDTO> getRecordDTOsByUserIdAndRecodeType(Long userId, String recodeType);

    /**
     * Get records by video ID and record type
     *
     * @param videoId Video ID
     * @param recodeType Record type
     * @return List of records
     */
    List<TcmVideoWatchRecord> getRecordsByVideoIdAndRecodeType(Long videoId, String recodeType);

    /**
     * Get records by video ID and record type and convert to DTO
     *
     * @param videoId Video ID
     * @param recodeType Record type
     * @return List of DTOs
     */
    List<TcmVideoWatchRecordDTO> getRecordDTOsByVideoIdAndRecodeType(Long videoId, String recodeType);

    /**
     * Get record by user ID, video ID and record type
     *
     * @param userId User ID
     * @param videoId Video ID
     * @param recodeType Record type
     * @return Optional record
     */
    Optional<TcmVideoWatchRecord> getRecordByUserIdAndVideoIdAndRecodeType(Long userId, Long videoId, String recodeType);

    /**
     * Get record by user ID, video ID and record type and convert to DTO
     *
     * @param userId User ID
     * @param videoId Video ID
     * @param recodeType Record type
     * @return Optional DTO
     */
    Optional<TcmVideoWatchRecordDTO> getRecordDTOByUserIdAndVideoIdAndRecodeType(Long userId, Long videoId, String recodeType);

    /**
     * Delete watch record by ID
     *
     * @param id Record ID
     */
    void deleteRecordById(Long id);

    /**
     * Delete watch records by user ID
     *
     * @param userId User ID
     * @return Number of deleted records
     */
    int deleteRecordsByUserId(Long userId);

    /**
     * Delete watch records by video ID
     *
     * @param videoId Video ID
     * @return Number of deleted records
     */
    int deleteRecordsByVideoId(Long videoId);

    /**
     * Delete record by user ID and video ID
     *
     * @param userId  User ID
     * @param videoId Video ID
     * @return Number of deleted records
     */
    int deleteRecordByUserIdAndVideoId(Long userId, Long videoId);

    /**
     * Get user's like and watch video history with video details
     *
     * @param userId User ID
     * @return List of video records with details
     */
    List<Map<String, Object>> getUserVideoHistoryWithDetails(Long userId);

    /**
     * Get user's like and watch video history with video details (with pagination)
     *
     * @param userId User ID
     * @param page Page number (0-based)
     * @param pageSize Page size
     * @return Map containing paginated results
     */
    Map<String, Object> getUserVideoHistoryWithDetails(Long userId, int page, int pageSize);
}
