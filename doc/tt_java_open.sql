# ************************************************************
# 根据TeamTalk开发的JAVA（spring cloud）版服务器用
# Version 2017-05-17
#
# Database: teamtalk
# Generation Time: 2017-05-17 10:10:39 +0000
# ************************************************************


DROP DATABASE IF EXISTS teamtalk;
CREATE DATABASE teamtalk DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE teamtalk;


DROP TABLE IF EXISTS `im_admin`;

CREATE TABLE `im_admin` (
  `id` mediumint(6) unsigned NOT NULL AUTO_INCREMENT,
  `uname` varchar(40) NOT NULL COMMENT '用户名',
  `pwd` varchar(256) NOT NULL COMMENT '密码',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '用户状态 0 :正常 1:删除 可扩展',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间´',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间´',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `im_admin` WRITE;
/*!40000 ALTER TABLE `im_admin` DISABLE KEYS */;

INSERT INTO `im_admin` (`id`, `uname`, `pwd`, `status`, `created`, `updated`)
VALUES
	(1,'admin','21232f297a57a5a743894a0e4a801fc3',0,0,0);

/*!40000 ALTER TABLE `im_admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table im_audio
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_audio`;

CREATE TABLE `im_audio` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_id` bigint(20) unsigned NOT NULL COMMENT '发送者Id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收者Id',
  `path` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '语音存储的地址',
  `size` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '文件大小',
  `duration` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '语音时长',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_from_id_to_id` (`from_id`,`to_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


# Dump of table im_depart
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_depart`;

CREATE TABLE `im_depart` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `depart_name` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '部门名称',
  `priority` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '显示优先级',
  `parent_id` bigint(20) unsigned NOT NULL COMMENT '上级部门id',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '状态',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_depart_name` (`depart_name`),
  KEY `idx_priority_status` (`priority`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



# Dump of table im_discovery
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_discovery`;

CREATE TABLE `im_discovery` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `item_name` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '名称',
  `item_url` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'URL',
  `item_priority` int(11) unsigned NOT NULL COMMENT '显示优先级',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '状态',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_item_name` (`item_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


# Dump of table im_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group`;

CREATE TABLE `im_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '群名称',
  `avatar` varchar(256) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '群头像',
  `creator` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建者用户id',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '群组类型，1-固定;2-临时群',
  `user_cnt` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '成员人数',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否删除,0-正常，1-删除',
  `version` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '群版本号',
  `last_chated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '最后聊天时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`(191)),
  KEY `idx_creator` (`creator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群信息';


# Dump of table im_group_member
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_member`;

CREATE TABLE `im_group_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '群Id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '是否退出群，0-正常，1-已退出',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_user_id_status` (`group_id`,`user_id`,`status`),
  KEY `idx_user_id_status_updated` (`user_id`,`status`,`updated`),
  KEY `idx_group_id_updated` (`group_id`,`updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户和群的关系表';


# Dump of table im_group_message_0
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_0`;

CREATE TABLE `im_group_message_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';



# Dump of table im_group_message_1
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_1`;

CREATE TABLE `im_group_message_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';


# Dump of table im_group_message_2
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_2`;

CREATE TABLE `im_group_message_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';


# Dump of table im_group_message_3
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_3`;

CREATE TABLE `im_group_message_3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';


# Dump of table im_group_message_4
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_4`;

CREATE TABLE `im_group_message_4` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';


# Dump of table im_group_message_5
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_5`;

CREATE TABLE `im_group_message_5` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';


# Dump of table im_group_message_6
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_6`;

CREATE TABLE `im_group_message_6` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';



# Dump of table im_group_message_7
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_group_message_7`;

CREATE TABLE `im_group_message_7` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';

DROP TABLE IF EXISTS `im_group_message_8`;

CREATE TABLE `im_group_message_8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';

DROP TABLE IF EXISTS `im_group_message_9`;

CREATE TABLE `im_group_message_9` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id_status_created` (`group_id`,`status`,`created`),
  KEY `idx_group_id_msg_id_status_created` (`group_id`,`msg_id`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';


# Dump of table im_message_0
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_0`;

CREATE TABLE `im_message_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


# Dump of table im_message_1
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_1`;

CREATE TABLE `im_message_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



# Dump of table im_message_2
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_2`;

CREATE TABLE `im_message_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


# Dump of table im_message_3
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_3`;

CREATE TABLE `im_message_3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

# Dump of table im_message_4
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_4`;

CREATE TABLE `im_message_4` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


# Dump of table im_message_5
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_5`;

CREATE TABLE `im_message_5` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

# Dump of table im_message_6
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_6`;

CREATE TABLE `im_message_6` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

# Dump of table im_message_7
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_7`;

CREATE TABLE `im_message_7` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

# Dump of table im_message_8
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_8`;

CREATE TABLE `im_message_8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

# Dump of table im_message_9
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_message_9`;

CREATE TABLE `im_message_9` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `relate_id` bigint(20) unsigned NOT NULL COMMENT '用户的关系id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '发送用户的id',
  `to_id` bigint(20) unsigned NOT NULL COMMENT '接收用户的id',
  `msg_id` bigint(20) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relate_id_status_created` (`relate_id`,`status`,`created`),
  KEY `idx_relate_id_status_msg_id_created` (`relate_id`,`status`,`msg_id`,`created`),
  KEY `idx_from_id_to_id_created` (`user_id`,`to_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

# Dump of table im_recent_session
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_recent_session`;

CREATE TABLE `im_recent_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `peer_id` bigint(20) unsigned NOT NULL COMMENT '对方id',
  `type` tinyint(2) unsigned DEFAULT '0' COMMENT '类型，1-用户,2-群组',
  `status` tinyint(2) unsigned DEFAULT '0' COMMENT '用户:0-正常, 1-用户A删除,群组:0-正常, 1-被删除',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id_peer_id_status_updated` (`user_id`,`peer_id`,`status`,`updated`),
  KEY `idx_user_id_peer_id_type` (`user_id`,`peer_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Dump of table im_relation_ship
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_relation_ship`;

CREATE TABLE `im_relation_ship` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `small_id` bigint(20) unsigned NOT NULL COMMENT '用户A的id',
  `big_id` bigint(20) unsigned NOT NULL COMMENT '用户B的id',
  `status` tinyint(2) unsigned DEFAULT '0' COMMENT '用户:0-正常, 1-用户A删除,群组:0-正常, 1-被删除',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_small_id_big_id_status_updated` (`small_id`,`big_id`,`status`,`updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Dump of table im_User
# ------------------------------------------------------------

DROP TABLE IF EXISTS `im_user`;

CREATE TABLE `im_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `sex` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '1男2女0未知',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户名',
  `domain` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '拼音',
  `nick` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '花名,绰号等',
  `password` varchar(256) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '混淆码',
  `phone` varchar(11) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '手机号码',
  `email` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'email',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '自定义用户头像',
  `depart_id` bigint(20) unsigned NOT NULL COMMENT '所属部门Id',
  `status` tinyint(2) unsigned DEFAULT '0' COMMENT '1. 试用期 2. 正式 3. 离职 4.实习',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `push_shield_status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '0关闭勿扰 1开启勿扰',
  `sign_info` varchar(128) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '个性签名',
  PRIMARY KEY (`id`),
  KEY `idx_domain` (`domain`),
  KEY `idx_name` (`name`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

