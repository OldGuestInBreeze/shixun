# 数据库设计说明

数据库名：`stray_animal_rescue`。

## 实体关系

- `animal`：动物档案，系统核心实体。
- `rescue_case`：救助线索与案例。
- `adoption_application`：领养申请，通过 `animal_id` 关联动物。
- `volunteer`：志愿者报名记录。
- `donation`：捐赠记录。
- `users`：后台管理员。

## 表结构摘要

| 表 | 关键字段 | 说明 |
| --- | --- | --- |
| users | username, password, role | 管理员账号 |
| animal | name, species, status, location | 动物档案与公示状态 |
| rescue_case | title, urgency, status, contact_phone | 救助线索和处理进度 |
| adoption_application | animal_id, applicant_name, status | 领养申请与审核状态 |
| volunteer | name, phone, skill, status | 志愿者报名与联系状态 |
| donation | donor_name, amount, donation_type | 捐赠公示和统计 |

## 约束与索引

- `users.username` 唯一，防止重复账号。
- `adoption_application.animal_id` 外键引用 `animal.id`。
- `status`、`species`、`urgency` 等字段建立索引，便于筛选和统计。

完整脚本见 `database/schema.sql`。
