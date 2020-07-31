/*
 Navicat Premium Data Transfer

 Source Server         : localMySQL
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : whitetown_dog

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 31/07/2020 14:30:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dept_info
-- ----------------------------
DROP TABLE IF EXISTS `dept_info`;
CREATE TABLE `dept_info`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dept_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门编码',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级部门ID',
  `dept_level` int(11) NOT NULL COMMENT '部门层级',
  `boss_position_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人应对应的职位ID',
  `boss_position_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人应对应职位名称',
  `boss_user_id` bigint(20) NULL DEFAULT NULL COMMENT '实际负责人ID',
  `boss_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实际负责人姓名',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公地址',
  `phone` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `description` varchar(254) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门描述',
  `dept_status` int(11) NULL DEFAULT NULL COMMENT '部门状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dept_info
-- ----------------------------
INSERT INTO `dept_info` VALUES (1, 'top', '最高层级', 0, 0, NULL, NULL, 1, 'GrainRain', '重庆-中国', '023-00000000', '无实际意义,代表最高层级', 0, '2020-07-19 17:49:39', 1, NULL, NULL);
INSERT INTO `dept_info` VALUES (2, 'board', '董事会', 7, 2, 1, '董事长', 15, '大佬徐', '中国-重庆-巴南区', '18883033906', '决策与战略支持', 0, '2020-07-19 22:37:15', 2, '2020-07-19 22:41:48', 2);
INSERT INTO `dept_info` VALUES (3, 'center', '总公司', 1, 1, 4, '总经理', 1, 'GrainRain', '重庆', '18883033906', '总公司', 0, '2020-07-23 23:06:40', NULL, NULL, NULL);
INSERT INTO `dept_info` VALUES (4, 'manager_office_center', '总经办', 3, 2, 4, '总经理', 1, 'GrainRain', '重庆', NULL, '总公司-总经办', 0, '2020-07-23 23:14:39', NULL, NULL, NULL);
INSERT INTO `dept_info` VALUES (5, 'r&d_center', '研发部', 3, 2, 6, '研发部长', 13, '僵尸粉', '重庆', '18883033906', '研发中心,隶属总部', 0, '2020-07-21 22:12:15', 1, NULL, NULL);
INSERT INTO `dept_info` VALUES (6, 'operations_center', '运营部', 3, 2, 8, '运营部长', NULL, NULL, '重庆', '18883033906', '运营中心,隶属总部', 0, '2020-07-23 23:09:58', NULL, NULL, NULL);
INSERT INTO `dept_info` VALUES (7, 'white_town', '白镇', 1, 1, 1, '董事长', 15, '大佬徐', '中国-重庆', '18883033906', '白镇中心', 0, '2020-07-25 16:14:19', NULL, NULL, NULL);
INSERT INTO `dept_info` VALUES (9, 'chengdu_center', '成都分公司', 1, 1, 16, '大区经理-成都', 12, '德州扑克', '成都-中国', '028-88888888', '成都销售公司', 0, '2020-07-25 22:36:41', 1, '2020-07-26 12:22:24', 1);
INSERT INTO `dept_info` VALUES (10, 'sales_chengdu', '销售部', 9, 2, 15, '销售部长', 12, '德州扑克', '成都-锦鲤', '028-00000000', '成都分公司销售部', 1, '2020-07-25 22:43:41', 1, '2020-07-26 12:24:40', 1);
INSERT INTO `dept_info` VALUES (11, 'finance_chengdu', '财务部', 9, 2, 17, '财务部长', 14, '安培', '成都-锦鲤', '028-00000001', '成都分公司财务部', 0, '2020-07-25 22:45:32', 1, '2020-07-26 12:29:09', 1);
INSERT INTO `dept_info` VALUES (12, 'sales_center', '销售部', 3, 2, NULL, NULL, NULL, NULL, '重庆-解放碑', '023-88885555', '总公司销售部门', 0, '2020-07-25 22:47:32', 1, NULL, NULL);

-- ----------------------------
-- Table structure for position_info
-- ----------------------------
DROP TABLE IF EXISTS `position_info`;
CREATE TABLE `position_info`  (
  `position_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID,关联部门表',
  `dept_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门编码',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `position_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位编码',
  `position_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位名称',
  `position_level` int(11) NULL DEFAULT NULL COMMENT '职位级别',
  `position_sort` int(11) NULL DEFAULT NULL COMMENT '职位排序',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位描述',
  `position_status` int(11) NULL DEFAULT NULL COMMENT '职位状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`position_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of position_info
-- ----------------------------
INSERT INTO `position_info` VALUES (1, 2, 'board', '董事会', 'Chairman', '董事长', 1, 1, '董事会主席', 0, '2020-07-20 22:26:59', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (2, 2, 'board', '董事会', 'Vice_Chairman', '副董事长', 2, 3, '副董事长', 0, '2020-07-20 22:27:01', 1, '2020-07-25 15:44:23', 1);
INSERT INTO `position_info` VALUES (3, 2, 'board', '董事会', 'basic_director', '董事', 2, 5, '董事会成员', 0, '2020-07-20 22:27:05', 1, '2020-07-25 15:43:45', 1);
INSERT INTO `position_info` VALUES (4, 4, 'manager_office_center', '总经办', 'manager_center', '总经理', 1, 1, '总公司-总经理', 0, '2020-07-23 23:20:39', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (6, 5, 'r&d_center', '研发部', 'R&D_Minister_Center', '研发部长', 1, 2, '总公司-研发部长', 0, '2020-07-21 22:40:26', 1, '2020-07-25 15:45:02', 1);
INSERT INTO `position_info` VALUES (7, 5, 'r&d_center', '研发部', 'r&d_project_manager_Center', '项目经理', 2, 3, '研发项目经理', 0, '2020-07-22 22:03:55', 2, NULL, NULL);
INSERT INTO `position_info` VALUES (8, 6, 'operations_center', '运营部', 'operations_manager_center', '运营部长', 1, 1, '总公司-运营部长', 0, '2020-07-24 08:44:43', 2, NULL, NULL);
INSERT INTO `position_info` VALUES (9, 4, 'manager_office_center', '总经办', 'financial_director_center', '财务总监', 1, 2, '总公司-财务总监', 0, '2020-07-25 11:57:40', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (10, 4, 'manager_office_center', '总经办', 'technical_director_center', '技术总监', 1, 3, '总公司-技术总监', 0, '2020-07-25 11:58:36', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (11, 5, 'r&d_center', '研发部', 'r&d_engineer_center', '开发工程师', 2, 2, '研发中心-研发工程师', 0, '2020-07-25 12:00:15', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (12, 6, 'operations_center', '运营部', 'operation_project_manager_center', '项目经理', 2, 3, '运营中心-项目经理', 0, '2020-07-25 12:02:47', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (13, 6, 'operations_center', '运营部', 'maintain_center', '运维工程师', 2, 4, '总公司-运维工程师', 0, '2020-07-25 12:04:14', 1, '2020-07-25 15:35:32', 1);
INSERT INTO `position_info` VALUES (14, 5, 'r&d_center', '研发部', 'test_enginner_center', '测试工程师', 2, 5, '研发中心-测试工程师', 0, '2020-07-25 15:36:43', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (15, 10, 'sales_chengdu', '销售部', 'chengdu_sales_manager', '销售部长', 1, 1, '销售部长', 0, '2020-07-26 10:37:54', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (16, 9, 'chengdu_center', '成都分公司', 'chengdu_leader', '大区经理-成都', 1, 1, '成都大区经理', 0, '2020-07-26 10:38:54', 1, NULL, NULL);
INSERT INTO `position_info` VALUES (17, 11, 'finance_chengdu', '财务部', 'chengdu_financial_manager', '财务部长', 1, 1, '成都财务部部长', 0, '2020-07-26 10:40:04', 1, NULL, NULL);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `menu_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 253 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (38, 6, 1);
INSERT INTO `role_menu` VALUES (70, 2, 1);
INSERT INTO `role_menu` VALUES (71, 2, 2);
INSERT INTO `role_menu` VALUES (72, 2, 11);
INSERT INTO `role_menu` VALUES (73, 2, 12);
INSERT INTO `role_menu` VALUES (123, 5, 1);
INSERT INTO `role_menu` VALUES (124, 5, 2);
INSERT INTO `role_menu` VALUES (125, 5, 19);
INSERT INTO `role_menu` VALUES (126, 3, 1);
INSERT INTO `role_menu` VALUES (127, 3, 3);
INSERT INTO `role_menu` VALUES (128, 3, 4);
INSERT INTO `role_menu` VALUES (129, 3, 14);
INSERT INTO `role_menu` VALUES (130, 3, 21);
INSERT INTO `role_menu` VALUES (131, 3, 13);
INSERT INTO `role_menu` VALUES (132, 3, 22);
INSERT INTO `role_menu` VALUES (133, 3, 23);
INSERT INTO `role_menu` VALUES (134, 3, 5);
INSERT INTO `role_menu` VALUES (135, 3, 15);
INSERT INTO `role_menu` VALUES (136, 3, 16);
INSERT INTO `role_menu` VALUES (137, 3, 24);
INSERT INTO `role_menu` VALUES (138, 3, 25);
INSERT INTO `role_menu` VALUES (139, 3, 6);
INSERT INTO `role_menu` VALUES (140, 3, 26);
INSERT INTO `role_menu` VALUES (141, 3, 27);
INSERT INTO `role_menu` VALUES (142, 3, 28);
INSERT INTO `role_menu` VALUES (143, 3, 7);
INSERT INTO `role_menu` VALUES (144, 3, 32);
INSERT INTO `role_menu` VALUES (145, 3, 29);
INSERT INTO `role_menu` VALUES (146, 3, 30);
INSERT INTO `role_menu` VALUES (147, 3, 31);
INSERT INTO `role_menu` VALUES (148, 3, 20);
INSERT INTO `role_menu` VALUES (149, 3, 33);
INSERT INTO `role_menu` VALUES (150, 3, 34);
INSERT INTO `role_menu` VALUES (151, 3, 35);
INSERT INTO `role_menu` VALUES (152, 3, 8);
INSERT INTO `role_menu` VALUES (153, 3, 9);
INSERT INTO `role_menu` VALUES (154, 3, 17);
INSERT INTO `role_menu` VALUES (155, 3, 10);
INSERT INTO `role_menu` VALUES (156, 3, 11);
INSERT INTO `role_menu` VALUES (157, 3, 12);
INSERT INTO `role_menu` VALUES (202, 7, 1);
INSERT INTO `role_menu` VALUES (203, 7, 8);
INSERT INTO `role_menu` VALUES (204, 7, 9);
INSERT INTO `role_menu` VALUES (205, 7, 17);
INSERT INTO `role_menu` VALUES (206, 7, 10);
INSERT INTO `role_menu` VALUES (229, 1, 1);
INSERT INTO `role_menu` VALUES (230, 1, 2);
INSERT INTO `role_menu` VALUES (231, 1, 19);
INSERT INTO `role_menu` VALUES (232, 1, 3);
INSERT INTO `role_menu` VALUES (233, 1, 4);
INSERT INTO `role_menu` VALUES (234, 1, 14);
INSERT INTO `role_menu` VALUES (235, 1, 21);
INSERT INTO `role_menu` VALUES (236, 1, 13);
INSERT INTO `role_menu` VALUES (237, 1, 22);
INSERT INTO `role_menu` VALUES (238, 1, 23);
INSERT INTO `role_menu` VALUES (239, 1, 5);
INSERT INTO `role_menu` VALUES (240, 1, 15);
INSERT INTO `role_menu` VALUES (241, 1, 24);
INSERT INTO `role_menu` VALUES (242, 1, 16);
INSERT INTO `role_menu` VALUES (243, 1, 25);
INSERT INTO `role_menu` VALUES (244, 1, 6);
INSERT INTO `role_menu` VALUES (245, 1, 7);
INSERT INTO `role_menu` VALUES (246, 1, 20);
INSERT INTO `role_menu` VALUES (247, 1, 8);
INSERT INTO `role_menu` VALUES (248, 1, 9);
INSERT INTO `role_menu` VALUES (249, 1, 17);
INSERT INTO `role_menu` VALUES (250, 1, 10);
INSERT INTO `role_menu` VALUES (251, 1, 11);
INSERT INTO `role_menu` VALUES (252, 1, 12);

-- ----------------------------
-- Table structure for sys_dic
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic`;
CREATE TABLE `sys_dic`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dic_type_code` bigint(20) NULL DEFAULT NULL,
  `dic_type_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dic_code` int(11) NULL DEFAULT NULL,
  `dic_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creat_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dic_type_code`(`dic_type_code`, `dic_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dic
-- ----------------------------
INSERT INTO `sys_dic` VALUES (1, 1, 'basicStatus', 0, '激活', '2020-06-20 16:35:54', 112233);
INSERT INTO `sys_dic` VALUES (2, 1, 'basicStatus', 1, '冻结', '2020-06-20 16:35:58', 112233);
INSERT INTO `sys_dic` VALUES (3, 1, 'basicStatus', 2, '删除', '2020-06-20 16:36:03', 112233);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单中文名称',
  `menu_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单英文编码',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级菜单id',
  `menu_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `menu_icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `menu_sort` int(11) NULL DEFAULT NULL COMMENT '菜单排序',
  `menu_level` int(11) NULL DEFAULT NULL COMMENT '第几级菜单',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单描述',
  `menu_status` int(11) NULL DEFAULT NULL COMMENT '菜单状态',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '顶级菜单', 'top', 0, '/dog', NULL, 0, 0, '顶级菜单项', 0, 1, '2020-06-23 11:07:09', NULL, NULL);
INSERT INTO `sys_menu` VALUES (2, '主控面板', 'main_panel', 1, '/dog/main', 'layui-icon-app', 1, 1, '主控面板模块', 0, 2, '2020-06-24 22:49:28', NULL, NULL);
INSERT INTO `sys_menu` VALUES (3, '权限管理', 'auth', 1, '/dog/auth', 'layui-icon-snowflake', 2, 1, '权限管理模块', 0, 2, '2020-06-25 16:30:31', NULL, NULL);
INSERT INTO `sys_menu` VALUES (4, '用户管理', 'auth_user', 3, '/dog/auth/user', NULL, 1, 2, '用户管理页面', 0, 2, '2020-06-25 16:32:08', 1, '2020-07-06 22:58:54');
INSERT INTO `sys_menu` VALUES (5, '角色管理', 'auth_role', 3, '/dog/auth/role', NULL, 2, 2, '角色管理页面', 0, 2, '2020-06-25 16:33:15', NULL, NULL);
INSERT INTO `sys_menu` VALUES (6, '菜单管理', 'auth_menu', 3, '/dog/auth/menu', NULL, 3, 2, '菜单管理页面', 0, 2, '2020-06-25 16:34:49', 1, '2020-07-19 16:14:47');
INSERT INTO `sys_menu` VALUES (7, '部门管理', 'auth_dept', 3, '/dog/auth/dept', NULL, 4, 2, '部门管理页面', 0, 2, '2020-06-25 16:36:18', NULL, NULL);
INSERT INTO `sys_menu` VALUES (8, '系统配置', 'sys_config', 1, '/dog/sys', 'layui-icon-set-fill', 3, 1, '系统管理模块', 0, 2, '2020-06-25 16:38:42', 1, '2020-07-19 16:15:40');
INSERT INTO `sys_menu` VALUES (9, '字典管理', 'sys_dic', 8, '/dog/sys/dic', NULL, 3, 2, '字典管理页面', 0, 2, '2020-07-11 23:00:49', NULL, NULL);
INSERT INTO `sys_menu` VALUES (10, '字典删除', 'sys_dic_del', 9, '', NULL, 2, 3, '字典删除按钮', 0, 1, '2020-07-11 23:02:54', 1, '2020-07-19 16:04:09');
INSERT INTO `sys_menu` VALUES (11, '组件管理', 'dog_component', 1, '/dog/comp', 'layui-icon-template', 4, 1, '组件信息管理模块', 0, 1, '2020-07-19 11:55:19', 1, '2020-07-19 15:55:40');
INSERT INTO `sys_menu` VALUES (12, 'Redis组件', 'redis_component', 11, 'dog/comp/redis', 'layui-icon-tabs', 1, 2, 'Redis组件介绍页面', 0, 1, '2020-07-19 11:59:54', NULL, NULL);
INSERT INTO `sys_menu` VALUES (13, '删除', 'user_delete', 4, '', '', 2, 3, '用户界面删除按钮', 0, 1, '2020-07-19 12:01:20', 1, '2020-07-26 12:36:38');
INSERT INTO `sys_menu` VALUES (14, '编辑', 'auth_user_update', 4, NULL, NULL, 1, 3, '用户更新按钮', 0, 1, '2020-07-19 12:07:28', 1, '2020-07-26 12:36:27');
INSERT INTO `sys_menu` VALUES (15, '编辑', 'auth_role_update', 5, NULL, NULL, 1, 3, '角色更新按钮', 0, 1, '2020-07-19 12:11:03', 1, '2020-07-26 21:29:46');
INSERT INTO `sys_menu` VALUES (16, '删除', 'auth_role_del', 5, NULL, NULL, 2, 3, '角色删除按钮', 0, 1, '2020-07-19 12:13:52', 1, '2020-07-22 23:05:41');
INSERT INTO `sys_menu` VALUES (17, '字典更新', 'dic_update', 9, NULL, NULL, 1, 3, '字典更新按钮', 0, 1, '2020-07-19 15:19:02', NULL, NULL);
INSERT INTO `sys_menu` VALUES (19, '项目介绍', 'pro_introduction', 2, '/dog/main/pro', 'layui-icon-rate', 1, 2, '项目介绍页面', 0, 1, '2020-07-19 16:17:28', 1, '2020-07-19 16:18:35');
INSERT INTO `sys_menu` VALUES (20, '职位管理', 'position_manager', 3, '/dog/auth/posi', NULL, 5, 2, '职位管理页面', 0, 1, '2020-07-22 22:26:57', NULL, NULL);
INSERT INTO `sys_menu` VALUES (21, '添加', 'user_add_button', 4, NULL, NULL, 1, 3, '用户添加按钮', 0, 1, '2020-07-26 12:37:17', NULL, NULL);
INSERT INTO `sys_menu` VALUES (22, '角色配置', 'user_role_configure', 4, NULL, NULL, 6, 3, '用户界面-角色配置按钮', 0, 1, '2020-07-26 21:24:39', NULL, NULL);
INSERT INTO `sys_menu` VALUES (23, '密码重置', 'user_reset_pwd', 4, NULL, NULL, 7, 3, '用户界面-密码重置按钮', 0, 1, '2020-07-26 21:26:15', NULL, NULL);
INSERT INTO `sys_menu` VALUES (24, '添加', 'role_add_button', 5, NULL, NULL, 1, 3, '角色界面-添加按钮', 0, 1, '2020-07-26 21:32:21', NULL, NULL);
INSERT INTO `sys_menu` VALUES (25, '菜单配置', 'role_menus_configure', 5, NULL, NULL, 100, 3, '角色管理界面-菜单配置按钮', 0, 1, '2020-07-26 21:35:21', NULL, NULL);
INSERT INTO `sys_menu` VALUES (26, '添加', 'menus_add_button', 6, NULL, NULL, 1, 3, '菜单管理页面-添加按钮', 0, 1, '2020-07-26 21:37:24', 1, '2020-07-26 21:54:52');
INSERT INTO `sys_menu` VALUES (27, '编辑', 'menus_update_button', 6, NULL, NULL, 2, 3, '菜单界面-编辑按钮', 0, 1, '2020-07-26 21:45:45', 1, '2020-07-26 21:55:44');
INSERT INTO `sys_menu` VALUES (28, '删除', 'menus_del_button', 6, NULL, NULL, 3, 3, '菜单管理页面-删除按钮', 0, 1, '2020-07-26 21:47:03', 1, '2020-07-26 21:55:55');
INSERT INTO `sys_menu` VALUES (29, '添加', 'dept_add_button', 7, NULL, NULL, 1, 3, '部门管理页面-添加按钮', 0, 1, '2020-07-26 21:50:43', 1, '2020-07-26 21:54:24');
INSERT INTO `sys_menu` VALUES (30, '编辑', 'dept_update_button', 7, NULL, NULL, 2, 3, '部门管理页面-编辑按钮', 0, 1, '2020-07-26 21:54:11', NULL, NULL);
INSERT INTO `sys_menu` VALUES (31, '删除', 'dept_del_button', 7, NULL, NULL, 3, 3, '部门管理页面-删除按钮', 0, 1, '2020-07-26 21:56:39', NULL, NULL);
INSERT INTO `sys_menu` VALUES (32, '指定负责人', 'dept_boss_button', 7, NULL, NULL, NULL, 3, '部门管理页面-指定负责人按钮', 0, 1, '2020-07-26 21:58:42', NULL, NULL);
INSERT INTO `sys_menu` VALUES (33, '添加', 'position_add_button', 20, NULL, NULL, 1, 3, '职位管理页面-添加按钮', 0, 1, '2020-07-26 22:00:30', NULL, NULL);
INSERT INTO `sys_menu` VALUES (34, '编辑', 'position_update_button', 20, NULL, NULL, 2, 3, '职位管理页面-编辑按钮', 0, 1, '2020-07-26 22:01:14', NULL, NULL);
INSERT INTO `sys_menu` VALUES (35, '删除', 'position-del_button', 20, NULL, NULL, 3, 3, '职位管理页面-删除按钮', 0, 1, '2020-07-26 22:01:50', NULL, NULL);

-- ----------------------------
-- Table structure for user_basic_info
-- ----------------------------
DROP TABLE IF EXISTS `user_basic_info`;
CREATE TABLE `user_basic_info`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像路径信息',
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `real_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `gender` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `position_id` bigint(20) NULL DEFAULT NULL COMMENT '职务ID',
  `position_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务名称',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `user_status` int(11) NULL DEFAULT NULL COMMENT '状态(字典)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `user_version` int(11) NULL DEFAULT NULL COMMENT '版本控制',
  PRIMARY KEY (`user_id`, `username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_basic_info
-- ----------------------------
INSERT INTO `user_basic_info` VALUES (1, '252363687457946', 'admin', 'f8267e1b95e7e54b08aff93a42151df9', 'rg`@Y61', 'GrainRain', '2018-09-03 00:00:00', '男', 4, '总经办', 4, '总经理', '306499331@126.com', '18883033906', 0, '2020-06-02 10:20:36', 1, '2020-07-23 23:21:58', 1, 5);
INSERT INTO `user_basic_info` VALUES (2, NULL, 'taixian', '32f0fd5b4b3e58358de083de9f2b68b9', '1aErQB8', '太闲', '1991-02-11 00:00:00', '男', 4, '总经办', 10, '技术总监', 'yung2mao@126.com', '18011674518', 0, '2020-06-18 15:45:07', 1, '2020-07-26 10:42:02', 1, 1);
INSERT INTO `user_basic_info` VALUES (3, NULL, 'yixia', 'aa9936ec1bbfff67b137f19a7c3cd03a', 'lkKKwsS', '易霞', '1987-11-23 00:00:00', '女', 4, '总经办', 9, '财务总监', 'yixia@126.com', '17309740884', 0, '2020-06-24 21:57:44', 2, '2020-07-25 16:27:19', 1, 4);
INSERT INTO `user_basic_info` VALUES (10, NULL, 'chendong', '85921c2bf87866709f41db3dd5567d1f', 'Zvp4o;<', '晨董', '2020-05-05 00:00:00', '男', 5, '研发部', 7, '项目经理', 'chendong@123.com', '15376438627', 0, '2020-06-27 22:17:21', 1, '2020-07-25 16:09:20', 1, 1);
INSERT INTO `user_basic_info` VALUES (12, NULL, 'diangonghuang', 'f2007bbdea28107a41c7c354c987d105', 'U9k2wnc', '德州扑克', '1990-07-03 01:00:00', '男', 9, '成都分公司', 16, '大区经理-成都', 'hung@126.com', '15946537656', 0, '2020-07-19 17:40:39', 2, '2020-07-26 10:40:51', 1, 0);
INSERT INTO `user_basic_info` VALUES (13, NULL, 'tufangjiang', 'b763517950002692b116b2967191bda5', 'UV4SsIg', '僵尸粉', '2019-07-10 00:00:00', '男', 5, '研发部', 6, '研发部长', 'jiang@126.com', '13978567865', 0, '2020-07-19 17:40:55', 2, '2020-07-26 10:42:22', 1, 0);
INSERT INTO `user_basic_info` VALUES (14, NULL, 'anpei', '46bdf0dc55b95d0d508fb85ed314aff8', '_\\LPbZR', '安培', '1997-07-08 00:00:00', '男', 11, '财务部', 17, '财务部长', 'anpei@126.com', '13245674328', 0, '2020-07-23 22:59:35', 1, '2020-07-26 12:34:52', 1, 2);
INSERT INTO `user_basic_info` VALUES (15, NULL, 'dalaoxu', 'ccb15fc9d4912a73feedbd5631f30150', 'QvZ9W5Q', '大佬徐', '2003-07-01 00:00:00', '男', 2, '董事会', 1, '董事长', 'dalaoxu@126.com', '18753745367', 0, '2020-07-25 11:53:35', 1, '2020-07-26 10:42:27', 1, 1);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `role_status` int(11) NULL DEFAULT NULL COMMENT '角色状态',
  `version` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  `create_user_id` bigint(50) NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(50) NULL DEFAULT NULL COMMENT '修改用户',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 'SUPER_MANAGER', '超级管理员', 1, 0, 2, 1, '2020-05-30 15:50:11', 1, '2020-05-30 15:50:17');
INSERT INTO `user_role` VALUES (2, 'NORMAL', '普通用户', 10, 0, 1, 2, '2020-06-28 21:35:49', 2, '2020-06-28 21:35:52');
INSERT INTO `user_role` VALUES (3, 'MANAGER', '子系统管理员', 2, 0, 5, 2, '2020-06-28 22:38:18', 1, '2020-07-25 08:58:10');
INSERT INTO `user_role` VALUES (5, 'GUA', '瓜神', 3, 0, 7, 1, '2020-07-02 22:11:19', 1, '2020-07-13 22:40:30');
INSERT INTO `user_role` VALUES (6, 'INSPECTORS', '督察', 4, 1, 2, 1, '2020-07-13 22:07:56', 1, '2020-07-14 23:18:48');
INSERT INTO `user_role` VALUES (7, 'OBSERVER', '观察员', 5, 0, 1, 1, '2020-07-23 22:59:04', 1, '2020-07-23 22:59:19');

-- ----------------------------
-- Table structure for user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_role_relation`;
CREATE TABLE `user_role_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = armscii8 COLLATE = armscii8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role_relation
-- ----------------------------
INSERT INTO `user_role_relation` VALUES (1, 1, 1);
INSERT INTO `user_role_relation` VALUES (2, 1, 2);
INSERT INTO `user_role_relation` VALUES (11, 10, 2);
INSERT INTO `user_role_relation` VALUES (19, 3, 2);
INSERT INTO `user_role_relation` VALUES (20, 3, 3);
INSERT INTO `user_role_relation` VALUES (21, 2, 3);
INSERT INTO `user_role_relation` VALUES (22, 12, 2);
INSERT INTO `user_role_relation` VALUES (23, 13, 2);
INSERT INTO `user_role_relation` VALUES (25, 14, 3);
INSERT INTO `user_role_relation` VALUES (26, 14, 5);
INSERT INTO `user_role_relation` VALUES (28, 15, 7);

SET FOREIGN_KEY_CHECKS = 1;
