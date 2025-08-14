# 贡献指南

感谢您对智慧旅游SaaS平台项目的关注！我们欢迎任何形式的贡献。

## 如何贡献

### 报告Bug
在提交bug报告之前，请先搜索是否已有相同的问题。如果找不到，请创建一个新的issue，包含以下信息：
- 清晰的标题和描述
- 复现步骤
- 预期行为和实际行为
- 环境信息（操作系统、浏览器、版本等）
- 相关截图或错误日志

### 提交功能请求
我们欢迎新功能的建议！请创建一个issue来描述：
- 功能的详细描述
- 解决的问题或带来的价值
- 可能的实现方案

### 提交代码

#### 开发流程
1. Fork项目到您的GitHub账户
2. 克隆Fork的仓库到本地
3. 创建新的功能分支 (`git checkout -b feature/AmazingFeature`)
4. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
5. 推送到分支 (`git push origin feature/AmazingFeature`)
6. 开启Pull Request

#### 代码规范
- 遵循项目现有的代码风格
- 添加必要的单元测试
- 确保所有测试通过
- 更新相关文档

#### 提交信息规范
- 使用清晰、简洁的提交信息
- 遵循[约定式提交](https://www.conventionalcommits.org/zh-hans/v1.0.0/)规范
- 示例：
  - `feat: 添加用户注册功能`
  - `fix: 修复登录页面样式问题`
  - `docs: 更新README文档`
  - `chore: 升级依赖版本`

### 代码审查
所有提交的Pull Request都需要经过代码审查。审查者会检查：
- 代码质量和规范
- 功能实现正确性
- 测试覆盖率
- 文档完整性

## 开发环境设置

请参考[README.md](README.md)中的快速开始部分来设置开发环境。

## 代码风格

### 后端 (Java)
- 遵循Google Java Style Guide
- 使用Lombok减少样板代码
- 合理使用注解和设计模式

### 前端 (Vue.js)
- 遵循Vue.js官方风格指南
- 使用ESLint和Prettier保持代码一致性
- 组件化开发，保持组件单一职责

## 测试

### 后端测试
- 单元测试使用JUnit 5
- 集成测试使用Spring Boot Test
- 数据库测试使用内存数据库H2

### 前端测试
- 单元测试使用Jest
- 组件测试使用Vue Test Utils
- 端到端测试使用Cypress

## 文档

- 更新相关功能的README文档
- 添加必要的代码注释
- 更新API文档

## 问题和讨论

如有任何问题，请：
1. 查看已有issue
2. 创建新的issue进行讨论
3. 加入我们的讨论群组（如果有的话）

再次感谢您的贡献！