/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : approval

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 30/04/2021 14:31:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for APPROVAL_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `APPROVAL_CONFIG`;
CREATE TABLE `APPROVAL_CONFIG` (
  `ID` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '主键id',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `RELATE_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '关联id',
  `NODE_INDEX` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '节点位置',
  `APPROVAL_ROLE` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '多个条件用,分割',
  `NODE_TYPE` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT 'SIMPLE' COMMENT '节点类型',
  `VERSION` tinyint unsigned DEFAULT '0' COMMENT '版本',
  `DEL_FLAG` tinyint unsigned DEFAULT '0' COMMENT '删除标志默认0 1为已删除',
  `CHILDREN_IDX` tinyint unsigned DEFAULT '0' COMMENT '在多节点状态下,子索引数',
  `NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `APPROVAL_TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '审批类型',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for APPROVAL_LOG
-- ----------------------------
DROP TABLE IF EXISTS `APPROVAL_LOG`;
CREATE TABLE `APPROVAL_LOG` (
  `ID` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '主键',
  `REASON` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '回滚的原因',
  `STATUS` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '状态',
  `RELATE_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '关联id',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `DEL_FLAG` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
  `NODE_INDEX` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '节点索引',
  `CHILDREN_IDX` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '在多节点状态下,子索引数',
  `USER_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '用户id',
  `APPROVAL_TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '审批类型',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for TEST   注 该表是作为一个测试表(只包含主要参数)，可按业务需求进行扩容，
-- ----------------------------
DROP TABLE IF EXISTS `TEST`;
CREATE TABLE `TEST` (
  `ID` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `DEL_FLAG` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `VERSION` tinyint NOT NULL DEFAULT '0' COMMENT '版本',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


