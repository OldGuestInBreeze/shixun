# 用例与建模说明

建模可参照 EA 样例绘制，下方 PlantUML 文件可作为用例图、类图、顺序图和活动图蓝本。

| 编号 | 用例 | 参与者 | 后置结果 |
| --- | --- | --- | --- |
| UC-01 | 浏览动物信息 | 公众用户 | 获得动物公开信息 |
| UC-02 | 提交领养申请 | 公众用户 | 生成待审核申请 |
| UC-03 | 提交救助线索 | 公众用户 | 生成待处理案例 |
| UC-04 | 志愿者报名 | 公众用户 | 生成志愿者记录 |
| UC-05 | 登记捐赠 | 公众用户 | 生成捐赠记录 |
| UC-06 | 维护动物档案 | 管理员 | 更新动物公示信息 |
| UC-07 | 审核领养申请 | 管理员 | 申请状态变更 |
| UC-08 | 维护救助案例 | 管理员 | 救助进度更新 |

## 建模文件

- `docs/modeling/use-case.puml`
- `docs/modeling/class-diagram.puml`
- `docs/modeling/adoption-sequence.puml`
- `docs/modeling/rescue-activity.puml`
