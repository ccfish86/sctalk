/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.20 : Database - teamtalk
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`teamtalk` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `teamtalk`;

CREATE TABLE `manager_info` (
  `manager_id` mediumint(6) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(40) NOT NULL COMMENT '管理员姓名',
  `password` char(32) NOT NULL COMMENT '管理员密码',
  `token` varchar(40) NOT NULL COMMENT '管理员token',
  `introduction` varchar(40) NOT NULL COMMENT '管理员介绍',
  `avatar` varchar(100) NOT NULL COMMENT '管理员头像',
  PRIMARY KEY (`manager_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='管理员信息表';

/*Data for the table `manager_info` */

insert  into `manager_info`(`manager_id`,`username`,`password`,`token`,`introduction`,`avatar`) values (1,'admin','21232f297a57a5a743894a0e4a801fc3','admin','超级管理员','static/pic/gg.gif'),(2,'member','21232f297a57a5a743894a0e4a801fc3','member','用户管理员','static/pic/gg.gif'),(5,'group','21232f297a57a5a743894a0e4a801fc3','group','群组管理员','static/pic/gg.gif'),(6,'depart','21232f297a57a5a743894a0e4a801fc3','depart','部门管理员','static/pic/gg.gif');

/*Table structure for table `manager_role_info` */

DROP TABLE IF EXISTS `manager_role_info`;

CREATE TABLE `manager_role_info` (
  `id` mediumint(6) NOT NULL AUTO_INCREMENT COMMENT '管理员-角色关系ID',
  `manager_id` mediumint(6) NOT NULL COMMENT '管理员ID',
  `role_id` mediumint(6) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='管理员-角色关系信息表';

/*Data for the table `manager_role_info` */

insert  into `manager_role_info`(`id`,`manager_id`,`role_id`) values (13,2,2),(18,5,4),(19,1,1),(20,1,3),(21,1,4);

/*Table structure for table `power_info` */

DROP TABLE IF EXISTS `power_info`;

CREATE TABLE `power_info` (
  `power_id` mediumint(6) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `power_name` varchar(40) NOT NULL COMMENT '权限名',
  `power_url` varchar(100) NOT NULL COMMENT '权限URL',
  `parent_id` mediumint(6) DEFAULT NULL COMMENT '父权限ID',
  PRIMARY KEY (`power_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='权限信息表';

/*Data for the table `power_info` */

insert  into `power_info`(`power_id`,`power_name`,`power_url`,`parent_id`) values (1,'用户 ','/layout/Layout',0),(2,'部门','/layout/Layout',0),(3,'群组','/layout/Layout',0),(4,'发现','/layout/Layout',0),(5,'管理员权限设置','/layout/Layout',0),(6,'用户管理','/function/Member',1),(7,'部门管理','/function/Depart',2),(8,'群组管理','/function/Group',3),(9,'发现管理','/function/Discovery',4),(10,'管理员设置','/management/Admin',5),(11,'角色设置','/management/Role',5),(12,'权限设置','/management/Power',5);

/*Table structure for table `role_info` */

DROP TABLE IF EXISTS `role_info`;

CREATE TABLE `role_info` (
  `role_id` mediumint(6) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(40) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

/*Data for the table `role_info` */

insert  into `role_info`(`role_id`,`role_name`) values (1,'超级管理员'),(2,'用户管理员'),(3,'部门管理员'),(4,'群组管理员'),(5,'发现管理员'),(6,'权限总管理员'),(7,'管理员设置'),(8,'管理员角色设置'),(9,'管理员权限设置');

/*Table structure for table `role_power_info` */

DROP TABLE IF EXISTS `role_power_info`;

CREATE TABLE `role_power_info` (
  `id` mediumint(6) NOT NULL AUTO_INCREMENT COMMENT '角色-权限关系ID',
  `role_id` mediumint(6) NOT NULL COMMENT '角色ID',
  `power_id` mediumint(6) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关系信息表';

/*Data for the table `role_power_info` */

insert  into `role_power_info`(`id`,`role_id`,`power_id`) values (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6),(7,1,7),(8,1,8),(9,1,9),(10,1,10),(11,1,11),(12,1,12),(13,2,1),(14,3,2),(15,4,3),(16,4,8),(17,5,4),(18,5,9),(19,2,6),(20,3,7),(21,6,5),(22,6,10),(23,6,11),(24,6,12),(25,7,5),(26,7,10),(27,8,5),(28,8,11),(39,9,5),(40,9,12);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
