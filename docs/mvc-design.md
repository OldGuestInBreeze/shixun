# MVC/BCE 架构设计

## MVC 分层

| 层 | 包/目录 | 职责 |
| --- | --- | --- |
| Model | `com.rescue.shixun.model` | JPA Entity，承载业务数据 |
| View | `src/main/resources/templates` | Thymeleaf 页面 |
| Controller | `com.rescue.shixun.controller` | Spring MVC Controller，接收请求并返回视图 |
| Repository | `com.rescue.shixun.repository` | 数据库访问 |
| Config | `com.rescue.shixun.config` | 初始化数据、密码工具 |

## BCE 对应

- Boundary：Thymeleaf 页面、Controller 请求入口。
- Control：Controller 中的业务流程控制。
- Entity：JPA Entity 和数据库表。

## 请求流程示例

```text
浏览器 -> PublicController -> Repository -> MySQL/H2
       <- Thymeleaf View <- Model 数据
```

后台地址通过 `HttpSession` 判断登录状态，未登录访问 `/admin/*` 时跳转到登录页。
