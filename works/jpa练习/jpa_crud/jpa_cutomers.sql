/*
MySQL Data Transfer
Source Host: localhost
Source Database: jpa
Target Host: localhost
Target Database: jpa
Date: 2020-11-22 15:28:37
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for jpa_cutomers
-- ----------------------------
DROP TABLE IF EXISTS `jpa_cutomers`;
CREATE TABLE `jpa_cutomers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) NOT NULL,
  `birth` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `jpa_cutomers` VALUES ('5', '20', '2020-11-22 14:46:37', '2020-11-22 14:46:37', 'tom@163.com', 'kkkkkkk');
INSERT INTO `jpa_cutomers` VALUES ('6', '20', '2020-11-22 14:47:28', '2020-11-22 14:47:28', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('8', '20', '2020-11-22 14:56:51', '2020-11-22 14:56:51', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('9', '20', '2020-11-22 14:59:57', '2020-11-22 14:59:57', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('10', '20', '2020-11-22 15:00:56', '2020-11-22 15:00:56', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('11', '20', '2020-11-22 15:14:58', '2020-11-22 15:14:58', 'tom@163.com', 'tom123');
INSERT INTO `jpa_cutomers` VALUES ('12', '20', '2020-11-22 15:16:12', '2020-11-22 15:16:13', 'tom@163.com', 'tom123');
