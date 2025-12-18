-- 创建帖子修改历史表
CREATE TABLE IF NOT EXISTS tcm_post_history (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID，关联tcm_posts表',
    version INT NOT NULL COMMENT '版本号，从1开始递增',
    title VARCHAR(255) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子内容',
    status INT DEFAULT 1 COMMENT '帖子状态：1-正常，0-禁用',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    category_id BIGINT NOT NULL COMMENT '板块ID',
    view_count BIGINT DEFAULT 0 COMMENT '浏览量',
    like_count BIGINT DEFAULT 0 COMMENT '点赞数',
    reply_count BIGINT DEFAULT 0 COMMENT '回复数',
    collect_count BIGINT DEFAULT 0 COMMENT '收藏数',
    description VARCHAR(500) DEFAULT '' COMMENT '帖子描述',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    is_essence TINYINT DEFAULT 0 COMMENT '是否精华：0-否，1-是',
    tag VARCHAR(100) DEFAULT '' COMMENT '帖子标签',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    reason VARCHAR(255) DEFAULT '' COMMENT '删除原因',
    
    -- 外键约束
    CONSTRAINT fk_tcm_post_history_post_id FOREIGN KEY (post_id) REFERENCES tcm_posts(id) ON DELETE CASCADE,
    
    -- 唯一索引：确保每个帖子的版本号唯一
    UNIQUE KEY uk_post_id_version (post_id, version),
    
    -- 普通索引
    INDEX idx_post_id (post_id),
    INDEX idx_version (version),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子修改历史表';