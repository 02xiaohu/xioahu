/*
MySQL Data Transfer
Source Host: localhost
Source Database: jpa
Target Host: localhost
Target Database: jpa
Date: 2020-12-5 22:27:19
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jpa_orders
-- ----------------------------
DROP TABLE IF EXISTS `jpa_orders`;
CREATE TABLE `jpa_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderName` varchar(255) DEFAULT NULL,
  `CUSTOMER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_85wpb7frm8305lho66wj458mk` (`CUSTOMER_ID`),
  CONSTRAINT `FK_85wpb7frm8305lho66wj458mk` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `jpa_cutomers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `jpa_cutomers` VALUES ('1', '20', '2020-11-22 14:46:37', '2020-11-22 14:46:37', 'tom@163.com', 'kkkkkkk');
INSERT INTO `jpa_cutomers` VALUES ('2', '20', '2020-11-22 14:47:28', '2020-11-22 14:47:28', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('3', '22', '2020-11-22 14:56:51', '2020-11-22 14:56:51', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('4', '22', '2020-11-22 14:59:57', '2020-11-22 14:59:57', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('5', '30', '2020-11-22 15:00:56', '2020-11-22 15:00:56', 'tom@163.com', 'tom');
INSERT INTO `jpa_cutomers` VALUES ('6', '30', '2020-11-22 15:14:58', '2020-11-22 15:14:58', 'tom@163.com', 'YYY');
INSERT INTO `jpa_cutomers` VALUES ('7', '32', '2020-11-22 15:16:12', '2020-11-22 15:16:13', 'tom@163.com', 'YYY');
INSERT INTO `jpa_cutomers` VALUES ('13', '18', '2020-12-05 21:29:59', '2020-12-05 21:29:59', 'gg@163.com', 'YYY');
INSERT INTO `jpa_cutomers` VALUES ('14', '18', '2020-12-05 21:31:53', '2020-12-05 21:31:53', 'gg@163.com', 'YYY');
INSERT INTO `jpa_orders` VALUES ('1', 'G-GG-1', '13');
INSERT INTO `jpa_orders` VALUES ('2', 'G-GG-2', '13');
INSERT INTO `jpa_orders` VALUES ('3', 'G-GG-1', '14');
INSERT INTO `jpa_orders` VALUES ('4', 'G-GG-2', '14');
INSERT INTO `jpa_orders` VALUES ('5', 'G-GG-not-1', null);
INSERT INTO `jpa_orders` VALUES ('6', 'G-GG-not-2', null);
