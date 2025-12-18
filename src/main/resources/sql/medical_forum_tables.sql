-- =====================================================
-- 医药论坛数据库表结构设计
-- 创建时间: 2024
-- 描述: 医药论坛系统所需的数据库表结构
-- =====================================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 1. 用户表 (tcm_users)
-- =====================================================
DROP TABLE IF EXISTS `tcm_users`;
CREATE TABLE `tcm_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `password` varchar(255) NOT NULL COMMENT '密码（加密）',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `profession` varchar(50) DEFAULT NULL COMMENT '职业',
  `hospital` varchar(100) DEFAULT NULL COMMENT '所属医院',
  `department` varchar(50) DEFAULT NULL COMMENT '科室',
  `title` varchar(50) DEFAULT NULL COMMENT '职称',
  `license_number` varchar(50) DEFAULT NULL COMMENT '执业证号',
  `qualification_level` tinyint(1) DEFAULT NULL COMMENT '资质等级：1-实习生，2-住院医，3-主治医，4-副主任医，5-主任医',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `email_verified` tinyint(1) NOT NULL DEFAULT '0' COMMENT '邮箱是否验证：0-否，1-是',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) DEFAULT NULL COMMENT '最后登录IP',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =====================================================
-- 2. 论坛板块表 (tcm_categories)
-- =====================================================
DROP TABLE IF EXISTS `tcm_categories`;
CREATE TABLE `tcm_categories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '板块ID',
  `name` varchar(100) NOT NULL COMMENT '板块名称',
  `description` text COMMENT '板块描述',
  `icon` varchar(255) DEFAULT NULL COMMENT '板块图标',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父板块ID，0为顶级板块',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序顺序',
  `post_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '帖子数量',
  `topic_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日新增帖子数',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort_order` (`sort_order`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛板块表';

