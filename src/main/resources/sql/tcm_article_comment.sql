-- 创建文章评论表
CREATE TABLE IF NOT EXISTS `tcm_article_comment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `article_id` BIGINT(20) NOT NULL COMMENT '文章ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '评论用户ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `parent_id` BIGINT(20) DEFAULT '0' COMMENT '父评论ID，0表示顶级评论',
  `status` TINYINT(4) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `like_count` INT(11) DEFAULT '0' COMMENT '点赞数',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_article_comment_article` FOREIGN KEY (`article_id`) REFERENCES `tcm_articles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_article_comment_user` FOREIGN KEY (`user_id`) REFERENCES `tcm_users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';
