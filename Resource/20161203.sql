/*
SQLyog Ultimate v11.11 (32 bit)
MySQL - 5.6.10 : Database - chat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`chat` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `chat`;

/*Table structure for table `chatinfo` */

DROP TABLE IF EXISTS `chatinfo`;

CREATE TABLE `chatinfo` (
  `CNO` int(2) NOT NULL COMMENT '记录编号',
  `CSENDUC` int(5) DEFAULT NULL COMMENT '发送者uc',
  `CRECEIVEUC` int(5) DEFAULT NULL COMMENT '接收者uc',
  `CDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发送日期',
  `TNO` int(3) DEFAULT NULL COMMENT '信息编号',
  PRIMARY KEY (`CNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `chatinfo` */

/*Table structure for table `friends` */

DROP TABLE IF EXISTS `friends`;

CREATE TABLE `friends` (
  `FUC` int(5) NOT NULL COMMENT '好友uc',
  `FSNO` int(2) NOT NULL COMMENT '所属分组',
  `FDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加日期',
  `UC` int(5) DEFAULT NULL COMMENT '本人uc',
  `REMARKS` varchar(30) DEFAULT '' COMMENT '好友备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `friends` */

insert  into `friends`(`FUC`,`FSNO`,`FDATE`,`UC`,`REMARKS`) values (99999,1,'2016-10-28 23:09:01',77777,''),(99999,1,'2016-10-28 23:09:14',88888,''),(77777,1,'2016-10-28 23:09:29',99999,''),(88888,1,'2016-10-29 00:13:51',99999,''),(77777,2,'2016-10-29 00:13:51',99999,'老11');

/*Table structure for table `grouptable` */

DROP TABLE IF EXISTS `grouptable`;

CREATE TABLE `grouptable` (
  `GNO` int(5) NOT NULL COMMENT '群编号',
  `GNAME` varchar(20) DEFAULT NULL COMMENT '群名称',
  `GDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`GNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `grouptable` */

insert  into `grouptable`(`GNO`,`GNAME`,`GDATE`) values (10001,'我们的回忆','2016-10-22 23:11:56');

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `LNO` int(5) NOT NULL AUTO_INCREMENT COMMENT '登录编号',
  `LIP` varchar(20) DEFAULT NULL COMMENT '登录ip',
  `LPORT` int(5) DEFAULT NULL COMMENT '登录port',
  `LDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '登录日期',
  `LSTATUS` int(1) DEFAULT NULL COMMENT '是否在线',
  `LUC` int(5) NOT NULL COMMENT 'uc',
  PRIMARY KEY (`LNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `login` */

/*Table structure for table `server` */

DROP TABLE IF EXISTS `server`;

CREATE TABLE `server` (
  `NAME` varchar(10) NOT NULL,
  `PWD` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `server` */

insert  into `server`(`NAME`,`PWD`) values ('admin','123456');

/*Table structure for table `subgroup` */

DROP TABLE IF EXISTS `subgroup`;

CREATE TABLE `subgroup` (
  `SNO` int(2) NOT NULL COMMENT '组号',
  `SNAME` varchar(20) NOT NULL COMMENT '组名',
  `SDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UC` int(5) NOT NULL COMMENT 'uc号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `subgroup` */

insert  into `subgroup`(`SNO`,`SNAME`,`SDATE`,`UC`) values (1,'我的好友','2016-11-06 20:23:40',99999),(2,'大学同学','2016-11-06 20:24:21',99999),(1,'我的好友','2016-11-06 20:25:42',77777),(1,'我的好友','2016-11-06 20:27:19',88888);

/*Table structure for table `text` */

DROP TABLE IF EXISTS `text`;

CREATE TABLE `text` (
  `TNO` int(3) NOT NULL AUTO_INCREMENT COMMENT '信息编号',
  `TCONTEXT` varchar(200) DEFAULT NULL COMMENT '内容',
  `TFONTTYPE` varchar(10) DEFAULT NULL COMMENT '字体类型',
  `TFIBTSIZE` int(5) DEFAULT NULL COMMENT '字体大小',
  `TFONTCOLOR` varchar(5) DEFAULT NULL COMMENT '字体颜色',
  PRIMARY KEY (`TNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `text` */

/*Table structure for table `usergroup` */

DROP TABLE IF EXISTS `usergroup`;

CREATE TABLE `usergroup` (
  `UGNO` int(2) NOT NULL AUTO_INCREMENT COMMENT '关系编号',
  `UC` int(5) NOT NULL COMMENT 'uc号',
  `GNO` int(5) NOT NULL COMMENT '群编号',
  PRIMARY KEY (`UGNO`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `usergroup` */

insert  into `usergroup`(`UGNO`,`UC`,`GNO`) values (1,77777,10001),(2,88888,10001),(3,99999,10001);

/*Table structure for table `userinfo` */

DROP TABLE IF EXISTS `userinfo`;

CREATE TABLE `userinfo` (
  `UC` int(5) NOT NULL COMMENT 'uc号',
  `PWD` varchar(10) NOT NULL COMMENT '密码',
  `SIGN` varchar(30) DEFAULT NULL COMMENT '签名',
  `PHOTOID` varchar(30) NOT NULL COMMENT '头像编号',
  `NICKNAME` varchar(30) NOT NULL COMMENT '昵称',
  `SEX` varchar(2) NOT NULL DEFAULT '男' COMMENT '性别',
  `BIRTHDAY` date DEFAULT NULL COMMENT '生日',
  `CONSTELLATION` varchar(60) DEFAULT NULL COMMENT '星座',
  `BLOODTYPE` varchar(10) DEFAULT NULL COMMENT '血型',
  `DIPLOMA` varchar(10) DEFAULT NULL COMMENT '学历',
  `TELEPHONE` varchar(15) DEFAULT NULL COMMENT '电话',
  `EMAIL` varchar(20) DEFAULT NULL COMMENT '电子邮件',
  `ADDRESS` varchar(30) DEFAULT NULL COMMENT '所在地',
  `STATUS` varchar(10) DEFAULT NULL COMMENT '登录状态',
  PRIMARY KEY (`UC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `userinfo` */

insert  into `userinfo`(`UC`,`PWD`,`SIGN`,`PHOTOID`,`NICKNAME`,`SEX`,`BIRTHDAY`,`CONSTELLATION`,`BLOODTYPE`,`DIPLOMA`,`TELEPHONE`,`EMAIL`,`ADDRESS`,`STATUS`) values (77777,'123','想睡觉','Image/Head/184_100.gif','system3','男','2016-10-16','狮子','B','博士','1212121212112','vito@163.com','香港','1'),(88888,'123','无聊','Image/Head/184_100.gif','system2','男','2016-10-16','狮子','B','本科','186172638127','vito@qq.com','北京','0'),(99999,'123','just for fun ！','Image/Head/184_100.gif','system1','男','2016-10-16','狮子','B','本科','18366121000','vito@live.com','济南','1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
