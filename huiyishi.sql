/*
 Navicat Premium Dump SQL

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80012 (8.0.12)
 Source Host           : localhost:3306
 Source Schema         : huiyishi

 Target Server Type    : MySQL
 Target Server Version : 80012 (8.0.12)
 File Encoding         : 65001

 Date: 12/05/2026 16:10:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for meeting_room
-- ----------------------------
DROP TABLE IF EXISTS `meeting_room`;
CREATE TABLE `meeting_room`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting_room
-- ----------------------------
INSERT INTO `meeting_room` VALUES (1, 'A101', '1');
INSERT INTO `meeting_room` VALUES (2, 'B101', '1');
INSERT INTO `meeting_room` VALUES (3, 'C101', '1');
INSERT INTO `meeting_room` VALUES (4, 'A201', '1');
INSERT INTO `meeting_room` VALUES (5, 'B301', '1');
INSERT INTO `meeting_room` VALUES (6, 'C201', '1');

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `room_id` int(11) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reserve_date` datetime NULL DEFAULT NULL,
  `start_time` time NULL DEFAULT NULL,
  `end_time` time NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation
-- ----------------------------
INSERT INTO `reservation` VALUES (3, 23064001, 1, '晨会', '2026-03-23 00:00:00', '08:00:00', '08:30:00', '3');
INSERT INTO `reservation` VALUES (4, 23064002, 2, '日常会议', '2026-03-24 00:00:00', '14:00:00', '15:00:00', '3');
INSERT INTO `reservation` VALUES (5, 23064001, 3, '面试', '2026-03-24 00:00:00', '16:00:00', '16:30:00', '3');
INSERT INTO `reservation` VALUES (6, 23064001, 5, '产品发布会', '2026-03-26 00:00:00', '14:00:00', '17:00:00', '1');
INSERT INTO `reservation` VALUES (7, 23064001, 4, '定期汇报', '2026-03-28 00:00:00', '13:00:00', '14:00:00', '1');
INSERT INTO `reservation` VALUES (8, 23064001, 6, '每周总结', '2026-03-29 00:00:00', '17:00:00', '18:00:00', '1');
INSERT INTO `reservation` VALUES (9, 23064001, 1, '晨会', '2026-03-30 00:00:00', '08:00:00', '08:30:00', '0');
INSERT INTO `reservation` VALUES (10, 23064004, 5, '例行会议', '2026-03-27 00:00:00', '15:00:00', '16:00:00', '0');
INSERT INTO `reservation` VALUES (11, 23064004, 3, '面试', '2026-03-28 00:00:00', '10:00:00', '10:30:00', '1');
INSERT INTO `reservation` VALUES (12, 23064003, 5, '晨会', '2026-03-27 00:00:00', '08:30:00', '09:00:00', '4');
INSERT INTO `reservation` VALUES (13, 23064005, 6, '晚会', '2026-03-25 00:00:00', '19:00:00', '19:30:00', '1');
INSERT INTO `reservation` VALUES (14, 23064005, 3, '技术部周例会', '2026-03-31 00:00:00', '12:00:00', '13:00:00', '4');
INSERT INTO `reservation` VALUES (15, 23064005, 4, '客户需求沟通会', '2026-03-31 00:00:00', '16:00:00', '18:00:00', '4');
INSERT INTO `reservation` VALUES (16, 23064005, 6, '面试安排', '2026-03-27 00:00:00', '09:00:00', '09:30:00', '4');
INSERT INTO `reservation` VALUES (17, 23064006, 5, '新员工入职培训', '2026-03-25 00:00:00', '19:00:00', '20:00:00', '1');
INSERT INTO `reservation` VALUES (18, 23064006, 1, '方案演示汇报', '2026-04-01 00:00:00', '15:00:00', '16:30:00', '4');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(255) NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(255) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (23064001, 'zhangsan', '$2a$10$vW/4nXVEtcv.3bTJ4G1Gr.FhtVkr4waXMc67ATXhY0ADxYAIzmvwa', '张三', 0, '21312432@qq.com');
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '管理员', 1, '21421432@qq.com');
INSERT INTO `user` VALUES (23064002, 'lisi', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '李四', 0, '23523432@qq.com');
INSERT INTO `user` VALUES (23064003, 'wangwu', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '王五', 0, '127463725@qq.com');
INSERT INTO `user` VALUES (23064004, 'xiaohong', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '小红', 0, '6326542@qq.com');
INSERT INTO `user` VALUES (23064005, 'xiaoqian', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '小钱', 0, '326567567@qq.com');
INSERT INTO `user` VALUES (23064006, 'xiaozhao', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '小赵', 0, '41221321341@qq.com');
INSERT INTO `user` VALUES (23064007, 'xiaosun', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '小孙', 0, '63256416@qq.com');
INSERT INTO `user` VALUES (23064008, 'xiaoxu', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '小徐', 0, '2354162312@qq.com');
INSERT INTO `user` VALUES (23064009, 'xiaochen', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '小陈', 0, '4231231@qq.com');
INSERT INTO `user` VALUES (23064010, 'xiaohuang', '$2a$10$IPOQE2qHCTaCFsvJAs27K.31b3PFWACkgSsLpTR2My3oTbkPI1H0e', '小黄', 0, '23213521@qq.com');

SET FOREIGN_KEY_CHECKS = 1;
