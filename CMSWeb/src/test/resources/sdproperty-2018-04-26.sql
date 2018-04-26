-- MySQL dump 10.13  Distrib 5.7.22, for Linux (x86_64)
--
-- Host: localhost    Database: sdproperty
-- ------------------------------------------------------
-- Server version	5.7.22-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_door`
--

DROP TABLE IF EXISTS `tb_door`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_door` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `address` varchar(128) DEFAULT NULL COMMENT '位置地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='门岗表，一个小区有1-n个门岗， 门禁和ipc，以来门岗';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_door_lock`
--

DROP TABLE IF EXISTS `tb_door_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_door_lock` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `door_id` int(11) DEFAULT NULL,
  `ip` varchar(32) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `line` int(11) DEFAULT '0',
  `time` int(11) DEFAULT '3',
  `name` varchar(128) DEFAULT NULL,
  `security_level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='门禁，门锁，一个门岗最多有一个门锁， 也可能没有';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_exception`
--

DROP TABLE IF EXISTS `tb_exception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_exception` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `method` varchar(128) DEFAULT NULL COMMENT '方法名称',
  `content` varchar(2048) DEFAULT NULL COMMENT '异常内容',
  `create_time` datetime DEFAULT NULL,
  `user_id` int(64) DEFAULT NULL COMMENT '修改人用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统异常保存表，用来存放系统异常。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_guest`
--

DROP TABLE IF EXISTS `tb_guest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_guest` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `image` varchar(2048) DEFAULT NULL COMMENT '图像地址',
  `code` varchar(128) DEFAULT NULL COMMENT '特征码',
  `guest_role_id` int(11) DEFAULT '1000' COMMENT '访客角色id',
  `first_time` datetime DEFAULT NULL COMMENT '第一次出现在snapshot时间',
  `last_time` datetime DEFAULT NULL COMMENT '最后一次出现在snapshot时间',
  `create_time` datetime DEFAULT NULL COMMENT '操作员第一次录入时间， 如录入业主身份',
  `name` varchar(32) DEFAULT NULL COMMENT '访客姓名',
  `card_no` varchar(18) DEFAULT NULL COMMENT '身份证号码',
  `access_guest_id` int(11) DEFAULT NULL COMMENT '被被访者id',
  `age` int(11) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL COMMENT '0-female, 1-male',
  `room` varchar(32) DEFAULT NULL COMMENT '房间号，业主才有',
  `count` int(11) DEFAULT NULL COMMENT '累计来访次数',
  `lock_start_time` datetime DEFAULT NULL COMMENT '锁自动开门时间，null为不限制时间',
  `lock_end_time` datetime DEFAULT NULL COMMENT '锁自动开门结束时间，在这个时间段之外的，不自动开门，null为不限制时间',
  `upload_image` varchar(1024) DEFAULT NULL COMMENT '上传的房客图片路径，上传的时候用',
  `user_id_create` int(11) DEFAULT NULL COMMENT '注册访客信息的用户id',
  `last_modify_user_id` int(11) DEFAULT NULL COMMENT '最后修改访客信息的用户id',
  `last_modify_time` datetime DEFAULT NULL COMMENT '最后修改访客信息时间',
  PRIMARY KEY (`id`),
  KEY `guest_code` (`code`),
  KEY `last_time` (`last_time`)
) ENGINE=InnoDB AUTO_INCREMENT=9992 DEFAULT CHARSET=utf8 COMMENT='访客表， 访客为guest';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_guest_modify_record`
--

DROP TABLE IF EXISTS `tb_guest_modify_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_guest_modify_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `guest_id` int(11) DEFAULT NULL COMMENT '访客id',
  `operation` varchar(100) DEFAULT NULL COMMENT '动作， add, update, delete',
  `guest_name` varchar(256) DEFAULT NULL COMMENT '访客名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10108 DEFAULT CHARSET=utf8 COMMENT='记录访客修改记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_guest_role`
--

DROP TABLE IF EXISTS `tb_guest_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_guest_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '访客角色名称',
  `security_level` int(11) DEFAULT NULL COMMENT '安全级别， 1-安全， 2-警告， 3-危险， 4-未知',
  `auto_open_door` tinyint(4) DEFAULT NULL COMMENT '1-自动开门， 0-不自动开门',
  `create_time` datetime DEFAULT NULL,
  `limit_time` tinyint(1) DEFAULT '0' COMMENT '是否限制开门时间，如果限制，则需要对每个房客设置开门时间',
  `open_time` int(11) DEFAULT '7' COMMENT '默认开门时间段，默认为7天；0表示永久',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8 COMMENT='访客角色表， 管理访客角色， 可以修改';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_init_session`
--

DROP TABLE IF EXISTS `tb_init_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_init_session` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `session` varchar(32) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL COMMENT '关联的userid',
  `is_available` tinyint(1) DEFAULT NULL COMMENT '是否有效',
  `platform` varchar(32) DEFAULT NULL COMMENT 'android or ios',
  `platform_version` varchar(16) DEFAULT NULL COMMENT '4.0,5.0etc',
  `version` int(11) DEFAULT NULL COMMENT 'app版本数字',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ipc`
