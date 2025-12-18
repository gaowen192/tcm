-- 创建帖子互动记录表
CREATE TABLE IF NOT EXISTS tcm_post_interaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    interaction_type VARCHAR(20) NOT NULL COMMENT '互动类型：LIKE(点赞)、WATCH(观看)',
    interaction_date DATETIME NOT NULL COMMENT '互动时间',
    is_deleted TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除：0-未删除，1-已删除',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    -- 联合唯一索引，确保同一用户对同一帖子的同一互动类型只记录一次
    UNIQUE KEY uk_user_post_type (user_id, post_id, interaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子互动记录表';