CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,      -- 自增主键
                       user_id VARCHAR(50) NOT NULL UNIQUE,    -- 用户ID（唯一）
                       email VARCHAR(100) NOT NULL UNIQUE,     -- 邮箱（唯一）
                       password VARCHAR(255) NOT NULL UNIQUE,  -- 密码（唯一）
                       pass_key VARCHAR(255) NOT NULL UNIQUE,  -- PassKey（唯一）
                       register_time DATETIME NOT NULL,        -- 注册时间（非空）
                       bio TEXT,                               -- 个人简介
                       age INT,                                -- 年龄
                       school VARCHAR(100),                    -- 学校
                       college VARCHAR(100),                   -- 学院
                       total_upload BIGINT DEFAULT 0,          -- 总上传量（默认 0）
                       total_download BIGINT DEFAULT 0,        -- 总下载量（默认 0）
                       invite_count INT DEFAULT 0,             -- 邀请数（默认 0）
                       magic_value INT DEFAULT 0,              -- 魔力值（默认 0）
                       work_count INT DEFAULT 0                -- 作品数（默认 0）
);
INSERT INTO users (
    user_id, email, password, pass_key, register_time, bio, age, school, college, total_upload, total_download, invite_count, magic_value, work_count
) VALUES (
             'test_user_1',
             'test1@example.com',
             'password123',
             'passkey123',
             NOW(),
             'This is a test user.',
             25,
             'Test University',
             'College of Engineering',
             100,
             50,
             3,
             500,
             2
         );