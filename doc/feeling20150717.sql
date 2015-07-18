/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.1.58-community : Database - feeling
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`feeling` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `feeling`;

/*Table structure for table `event_base_info` */

DROP TABLE IF EXISTS `event_base_info`;

CREATE TABLE `event_base_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `event_type` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT 'pic' COMMENT 'pic:图片|text:文字|vote:调查|video:视频|audio:语音',
  `event_city` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '发布事件城市名称',
  `lat` float DEFAULT '0' COMMENT '发布事件的纬度',
  `lon` float DEFAULT '0' COMMENT '发布事件的经度',
  `location_long_code` bigint(8) DEFAULT '0' COMMENT '长整形数字坐标',
  `is_display` tinyint(2) DEFAULT '1' COMMENT '0:不显示  1：显示',
  `spread_times` int(4) DEFAULT '0' COMMENT '被传播次数',
  `skip_times` int(4) DEFAULT '0' COMMENT '被忽略次数(超50次不再推荐)',
  `comment_times` int(4) DEFAULT '0' COMMENT '被评论次数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `location_hash` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '经纬度hash值',
  `mobile` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号-游客',
  `nick_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '昵称(唯一且不能修改)',
  `device_type` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '设备类型',
  `device_imei` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'imei号',
  `device_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '设备id',
  PRIMARY KEY (`id`),
  KEY `i_uid` (`uid`),
  KEY `local_near_index` (`is_display`,`location_long_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户创建事件表';

/*Table structure for table `event_comment_record` */

DROP TABLE IF EXISTS `event_comment_record`;

