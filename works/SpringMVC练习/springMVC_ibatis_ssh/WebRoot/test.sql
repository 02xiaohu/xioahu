/*
MySQL Data Transfer
Source Host: localhost
Source Database: test
Target Host: localhost
Target Database: test
Date: 2015-2-22 18:04:02
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for ibatis
-- ----------------------------
DROP TABLE IF EXISTS `ibatis`;
CREATE TABLE `ibatis` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `ibatis` VALUES ('4', 'ls', '123456');
INSERT INTO `ibatis` VALUES ('5', 'zhangsan', '123456');
INSERT INTO `ibatis` VALUES ('6', 'lisi', '123456');
