package com.ride.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医药论坛用户实体类
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@Entity
@Table(name = "tcm_users")
public class TcmUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(name = "email", nullable = true, unique = true, length = 100)
    private String email;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "real_name", length = 50)
    private String realName;
    
    @Column(name = "avatar", length = 255)
    private String avatar;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "gender")
    private Integer gender; // 0-未知，1-男，2-女
    
    @Column(name = "birthday")
    private LocalDate birthday;
    
    @Column(name = "profession", length = 50)
    private String profession;
    
    @Column(name = "hospital", length = 100)
    private String hospital;
    
    @Column(name = "department", length = 50)
    private String department;
    
    @Column(name = "title", length = 50)
    private String title;
    
    @Column(name = "license_number", length = 50)
    private String licenseNumber;
    
    @Column(name = "qualification_level")
    private Integer qualificationLevel; // 1-实习生，2-住院医，3-主治医，4-副主任医，5-主任医
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-禁用，1-正常
    
    @Column(name = "email_verified", nullable = false)
    private Integer emailVerified = 0; // 0-否，1-是
    
    @Column(name = "user_type", nullable = false)
    private Integer userType = 0; // 0-普通用户，1-医生
    
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 无参构造函数
    public TcmUser() {
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public Integer getQualificationLevel() { return qualificationLevel; }
    public void setQualificationLevel(Integer qualificationLevel) { this.qualificationLevel = qualificationLevel; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Integer emailVerified) { this.emailVerified = emailVerified; }
    public Integer getUserType() { return userType; }
    public void setUserType(Integer userType) { this.userType = userType; }
    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; }
    public String getLastLoginIp() { return lastLoginIp; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // toString方法
    @Override
    public String toString() {
        return "TcmUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", profession='" + profession + '\'' +
                ", hospital='" + hospital + '\'' +
                ", department='" + department + '\'' +
                ", title='" + title + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", qualificationLevel=" + qualificationLevel +
                ", status=" + status +
                ", emailVerified=" + emailVerified +
                ", userType=" + userType +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + "'" +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // equals方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcmUser tcmUser = (TcmUser) o;
        return java.util.Objects.equals(id, tcmUser.id) &&
                java.util.Objects.equals(username, tcmUser.username) &&
                java.util.Objects.equals(email, tcmUser.email) &&
                java.util.Objects.equals(password, tcmUser.password) &&
                java.util.Objects.equals(realName, tcmUser.realName) &&
                java.util.Objects.equals(avatar, tcmUser.avatar) &&
                java.util.Objects.equals(phone, tcmUser.phone) &&
                java.util.Objects.equals(gender, tcmUser.gender) &&
                java.util.Objects.equals(birthday, tcmUser.birthday) &&
                java.util.Objects.equals(profession, tcmUser.profession) &&
                java.util.Objects.equals(hospital, tcmUser.hospital) &&
                java.util.Objects.equals(department, tcmUser.department) &&
                java.util.Objects.equals(title, tcmUser.title) &&
                java.util.Objects.equals(licenseNumber, tcmUser.licenseNumber) &&
                java.util.Objects.equals(qualificationLevel, tcmUser.qualificationLevel) &&
                java.util.Objects.equals(status, tcmUser.status) &&
                java.util.Objects.equals(emailVerified, tcmUser.emailVerified) &&
                java.util.Objects.equals(userType, tcmUser.userType) &&
                java.util.Objects.equals(lastLoginTime, tcmUser.lastLoginTime) &&
                java.util.Objects.equals(lastLoginIp, tcmUser.lastLoginIp) &&
                java.util.Objects.equals(createdAt, tcmUser.createdAt) &&
                java.util.Objects.equals(updatedAt, tcmUser.updatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, username, email, password, realName, avatar, phone, gender, 
                                    birthday, profession, hospital, department, title, licenseNumber, 
                                    qualificationLevel, status, emailVerified, userType, lastLoginTime, lastLoginIp, 
                                    createdAt, updatedAt);
    }
}
