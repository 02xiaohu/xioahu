/*
MySQL Data Transfer
Source Host: localhost
Source Database: hibernate_many2one
Target Host: localhost
Target Database: hibernate_many2one
Date: 2016-2-22 17:15:45
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `groupid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCB63CCB6C3D18669` (`groupid`),
  CONSTRAINT `FKCB63CCB6C3D18669` FOREIGN KEY (`groupid`) REFERENCES `t_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `t_group` VALUES ('4', 'zte');
INSERT INTO `t_group` VALUES ('5', 'zte');
INSERT INTO `t_user` VALUES ('5', 'zhangsan', '4');
INSERT INTO `t_user` VALUES ('6', 'lisi', '4');
INSERT INTO `t_user` VALUES ('7', 'zhangsan', '5');
INSERT INTO `t_user` VALUES ('8', 'lisi', '5');
