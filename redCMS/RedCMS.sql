-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.7.16


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema redcmsv6
--

CREATE DATABASE IF NOT EXISTS redcmsv6;
USE redcmsv6;

--
-- Temporary table structure for view `alldata`
--
DROP TABLE IF EXISTS `alldata`;
DROP VIEW IF EXISTS `alldata`;
CREATE TABLE `alldata` (
  `id` bigint(20) unsigned,
  `channel_id` int(11) unsigned,
  `title` varchar(100),
  `tags` varchar(55),
  `author` varchar(25),
  `level` int(11) unsigned,
  `txt1` longtext,
  `txt2` longtext,
  `dis` mediumtext,
  `state` int(11) unsigned,
  `createtime` datetime,
  `pic1` varchar(200),
  `pic2` varchar(200),
  `pic3` varchar(200),
  `links` varchar(100),
  `c1` varchar(100),
  `c2` varchar(100),
  `c3` varchar(100),
  `c4` varchar(100),
  `n1` int(11) unsigned,
  `n2` int(11) unsigned,
  `n3` int(11) unsigned,
  `d1` varchar(45),
  `d2` varchar(45),
  `attach1` varchar(100),
  `attach2` varchar(100),
  `content_tem` varchar(65)
);

--
-- Definition of table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uname` varchar(45) NOT NULL COMMENT '用户名',
  `upwd` varchar(45) NOT NULL COMMENT '密码',
  `upur` varchar(45) DEFAULT '100000000' COMMENT '权限',
  `active` tinyint(1) unsigned DEFAULT '1' COMMENT '活动',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='管理员帐号';

--
-- Dumping data for table `admin`
--

/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` (`id`,`uname`,`upwd`,`upur`,`active`) VALUES 
 (1,'admin@qq.com','21232f297a57a5a743894a0e4a801fc3','100000000',1),
 (5,'mysql@qq.com','21232f297a57a5a743894a0e4a801fc3','001000',1);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;


--
-- Definition of table `attachs`
--

