/*
MySQL Data Transfer
Source Host: localhost
Source Database: hibernate_one2one_pk_1
Target Host: localhost
Target Database: hibernate_one2one_pk_1
Date: 2015-11-15 13:38:20
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_idcard
-- ----------------------------
DROP TABLE IF EXISTS `t_idcard`;
CREATE TABLE `t_idcard` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cardNo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_person
-- ----------------------------
DROP TABLE IF EXISTS `t_person`;
CREATE TABLE `t_person` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK785BED802591D1EF` (`id`),
  CONSTRAINT `FK785BED802591D1EF` FOREIGN KEY (`id`) REFERENCES `t_idcard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `t_idcard` VALUES ('1', '2345667');
INSERT INTO `t_idcard` VALUES ('3', '2345667');
INSERT INTO `t_person` VALUES ('1', 'Tom');
INSERT INTO `t_person` VALUES ('3', 'Tom');
