-- 创建医药论坛文章修改历史表
CREATE TABLE IF NOT EXISTS `tcm_article_history` (
  `history_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '历史记录ID',
  `article_id` BIGINT(20) NOT NULL COMMENT '文章ID，关联tcm_articles表',
  `version` INT(11) NOT NULL COMMENT '版本号，从1开始递增',
  `title` VARCHAR(255) NOT NULL COMMENT '文章标题',
  `content` LONGTEXT NOT NULL COMMENT '文章内容',
  `user_id` BIGINT(20) NOT NULL COMMENT '作者ID',
  `category_id` BIGINT(20) NOT NULL COMMENT '板块ID',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '文章标签，用逗号分隔',
  `summary` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
  `status` TINYINT(4) DEFAULT '1' COMMENT '状态：0-禁用，1-启用，2-审核中，3-审核失败',
  `view_count` INT(11) DEFAULT '0' COMMENT '阅读次数',
  `like_count` INT(11) DEFAULT '0' COMMENT '点赞次数',
  `comment_count` INT(11) DEFAULT '0' COMMENT '评论次数',
  `collect_count` INT(11) DEFAULT '0' COMMENT '收藏次数',
  `is_hot` TINYINT(1) DEFAULT '0' COMMENT '是否热门：0-否，1-是',
  `is_recommended` TINYINT(1) DEFAULT '0' COMMENT '是否推荐：0-否，1-是',
  `is_updated` TINYINT(1) DEFAULT '0' COMMENT '是否更新：0-未更新，1-已更新',
  `meta_keywords` VARCHAR(255) DEFAULT NULL COMMENT 'SEO关键字',
  `meta_description` VARCHAR(500) DEFAULT NULL COMMENT 'SEO描述',
  `source` VARCHAR(100) DEFAULT NULL COMMENT '文章来源',
  `author` VARCHAR(100) DEFAULT NULL COMMENT '文章作者（外部作者）',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
  `word_count` INT(11) DEFAULT '0' COMMENT '文章字数',
  `is_original` TINYINT(1) DEFAULT '1' COMMENT '是否原创：0-否，1-是',
  `original_url` VARCHAR(255) DEFAULT NULL COMMENT '原创文章URL（如果有）',
  `sort_order` INT(11) DEFAULT '0' COMMENT '排序字段',
  `created_at` DATETIME DEFAULT NULL COMMENT '文章创建时间',
  `updated_at` DATETIME DEFAULT NULL COMMENT '文章更新时间',
  `deleted_at` DATETIME DEFAULT NULL COMMENT '文章删除时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '文章备注',
  `seo_title` VARCHAR(200) DEFAULT NULL COMMENT 'SEO标题',
  `seo_keywords` VARCHAR(500) DEFAULT NULL COMMENT 'SEO关键词',
  `seo_description` VARCHAR(500) DEFAULT NULL COMMENT 'SEO描述',
  `author_ip` VARCHAR(50) DEFAULT NULL COMMENT '作者IP地址',
  `published_at` DATETIME DEFAULT NULL COMMENT '发布时间',
  `history_created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '历史记录创建时间',
  PRIMARY KEY (`history_id`),
  
  -- 外键约束
  CONSTRAINT fk_tcm_article_history_article_id FOREIGN KEY (`article_id`) REFERENCES `tcm_articles`(`id`) ON DELETE CASCADE,
  
  -- 唯一索引：确保每个文章的版本号唯一
  UNIQUE KEY `uk_article_id_version` (`article_id`, `version`),
  
  -- 普通索引
  INDEX `idx_article_id` (`article_id`),
  INDEX `idx_version` (`version`),
  INDEX `idx_history_created_at` (`history_created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医药论坛文章修改历史表';