DROP TABLE IF EXISTS `attachs`;
CREATE TABLE `attachs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `path` varchar(100) NOT NULL COMMENT '新路径',
  `mimetype` varchar(55) DEFAULT NULL COMMENT 'MIME',
  `orgname` varchar(55) NOT NULL COMMENT '原名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='附件';

--
-- Dumping data for table `attachs`
--

/*!40000 ALTER TABLE `attachs` DISABLE KEYS */;
INSERT INTO `attachs` (`id`,`path`,`mimetype`,`orgname`) VALUES 
 (2,'/redcmsv6/ups/image/20181206//20181206095530_469.jpg','image/jpeg','hacker.jpg'),
 (3,'/redcmsv6/ups/image/20181206//20181206103535_928.jpg','image/jpeg','hacker.jpg'),
 (4,'/redcmsv6/ups/image/20181206//20181206104040_407.jpg','image/jpeg','timg.jpg'),
 (5,'/redcmsv6/ups/image/20181206//20181206104058_10.jpg','image/jpeg','hacker.jpg'),
 (6,'/redcmsv6/ups/image/20181206//20181206105716_668.jpg','image/jpeg','hacker.jpg'),
 (7,'/redcmsv6/ups/image/20181206//20181206110522_657.jpg','image/jpeg','hacker.jpg'),
 (8,'/redcmsv6/ups/image/20181208//20181208202708_456.jpg','image/jpeg','hacker.jpg'),
 (9,'/redcmsv6/ups/image/20181208//20181208205145_169.jpg','image/jpeg','hacker.jpg'),
 (10,'/redcmsv6/ups/image/20181209//20181209094004_571.jpg','image/jpeg','hacker.jpg'),
 (11,'/redcmsv6/ups/image/20181209//20181209101129_418.jpg','image/jpeg','hacker.jpg'),
 (12,'/redcmsv6/ups/image/20181209//20181209101138_434.jpg','image/jpeg','hacker.jpg'),
 (13,'/redcmsv6/ups/image/20181209//20181209101146_404.jpg','image/jpeg','timg.jpg'),
 (14,'/redcmsv6/ups/image/20181209//20181209101331_723.jpg','image/jpeg','hacker.jpg'),
 (15,'/redcmsv6/ups/image/20181209//20181209101339_540.jpg','image/jpeg','timg.jpg'),
 (16,'/redcmsv6/ups/image/20181209//20181209103205_153.jpg','image/jpeg','hacker.jpg'),
 (17,'/redcmsv6/ups/image/20181209//20181209110630_270.jpg','image/jpeg','timg.jpg'),
 (18,'/redcmsv6/ups/image/20181209//20181209110736_471.jpg','image/jpeg','hacker.jpg'),
 (19,'/redcmsv6/ups/image/20181209//20181209110803_692.jpg','image/jpeg','hacker.jpg'),
 (20,'/redcmsv6/ups/image/20181209//20181209113649_273.jpg','image/jpeg','hacker.jpg'),
 (21,'/redcmsv6/ups/image/20181209//20181209114416_265.jpg','image/jpeg','hacker.jpg'),
 (22,'/redcmsv6/ups/image/20181211//20181211092047_259.jpg','image/jpeg','hacker.jpg'),
 (23,'/redcmsv6/ups/image/20181220//20181220085325_734.jpg','image/jpeg','hacker.jpg'),
 (24,'/redcmsv6/ups/image/20181220//20181220085728_335.jpg','image/jpeg','a头像.jpg'),
 (25,'/redcmsv6/ups/image/20181220//20181220090018_1.jpg','image/jpeg','头像.jpg'),
 (26,'/redcmsv6/ups/image/20181220//20181220090209_363.jpg','image/jpeg','头像.jpg'),
 (27,'/redcmsv6/ups/image/20181220//20181220110713_698.jpg','image/jpeg','hacker.jpg');
/*!40000 ALTER TABLE `attachs` ENABLE KEYS */;


--
-- Definition of table `channel`
--

DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `model_id` bigint(20) unsigned DEFAULT NULL COMMENT '''模型id''',
  `name` varchar(45) NOT NULL COMMENT '''栏目名字''',
  `title` varchar(65) DEFAULT NULL COMMENT '''meta标题''',
  `keywords` varchar(65) DEFAULT NULL COMMENT '''meta关键词''',
  `description` varchar(100) DEFAULT NULL COMMENT '''meta描述''',
  `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '''父id''',
  `pic01` varchar(65) DEFAULT NULL COMMENT '图1',
  `pic02` varchar(45) DEFAULT NULL COMMENT '图2',
  `priority` int(2) unsigned DEFAULT '10' COMMENT '排序',
  `links` varchar(100) DEFAULT NULL COMMENT '外链',
  `t_name` varchar(45) DEFAULT NULL COMMENT '分表名',
  `index_tem` varchar(45) DEFAULT NULL COMMENT 'pc首页模版',
  `list_tem` varchar(65) DEFAULT NULL COMMENT 'pc列表页模版',
  `content_tem` varchar(65) DEFAULT NULL COMMENT 'pc内容页',
  `create_time` datetime DEFAULT NULL COMMENT '增加时间',
  `txt` text COMMENT '内容',
  `txt1` text COMMENT '扩展',
  `txt2` text COMMENT '扩展',
  `num01` int(10) unsigned DEFAULT NULL COMMENT '扩展01',
  `num02` int(10) unsigned DEFAULT NULL COMMENT '扩展01',
  `date1` varchar(45) DEFAULT NULL COMMENT '扩展01',
  `date2` varchar(45) DEFAULT NULL COMMENT '扩展01',
  PRIMARY KEY (`id`),
  KEY `FK_channel_1` (`model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='栏目';

--
-- Dumping data for table `channel`
--

/*!40000 ALTER TABLE `channel` DISABLE KEYS */;
INSERT INTO `channel` (`id`,`model_id`,`name`,`title`,`keywords`,`description`,`parent_id`,`pic01`,`pic02`,`priority`,`links`,`t_name`,`index_tem`,`list_tem`,`content_tem`,`create_time`,`txt`,`txt1`,`txt2`,`num01`,`num02`,`date1`,`date2`) VALUES 
 (3,4,'通信与信息学院','通工','安全','对抗',0,'/redcmsv6/ups/image/20181209/20181209103205_153.jpg','',1,'link1','data1','new_index','new_list','down_content','2018-12-19 21:35:16','<div align=\"left\">\r\n	<img src=\"/redcmsv6/ups/image/20181206/20181206104058_10.jpg\" alt=\"\" /><br />\r\n</div>','<h1>\r\n	内容一<br />\r\n</h1>','内容二',3,3,'2018-12-02 00:00:00','2018-12-04 00:00:00'),
 (4,2,'自动化学院','自动化','电气','测控',0,'','',1,'link2','data1','new_index','new_list','down_content','2018-12-19 20:46:09','<h2>\r\n	内容一 <br />\r\n</h2>','<h2>\r\n	内容二 <br />\r\n</h2>','内容三',3,2,'2018-12-12 00:00:00','2018-12-06 10:43:49'),
 (6,4,'电子信息工程学院','光电','电磁','电子',0,'','',1,'link4','data1','new_index','new_list','down_content','2018-12-19 20:46:36','<h1>\r\n	内容一\r\n</h1>','','',1,1,'','2018-12-06 00:00:00'),
 (8,2,'自动化','1班','2班','',4,'','',10,'link5','data2','new_index','new_list','down_content','2018-12-19 20:46:26','<h2>\r\n	内容一\r\n</h2>','','',1,1,'','2018-12-07 00:00:00'),
 (9,3,'教务办','处理学籍信息','','',0,'','',1,'xupt','data1','new_index','new_list','down_content','2018-12-19 20:47:01','教务办<br />','','',1,1,'2018-12-11 00:00:00','2018-12-06 10:52:29'),
 (11,4,'通信工程','通工1601','通工1610','通工1611',3,'','',10,'link7','data2','new_index','new_list','down_content','2018-12-19 21:35:23','<img src=\"/redcmsv6/ups/image/20181208/20181208202708_456.jpg\" alt=\"\" />','','',1,1,'',''),
 (12,4,'光电','光电1601','光电1602','',6,'','',10,'link8','data2','new_index','new_list','down_content','2018-12-19 20:46:48','<img src=\"/redcmsv6/ups/image/20181208/20181208205145_169.jpg\" alt=\"\" />','内容一','',1,1,'','2018-12-08 20:52:01'),
 (13,4,'安全工程','安全专业','meta','meta',3,'/redcmsv6/ups/image/20181220/20181220085728_335.jpg','',10,'link','data2','index','new_list','down_content','2018-12-20 08:58:01','我是内容<br />','我是内容一','我是内容二',2,4,'2018-12-25 00:00:00','2018-12-20 00:00:00');
/*!40000 ALTER TABLE `channel` ENABLE KEYS */;


--
-- Definition of table `channel_attr`
--

DROP TABLE IF EXISTS `channel_attr`;
CREATE TABLE `channel_attr` (
  `channel_id` bigint(20) unsigned NOT NULL,
  `field_name` varchar(100) NOT NULL,
  `field_value` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='栏目扩展属性';

--
-- Dumping data for table `channel_attr`
--

/*!40000 ALTER TABLE `channel_attr` DISABLE KEYS */;
INSERT INTO `channel_attr` (`channel_id`,`field_name`,`field_value`) VALUES 
 (33,'price','null'),
 (6,'price','1200'),
 (12,'price','234'),
 (3,'price','250'),
 (11,'price',''),
 (13,'price','34 ');
/*!40000 ALTER TABLE `channel_attr` ENABLE KEYS */;


--
-- Definition of table `channel_field`
--

DROP TABLE IF EXISTS `channel_field`;
CREATE TABLE `channel_field` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `field` varchar(45) NOT NULL COMMENT '字段名',
  `field_dis` varchar(45) NOT NULL COMMENT '字段描述',
  `priority` int(2) unsigned DEFAULT '10' COMMENT '排序',
  `def_value` varchar(255) DEFAULT NULL COMMENT '默认值 ',
  `opt_value` varchar(255) DEFAULT NULL COMMENT '可选 值 ',
  `txt_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `help_info` varchar(65) DEFAULT NULL COMMENT '帮助信息',
  `data_type` tinyint(1) unsigned DEFAULT '1' COMMENT '数据类型 ',
  `is_single` tinyint(1) unsigned DEFAULT '1' COMMENT '单独行',
  `is_channel` tinyint(1) unsigned DEFAULT '1' COMMENT '是否是栏目',
  `is_custom` tinyint(1) unsigned DEFAULT '0' COMMENT '自定义',
  `is_display` tinyint(1) unsigned DEFAULT '1' COMMENT '显示',
  `is_required` tinyint(1) unsigned DEFAULT '0' COMMENT '必须',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='模型字段项';

--
-- Dumping data for table `channel_field`
--

/*!40000 ALTER TABLE `channel_field` DISABLE KEYS */;
INSERT INTO `channel_field` (`id`,`field`,`field_dis`,`priority`,`def_value`,`opt_value`,`txt_size`,`help_info`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`) VALUES 
 (1,'name','栏目名',3,NULL,NULL,NULL,NULL,1,0,1,2,1,1),
 (2,'title','meta标题',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (3,'keywords','meta关键词',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (4,'description','meta描述',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (5,'pic01','图1',10,NULL,NULL,NULL,NULL,5,0,1,0,1,0),
 (6,'pic02','图2',10,NULL,NULL,NULL,NULL,5,0,1,0,1,0),
 (7,'priority','排序',10,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (8,'links','外链',5,NULL,NULL,NULL,NULL,1,1,1,0,1,0),
 (9,'t_name','分表名',10,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (10,'index_tem','首页模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (11,'list_tem','列表模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (12,'content_tem','内容模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (13,'txt','内容',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (14,'txt1','内容1',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (15,'txt2','内容2',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (16,'num01','数字1',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (17,'num02','数字2',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (18,'date1','日期1',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (19,'date2','日期2',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (20,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0),
 (21,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0);
/*!40000 ALTER TABLE `channel_field` ENABLE KEYS */;


--
-- Definition of table `data`
--

DROP TABLE IF EXISTS `data`;
CREATE TABLE `data` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `channel_id` int(10) unsigned DEFAULT NULL COMMENT '栏目',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `tags` varchar(55) DEFAULT NULL COMMENT '标签',
  `author` varchar(25) DEFAULT 'admin' COMMENT '作者',
  `level` int(10) unsigned DEFAULT '9' COMMENT '级别',
  `txt1` mediumtext COMMENT '内容一',
  `txt2` mediumtext COMMENT '内容二',
  `dis` text COMMENT '描述',
  `state` int(10) unsigned DEFAULT NULL COMMENT '状态',
  `createtime` datetime DEFAULT NULL COMMENT '创建日期',
  `pic1` varchar(100) DEFAULT NULL COMMENT '图1',
  `pic2` varchar(100) DEFAULT NULL COMMENT '图2',
  `pic3` varchar(100) DEFAULT NULL COMMENT '图3',
  `links` varchar(100) DEFAULT NULL COMMENT '外链',
  `c1` varchar(100) DEFAULT NULL COMMENT '扩展字符串字段',
  `c2` varchar(100) DEFAULT NULL,
  `c3` varchar(100) DEFAULT NULL,
  `c4` varchar(100) DEFAULT NULL,
  `n1` int(10) unsigned DEFAULT NULL COMMENT '扩展数字字段',
  `n2` int(10) unsigned DEFAULT NULL,
  `n3` int(10) unsigned DEFAULT NULL,
  `d1` varchar(45) DEFAULT NULL COMMENT '扩展日期',
  `d2` varchar(45) DEFAULT NULL,
  `attach1` varchar(100) DEFAULT NULL COMMENT '附件1',
  `attach2` varchar(100) DEFAULT NULL COMMENT '附件2',
  `content_tem` varchar(65) DEFAULT NULL COMMENT '内空模版',
  PRIMARY KEY (`id`),
  KEY `FK_data0_1` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据';

--
-- Dumping data for table `data`
--

/*!40000 ALTER TABLE `data` DISABLE KEYS */;
/*!40000 ALTER TABLE `data` ENABLE KEYS */;


--
-- Definition of table `data1`
--

DROP TABLE IF EXISTS `data1`;
CREATE TABLE `data1` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `channel_id` int(10) unsigned DEFAULT NULL COMMENT '栏目',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `tags` varchar(55) DEFAULT NULL COMMENT '标签',
  `author` varchar(25) DEFAULT 'admin' COMMENT '作者',
  `level` int(10) unsigned DEFAULT '9' COMMENT '级别',
  `txt1` mediumtext COMMENT '内容一',
  `txt2` mediumtext COMMENT '内容二',
  `dis` text COMMENT '描述',
  `state` int(10) unsigned DEFAULT NULL COMMENT '状态',
  `createtime` datetime DEFAULT NULL COMMENT '创建日期',
  `pic1` varchar(200) DEFAULT NULL COMMENT '图1',
  `pic2` varchar(200) DEFAULT NULL COMMENT '图2',
  `pic3` varchar(200) DEFAULT NULL COMMENT '图3',
  `links` varchar(100) DEFAULT NULL COMMENT '外链',
  `c1` varchar(100) DEFAULT NULL COMMENT '扩展字符串字段',
  `c2` varchar(100) DEFAULT NULL,
  `c3` varchar(100) DEFAULT NULL,
  `c4` varchar(100) DEFAULT NULL,
  `n1` int(10) unsigned DEFAULT NULL COMMENT '扩展数字字段',
  `n2` int(10) unsigned DEFAULT NULL,
  `n3` int(10) unsigned DEFAULT NULL,
  `d1` varchar(45) DEFAULT NULL COMMENT '扩展日期',
  `d2` varchar(45) DEFAULT NULL,
  `attach1` varchar(100) DEFAULT NULL COMMENT '附件1',
  `attach2` varchar(100) DEFAULT NULL COMMENT '附件2',
  `content_tem` varchar(65) DEFAULT NULL COMMENT '内空模版',
  PRIMARY KEY (`id`),
  KEY `FK_data0_1` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据';

--
-- Dumping data for table `data1`
--

/*!40000 ALTER TABLE `data1` DISABLE KEYS */;
/*!40000 ALTER TABLE `data1` ENABLE KEYS */;


--
-- Definition of table `data2`
--

DROP TABLE IF EXISTS `data2`;
CREATE TABLE `data2` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `channel_id` int(10) unsigned DEFAULT NULL COMMENT '栏目',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `tags` varchar(55) DEFAULT NULL COMMENT '标签',
  `author` varchar(25) DEFAULT 'admin' COMMENT '作者',
  `level` int(10) unsigned DEFAULT '9' COMMENT '级别',
  `txt1` mediumtext COMMENT '内容一',
  `txt2` mediumtext COMMENT '内容二',
  `dis` text COMMENT '描述',
  `state` int(10) unsigned DEFAULT NULL COMMENT '状态',
  `createtime` datetime DEFAULT NULL COMMENT '创建日期',
  `pic1` varchar(100) DEFAULT NULL COMMENT '图1',
  `pic2` varchar(100) DEFAULT NULL COMMENT '图2',
  `pic3` varchar(100) DEFAULT NULL COMMENT '图3',
  `links` varchar(100) DEFAULT NULL COMMENT '外链',
  `c1` varchar(100) DEFAULT NULL COMMENT '扩展字符串字段',
  `c2` varchar(100) DEFAULT NULL,
  `c3` varchar(100) DEFAULT NULL,
  `c4` varchar(100) DEFAULT NULL,
  `n1` int(10) unsigned DEFAULT NULL COMMENT '扩展数字字段',
  `n2` int(10) unsigned DEFAULT NULL,
  `n3` int(10) unsigned DEFAULT NULL,
  `d1` varchar(45) DEFAULT NULL COMMENT '扩展日期',
  `d2` varchar(45) DEFAULT NULL,
  `attach1` varchar(100) DEFAULT NULL COMMENT '附件1',
  `attach2` varchar(100) DEFAULT NULL COMMENT '附件2',
  `content_tem` varchar(65) DEFAULT NULL COMMENT '内空模版',
  PRIMARY KEY (`id`),
  KEY `FK_data0_1` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据';

--
-- Dumping data for table `data2`
--

/*!40000 ALTER TABLE `data2` DISABLE KEYS */;
INSERT INTO `data2` (`id`,`channel_id`,`title`,`tags`,`author`,`level`,`txt1`,`txt2`,`dis`,`state`,`createtime`,`pic1`,`pic2`,`pic3`,`links`,`c1`,`c2`,`c3`,`c4`,`n1`,`n2`,`n3`,`d1`,`d2`,`attach1`,`attach2`,`content_tem`) VALUES 
 (1022326806589526017,24,'在夺载','','',9,'','枯','',1,'2018-07-26 11:44:39','','','','','','','','',1,1,1,'','',NULL,NULL,'down_content'),
 (1071588772090499072,11,'通工1611','班级','liakng',7,'content','<img src=\"/redcmsv6/ups/image/20181209/20181209101331_723.jpg\" alt=\"\" />','我们是王牌班级',1,'2018-12-09 10:14:07','/redcmsv6/ups/image/20181209/20181209101339_540.jpg','','','','','','','',1,1,1,'2018-12-13 00:00:00','2018-12-09 10:13:49','','','down_content'),
 (1071602476001546240,12,'光电1601','光电1601班级','likang',6,'content3','lallala<br />','<img src=\"/redcmsv6/ups/image/20181209/20181209110736_471.jpg\" alt=\"\" />',1,'2018-12-11 18:02:21','/redcmsv6/ups/image/20181209/20181209110803_692.jpg','','','','','','','',1,1,1,'2018-12-09 11:08:13','2018-12-09 11:08:11','','','down_content'),
 (1072300261545955328,8,'自动化1604','自动1604班级','likang',6,'SKR~','lallalalallala','我们是自动化专业！',1,'2018-12-19 22:52:51','/redcmsv6/ups/image/20181211/20181211092047_259.jpg','','','','','','','',1,1,1,'2018-12-22 00:00:00','2018-12-11 09:20:54','','','down_content'),
 (1075556620169187328,11,'通工1610','通工','孙维珍',4,'我是内容！','我是内容2.。。。。。。。。。。。。','我是通工1610班的一名同学！<br />',1,'2018-12-20 09:00:55','/redcmsv6/ups/image/20181220/20181220090018_1.jpg','','','link4','','','','',1,1,1,'2018-12-20 00:00:00','2018-12-21 00:00:00','','','down_content'),
 (1075556992333975552,11,'通工1604','王牌专业','大白',7,'我是内容1','我是内容2','我是通工1604 的一名同学！<br />',1,'2018-12-20 09:02:25','/redcmsv6/ups/image/20181220/20181220090209_363.jpg','','','','','','','',1,1,1,'2018-12-20 00:00:00','2018-12-21 00:00:00','','','down_content'),
 (1075588550558289920,13,'安全1601','安全','蒲潘婷',8,'我是内容1','我是内容2.。。。。。。。。。。。。。。。。。','<p>\r\n	我是描述..安全...\r\n</p>\r\n<p>\r\n	<br />\r\n</p>',2,'2018-12-25 19:39:25','/redcmsv6/ups/image/20181220/20181220110713_698.jpg','','','link2','','','','',1,1,1,'2018-12-03 00:00:00','2018-12-20 00:00:00','','','down_content'),
 (1077529636356808704,13,'安全1604','心动','不知',10,'content','content2<br />','<h1>\r\n	我是心动...<br />\r\n</h1>',1,'2018-12-25 19:40:59','','','','','','','','',1,1,1,'','','','','down_content'),
 (1077529796033961984,8,'自动化1603','自动','佘继峰',10,'','','我是描述。。。。',1,'2018-12-25 19:41:38','','','','','','','','',1,1,1,'','','','','down_content');
/*!40000 ALTER TABLE `data2` ENABLE KEYS */;


--
-- Definition of table `data3`
--

DROP TABLE IF EXISTS `data3`;
CREATE TABLE `data3` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `channel_id` int(10) unsigned DEFAULT NULL COMMENT '栏目',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `tags` varchar(55) DEFAULT NULL COMMENT '标签',
  `author` varchar(25) DEFAULT 'admin' COMMENT '作者',
  `level` int(10) unsigned DEFAULT '9' COMMENT '级别',
  `txt1` mediumtext COMMENT '内容一',
  `txt2` mediumtext COMMENT '内容二',
  `dis` text COMMENT '描述',
  `state` int(10) unsigned DEFAULT NULL COMMENT '状态',
  `createtime` datetime DEFAULT NULL COMMENT '创建日期',
  `pic1` varchar(100) DEFAULT NULL COMMENT '图1',
  `pic2` varchar(100) DEFAULT NULL COMMENT '图2',
  `pic3` varchar(100) DEFAULT NULL COMMENT '图3',
  `links` varchar(100) DEFAULT NULL COMMENT '外链',
  `c1` varchar(100) DEFAULT NULL COMMENT '扩展字符串字段',
  `c2` varchar(100) DEFAULT NULL,
  `c3` varchar(100) DEFAULT NULL,
  `c4` varchar(100) DEFAULT NULL,
  `n1` int(10) unsigned DEFAULT NULL COMMENT '扩展数字字段',
  `n2` int(10) unsigned DEFAULT NULL,
  `n3` int(10) unsigned DEFAULT NULL,
  `d1` varchar(45) DEFAULT NULL COMMENT '扩展日期',
  `d2` varchar(45) DEFAULT NULL,
  `attach1` varchar(100) DEFAULT NULL COMMENT '附件1',
  `attach2` varchar(100) DEFAULT NULL COMMENT '附件2',
  `content_tem` varchar(65) DEFAULT NULL COMMENT '内空模版',
  PRIMARY KEY (`id`),
  KEY `FK_data0_1` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据';

--
-- Dumping data for table `data3`
--

/*!40000 ALTER TABLE `data3` DISABLE KEYS */;
/*!40000 ALTER TABLE `data3` ENABLE KEYS */;


--
-- Definition of table `data4`
--

DROP TABLE IF EXISTS `data4`;
CREATE TABLE `data4` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `channel_id` int(10) unsigned DEFAULT NULL COMMENT '栏目',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `tags` varchar(55) DEFAULT NULL COMMENT '标签',
  `author` varchar(25) DEFAULT 'admin' COMMENT '作者',
  `level` int(10) unsigned DEFAULT '9' COMMENT '级别',
  `txt1` mediumtext COMMENT '内容一',
  `txt2` mediumtext COMMENT '内容二',
  `dis` text COMMENT '描述',
  `state` int(10) unsigned DEFAULT NULL COMMENT '状态',
  `createtime` datetime DEFAULT NULL COMMENT '创建日期',
  `pic1` varchar(100) DEFAULT NULL COMMENT '图1',
  `pic2` varchar(100) DEFAULT NULL COMMENT '图2',
  `pic3` varchar(100) DEFAULT NULL COMMENT '图3',
  `links` varchar(100) DEFAULT NULL COMMENT '外链',
  `c1` varchar(100) DEFAULT NULL COMMENT '扩展字符串字段',
  `c2` varchar(100) DEFAULT NULL,
  `c3` varchar(100) DEFAULT NULL,
  `c4` varchar(100) DEFAULT NULL,
  `n1` int(10) unsigned DEFAULT NULL COMMENT '扩展数字字段',
  `n2` int(10) unsigned DEFAULT NULL,
  `n3` int(10) unsigned DEFAULT NULL,
  `d1` varchar(45) DEFAULT NULL COMMENT '扩展日期',
  `d2` varchar(45) DEFAULT NULL,
  `attach1` varchar(100) DEFAULT NULL COMMENT '附件1',
  `attach2` varchar(100) DEFAULT NULL COMMENT '附件2',
  `content_tem` varchar(65) DEFAULT NULL COMMENT '内空模版',
  PRIMARY KEY (`id`),
  KEY `FK_data0_1` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据';

--
-- Dumping data for table `data4`
--

/*!40000 ALTER TABLE `data4` DISABLE KEYS */;
/*!40000 ALTER TABLE `data4` ENABLE KEYS */;


--
-- Definition of table `data_attr`
--

DROP TABLE IF EXISTS `data_attr`;
CREATE TABLE `data_attr` (
  `data_id` bigint(20) unsigned DEFAULT NULL,
  `field_name` varchar(65) DEFAULT NULL,
  `field_value` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容扩展字段';

--
-- Dumping data for table `data_attr`
--

/*!40000 ALTER TABLE `data_attr` DISABLE KEYS */;
INSERT INTO `data_attr` (`data_id`,`field_name`,`field_value`) VALUES 
 (1071588772090499072,'price','1213'),
 (1071602476001546240,'price','31323'),
 (1075556620169187328,'price',''),
 (1075556992333975552,'price',''),
 (1075588550558289920,'price','3232'),
 (1077529636356808704,'price','');
/*!40000 ALTER TABLE `data_attr` ENABLE KEYS */;


--
-- Definition of table `data_field`
--

DROP TABLE IF EXISTS `data_field`;
CREATE TABLE `data_field` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `field` varchar(45) NOT NULL COMMENT '字段名',
  `field_dis` varchar(45) NOT NULL COMMENT '字段描述',
  `priority` int(2) unsigned DEFAULT '10' COMMENT '排序',
  `def_value` varchar(255) DEFAULT NULL COMMENT '默认值 ',
  `opt_value` varchar(255) DEFAULT NULL COMMENT '可选 值 ',
  `txt_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `help_info` varchar(65) DEFAULT NULL COMMENT '帮助信息',
  `data_type` tinyint(1) unsigned DEFAULT '1' COMMENT '数据类型 ',
  `is_single` tinyint(1) unsigned DEFAULT '1' COMMENT '单独行',
  `is_channel` tinyint(1) unsigned DEFAULT '1' COMMENT '是否是栏目',
  `is_custom` tinyint(1) unsigned DEFAULT '1' COMMENT '自定义',
  `is_display` tinyint(1) unsigned DEFAULT '1' COMMENT '显示',
  `is_required` tinyint(1) unsigned DEFAULT '0' COMMENT '必须',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='模型字段项';

--
-- Dumping data for table `data_field`
--

/*!40000 ALTER TABLE `data_field` DISABLE KEYS */;
INSERT INTO `data_field` (`id`,`field`,`field_dis`,`priority`,`def_value`,`opt_value`,`txt_size`,`help_info`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`) VALUES 
 (1,'title','标题',3,NULL,NULL,NULL,NULL,1,1,0,2,1,1),
 (2,'tags','标签',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (3,'author','作者',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (4,'level','排序',3,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (5,'txt1','内容1',6,NULL,NULL,NULL,NULL,1,1,0,2,1,0),
 (6,'txt2','内容2',6,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (7,'dis','描述',5,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (8,'state','状态',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (10,'pic1','图1',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (11,'pic2','图2',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (12,'pic3','图3',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (13,'links','外链',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (14,'c1','扩展字符串1',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (15,'c2','扩展字符串2',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (16,'c3','扩展字符串3',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (17,'c4','扩展字符串4',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (18,'n1','扩展数字1',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (19,'n2','扩展数字2',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (20,'n3','扩展数字3',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (21,'d1','日期1',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (22,'d2','日期2',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (23,'attachs','附件集',10,NULL,NULL,NULL,NULL,7,0,0,0,1,0),
 (24,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (25,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (26,'content_tem','内容模版',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0);
/*!40000 ALTER TABLE `data_field` ENABLE KEYS */;


--
-- Definition of table `model`
--

DROP TABLE IF EXISTS `model`;
CREATE TABLE `model` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(45) NOT NULL COMMENT '名字',
  `path` varchar(45) DEFAULT NULL COMMENT '路径',
  `title_width` int(10) DEFAULT '139' COMMENT '标题 图宽',
  `title_height` int(10) DEFAULT '139',
  `content_width` int(10) DEFAULT '310' COMMENT '内容图宽',
  `content_height` int(10) DEFAULT '310',
  `priority` int(10) DEFAULT '10' COMMENT '排序',
  `has_content` int(10) DEFAULT '1' COMMENT '是否有内容',
  `is_def` int(10) DEFAULT '0' COMMENT '是否默认',
  `template` varchar(45) DEFAULT NULL COMMENT '模版',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='模型表';

--
-- Dumping data for table `model`
--

/*!40000 ALTER TABLE `model` DISABLE KEYS */;
INSERT INTO `model` (`id`,`name`,`path`,`title_width`,`title_height`,`content_width`,`content_height`,`priority`,`has_content`,`is_def`,`template`) VALUES 
 (2,'图片','picture',130,130,310,310,2,-1,-1,'-1'),
 (3,'下载','download',130,130,310,310,3,-1,-1,'-1'),
 (4,'文章','article',130,130,310,310,1,-1,-1,'-1');
/*!40000 ALTER TABLE `model` ENABLE KEYS */;


--
-- Definition of table `model_item`
--

DROP TABLE IF EXISTS `model_item`;
CREATE TABLE `model_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `model_id` bigint(20) unsigned DEFAULT NULL,
  `field` varchar(45) NOT NULL COMMENT '字段名',
  `field_dis` varchar(45) NOT NULL COMMENT '字段描述',
  `priority` int(2) unsigned DEFAULT '10' COMMENT '排序',
  `def_value` varchar(255) DEFAULT NULL COMMENT '默认值 ',
  `opt_value` varchar(255) DEFAULT NULL COMMENT '可选 值 ',
  `txt_size` varchar(20) DEFAULT NULL COMMENT '长度',
  `help_info` varchar(65) DEFAULT NULL COMMENT '帮助信息',
  `data_type` int(1) DEFAULT '1' COMMENT '数据类型 ',
  `is_single` int(1) DEFAULT '1' COMMENT '单独行',
  `is_channel` int(1) DEFAULT '1' COMMENT '是否是栏目',
  `is_custom` int(1) DEFAULT '1' COMMENT '自定义',
  `is_display` int(1) DEFAULT '1' COMMENT '显示',
  `is_required` int(1) DEFAULT '0' COMMENT '必须',
  PRIMARY KEY (`id`),
  KEY `FK_model_item_1` (`model_id`),
  CONSTRAINT `FK_model_item_1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8 COMMENT='模型字段项';

--
-- Dumping data for table `model_item`
--

/*!40000 ALTER TABLE `model_item` DISABLE KEYS */;
INSERT INTO `model_item` (`id`,`model_id`,`field`,`field_dis`,`priority`,`def_value`,`opt_value`,`txt_size`,`help_info`,`data_type`,`is_single`,`is_channel`,`is_custom`,`is_display`,`is_required`) VALUES 
 (47,2,'name','栏目名',3,NULL,NULL,NULL,NULL,1,0,1,2,1,1),
 (48,2,'title','meta标题',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (49,2,'keywords','meta关键词',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (50,2,'description','meta描述',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (51,2,'pic01','图1',10,NULL,NULL,NULL,NULL,5,0,1,0,1,0),
 (52,2,'pic02','图2',10,NULL,NULL,NULL,NULL,5,0,1,0,1,0),
 (53,2,'priority','排序',10,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (54,2,'links','外链',5,NULL,NULL,NULL,NULL,1,1,1,0,1,0),
 (55,2,'t_name','分表名',10,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (56,2,'index_tem','首页模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (57,2,'list_tem','列表模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (58,2,'content_tem','内容模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (59,2,'txt','内容',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (60,2,'txt1','内容1',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (61,2,'txt2','内容2',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (62,2,'num01','数字1',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (63,2,'num02','数字2',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (64,2,'date1','日期1',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (65,2,'date2','日期2',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (66,2,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0),
 (67,2,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0),
 (68,2,'title','标题',3,NULL,NULL,NULL,NULL,1,1,0,2,1,1),
 (69,2,'tags','标签',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (70,2,'author','作者',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (71,2,'level','排序',3,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (72,2,'txt1','内容1',6,NULL,NULL,NULL,NULL,1,1,0,2,1,0),
 (73,2,'txt2','内容2',6,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (74,2,'dis','描述',5,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (75,2,'state','状态',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (76,2,'pic1','图1',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (77,2,'pic2','图2',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (78,2,'pic3','图3',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (79,2,'links','外链',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (80,2,'c1','扩展字符串1',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (81,2,'c2','扩展字符串2',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (82,2,'c3','扩展字符串3',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (83,2,'c4','扩展字符串4',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (84,2,'n1','扩展数字1',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (85,2,'n2','扩展数字2',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (86,2,'n3','扩展数字3',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (87,2,'d1','日期1',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (88,2,'d2','日期2',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (89,2,'attachs','附件集',10,NULL,NULL,NULL,NULL,7,0,0,0,1,0),
 (90,2,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (91,2,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (92,2,'content_tem','内容模版',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (93,3,'name','栏目名',3,NULL,NULL,NULL,NULL,1,0,1,2,1,1),
 (94,3,'title','meta标题',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (95,3,'keywords','meta关键词',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (96,3,'description','meta描述',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (97,3,'pic01','图1',10,NULL,NULL,NULL,NULL,5,0,1,0,1,0),
 (98,3,'pic02','图2',10,NULL,NULL,NULL,NULL,5,0,1,0,1,0),
 (99,3,'priority','排序',10,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (100,3,'links','外链',5,NULL,NULL,NULL,NULL,1,1,1,0,1,0),
 (101,3,'t_name','分表名',10,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (102,3,'index_tem','首页模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (103,3,'list_tem','列表模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (104,3,'content_tem','内容模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (105,3,'txt','内容',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (106,3,'txt1','内容1',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (107,3,'txt2','内容2',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (108,3,'num01','数字1',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (109,3,'num02','数字2',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (110,3,'date1','日期1',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (111,3,'date2','日期2',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (112,3,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0),
 (113,3,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0),
 (114,3,'title','标题',3,NULL,NULL,NULL,NULL,1,1,0,2,1,1),
 (115,3,'tags','标签',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (116,3,'author','作者',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (117,3,'level','排序',3,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (118,3,'txt1','内容1',6,NULL,NULL,NULL,NULL,1,1,0,2,1,0),
 (119,3,'txt2','内容2',6,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (120,3,'dis','描述',5,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (121,3,'state','状态',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (122,3,'pic1','图1',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (123,3,'pic2','图2',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (124,3,'pic3','图3',7,NULL,NULL,NULL,NULL,5,0,0,0,1,0),
 (125,3,'links','外链',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (126,3,'c1','扩展字符串1',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (127,3,'c2','扩展字符串2',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (128,3,'c3','扩展字符串3',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (129,3,'c4','扩展字符串4',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (130,3,'n1','扩展数字1',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (131,3,'n2','扩展数字2',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (132,3,'n3','扩展数字3',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (133,3,'d1','日期1',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (134,3,'d2','日期2',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (135,3,'attachs','附件集',10,NULL,NULL,NULL,NULL,7,0,0,0,1,0),
 (136,3,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (137,3,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (138,3,'content_tem','内容模版',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (141,4,'name','栏目名',3,NULL,NULL,NULL,NULL,1,0,1,2,1,1),
 (142,4,'title','meta标题',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (143,4,'keywords','meta关键词',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (144,4,'description','meta描述',4,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (145,4,'pic01','图1',10,'','','','',5,0,1,0,1,0),
 (146,4,'pic02','图2',10,'','','','',5,0,1,0,1,0),
 (147,4,'priority','排序',10,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (148,4,'links','外链',5,NULL,NULL,NULL,NULL,1,1,1,0,1,0),
 (149,4,'t_name','分表名',10,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (150,4,'index_tem','首页模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (151,4,'list_tem','列表模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (152,4,'content_tem','内容模版',7,NULL,NULL,NULL,NULL,1,0,1,0,1,0),
 (153,4,'txt','内容',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (154,4,'txt1','内容1',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (155,4,'txt2','内容2',5,NULL,NULL,NULL,NULL,3,1,1,0,1,0),
 (156,4,'num01','数字1',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (157,4,'num02','数字2',8,NULL,NULL,NULL,NULL,2,0,1,0,1,0),
 (158,4,'date1','日期1',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (159,4,'date2','日期2',9,NULL,NULL,NULL,NULL,4,0,1,0,1,0),
 (160,4,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0),
 (161,4,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,1,0,1,0),
 (162,4,'title','标题',3,NULL,NULL,NULL,NULL,1,1,0,2,1,1),
 (163,4,'tags','标签',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (164,4,'author','作者',3,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (165,4,'level','排序',3,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (166,4,'txt1','内容1',6,NULL,NULL,NULL,NULL,1,1,0,2,1,0),
 (167,4,'txt2','内容2',6,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (168,4,'dis','描述',5,NULL,NULL,NULL,NULL,3,1,0,0,1,0),
 (169,4,'state','状态',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (170,4,'pic1','图1',7,'','','','',5,0,0,0,1,0),
 (171,4,'pic2','图2',7,'','','','',5,0,0,0,1,0),
 (172,4,'pic3','图3',7,'','','','',5,0,0,0,1,0),
 (173,4,'links','外链',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (174,4,'c1','扩展字符串1',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (175,4,'c2','扩展字符串2',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (176,4,'c3','扩展字符串3',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (177,4,'c4','扩展字符串4',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (178,4,'n1','扩展数字1',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (179,4,'n2','扩展数字2',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (180,4,'n3','扩展数字3',10,NULL,NULL,NULL,NULL,2,0,0,0,1,0),
 (181,4,'d1','日期1',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (182,4,'d2','日期2',10,NULL,NULL,NULL,NULL,4,0,0,0,1,0),
 (183,4,'attachs','附件集',10,NULL,NULL,NULL,NULL,7,0,0,0,1,0),
 (184,4,'pics1','图集1',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (185,4,'pics2','图集2',10,NULL,NULL,NULL,NULL,6,0,0,0,1,0),
 (186,4,'content_tem','内容模版',10,NULL,NULL,NULL,NULL,1,0,0,0,1,0),
 (187,4,'price','单价',7,'0','','20','',3,1,0,1,1,0),
 (189,4,'price','单价',6,'22','','220','',1,1,1,1,1,0);
/*!40000 ALTER TABLE `model_item` ENABLE KEYS */;


--
-- Definition of table `pictures`
--

DROP TABLE IF EXISTS `pictures`;
CREATE TABLE `pictures` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `channel_id` bigint(20) unsigned DEFAULT NULL COMMENT '栏目id',
  `data_id` bigint(20) unsigned DEFAULT NULL COMMENT '数据id',
  `path` varchar(200) NOT NULL COMMENT '路径',
  `picdis` varchar(100) DEFAULT NULL COMMENT '描述',
  `priority` int(10) unsigned NOT NULL DEFAULT '10' COMMENT '排序',
  `sequ` int(10) unsigned DEFAULT NULL COMMENT '第几个图集',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='图集';

--
-- Dumping data for table `pictures`
--

/*!40000 ALTER TABLE `pictures` DISABLE KEYS */;
INSERT INTO `pictures` (`id`,`channel_id`,`data_id`,`path`,`picdis`,`priority`,`sequ`) VALUES 
 (15,NULL,NULL,'/redcmsv6/ups/image/20181206//20181206095504_3.jpg',NULL,10,NULL),
 (16,3,NULL,'/redcmsv6/ups/image/20181206//20181206104149_84.jpg','hacker',2,1),
 (17,3,NULL,'/redcmsv6/ups/image/20181206//20181206104200_722.jpg','desktop',3,2),
 (18,4,NULL,'/redcmsv6/ups/image/20181206//20181206104358_923.jpg','啦啦',2,1),
 (19,4,NULL,'/redcmsv6/ups/image/20181206//20181206104407_250.jpg','图集',7,1),
 (22,6,NULL,'/redcmsv6/ups/image/20181206//20181206104713_910.jpg','图集1',1,1),
 (23,8,NULL,'/redcmsv6/ups/image/20181206//20181206104851_137.jpg','hacker.jpg',1,1),
 (25,NULL,NULL,'/redcmsv6/ups/image/20181206//20181206105730_269.jpg',NULL,10,NULL),
 (26,NULL,NULL,'/redcmsv6/ups/image/20181206//20181206110534_74.jpg',NULL,10,NULL),
 (28,12,NULL,'/redcmsv6/ups/image/20181208//20181208205208_466.jpg','hacker',6,1),
 (29,12,NULL,'/redcmsv6/ups/image/20181208//20181208205217_10.jpg','timp.',7,2),
 (30,NULL,NULL,'/redcmsv6/ups/image/20181209//20181209101206_480.jpg',NULL,10,NULL),
 (31,NULL,NULL,'/redcmsv6/ups/image/20181209//20181209101206_967.jpg',NULL,10,NULL),
 (32,NULL,1071588772090499072,'/redcmsv6/ups/image/20181209//20181209101354_1.jpg','lal',4,1),
 (33,NULL,1071588772090499072,'/redcmsv6/ups/image/20181209//20181209101354_831.jpg','ziana',5,1),
 (34,NULL,1071602476001546240,'/redcmsv6/ups/image/20181209//20181209110819_180.jpg','ff',1,1),
 (35,NULL,1071602476001546240,'/redcmsv6/ups/image/20181209//20181209110819_513.jpg','fff',1,1),
 (36,NULL,1072300261545955328,'/redcmsv6/ups/image/20181211//20181211092059_929.jpg','hacker',5,1),
 (37,NULL,1072300261545955328,'/redcmsv6/ups/image/20181211//20181211092059_290.jpg','骇客',6,1),
 (38,NULL,NULL,'/redcmsv6/ups/image/20181220//20181220085255_362.jpg',NULL,10,NULL),
 (39,13,NULL,'/redcmsv6/ups/image/20181220//20181220085737_497.jpg','我是HEacker',8,1),
 (40,NULL,1075556620169187328,'/redcmsv6/ups/image/20181220//20181220090043_414.jpg','我是hacker',1,1),
 (41,NULL,1075556992333975552,'/redcmsv6/ups/image/20181220//20181220090217_813.jpg','hacker',1,1),
 (42,NULL,1075588550558289920,'/redcmsv6/ups/image/20181220//20181220110739_340.jpg','likang',8,2),
 (43,NULL,1077529636356808704,'/redcmsv6/ups/image/20181225//20181225194037_543.jpg','心动的感觉',1,2);
/*!40000 ALTER TABLE `pictures` ENABLE KEYS */;


--
-- Definition of table `table_function`
--

DROP TABLE IF EXISTS `table_function`;
CREATE TABLE `table_function` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `function` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='说明各个表的作用';

--
-- Dumping data for table `table_function`
--

/*!40000 ALTER TABLE `table_function` DISABLE KEYS */;
INSERT INTO `table_function` (`id`,`name`,`function`) VALUES 
 (1,'model','模型表，每一个模型对应栏目和内容字段'),
 (2,'Channel_filed','每个栏目的字段，固定的'),
 (3,'Data_filed','每个内容的字段，存放所有内容字段，表的字段是内容字段的性质'),
 (4,'Model_item','每个模型的所有内容和栏目字段都会保存'),
 (5,'Data_attr 和 Channel_attr','内容和栏目新增加的字段，也是自定义字段'),
 (6,'attachs','附件表'),
 (7,'admin','账号管理');
/*!40000 ALTER TABLE `table_function` ENABLE KEYS */;


--
-- Definition of view `alldata`
--

DROP TABLE IF EXISTS `alldata`;
DROP VIEW IF EXISTS `alldata`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `alldata` AS select `data1`.`id` AS `id`,`data1`.`channel_id` AS `channel_id`,`data1`.`title` AS `title`,`data1`.`tags` AS `tags`,`data1`.`author` AS `author`,`data1`.`level` AS `level`,`data1`.`txt1` AS `txt1`,`data1`.`txt2` AS `txt2`,`data1`.`dis` AS `dis`,`data1`.`state` AS `state`,`data1`.`createtime` AS `createtime`,`data1`.`pic1` AS `pic1`,`data1`.`pic2` AS `pic2`,`data1`.`pic3` AS `pic3`,`data1`.`links` AS `links`,`data1`.`c1` AS `c1`,`data1`.`c2` AS `c2`,`data1`.`c3` AS `c3`,`data1`.`c4` AS `c4`,`data1`.`n1` AS `n1`,`data1`.`n2` AS `n2`,`data1`.`n3` AS `n3`,`data1`.`d1` AS `d1`,`data1`.`d2` AS `d2`,`data1`.`attach1` AS `attach1`,`data1`.`attach2` AS `attach2`,`data1`.`content_tem` AS `content_tem` from `data1` union select `data2`.`id` AS `id`,`data2`.`channel_id` AS `channel_id`,`data2`.`title` AS `title`,`data2`.`tags` AS `tags`,`data2`.`author` AS `author`,`data2`.`level` AS `level`,`data2`.`txt1` AS `txt1`,`data2`.`txt2` AS `txt2`,`data2`.`dis` AS `dis`,`data2`.`state` AS `state`,`data2`.`createtime` AS `createtime`,`data2`.`pic1` AS `pic1`,`data2`.`pic2` AS `pic2`,`data2`.`pic3` AS `pic3`,`data2`.`links` AS `links`,`data2`.`c1` AS `c1`,`data2`.`c2` AS `c2`,`data2`.`c3` AS `c3`,`data2`.`c4` AS `c4`,`data2`.`n1` AS `n1`,`data2`.`n2` AS `n2`,`data2`.`n3` AS `n3`,`data2`.`d1` AS `d1`,`data2`.`d2` AS `d2`,`data2`.`attach1` AS `attach1`,`data2`.`attach2` AS `attach2`,`data2`.`content_tem` AS `content_tem` from `data2` union select `data3`.`id` AS `id`,`data3`.`channel_id` AS `channel_id`,`data3`.`title` AS `title`,`data3`.`tags` AS `tags`,`data3`.`author` AS `author`,`data3`.`level` AS `level`,`data3`.`txt1` AS `txt1`,`data3`.`txt2` AS `txt2`,`data3`.`dis` AS `dis`,`data3`.`state` AS `state`,`data3`.`createtime` AS `createtime`,`data3`.`pic1` AS `pic1`,`data3`.`pic2` AS `pic2`,`data3`.`pic3` AS `pic3`,`data3`.`links` AS `links`,`data3`.`c1` AS `c1`,`data3`.`c2` AS `c2`,`data3`.`c3` AS `c3`,`data3`.`c4` AS `c4`,`data3`.`n1` AS `n1`,`data3`.`n2` AS `n2`,`data3`.`n3` AS `n3`,`data3`.`d1` AS `d1`,`data3`.`d2` AS `d2`,`data3`.`attach1` AS `attach1`,`data3`.`attach2` AS `attach2`,`data3`.`content_tem` AS `content_tem` from `data3` union select `data4`.`id` AS `id`,`data4`.`channel_id` AS `channel_id`,`data4`.`title` AS `title`,`data4`.`tags` AS `tags`,`data4`.`author` AS `author`,`data4`.`level` AS `level`,`data4`.`txt1` AS `txt1`,`data4`.`txt2` AS `txt2`,`data4`.`dis` AS `dis`,`data4`.`state` AS `state`,`data4`.`createtime` AS `createtime`,`data4`.`pic1` AS `pic1`,`data4`.`pic2` AS `pic2`,`data4`.`pic3` AS `pic3`,`data4`.`links` AS `links`,`data4`.`c1` AS `c1`,`data4`.`c2` AS `c2`,`data4`.`c3` AS `c3`,`data4`.`c4` AS `c4`,`data4`.`n1` AS `n1`,`data4`.`n2` AS `n2`,`data4`.`n3` AS `n3`,`data4`.`d1` AS `d1`,`data4`.`d2` AS `d2`,`data4`.`attach1` AS `attach1`,`data4`.`attach2` AS `attach2`,`data4`.`content_tem` AS `content_tem` from `data4`;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