-- =====================================================
-- 3. 帖子表 (tcm_posts)
-- =====================================================
DROP TABLE IF EXISTS `tcm_posts`;
CREATE TABLE `tcm_posts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `title` varchar(200) NOT NULL COMMENT '帖子标题',
  `content` longtext NOT NULL COMMENT '帖子内容',
  `summary` varchar(500) DEFAULT NULL COMMENT '帖子摘要',
  `user_id` bigint(20) NOT NULL COMMENT '作者ID',
  `category_id` bigint(20) NOT NULL COMMENT '板块ID',
  `tags` varchar(500) DEFAULT NULL COMMENT '标签，逗号分隔',
  `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `reply_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '回复数量',
  `like_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点赞数量',
  `collect_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '收藏数量',
  `is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否置顶：0-否，1-是',
  `is_essence` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否精华：0-否，1-是',
  `is_hot` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否热门：0-否，1-是',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-已删除，1-正常，2-已锁定',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_reply_time` timestamp NULL DEFAULT NULL COMMENT '最后回复时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_last_reply_time` (`last_reply_time`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_is_essence` (`is_essence`),
  KEY `idx_view_count` (`view_count`),
  FULLTEXT KEY `ft_title_content` (`title`,`content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- =====================================================
-- 4. 回复表 (tcm_replies)
-- =====================================================
DROP TABLE IF EXISTS `tcm_replies`;
CREATE TABLE `tcm_replies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '回复用户ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父回复ID，0为顶级回复',
  `reply_to_user_id` bigint(20) DEFAULT '0' COMMENT '被回复的用户ID',
  `content` text NOT NULL COMMENT '回复内容',
  `like_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点赞数量',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-已删除，1-正常',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`),
  FULLTEXT KEY `ft_content` (`content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回复表';

-- =====================================================
-- 5. 用户角色表 (tcm_user_roles)
-- =====================================================
DROP TABLE IF EXISTS `tcm_user_roles`;
CREATE TABLE `tcm_user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role` varchar(50) NOT NULL COMMENT '角色：user-普通用户，moderator-版主，admin-管理员，super_admin-超级管理员',
  `category_id` bigint(20) DEFAULT NULL COMMENT '管理板块ID（版主专用）',
  `granted_by` bigint(20) DEFAULT NULL COMMENT '授权人ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role_category` (`user_id`,`role`,`category_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- =====================================================
-- 6. 帖子点赞表 (tcm_post_likes)
-- =====================================================
DROP TABLE IF EXISTS `tcm_post_likes`;
CREATE TABLE `tcm_post_likes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `target_id` bigint(20) NOT NULL COMMENT '目标ID（帖子或回复ID）',
  `target_type` tinyint(1) NOT NULL COMMENT '目标类型：1-帖子，2-回复',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`,`target_id`,`target_type`),
  KEY `idx_target` (`target_id`,`target_type`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- =====================================================
-- 7. 用户关注表 (tcm_user_follows)
-- =====================================================
DROP TABLE IF EXISTS `tcm_user_follows`;
CREATE TABLE `tcm_user_follows` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `follower_id` bigint(20) NOT NULL COMMENT '关注者ID',
  `following_id` bigint(20) NOT NULL COMMENT '被关注者ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follow_relation` (`follower_id`,`following_id`),
  KEY `idx_follower_id` (`follower_id`),
  KEY `idx_following_id` (`following_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注表';

-- =====================================================
-- 8. 帖子收藏表 (tcm_post_collects)
-- =====================================================
DROP TABLE IF EXISTS `tcm_post_collects`;
CREATE TABLE `tcm_post_collects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_post` (`user_id`,`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子收藏表';

-- =====================================================
-- 9. 消息通知表 (tcm_notifications)
-- =====================================================
DROP TABLE IF EXISTS `tcm_notifications`;
CREATE TABLE `tcm_notifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` bigint(20) NOT NULL COMMENT '接收用户ID',
  `type` varchar(50) NOT NULL COMMENT '通知类型：reply-回复，like-点赞，follow-关注，system-系统',
  `title` varchar(200) NOT NULL COMMENT '通知标题',
  `content` text COMMENT '通知内容',
  `related_id` bigint(20) DEFAULT NULL COMMENT '关联ID（帖子ID、回复ID等）',
  `related_type` varchar(50) DEFAULT NULL COMMENT '关联类型',
  `from_user_id` bigint(20) DEFAULT NULL COMMENT '发送用户ID',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读：0-否，1-是',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `read_at` timestamp NULL DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- =====================================================
-- 10. 插入初始数据
-- =====================================================

-- 插入初始板块数据
INSERT INTO `tcm_categories` (`name`, `description`, `parent_id`, `sort_order`) VALUES
('综合讨论', '医药综合讨论区', 0, 1),
('临床医学', '临床医学交流区', 0, 2),
('药学讨论', '药学专业讨论区', 0, 3),
('医学影像', '医学影像诊断讨论', 0, 4),
('护理园地', '护理专业交流', 0, 5),
('医学检验', '检验医学讨论', 0, 6),
('公共卫生', '公共卫生讨论', 0, 7),
('医学教育', '医学教育培训', 0, 8),
('学术会议', '学术会议信息', 0, 9),
('求职招聘', '医药行业招聘', 0, 10);

-- 插入子板块数据
INSERT INTO `tcm_categories` (`name`, `description`, `parent_id`, `sort_order`) VALUES
('内科', '内科疾病讨论', 2, 1),
('外科', '外科疾病讨论', 2, 2),
('儿科', '儿科疾病讨论', 2, 3),
('妇产科', '妇产科疾病讨论', 2, 4),
('心血管', '心血管疾病讨论', 2, 5),
('呼吸科', '呼吸疾病讨论', 2, 6),
('消化科', '消化系统疾病讨论', 2, 7),
('神经科', '神经系统疾病讨论', 2, 8),
('内分泌科', '内分泌疾病讨论', 2, 9);

-- 设置外键约束
SET FOREIGN_KEY_CHECKS = 1;