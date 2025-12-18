package com.ride.mapper;

import com.ride.entity.TcmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 医药论坛用户数据访问层
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Repository
public interface TcmUserRepository extends JpaRepository<TcmUser, Long> {
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    Optional<TcmUser> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<TcmUser> findByEmail(String email);
    
    /**
     * 根据手机号查找用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    Optional<TcmUser> findByPhone(String phone);
    
    /**
     * 根据状态查找用户列表
     * 
     * @param status 状态
     * @return 用户列表
     */
    List<TcmUser> findByStatus(Integer status);
    
    /**
     * 根据专业查找用户列表
     * 
     * @param profession 专业
     * @return 用户列表
     */
    List<TcmUser> findByProfession(String profession);
    
    /**
     * 根据医院查找用户列表
     * 
     * @param hospital 医院
     * @return 用户列表
     */
    List<TcmUser> findByHospital(String hospital);
    
    /**
     * 模糊查询用户名
     * 
     * @param username 用户名关键字
     * @return 用户列表
     */
    @Query("SELECT u FROM TcmUser u WHERE u.username LIKE CONCAT('%', :username, '%')")
    List<TcmUser> findByUsernameLike(@Param("username") String username);
    
    /**
     * 统计活跃用户数量
     * 
     * @return 活跃用户数量
     */
    @Query("SELECT COUNT(u) FROM TcmUser u WHERE u.status = 1")
    long countActiveUsers();
    
    /**
     * 根据专业统计用户数量
     * 
     * @param profession 专业
     * @return 用户数量
     */
    @Query("SELECT COUNT(u) FROM TcmUser u WHERE u.profession = :profession AND u.status = 1")
    long countByProfession(@Param("profession") String profession);
    
    /**
     * 查询发帖最多的5个用户
     * 
     * @return 包含用户信息和发帖数量的列表
     */
    @Query(value = "SELECT u.id, u.username, u.real_name, u.title, COUNT(m.id) as postCount\n" +
            "                   FROM tcm_users u LEFT JOIN tcm_posts m ON u.id = m.user_id \n" +
            "                   GROUP BY u.id\n" +
            "                   ORDER BY postCount DESC \n" +
            "                   LIMIT 5", nativeQuery = true)
    List<Map<String, Object>> findTop5UsersByPostCount();
}
