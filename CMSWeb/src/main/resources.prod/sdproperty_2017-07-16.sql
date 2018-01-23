# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 182.18.26.42 (MySQL 5.6.35)
# Database: sdproperty
# Generation Time: 2017-07-16 14:51:49 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table tb_door
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_door`;

CREATE TABLE `tb_door` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `address` varchar(128) DEFAULT NULL COMMENT '位置地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门岗表，一个小区有1-n个门岗， 门禁和ipc，以来门岗';

LOCK TABLES `tb_door` WRITE;
/*!40000 ALTER TABLE `tb_door` DISABLE KEYS */;

INSERT INTO `tb_door` (`id`, `name`, `address`, `create_time`)
VALUES
	(1,'小区大门','小区大门口','2017-07-15 16:29:44'),
	(7,'小区东门','东门','2017-07-15 17:43:06'),
	(8,'小区西门','西门','2017-07-15 17:43:18'),
	(9,'小区南门','南门','2017-07-15 17:43:30');

/*!40000 ALTER TABLE `tb_door` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_door_lock
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_door_lock`;

CREATE TABLE `tb_door_lock` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `door_id` int(11) DEFAULT NULL,
  `address` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门禁，门锁，一个门岗最多有一个门锁， 也可能没有';



# Dump of table tb_guest
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_guest`;

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
  PRIMARY KEY (`id`),
  KEY `guest_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='访客表， 访客为guest';

LOCK TABLES `tb_guest` WRITE;
/*!40000 ALTER TABLE `tb_guest` DISABLE KEYS */;

INSERT INTO `tb_guest` (`id`, `image`, `code`, `guest_role_id`, `first_time`, `last_time`, `create_time`, `name`, `card_no`, `access_guest_id`, `age`, `gender`, `room`, `count`)
VALUES
	(1,NULL,'',1000,NULL,NULL,'2017-07-10 08:30:52','11','1',NULL,1,NULL,'11',NULL),
	(5,NULL,'',1000,NULL,NULL,'2017-07-10 08:39:09','11','1',NULL,1,NULL,'1',NULL),
	(6,NULL,'',4,NULL,NULL,'2017-07-10 08:41:48','11','11',NULL,11,NULL,'11',NULL),
	(7,NULL,'',1000,NULL,NULL,'2017-07-10 08:50:30','','',NULL,166,NULL,'',NULL),
	(8,NULL,'',1,NULL,NULL,'2017-07-13 13:23:36','','',NULL,25,NULL,'',NULL);

/*!40000 ALTER TABLE `tb_guest` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_guest_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_guest_role`;

CREATE TABLE `tb_guest_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '访客角色名称',
  `security_level` int(11) DEFAULT NULL COMMENT '安全级别， 1-安全， 2-警告， 3-危险， 4-未知',
  `auto_open_door` tinyint(4) DEFAULT NULL COMMENT '1-自动开门， 0-不自动开门',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='访客角色表， 管理访客角色， 可以修改';

LOCK TABLES `tb_guest_role` WRITE;
/*!40000 ALTER TABLE `tb_guest_role` DISABLE KEYS */;

INSERT INTO `tb_guest_role` (`id`, `name`, `security_level`, `auto_open_door`, `create_time`)
VALUES
	(1,'业主',1,1,'2017-07-06 22:43:15'),
	(2,'客人',1,1,'2017-07-06 22:43:22'),
	(3,'快递',2,0,'2017-07-08 10:55:01'),
	(4,'外卖',3,0,'2017-07-06 22:42:57'),
	(1000,'陌生人',4,0,'2017-07-06 22:42:57');

/*!40000 ALTER TABLE `tb_guest_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_init_session
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_init_session`;

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



# Dump of table tb_ipc
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_ipc`;

CREATE TABLE `tb_ipc` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `address` varchar(1024) DEFAULT NULL COMMENT '摄像头地址， 如 rtsp://192.168.3.1',
  `door_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建记录时服务器时间',
  `camera_id` varchar(64) DEFAULT NULL COMMENT ' 摄像头在sdk私库中的id',
  `snap_timestamp` varchar(32) DEFAULT NULL COMMENT 'sdk抓拍的时间',
  PRIMARY KEY (`id`),
  KEY `door_id` (`door_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `tb_ipc` WRITE;
/*!40000 ALTER TABLE `tb_ipc` DISABLE KEYS */;

