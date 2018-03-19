# Host: 192.168.10.9  (Version 5.7.18)
# Date: 2018-02-27 17:03:04
# Generator: MySQL-Front 6.0  (Build 1.163)


#
# Structure for table "im_transmit_file"
#

CREATE TABLE `im_transmit_file` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `from_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '发送者ID',
  `to_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '目标ID',
  `file_name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
  `size` int(11) NOT NULL DEFAULT '0' COMMENT '大小',
  `task_id` varchar(255) NOT NULL DEFAULT '' COMMENT '任务ID',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `created` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='离线文件';

#
# Data for table "im_transmit_file"
#

