-- =====================================================
-- 视频评论表 (tcm_video_comments)
-- =====================================================
DROP TABLE IF EXISTS `tcm_video_comments`;
CREATE TABLE `tcm_video_comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `video_id` bigint(20) NOT NULL COMMENT '关联的视频ID',
  `user_id` bigint(20) NOT NULL COMMENT '评论用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `reply_to_comment_id` bigint(20) DEFAULT NULL COMMENT '回复的评论ID，用于嵌套回复',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常，2-审核中',
  `like_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点赞次数',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_reply_to_comment_id` (`reply_to_comment_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_updated_at` (`updated_at`),
  KEY `idx_like_count` (`like_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频评论表';