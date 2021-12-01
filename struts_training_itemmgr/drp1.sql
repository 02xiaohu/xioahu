/*
MySQL Data Transfer
Source Host: localhost
Source Database: drp1
Target Host: localhost
Target Database: drp1
Date: 2014-2-4 0:44:52
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_data_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_data_dict`;
CREATE TABLE `t_data_dict` (
  `id` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_items
-- ----------------------------
DROP TABLE IF EXISTS `t_items`;
CREATE TABLE `t_items` (
  `item_no` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `spec` varchar(255) DEFAULT NULL,
  `pattern` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_no`),
  KEY `FKA06D24359F328157` (`unit`),
  KEY `FKA06D2435FE0C88CB` (`category`),
  CONSTRAINT `FKA06D24359F328157` FOREIGN KEY (`unit`) REFERENCES `t_data_dict` (`id`),
  CONSTRAINT `FKA06D2435FE0C88CB` FOREIGN KEY (`category`) REFERENCES `t_data_dict` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `t_data_dict` VALUES ('B01', 'item_category', 'yiliaoqixie');
INSERT INTO `t_data_dict` VALUES ('B02', 'item_category', 'zhongchengyao');
INSERT INTO `t_data_dict` VALUES ('B03', 'item_category', 'xiyao');
INSERT INTO `t_data_dict` VALUES ('C01', 'item_unit', 'he');
INSERT INTO `t_data_dict` VALUES ('C02', 'item_unit', 'pian');
INSERT INTO `t_data_dict` VALUES ('C03', 'item_unit', 'xiang');
INSERT INTO `t_items` VALUES ('a0001', '112233', '4445555', 'etrtryy', 'B01', 'C01');
INSERT INTO `t_items` VALUES ('a0002', '2324342', '424424242', '42424242424', 'B02', 'C01');
INSERT INTO `t_items` VALUES ('a0003', 'wqewe', 'wrerw', 'rwrwrw', 'B01', 'C01');
