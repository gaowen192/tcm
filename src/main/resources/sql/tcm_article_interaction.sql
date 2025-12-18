-- 创建文章互动表（点赞和观看记录）
CREATE TABLE `tcm_article_interaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `is_liked` tinyint(1) DEFAULT '0' COMMENT '是否点赞：0-未点赞，1-已点赞',
  `liked_at` datetime DEFAULT NULL COMMENT '点赞时间',
  `viewed_at` datetime DEFAULT NULL COMMENT '观看时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_article` (`user_id`,`article_id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_liked_at` (`liked_at`),
  KEY `idx_viewed_at` (`viewed_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章互动表（点赞和观看记录）';