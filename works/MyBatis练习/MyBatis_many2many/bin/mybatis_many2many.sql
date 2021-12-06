/*
MySQL Data Transfer
Source Host: localhost
Source Database: mybatis_many2many
Target Host: localhost
Target Database: mybatis_many2many
Date: 2016-2-23 15:55:50
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `userid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FK331DEE5FDC0ABE1` (`userid`),
  KEY `FK331DEE5F86B5677` (`roleid`),
  CONSTRAINT `FK331DEE5F86B5677` FOREIGN KEY (`roleid`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FK331DEE5FDC0ABE1` FOREIGN KEY (`userid`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

 
