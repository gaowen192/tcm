-- Create video watch record table
CREATE TABLE IF NOT EXISTS video_watch_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    video_id BIGINT NOT NULL COMMENT '视频ID',
    watch_time INT NOT NULL DEFAULT 0 COMMENT '观看时长（秒）',
    watch_progress INT NOT NULL DEFAULT 0 COMMENT '观看进度（百分比）',
    watch_date DATE NOT NULL COMMENT '观看日期',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- Unique constraint to ensure only one record per user-video pair
    UNIQUE KEY uk_user_video (user_id, video_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频观看记录表';

-- Create indexes for better query performance
CREATE INDEX idx_user_id ON video_watch_record(user_id);
CREATE INDEX idx_video_id ON video_watch_record(video_id);
CREATE INDEX idx_watch_date ON video_watch_record(watch_date);