INSERT INTO `tb_ipc` (`id`, `name`, `address`, `door_id`, `create_time`, `camera_id`, `snap_timestamp`)
VALUES
	(1,'小区大门','小区入口',1,'2017-07-04 19:06:29','3d9c794e-cf9b-4e7f-9b6e-16c7d580250b',NULL),
	(3,'ipc1','ddd',1,'2017-07-15 16:29:18',NULL,NULL),
	(4,'ee','ee',7,'2017-07-06 23:22:49',NULL,NULL);

/*!40000 ALTER TABLE `tb_ipc` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_snapshot
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_snapshot`;

CREATE TABLE `tb_snapshot` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `imagefile` varchar(256) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ipc_id` int(11) DEFAULT NULL COMMENT '拍照的摄像头id',
  PRIMARY KEY (`id`),
  KEY `ipc_id` (`ipc_id`),
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='快照，一次快照，有1-n个人，待定';

LOCK TABLES `tb_snapshot` WRITE;
/*!40000 ALTER TABLE `tb_snapshot` DISABLE KEYS */;

INSERT INTO `tb_snapshot` (`id`, `imagefile`, `create_time`, `ipc_id`)
VALUES
	(1,NULL,'2017-07-12 13:49:24',0),
	(2,NULL,'2017-07-12 13:50:07',0),
	(3,NULL,'2017-07-12 13:51:21',0),
	(4,NULL,'2017-07-12 14:01:03',0),
	(5,NULL,'2017-07-12 14:01:31',0),
	(6,NULL,'2017-07-12 14:07:39',0),
	(7,NULL,'2017-07-12 14:08:03',0),
	(8,NULL,'2017-07-12 14:10:33',0),
	(9,NULL,'2017-07-12 14:10:49',0),
	(10,NULL,'2017-07-12 14:10:56',0),
	(11,NULL,'2017-07-12 14:11:56',0),
	(12,NULL,'2017-07-12 14:12:00',0),
	(13,NULL,'2017-07-12 14:15:51',0),
	(14,NULL,'2017-07-12 14:17:40',0),
	(15,NULL,'2017-07-12 14:18:56',0),
	(16,NULL,'2017-07-12 14:28:35',0),
	(17,NULL,'2017-07-12 14:31:33',0),
	(18,NULL,'2017-07-12 14:31:37',0),
	(19,NULL,'2017-07-12 14:31:47',0),
	(20,NULL,'2017-07-12 14:31:54',0),
	(21,NULL,'2017-07-12 14:32:17',0),
	(22,NULL,'2017-07-12 14:39:49',0),
	(23,NULL,'2017-07-12 14:39:51',0),
	(24,NULL,'2017-07-12 14:39:57',0),
	(25,NULL,'2017-07-12 14:40:08',0),
	(26,NULL,'2017-07-12 14:40:11',0),
	(27,NULL,'2017-07-12 14:40:15',0),
	(28,NULL,'2017-07-12 14:40:19',0),
	(29,NULL,'2017-07-12 14:40:23',0),
	(30,NULL,'2017-07-12 14:40:28',0),
	(31,NULL,'2017-07-12 14:40:34',0),
	(32,NULL,'2017-07-12 14:40:38',0),
	(33,NULL,'2017-07-12 14:40:40',0),
	(34,NULL,'2017-07-12 14:40:50',0),
	(35,NULL,'2017-07-12 14:59:15',0),
	(36,NULL,'2017-07-12 14:59:17',0),
	(37,NULL,'2017-07-12 14:59:19',0),
	(38,NULL,'2017-07-12 15:25:13',0),
	(39,NULL,'2017-07-13 14:09:07',0),
	(40,NULL,'2017-07-13 14:09:27',0),
	(41,NULL,'2017-07-13 14:09:34',0),
	(42,NULL,'2017-07-13 14:09:43',0),
	(43,NULL,'2017-07-13 14:16:39',0),
	(44,NULL,'2017-07-13 14:17:24',0),
	(45,NULL,'2017-07-13 14:18:06',0),
	(46,NULL,'2017-07-13 14:18:38',0),
	(47,NULL,'2017-07-13 14:18:43',0),
	(48,NULL,'2017-07-13 14:39:24',0),
	(49,NULL,'2017-07-13 14:39:44',0),
	(50,NULL,'2017-07-13 14:39:59',0),
	(51,NULL,'2017-07-13 14:40:45',0),
	(52,NULL,'2017-07-13 14:41:11',0),
	(53,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:32:43',1),
	(54,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:33:46',1),
	(55,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:35:08',1),
	(56,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:36:01',1),
	(57,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:36:58',1),
	(58,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:38:40',1),
	(59,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:43:06',1),
	(60,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:47:16',1),
	(61,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:48:10',1),
	(62,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:48:33',1),
	(63,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:48:38',1),
	(64,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:51:21',1),
	(65,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:53:15',1),
	(66,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:55:43',1),
	(67,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 16:57:47',1),
	(68,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 21:16:41',1),
	(69,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 21:17:07',1),
	(70,'/home/n-tech-xu/images/2017-07-13/14:41:10.34702.jpeg','2017-07-15 21:17:08',1);

/*!40000 ALTER TABLE `tb_snapshot` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_snapshot_face
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_snapshot_face`;

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
  `face_id` bigint(11) DEFAULT NULL COMMENT 'sdk 传过来的id，保留',
  `gender` varchar(9) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ipc_id` (`ipc_id`),
  KEY `snapshot_id` (`snapshot_id`),
  KEY `guest_code` (`guest_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每一次抓拍图像，里面的对应人的信息，通过snapshot_id关联抓拍事件，通过guest_code关联用户';

LOCK TABLES `tb_snapshot_face` WRITE;
/*!40000 ALTER TABLE `tb_snapshot_face` DISABLE KEYS */;

INSERT INTO `tb_snapshot_face` (`id`, `snapshot_id`, `guest_code`, `ipc_id`, `galleries`, `meta`, `photo`, `timestamp`, `x1`, `x2`, `y1`, `y2`, `normalized`, `age`, `thumbnail`, `photo_hash`, `person_id`, `face_id`, `gender`)
VALUES
	(4,67,'20',1,'','','http://attach.bbs.miui.com/forum/201706/08/072245vk4opx94vzkkj9jr.jpg',NULL,0,199,-16,183,'http://127.0.0.1:3333/uploads//20170713/14999280429919252.jpeg',20,'http://127.0.0.1:3333/uploads//20170713/1499928042989949.jpeg','3cbfcf2fafa58d043355476e0e3c77a4',20,3839815790104905,'male'),
	(5,68,'20',1,'','','http://attach.bbs.miui.com/forum/201706/08/072245vk4opx94vzkkj9jr.jpg',NULL,0,199,-16,183,'http://127.0.0.1:3333/uploads//20170713/14999280429919252.jpeg',20,'http://127.0.0.1:3333/uploads//20170713/1499928042989949.jpeg','3cbfcf2fafa58d043355476e0e3c77a4',20,3839815790104905,'male'),
	(6,69,'20',1,'','','http://attach.bbs.miui.com/forum/201706/08/072245vk4opx94vzkkj9jr.jpg',NULL,0,199,-16,183,'http://127.0.0.1:3333/uploads//20170713/14999280429919252.jpeg',20,'http://127.0.0.1:3333/uploads//20170713/1499928042989949.jpeg','3cbfcf2fafa58d043355476e0e3c77a4',20,3839815790104905,'male'),
	(7,70,'20',1,'','','http://attach.bbs.miui.com/forum/201706/08/072245vk4opx94vzkkj9jr.jpg',NULL,0,199,-16,183,'http://127.0.0.1:3333/uploads//20170713/14999280429919252.jpeg',20,'http://127.0.0.1:3333/uploads//20170713/1499928042989949.jpeg','3cbfcf2fafa58d043355476e0e3c77a4',20,3839815790104905,'male');

/*!40000 ALTER TABLE `tb_snapshot_face` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_sys
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_sys`;

CREATE TABLE `tb_sys` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统设置表，比如设置 图片链接的域名头部，  license等';



# Dump of table tb_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_user`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;

INSERT INTO `tb_user` (`id`, `password`, `mobile`, `name`, `role_id`, `status`, `create_time`, `last_time`, `door_id`)
VALUES
	(1,'1','1','31',1,0,'2017-07-02 22:12:54',NULL,NULL),
	(32,'1','133','33',2,0,'2017-07-02 22:12:35',NULL,NULL),
	(33,'1','13333','南门保安',1,0,'2017-07-15 18:00:58',NULL,9),
	(34,'1','1444','东门保安',3,0,'2017-07-15 18:00:43',NULL,7),
	(35,'1','13333','大门保安张三',3,0,'2017-07-15 17:43:58',NULL,1),
	(36,'1','139','西门保安',3,0,'2017-07-15 18:01:57',NULL,8);

/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
