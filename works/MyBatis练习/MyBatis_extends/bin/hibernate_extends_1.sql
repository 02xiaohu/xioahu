/*
MySQL Data Transfer
Source Host: localhost
Source Database: hibernate_extends_1
Target Host: localhost
Target Database: hibernate_extends_1
Date: 2015-11-20 11:35:52
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_animal
-- ----------------------------
DROP TABLE IF EXISTS `t_animal`;
CREATE TABLE `t_animal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sex` bit(1) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `t_animal` VALUES ('1', 'P', 'pig', '', '100', null);
INSERT INTO `t_animal` VALUES ('2', 'B', 'bird', '', null, '50');
INSERT INTO `t_animal` VALUES ('3', 'P', 'pig2', '', '102', null);
INSERT INTO `t_animal` VALUES ('4', 'P', 'pig3', '', '103', null);
INSERT INTO `t_animal` VALUES ('5', 'B', 'bird2', '', null, '52');
