/*
MySQL Data Transfer
Source Host: localhost
Source Database: sssp
Target Host: localhost
Target Database: sssp
Date: 2020-12-20 11:34:08
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_person
-- ----------------------------
DROP TABLE IF EXISTS `t_person`;
CREATE TABLE `t_person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `birth` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_en4d8jxctnpr9regernmlkw5j` (`address_id`),
  CONSTRAINT `FK_en4d8jxctnpr9regernmlkw5j` FOREIGN KEY (`address_id`) REFERENCES `t_address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `t_address` VALUES ('1', 'shenzhen');
INSERT INTO `t_address` VALUES ('2', 'nanchang');
INSERT INTO `t_address` VALUES ('3', 'neimenggu');
INSERT INTO `t_person` VALUES ('1', '2020-12-20 11:02:32', 'tom@123.com', 'tom', '1');
INSERT INTO `t_person` VALUES ('2', '2020-12-20 11:10:27', 'bush@123.com', 'bush', '2');
INSERT INTO `t_person` VALUES ('3', '2020-12-20 11:27:55', 'tony@123.com', 'tony', '3');
