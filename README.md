# 流浪动物救助智能信息公示平台

基于 JavaWeb MVC 的课程实训项目，围绕“流浪动物救助智能信息公示”场景，实现公开展示、救助线索、领养申请、志愿者报名、捐赠公示和后台管理等完整功能。

## 技术栈

- 开发工具：IntelliJ IDEA（导入 Maven 项目）
- 后端：Java 8、Spring Boot 2.7、Spring MVC、Spring Data JPA
- 前端：Thymeleaf、HTML5、CSS3
- 数据库：MySQL 8.x（默认也支持 H2 内存库便于演示）
- 架构：MVC / BCE 分层

## 功能模块

### 公众端

1. 首页数据看板：动物档案、待领养、救助中、志愿者、捐赠金额统计。
2. 动物信息公示：动物列表、关键字/状态筛选、动物详情。
3. 领养申请：对待领养动物提交申请。
4. 救助线索：查看救助案例，提交新的救助线索。
5. 志愿者报名：登记技能、可服务时间和联系方式。
6. 爱心捐赠：登记资金、物资或服务捐赠。

### 管理端

1. 管理员登录/退出。
2. 后台看板。
3. 动物档案新增、编辑、删除。
4. 救助案例新增、编辑、删除、状态维护。
5. 领养申请审核。
6. 志愿者联系状态维护。
7. 捐赠记录和金额汇总。

默认管理员：`admin` / `admin123`。

## 启动方式

### 1. 直接演示

```bash
mvn spring-boot:run
```

访问 `http://localhost:8080`。默认使用 H2 内存数据库，并自动写入演示数据。

### 2. 使用 MySQL

先在 MySQL 中执行：

```sql
source database/schema.sql;
```

再设置环境变量：

```powershell
$env:SPRING_PROFILES_ACTIVE="mysql"
$env:DB_URL="jdbc:mysql://localhost:3306/stray_animal_rescue?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8"
$env:DB_USER="root"
$env:DB_PASSWORD="你的数据库密码"
mvn spring-boot:run
```

### 3. IDEA 运行

1. 用 IDEA 打开项目根目录。
2. 等待 Maven 依赖导入。
3. 运行 `com.rescue.shixun.ShixunApplication`。
4. 浏览器访问 `http://localhost:8080`。

## 构建和测试

```bash
mvn test
mvn package
```

## 目录结构

```text
src/main/java/com/rescue/shixun
├── config      # 初始化数据与配置
├── controller  # MVC Controller
├── model       # Entity 实体
└── repository  # JPA Repository
src/main/resources/templates # Thymeleaf 视图
src/main/resources/static    # 前端样式
database/schema.sql          # MySQL 建库脚本
docs                         # 需求、数据库、MVC、建模文档
```

## 文档

- [需求规格说明](docs/requirements.md)
- [数据库设计说明](docs/database-design.md)
- [MVC/BCE 架构设计](docs/mvc-design.md)
- [用例与建模说明](docs/use-case-model.md)
- [PlantUML 建模文件](docs/modeling)
