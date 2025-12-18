CREATE TABLE IF NOT EXISTS tcm_system_messages (
  id BIGINT AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
  title VARCHAR(255) NOT NULL COMMENT '消息标题',
  content TEXT NOT NULL COMMENT '消息内容',
  message_type TINYINT DEFAULT 0 COMMENT '消息类型：0-系统通知 1-活动通知 2-其他',
  target_type TINYINT DEFAULT 0 COMMENT '目标类型：0-所有用户 1-指定用户 2-指定角色',
  target_value VARCHAR(255) DEFAULT NULL COMMENT '目标值，根据target_type不同存储不同内容',
  user_id BIGINT DEFAULT NULL COMMENT '用户ID，可为空',
  is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
  status TINYINT DEFAULT 0 COMMENT '状态：0-正常 1-已删除',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_message_type (message_type),
  INDEX idx_target_type (target_type),
  INDEX idx_created_at (created_at),
  INDEX idx_user_id (user_id)
) COMMENT='中医药系统消息表';

-- 插入示例数据
INSERT INTO tcm_system_messages (title, content, message_type, target_type, target_value, user_id, is_read, status)
VALUES 
('中医药系统升级通知', '尊敬的用户，中医药系统将于本周日凌晨2:00-4:00进行升级维护，期间可能无法正常使用，敬请谅解。', 0, 0, NULL, NULL, FALSE, 0),
('中医药知识竞赛', '「中医药知识竞赛」活动正在火热进行中，参与即有机会获得精美礼品！', 1, 0, NULL, NULL, FALSE, 0),
('中医药在线问诊功能上线', '平台新增「中医药在线问诊」功能，欢迎体验！', 0, 0, NULL, NULL, FALSE, 0);