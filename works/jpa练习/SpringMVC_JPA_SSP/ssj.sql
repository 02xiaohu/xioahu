/*
MySQL Data Transfer
Source Host: localhost
Source Database: ssj
Target Host: localhost
Target Database: ssj
Date: 2020-12-6 13:44:41
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for jpa_persons
-- ----------------------------
DROP TABLE IF EXISTS `jpa_persons`;
CREATE TABLE `jpa_persons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `t_age` int(11) DEFAULT NULL,
  `t_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `jpa_persons` VALUES ('1', '23', 'tom');
INSERT INTO `jpa_persons` VALUES ('2', '21', 'bush');
INSERT INTO `jpa_persons` VALUES ('3', '23', 'tom123');
INSERT INTO `jpa_persons` VALUES ('4', '21', 'bush123');