CREATE TABLE `event_comment_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `eid` int(11) NOT NULL COMMENT '事件id',
  `uid` int(11) DEFAULT '0' COMMENT '评论用户id,游客则为0',
  `lat` float DEFAULT NULL COMMENT '纬度',
  `lon` float DEFAULT NULL COMMENT '经度',
  `device_type` tinyint(2) DEFAULT '1' COMMENT '1 ios,2 android',
  `device_imei` varchar(64) DEFAULT NULL COMMENT 'imei号',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备id',
  `location_long_code` bigint(15) DEFAULT NULL COMMENT '长整形数字坐标',
  `status` tinyint(1) DEFAULT '1' COMMENT '1 可用 0 不可用',
  `create_time` datetime NOT NULL DEFAULT '1997-01-01 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号，已注册用户可空',
  `comment` varchar(1024) DEFAULT NULL COMMENT '评论内容',
  `location_hash` varchar(32) DEFAULT NULL COMMENT '经纬度hash值',
  `is_display` tinyint(2) DEFAULT '1' COMMENT '0:不显示 1：显示',
  `nick_name` varchar(32) DEFAULT NULL COMMENT '昵称(唯一且不能修改)',
  PRIMARY KEY (`id`),
  KEY `from_event_id` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件评论的记录';

/*Table structure for table `event_cycle_record` */

DROP TABLE IF EXISTS `event_cycle_record`;

CREATE TABLE `event_cycle_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `from_eid` int(11) DEFAULT NULL COMMENT '上层事件id，0则是发起',
  `uid` int(11) DEFAULT NULL COMMENT '用户id',
  `lat` float DEFAULT NULL COMMENT '纬度',
  `lon` float DEFAULT NULL COMMENT '经度',
  `device_type` tinyint(2) DEFAULT '1' COMMENT '1 ios,2 android',
  `device_imei` varchar(192) COLLATE utf8_bin DEFAULT NULL COMMENT 'imei',
  `device_id` varchar(192) COLLATE utf8_bin DEFAULT NULL COMMENT '设备id',
  `location_long_code` bigint(15) DEFAULT NULL COMMENT '长整形数字坐标',
  `create_time` datetime NOT NULL DEFAULT '1997-01-01 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `mobile` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号，已注册用户可空',
  `eid` int(11) NOT NULL COMMENT '原始事件id',
  `location_hash` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '经纬度hash值',
  PRIMARY KEY (`id`),
  KEY `location_long_code` (`location_long_code`,`mobile`,`uid`),
  KEY `location_hash` (`location_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='事件转发流转记录表';

/*Table structure for table `event_pic` */

DROP TABLE IF EXISTS `event_pic`;

CREATE TABLE `event_pic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `eid` int(11) NOT NULL COMMENT '事件ID',
  `uid` int(11) DEFAULT '0' COMMENT '用户编号',
  `pic_path` varchar(64) NOT NULL COMMENT '图片相对路径',
  `pic_type` varchar(8) NOT NULL COMMENT '图片类型：jpg|jpeg|png|gif',
  `remark` varchar(64) DEFAULT NULL COMMENT '图片说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `eid` (`eid`),
  KEY `i_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片事件表';

/*Table structure for table `event_text` */

DROP TABLE IF EXISTS `event_text`;

CREATE TABLE `event_text` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `eid` int(11) NOT NULL COMMENT '事件编号',
  `uid` int(11) DEFAULT NULL COMMENT '用户编号',
  `content` varchar(256) NOT NULL COMMENT '文字内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`eid`),
  KEY `i_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文字事件表';

/*Table structure for table `event_vote` */

DROP TABLE IF EXISTS `event_vote`;

CREATE TABLE `event_vote` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `eid` int(11) NOT NULL COMMENT '事件编号',
  `uid` int(11) DEFAULT NULL COMMENT '用户编号',
  `vote_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '投票类型（1：单选 2：多选）',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `option1` varchar(16) DEFAULT NULL COMMENT '选项1',
  `option2` varchar(16) DEFAULT NULL COMMENT '选项2',
  `option3` varchar(16) DEFAULT NULL COMMENT '选项3',
  `option4` varchar(16) DEFAULT NULL COMMENT '选项4',
  `option5` varchar(16) DEFAULT NULL COMMENT '选项5',
  `option6` varchar(16) DEFAULT NULL COMMENT '选项6',
  `votes1` int(4) DEFAULT '0' COMMENT '选项1得票数',
  `votes2` int(4) DEFAULT '0' COMMENT '选项2得票数',
  `votes3` int(4) DEFAULT '0' COMMENT '选项3得票数',
  `votes4` int(4) DEFAULT '0' COMMENT '选项4得票数',
  `votes5` int(4) DEFAULT '0' COMMENT '选项5得票数',
  `votes6` int(4) DEFAULT '0' COMMENT '选项6得票数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `eid` (`eid`),
  KEY `i_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投票事件表';

/*Table structure for table `user_base_info` */

DROP TABLE IF EXISTS `user_base_info`;

CREATE TABLE `user_base_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `nick_name` varchar(32) DEFAULT NULL COMMENT '昵称(唯一且不能修改)',
  `gender` tinyint(2) DEFAULT '0' COMMENT '性别 0:女  1：男',
  `pwd` varchar(64) DEFAULT NULL COMMENT '密码 MD5+Salt',
  `city_code` varchar(8) DEFAULT NULL COMMENT '城市编码',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `avatar` varchar(64) DEFAULT NULL COMMENT '头像相对路径',
  `status` tinyint(2) DEFAULT '0' COMMENT '-9: 注销 0:正常 1:锁定',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8 COMMENT='用户信息';

/*Table structure for table `user_locus_info` */

DROP TABLE IF EXISTS `user_locus_info`;

CREATE TABLE `user_locus_info` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '用户编号',
  `locus_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '类型（0:注册 1：登录 ）',
  `lat` float NOT NULL DEFAULT '0' COMMENT '纬度',
  `lon` float NOT NULL DEFAULT '0' COMMENT '经度',
  `location_long_code` bigint(8) NOT NULL DEFAULT '0' COMMENT '长整型坐标',
  `device_type` varchar(16) NOT NULL COMMENT 'android|ios',
  `device_imei` varchar(64) DEFAULT NULL COMMENT 'imei号',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `location_hash` varchar(32) DEFAULT NULL COMMENT '经纬度hash值',
  PRIMARY KEY (`id`),
  KEY `i_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户行为轨迹表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
