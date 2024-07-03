# 数据库初始化

-- 创建库
create database if not exists easy_judge;

-- 切换库
use easy_judge;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment primary key comment 'id',
    userAccount  varchar(256) unique                    not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists problem
(
    id            bigint auto_increment primary key comment 'id',
    title         varchar(512)                       null comment '标题',
    content       text                               null comment '内容',
    tags          varchar(1024)                      null comment '标签列表（json数组）',
    submitCount   int      default 0                 not null comment '题目提交数',
    acceptedCount int      default 0                 not null comment '题目通过数',
    judgeCase     text                               null comment '判题用例（json数组）',
    judgeConfig   text                               null comment '判题配置（json对象）',
    solution      text                               null comment '题解',
    thumbNum      int      default 0                 not null comment '点赞数',
    favourNum     int      default 0                 not null comment '收藏数',
    userId        bigint                             not null comment '创建用户id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

create table if not exists problem_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    problemId bigint                             not null comment '题目id',
    userId     bigint                             not null comment '提交用户id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_problemId (problemId),
    index idx_userId (userId)
) comment '题目提交';

-- 题目收藏表
create table if not exists problem_favour
(
    id         bigint auto_increment primary key comment 'id',
    userId     bigint unique                      not null comment '收藏用户id',
    problemId  bigint unique                      not null comment '收藏题目id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId),
    index idx_problemId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;