--

DROP TABLE IF EXISTS `tb_ipc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_ipc` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `address` varchar(512) DEFAULT NULL COMMENT '摄像头地址， 如 rtsp://192.168.3.1',
  `door_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建记录时服务器时间',
  `camera_id` varchar(64) DEFAULT NULL COMMENT ' 摄像头在sdk私库中的id',
  `snap_timestamp` varchar(32) DEFAULT NULL COMMENT 'sdk抓拍的时间',
  `netaddress` varchar(512) DEFAULT NULL COMMENT '摄像头外网地址，开发测试用',
  PRIMARY KEY (`id`),
  KEY `door_id` (`door_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_snapshot`
--

DROP TABLE IF EXISTS `tb_snapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_snapshot` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `imagefile` varchar(256) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ipc_id` int(11) DEFAULT NULL COMMENT '摄像头id,与tb_ipc中id对应',
  `guest_code` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ipc_id` (`ipc_id`),
  KEY `create_time` (`create_time`),
  KEY `guest_code` (`guest_code`)
) ENGINE=InnoDB AUTO_INCREMENT=153100 DEFAULT CHARSET=utf8 COMMENT='快照记录，检测到的人脸都会别记录在这';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_snapshot_face`
--

DROP TABLE IF EXISTS `tb_snapshot_face`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_snapshot_face` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `snapshot_id` int(11) DEFAULT NULL,
  `guest_code` varchar(128) DEFAULT NULL,
  `ipc_id` int(11) DEFAULT NULL,
  `galleries` varchar(128) DEFAULT NULL,
  `meta` varchar(128) DEFAULT NULL,
  `photo` varchar(512) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `x1` int(11) DEFAULT NULL,
  `x2` int(11) DEFAULT NULL,
  `y1` int(11) DEFAULT NULL,
  `y2` int(11) DEFAULT NULL,
  `normalized` varchar(512) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `thumbnail` varchar(512) DEFAULT NULL,
  `photo_hash` varchar(64) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL COMMENT 'guest_code',
  `face_id` bigint(11) DEFAULT NULL COMMENT 'sdk face code',
  `gender` varchar(9) DEFAULT NULL,
  `confidence` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ipc_id` (`ipc_id`),
  KEY `snapshot_id` (`snapshot_id`),
  KEY `guest_code` (`guest_code`)
) ENGINE=InnoDB AUTO_INCREMENT=29705 DEFAULT CHARSET=utf8 COMMENT='快照中发现是库中存储的人脸就在该表中存储';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_sys`
--

DROP TABLE IF EXISTS `tb_sys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `keyname` varchar(128) DEFAULT NULL COMMENT '配置名称',
  `value` varchar(512) DEFAULT NULL COMMENT '配置值',
  `comment` varchar(256) DEFAULT NULL COMMENT '配置信息描述',
  `name` varchar(128) DEFAULT NULL COMMENT '',
  `update_time` datetime DEFAULT NULL,
  `update_user_id` varchar(64) DEFAULT NULL COMMENT '更新信息的用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='系统配置相关参数';
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `tb_sys`
--

LOCK TABLES `tb_sys` WRITE;

INSERT INTO `tb_sys` VALUES (1,'confidence','75','百分比， 比如80%，则填80','信任度界线','2018-04-09 08:31:40','1')(2,'sysname','恩钛智慧小区','NULL','系统名称，在保安用户界面显示','2017-11-14 16:40:26','1');

UNLOCK TABLES;
--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `password` varchar(32) NOT NULL DEFAULT '',
  `mobile` varchar(32) NOT NULL DEFAULT '' COMMENT '不能重复',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `role_id` int(11) NOT NULL DEFAULT '1' COMMENT '1-系统管理员，2-操作员，3-保安',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-正常，1-冻结',
  `create_time` datetime DEFAULT NULL COMMENT '用户创建时间',
  `last_time` datetime DEFAULT NULL COMMENT '用户最后访问时间',
  `door_id` int(11) DEFAULT NULL COMMENT '用户所属的门岗id，只有当用户为保安用户时有效',
  PRIMARY KEY (`id`),
  KEY `mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1,'c4ca4238a0b923820dcc509a6f75849b','1','管理员A',1,0,'2017-07-02 22:12:54',NULL,NULL),(36,'c4ca4238a0b923820dcc509a6f75849b','139','西门保安',3,0,'2017-07-15 18:01:57',NULL,8);
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-26 14:14:08
