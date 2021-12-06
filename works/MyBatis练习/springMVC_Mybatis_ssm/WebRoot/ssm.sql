/*
MySQL Data Transfer
Source Host: localhost
Source Database: ssm
Target Host: localhost
Target Database: ssm
Date: 2015-11-29 17:28:06
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'zhangsan', '123456');
INSERT INTO `user` VALUES ('2', 'lisi', '123456');
INSERT INTO `user` VALUES ('10', 'tom ', '12345678');
INSERT INTO `user` VALUES ('11', 'wangwu', '12345678');
INSERT INTO `user` VALUES ('13', 'lisa', '1234567890');
INSERT INTO `user` VALUES ('15', 'maliu', '123456');
