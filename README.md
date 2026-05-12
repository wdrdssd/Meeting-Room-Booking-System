# 会议室预约管理系统

基于 Spring Boot + Vue 的前后端分离会议室预约系统，支持用户预约、管理员审批、状态自动流转、数据统计等功能。
## 技术栈

### 后 端
- Spring Boot 2.x
- MyBatis
- MySQL 8.0
- JWT
- BCryptPasswordEncoder

### 前端
- Vue 2
- Element UI
- Axios

## 核心功能

| 角色 | 功能 |
|------|------|
| 普通用户 | 登录、日历查看预约、创建预约（需审批）、查看我的预约、取消预约/申请、重新预约、修改密码 |
| 管理员 | 登录、审批预约（通过/拒绝）、查看所有预约、数据统计（总览/趋势/会议室排行） |
| 系统定时任务 | 自动更新进行中/已完成状态、自动取消超时未审批的预约 |

## 启动说明

### 环境要求
- JDK 8 或 11
- MySQL 8.0
- Maven 3.6+
- Node.js 14+

### 1. 创建数据库

CREATE DATABASE huiyishi CHARACTER SET utf8mb4;

### 2. 导入 SQL 文件

项目根目录下的 huiyishi.sql

### 3. 修改数据库配置

编辑 src/main/resources/application.properties：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/huiyishi?useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=你的用户名
spring.datasource.password=你的密码
```
### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```
后端运行在 http://localhost:8080

### 5. 启动前端

```bash
cd frontend
npm install
npm run serve
```
前端运行在 http://localhost:8081

### 测试账号

| 角色 | 用户名 | 密码 |
|------|------|------|
|普通用户|	zhangsan|	123456|
|管理员|	admin	|123456|

- 密码已通过 BCrypt加密存储

### 项目亮点

- 预约冲突检测：同一会议室同一时间段不可重复预约

- 审批流程：用户预约后状态为“待审批”，管理员通过后变为“已预约”，拒绝后变为“已取消”

- 状态自动流转：定时任务每分钟执行一次，自动更新“进行中”和“已完成”状态

- 超时自动取消：超过预约开始时间的待审批预约自动设为“已取消”

- 重新预约：用户可在“我的预约”页面携带原预约参数一键重新预约

### API 接口示例

|方法|	路径|	说明|
|------|------|------|
|POST|	/api/login|	用户登录，返回 JWT Token|
|GET|	/api/rooms|	获取可用会议室列表|
|POST|	/api/reservations|	创建预约|
|GET|	/api/reservations/my|	获取当前用户的预约列表|
|PUT|	/api/reservations/{id}/cancel|	取消预约|
|GET|	/api/statistics/overview|	获取统计概览（管理员）|
|PUT|	/api/reservations/{id}/approve|	审批预约（管理员）|
