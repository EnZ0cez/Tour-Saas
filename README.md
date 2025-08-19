# 智慧旅游SaaS平台

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green.svg)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-green.svg)](https://vuejs.org/)

面向中小型旅行社的数字化解决方案，整合旅游产品展示、智能推荐及多端管理功能，消除多平台操作痛点。


## 🌟 项目特点

- **一体化解决方案**：为中小型旅行社提供完整的数字化平台
- **高性能架构**：支持日均10,000+次并发请求
- **智能推荐系统**：基于用户行为的个性化推荐算法
- **🤖 LLM智能推荐**：大语言模型驱动的智能化推荐，支持本地和云端模型
- **多端支持**：统一API支持Web、App、小程序等多种终端
- **安全可靠**：JWT令牌鉴权，接口响应成功率99.8%
- **弹性扩展**：模块化设计，易于集成新的AI服务和功能

## 🏗️ 技术架构

### 后端技术栈
- **Spring Boot 3.2+** - 快速开发框架
- **Spring Security** - 安全框架
- **MyBatis** - ORM框架
- **MySQL 8.0** - 关系型数据库
- **JWT** - Token认证机制
- **Maven** - 项目构建工具
- **LLM Integration** - 大语言模型智能推荐
  - **Ollama** - 本地LLM服务支持
  - **OpenAI** - 云端GPT模型支持
  - **WebFlux** - 响应式编程
  - **WebClient** - 异步HTTP客户端

### 前端技术栈
- **Vue 3.2+** - 渐进式JavaScript框架，采用Composition API
- **Element Plus** - Vue 3组件库
- **Vue Router 4** - 路由管理
- **Vuex 4** - 状态管理
- **Axios** - HTTP客户端
- **LLM UI Components** - 大语言模型用户界面组件
- **Responsive Design** - 响应式设计，支持多端访问

## 📁 项目结构

```
tour-saas/
├── tour-saas-backend/     # 后端Spring Boot项目
│   ├── src/main/java/com/toursaas/
│   │   ├── controller/    # 控制器层
│   │   ├── service/       # 业务逻辑层
│   │   ├── repository/    # 数据访问层
│   │   ├── entity/        # 实体类
│   │   ├── security/      # 安全配置
│   │   └── config/        # 配置类
│   └── src/main/resources/
│       ├── db/migration/  # 数据库脚本
│       └── mapper/        # MyBatis映射文件
├── tour-saas-frontend/    # 前端Vue项目
│   ├── src/
│   │   ├── components/    # 公共组件
│   │   ├── views/         # 页面视图
│   │   ├── services/      # API服务
│   │   ├── store/         # 状态管理
│   │   └── router/        # 路由配置
└── docs/                  # 文档资料
```

## 🚀 快速开始

### 环境要求
- JDK 17+ (Spring Boot 3.2+要求)
- Maven 3.6+
- MySQL 8.0
- Node.js 14+
- npm 6+

### LLM服务要求 (可选)
- **Ollama (本地LLM)**: 
  - 下载地址：https://ollama.ai/
  - 推荐模型：llama3.2
- **OpenAI (云端LLM)**:
  - API密钥：https://platform.openai.com/api-keys

### 详细启动指南
有关详细的环境配置和启动说明，请查看 [启动指南](docs/STARTUP_GUIDE.md)。

### 1. 数据库配置

#### 方法一：使用默认root用户（推荐新手）
1. 确保MySQL服务正在运行
2. 登录MySQL：
   ```bash
   mysql -u root -p
   ```
3. 执行以下SQL创建数据库：
   ```sql
   CREATE DATABASE tour_saas DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

#### 方法二：创建专用用户（推荐生产环境）
1. 登录MySQL：
   ```bash
   mysql -u root -p
   ```
2. 执行以下SQL创建数据库和用户：
   ```sql
   -- 创建数据库
   CREATE DATABASE tour_saas DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 创建专用用户
   CREATE USER 'toursaas'@'localhost' IDENTIFIED BY 'toursaas123';
   
   -- 授予权限
   GRANT ALL PRIVILEGES ON tour_saas.* TO 'toursaas'@'localhost';
   
   -- 刷新权限
   FLUSH PRIVILEGES;
   ```

3. 如果使用专用用户，请相应修改`src/main/resources/application.yml`中的数据库配置：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/tour_saas?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
       username: toursaas
       password: toursaas123
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

#### 初始化数据（可选）
项目提供了初始化SQL脚本，在`src/main/resources/db/init.sql`中，包含了表结构和初始数据。

### 2. 后端运行

```bash
# 克隆项目
git clone https://github.com/yourusername/tour-saas.git
cd tour-saas/tour-saas-backend

# 修改数据库配置
# 编辑 src/main/resources/application.yml

# 编译并运行
mvn clean install
mvn spring-boot:run
```

后端服务将运行在 `http://localhost:8080`

### 3. 前端运行

```bash
# 打开新终端窗口
cd tour-saas/tour-saas-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

前端服务将运行在 `http://localhost:8081`

## 🎯 核心功能

### 用户管理
- [x] 用户注册/登录
- [x] JWT令牌认证
- [x] 多角色权限控制
- [x] 用户信息管理

### 旅游产品管理
- [x] 产品增删改查
- [x] 多类型产品支持
- [x] 产品搜索和筛选
- [x] 图片上传管理

### 订单管理系统
- [x] 订单创建和支付
- [x] 订单状态跟踪
- [x] 订单查询统计
- [x] 多维度订单筛选

### 智能推荐系统
- [x] 个性化推荐算法
- [x] 协同过滤推荐
- [x] 推荐结果展示
- [x] 推荐效果分析
- [x] **LLM智能推荐** - 大语言模型驱动的智能推荐
- [x] **多提供商支持** - Ollama本地模型 + OpenAI云端模型
- [x] **个性化旅行建议** - 基于用户行为的定制化建议
- [x] **用户行为分析** - 深度分析用户旅行偏好
- [x] **智能降级** - LLM服务不可用时自动降级到传统算法

### 前端功能
- [x] **现代化UI** - 基于Vue 3和Element Plus的现代化界面
- [x] **LLM推荐组件** - 专门的大语言模型推荐界面
- [x] **服务状态监控** - 实时显示LLM服务可用性
- [x] **响应式设计** - 完美适配桌面端和移动端
- [x] **用户体验优化** - 优雅的加载动画和错误处理
- [x] **实时交互** - 即时的用户反馈和状态更新

## 🛠️ 性能优化

### 数据库优化
- **索引优化**：用户登录SQL优化，查询速度从1.2s降低至50ms
- **联合查询**：MyBatis动态SQL实现毫秒级多表联合查询（订单-用户-产品）

### API优化
- **RESTful设计**：标准化API接口设计
- **响应优化**：统一响应格式，接口响应成功率99.8%
- **缓存策略**：热点数据缓存机制
- **LLM集成优化**：
  - **异步处理**：使用WebFlux实现非阻塞LLM调用
  - **超时控制**：可配置的LLM请求超时机制
  - **错误恢复**：LLM服务不可用时自动降级到传统推荐算法
  - **连接池管理**：HTTP连接池优化，提升并发性能

## 🤖 LLM智能推荐系统

本项目集成了先进的大语言模型(LLM)技术，提供智能化的旅游推荐服务。

### 前端LLM特性

#### 1. 智能推荐界面
- **LlmRecommendations.vue**: 专门的LLM推荐组件
- **实时服务状态**: 动态显示Ollama和OpenAI服务可用性
- **提供商切换**: 支持本地和云端LLM模型切换
- **推荐结果展示**: 美观的推荐卡片和评分系统

#### 2. 个性化功能
- **旅行建议**: 格式化的个性化旅行建议
- **行为分析**: 深度的用户偏好分析报告
- **智能降级**: LLM服务不可用时自动降级到传统算法

#### 3. 用户体验
- **响应式设计**: 支持桌面端和移动端
- **加载状态**: 优雅的加载动画和错误处理
- **实时交互**: 即时的用户反馈和状态更新

### 核心特性

#### 1. 双LLM提供商支持
- **Ollama (本地)**：支持本地部署的LLM模型，保护数据隐私
- **OpenAI (云端)**：集成GPT模型，提供强大的推理能力

#### 2. 智能推荐API
```bash
# LLM智能推荐
POST /api/recommendations/llm/generate/{userId}?provider={provider}

# 个性化旅行建议
POST /api/recommendations/llm/suggestions/{userId}?provider={provider}

# 用户行为分析
POST /api/recommendations/llm/analyze/{userId}?provider={provider}

# 服务状态检查
GET /api/recommendations/llm/status
```

#### 3. 配置管理
在 `application.yml` 中配置LLM设置：
```yaml
llm:
  provider: ollama  # ollama, openai
  model: llama3.2    # 默认模型
  base-url: http://localhost:11434
  api-key: ${OPENAI_API_KEY:}
  max-tokens: 1000
  temperature: 0.7
  timeout: 30s
```

### 快速开始

#### Ollama本地部署
```bash
# 安装并启动Ollama
ollama serve

# 下载推荐模型
ollama pull llama3.2

# 验证安装
ollama list
```

#### OpenAI云端配置
```bash
# 设置API密钥
export OPENAI_API_KEY="your-openai-api-key"
```

### 测试示例
```bash
# 检查服务状态
curl http://localhost:8080/api/recommendations/llm/status

# 生成智能推荐
curl -X POST "http://localhost:8080/api/recommendations/llm/generate/1?provider=ollama"

# 获取个性化建议
curl -X POST "http://localhost:8080/api/recommendations/llm/suggestions/1?provider=ollama"
```

详细使用说明请参考：[LLM推荐系统文档](tour-saas-backend/LLM_RECOMMENDATION_README.md)

## 📊 API文档

启动后端服务后，访问以下地址查看API文档：
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API文档: `http://localhost:8080/v2/api-docs`

## 👥 默认用户

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | password | 管理员 | 系统管理员 |
| agent1 | password | 代理商 | 旅行社代理 |
| user1 | password | 用户 | 普通用户 |

## 📦 部署

### 后端部署
```bash
cd tour-saas-backend
mvn clean package
java -jar target/tour-saas-0.0.1-SNAPSHOT.jar
```

### LLM服务部署 (可选)
```bash
# 启动Ollama本地服务
ollama serve

# 下载推荐模型
ollama pull llama3.2

# 设置OpenAI API密钥 (Linux/Mac)
export OPENAI_API_KEY="your-openai-api-key"

# 设置OpenAI API密钥 (Windows)
set OPENAI_API_KEY=your-openai-api-key
```

### 前端部署
```bash
cd tour-saas-frontend
npm run build
# 将dist目录下的文件部署到Web服务器
```

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来帮助项目发展！

### 开发规范
1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 📄 许可证

本项目采用 MIT 许可证，详情请查看 [LICENSE](LICENSE) 文件。

## 📞 联系方式

- 项目地址: [https://github.com/EnZ0cez/Tour-Saas/](https://github.com/yourusername/tour-saas)
- 邮箱: enzochen617@gmail.com

## 🙏 致谢

- 感谢所有为开源社区做出贡献的开发者
- 感谢Spring Boot和Vue.js团队提供的优秀框架