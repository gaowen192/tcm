package com.ride.mapper;

import com.ride.entity.TcmVideoWatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TCM Video watch record repository
 */
@Repository
public interface TcmVideoWatchRecordRepository extends JpaRepository<TcmVideoWatchRecord, Long> {

    /**
     * Find watch records by user ID
     *
     * @param userId User ID
     * @return List of watch records
     */
    List<TcmVideoWatchRecord> findByUserId(Long userId);

    /**
     * Find watch records by video ID
     *
     * @param videoId Video ID
     * @return List of watch records
     */
    List<TcmVideoWatchRecord> findByVideoId(Long videoId);

    /**
     * Find watch record by user ID and video ID
     *
     * @param userId  User ID
     * @param videoId Video ID
     * @return Optional watch record
     */
    Optional<TcmVideoWatchRecord> findByUserIdAndVideoId(Long userId, Long videoId);

    /**
     * Find records by user ID and record type
     *
     * @param userId User ID
     * @param recodeType Record type
     * @return List of records
     */
    List<TcmVideoWatchRecord> findByUserIdAndRecodeType(Long userId, String recodeType);

    /**
     * Find records by video ID and record type
     *
     * @param videoId Video ID
     * @param recodeType Record type
     * @return List of records
     */
    List<TcmVideoWatchRecord> findByVideoIdAndRecodeType(Long videoId, String recodeType);

    /**
     * Find record by user ID, video ID and record type
     *
     * @param userId User ID
     * @param videoId Video ID
     * @param recodeType Record type
     * @return Optional record
     */
    Optional<TcmVideoWatchRecord> findByUserIdAndVideoIdAndRecodeType(Long userId, Long videoId, String recodeType);



    /**
     * Delete watch records by user ID
     *
     * @param userId User ID
     * @return Number of deleted records
     */
    @Modifying
    @Transactional
    int deleteByUserId(Long userId);

    /**
     * Delete watch records by video ID
     *
     * @param videoId Video ID
     * @return Number of deleted records
     */
    @Modifying
    @Transactional
    int deleteByVideoId(Long videoId);

    /**
     * Get user's like and watch video history with video details
     *
     * @param userId User ID
     * @return List of video records with details
     */
    @Query("SELECT new map(v.id as videoId, v.title as title, v.description as description, v.thumbnailPath as thumbnailPath, " +
           "v.viewCount as viewCount, v.likeCount as likeCount, v.duration as duration, v.createdAt as createdAt, " +
           "wr.recodeType as recodeType, MIN(wr.watchDate) as firstInteractionDate) " +
           "FROM TcmVideoWatchRecord wr JOIN TcmVideo v ON wr.videoId = v.id " +
           "WHERE wr.userId = :userId GROUP BY v.id, v.title, v.description, v.thumbnailPath, " +
           "v.viewCount, v.likeCount, v.duration, v.createdAt, wr.recodeType " +
           "ORDER BY firstInteractionDate DESC")
    List<Map<String, Object>> findUserVideoHistoryWithDetails(@Param("userId") Long userId);

    /**
     * Get user's like and watch video history with video details (with pagination)
     *
     * @param userId User ID
     * @param pageable Pageable object
     * @return List of video records with details
     */
    @Query("SELECT new map(v.id as videoId, v.title as title, v.description as description, v.thumbnailPath as thumbnailPath, " +
           "v.viewCount as viewCount, v.likeCount as likeCount, v.duration as duration, v.createdAt as createdAt, " +
           "wr.recodeType as recodeType, MIN(wr.watchDate) as firstInteractionDate) " +
           "FROM TcmVideoWatchRecord wr JOIN TcmVideo v ON wr.videoId = v.id " +
           "WHERE wr.userId = :userId GROUP BY v.id, v.title, v.description, v.thumbnailPath, " +
           "v.viewCount, v.likeCount, v.duration, v.createdAt, wr.recodeType " +
           "ORDER BY firstInteractionDate DESC")
    org.springframework.data.domain.Page<Map<String, Object>> findUserVideoHistoryWithDetails(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);
}
