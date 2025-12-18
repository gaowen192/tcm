package com.ride.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 医药论坛用户数据传输对象
 * 
 * @author 千行团队
 * @version 1.0.0
 */
public class TcmUserDTO {
    
    private Long id;
    private String username;
    private String email;
    private String realName;
    private String avatar;
    private String phone;
    private Integer gender; // 0-未知，1-男，2-女
    private LocalDate birthday;
    private String profession;
    private String hospital;
    private String department;
    private String title;
    private String licenseNumber;
    private Integer qualificationLevel; // 1-实习生，2-住院医，3-主治医，4-副主任医，5-主任医
    private Integer status; // 0-禁用，1-正常
    private Integer emailVerified; // 0-否，1-是
    private Integer userType; // 0-普通用户，1-医生
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getter和Setter方法
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(Integer qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString方法
    @Override
    public String toString() {
        return "TcmUserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
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
        TcmUserDTO that = (TcmUserDTO) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(username, that.username) &&
                java.util.Objects.equals(email, that.email) &&
                java.util.Objects.equals(realName, that.realName) &&
                java.util.Objects.equals(avatar, that.avatar) &&
                java.util.Objects.equals(phone, that.phone) &&
                java.util.Objects.equals(gender, that.gender) &&
                java.util.Objects.equals(birthday, that.birthday) &&
                java.util.Objects.equals(profession, that.profession) &&
                java.util.Objects.equals(hospital, that.hospital) &&
                java.util.Objects.equals(department, that.department) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(licenseNumber, that.licenseNumber) &&
                java.util.Objects.equals(qualificationLevel, that.qualificationLevel) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(emailVerified, that.emailVerified) &&
                java.util.Objects.equals(userType, that.userType) &&
                java.util.Objects.equals(lastLoginTime, that.lastLoginTime) &&
                java.util.Objects.equals(lastLoginIp, that.lastLoginIp) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(updatedAt, that.updatedAt);
    }

    // hashCode方法
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, username, email, realName, avatar, phone, gender, birthday, 
                                    profession, hospital, department, title, licenseNumber, 
                                    qualificationLevel, status, emailVerified, userType, lastLoginTime, 
                                    lastLoginIp, createdAt, updatedAt);
    }
}
