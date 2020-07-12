/*
 Navicat Premium Data Transfer

 Source Server         : macMySQL
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : whitetown_dog

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 11/07/2020 21:33:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` VALUES (1, 1, 1);
INSERT INTO `role_menu` VALUES (2, 1, 2);
INSERT INTO `role_menu` VALUES (3, 1, 3);
INSERT INTO `role_menu` VALUES (4, 1, 4);
INSERT INTO `role_menu` VALUES (5, 1, 5);
INSERT INTO `role_menu` VALUES (6, 1, 6);
INSERT INTO `role_menu` VALUES (7, 1, 7);
INSERT INTO `role_menu` VALUES (8, 1, 8);
INSERT INTO `role_menu` VALUES (9, 2, 1);
INSERT INTO `role_menu` VALUES (10, 2, 2);
INSERT INTO `role_menu` VALUES (11, 2, 3);
COMMIT;

-- ----------------------------
-- Table structure for sys_dic
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic`;
CREATE TABLE `sys_dic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dic_type_code` bigint(20) DEFAULT NULL,
  `dic_type_name` varchar(50) DEFAULT NULL,
  `dic_code` int(11) DEFAULT NULL,
  `dic_name` varchar(50) DEFAULT NULL,
  `creat_time` datetime DEFAULT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dic_type_code` (`dic_type_code`,`dic_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dic
-- ----------------------------
BEGIN;
INSERT INTO `sys_dic` VALUES (1, 1, 'userStatus', 0, '激活', '2020-06-20 16:35:54', 112233);
INSERT INTO `sys_dic` VALUES (2, 1, 'userStatus', 1, '冻结', '2020-06-20 16:35:58', 112233);
INSERT INTO `sys_dic` VALUES (3, 1, 'userStatus', 2, '删除', '2020-06-20 16:36:03', 112233);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(30) DEFAULT NULL COMMENT '菜单中文名称',
  `menu_code` varchar(30) DEFAULT NULL COMMENT '菜单英文编码',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单id',
  `menu_url` varchar(50) DEFAULT NULL COMMENT '路由地址',
  `menu_icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `menu_sort` int(11) DEFAULT NULL COMMENT '菜单排序',
  `menu_level` int(11) DEFAULT NULL COMMENT '第几级菜单',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单描述',
  `menu_status` int(11) DEFAULT NULL COMMENT '菜单状态',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '顶级菜单', 'top', 1, '/dog', NULL, 0, 0, '顶级菜单项', 0, 1, '2020-06-23 11:07:09', NULL, NULL);
INSERT INTO `sys_menu` VALUES (2, '主控面板', 'main_panel', 1, '/dog/main', 'layui-icon-app', 1, 1, '主控面板模块', 0, 2, '2020-06-24 22:49:28', NULL, NULL);
INSERT INTO `sys_menu` VALUES (3, '权限管理', 'auth', 1, '/dog/auth', 'layui-icon-snowflake', 2, 1, '权限管理模块', 0, 2, '2020-06-25 16:30:31', NULL, NULL);
INSERT INTO `sys_menu` VALUES (4, '用户管理', 'auth_user', 3, '/dog/auth/user', NULL, 1, 2, '用户管理页面', 0, 2, '2020-06-25 16:32:08', 1, '2020-07-06 22:58:54');
INSERT INTO `sys_menu` VALUES (5, '角色管理', 'auth_role', 3, '/dog/auth/role', NULL, 2, 2, '角色管理页面', 0, 2, '2020-06-25 16:33:15', NULL, NULL);
INSERT INTO `sys_menu` VALUES (6, '菜单管理', 'auth_menu', 3, '/dog/auth/menu', NULL, 3, 2, '菜单管理页面', 0, 2, '2020-06-25 16:34:49', NULL, NULL);
INSERT INTO `sys_menu` VALUES (7, '部门管理', 'auth_dept', 3, '/dog/auth/dept', NULL, 4, 2, '部门管理页面', 1, 2, '2020-06-25 16:36:18', NULL, NULL);
INSERT INTO `sys_menu` VALUES (8, '系统配置', 'sys_config', 1, '/dog/sys', 'layui-icon-set-fill', 3, 1, '系统管理模块', 0, 2, '2020-06-25 16:38:42', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_basic_info
-- ----------------------------
DROP TABLE IF EXISTS `user_basic_info`;
CREATE TABLE `user_basic_info` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像路径信息',
  `username` varchar(45) NOT NULL COMMENT '账号',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'md5密码盐',
  `real_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名字',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `gender` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别(字典)',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电子邮件',
  `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `user_status` int(11) DEFAULT NULL COMMENT '状态(字典)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人',
  `user_version` int(11) DEFAULT NULL COMMENT '版本控制',
  PRIMARY KEY (`user_id`,`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户基本信息表';

-- ----------------------------
-- Records of user_basic_info
-- ----------------------------
BEGIN;
INSERT INTO `user_basic_info` VALUES (1, '252363687457946', 'admin', '79ac68c118eb3045dce0f5a1c26f8a2a', '435esgfew', 'GrainRain', '2018-09-03 08:00:00', '男', '306499331@126.com', '18883033906', 0, '2020-06-02 10:20:36', 1, '2020-06-27 22:21:12', 1, 4);
INSERT INTO `user_basic_info` VALUES (2, NULL, 'taixian', '32f0fd5b4b3e58358de083de9f2b68b9', '1aErQB8', '太闲', '1991-02-11 08:00:00', '男', 'yung2mao@126.com', '18011674518', 0, '2020-06-18 15:45:07', 1, '2020-06-18 18:34:42', 2, 0);
INSERT INTO `user_basic_info` VALUES (3, NULL, 'yixia', '4d57457e41f68dd102c4c3f5b1c69aaa', 'aIq\\rlj', '易霞', '1987-11-23 08:00:00', '女', 'yixia@126.com', '17309740884', 0, '2020-06-24 21:57:44', 2, '2020-06-27 22:27:57', 1, 0);
INSERT INTO `user_basic_info` VALUES (10, NULL, 'dongchen', '85921c2bf87866709f41db3dd5567d1f', 'Zvp4o;<', '晨董', '2020-05-05 08:00:00', '男', 'chendong@123.com', '15376438627', 0, '2020-06-27 22:17:21', 1, '2020-06-27 22:27:49', 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提示',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `role_status` int(11) DEFAULT NULL COMMENT '角色状态',
  `version` int(11) DEFAULT NULL COMMENT '乐观锁',
  `create_user_id` bigint(50) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(50) DEFAULT NULL COMMENT '修改用户',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES (1, 'SUPER_MANGER', '超级管理员', 1, 0, 2, 1, '2020-05-30 15:50:11', 1, '2020-05-30 15:50:17');
INSERT INTO `user_role` VALUES (2, 'NORMAL', '普通用户', 10, 0, 1, 2, '2020-06-28 21:35:49', 2, '2020-06-28 21:35:52');
INSERT INTO `user_role` VALUES (3, 'MANAGER', '子系统管理员', 2, 0, 4, 2, '2020-06-28 22:38:18', 2, '2020-06-29 22:09:51');
INSERT INTO `user_role` VALUES (5, 'GUA', '瓜神', 3, 1, 2, 1, '2020-07-02 22:11:19', 1, '2020-07-02 22:19:19');
COMMIT;

-- ----------------------------
-- Table structure for user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_role_relation`;
CREATE TABLE `user_role_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=armscii8;

-- ----------------------------
-- Records of user_role_relation
-- ----------------------------
BEGIN;
INSERT INTO `user_role_relation` VALUES (1, 1, 1);
INSERT INTO `user_role_relation` VALUES (2, 1, 2);
INSERT INTO `user_role_relation` VALUES (3, 2, 2);
INSERT INTO `user_role_relation` VALUES (4, 3, 2);
INSERT INTO `user_role_relation` VALUES (11, 10, 2);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
