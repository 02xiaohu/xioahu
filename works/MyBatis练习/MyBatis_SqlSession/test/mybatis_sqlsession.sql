/*
MySQL Data Transfer
Source Host: localhost
Source Database: mybatis_sqlsession
Target Host: localhost
Target Database: mybatis_sqlsession
Date: 2015-11-14 21:52:17
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) DEFAULT NULL,
  `userAge` int(11) DEFAULT NULL,
  `userAddress` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `article` VALUES ('1', '1', 'test_title_1', 'test_content_1');
INSERT INTO `article` VALUES ('2', '1', 'test_title_2', 'test_content_2');
INSERT INTO `article` VALUES ('3', '1', 'test_title_3', 'test_content_3');
INSERT INTO `article` VALUES ('4', '1', 'test_title_4', 'test_content_4');
INSERT INTO `user` VALUES ('1', 'tom', '21', 'nanchang');
INSERT INTO `user` VALUES ('2', 'lisa', '22', '浦东创新园区');
