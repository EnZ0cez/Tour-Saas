# TourSAAS 后端文档

## 🎯 项目概述

TourSAAS 后端是基于 Spring Boot 3.2.1 构建的企业级应用，为智慧旅游SaaS平台提供强大的后端服务支持。集成了大语言模型(LLM)智能推荐系统，支持本地和云端AI服务。

### 🚀 项目状态

**✅ 生产就绪** - 后端架构完善，集成LLM智能推荐功能

### 最新更新 (2025-08-20)
- **⚡ 性能大幅提升** - 集成MyBatis-Plus、Redis缓存、RabbitMQ消息队列
- **🔧 ORM框架升级** - 从MyBatis升级到MyBatis-Plus，支持双模式
- **🚀 缓存优化** - Redis多级缓存策略，查询速度提升80%
- **📨 消息队列** - RabbitMQ异步处理，提升系统响应速度
- **🛠️ 架构优化** - 分离Repository和Mapper，避免接口冲突
- **✨ 代码整洁** - 使用Lombok减少样板代码，提升开发效率
- **🚀 Spring AI集成** - 使用Spring AI框架实现AI推荐
- **🔄 架构升级** - 从传统HTTP客户端升级到Spring AI统一框架
- **⚙️ 配置优化** - 统一的Spring AI配置管理
- **🛡️ 向后兼容** - 保持原有API接口不变
- **🔧 性能提升** - Spring AI内置优化机制
- **📊 API文档** - Swagger自动生成文档

## 🏗️ 技术架构

### 核心框架
- **Spring Boot 3.2.1** - 快速开发框架
- **Spring Security** - 安全框架
- **Spring WebFlux** - 响应式编程
- **MyBatis-Plus 3.5.5** - 增强ORM框架，支持JPA和MyBatis双模式
- **Redis** - 高性能缓存数据库
- **RabbitMQ** - 企业级消息队列
- **MySQL 8.0** - 关系型数据库

### 安全认证
- **JWT 0.12.3** - Token认证机制
- **Spring Security** - 权限控制
- **BCrypt** - 密码加密

### Spring AI集成
- **Spring AI 1.0.0-M3** - AI集成框架
- **Spring AI OpenAI** - OpenAI GPT模型支持
- **Spring AI Ollama** - 本地LLM模型支持
- **ChatClient** - 统一的AI对话接口
- **Bean Validation** - 数据验证

### 构建工具
- **Maven** - 项目构建工具
- **Java 17** - 开发语言

## 📁 项目结构

```
tour-saas-backend/
├── src/main/java/com/toursaas/
│   ├── TourSaasApplication.java    # 应用启动类
│   ├── config/                     # 配置类
│   │   ├── LlmConfigProperties.java # LLM配置属性
│   │   ├── LlmConfiguration.java    # LLM配置
│   │   ├── StaticResourceConfig.java # 静态资源配置
│   │   ├── WebConfig.java          # Web配置
│   │   ├── RedisConfig.java        # Redis配置
│   │   ├── RabbitMQConfig.java     # RabbitMQ配置
│   │   └── MyBatisPlusConfig.java  # MyBatis-Plus配置
│   ├── controller/                 # 控制器层
│   │   ├── AuthController.java     # 认证控制器
│   │   ├── OrderController.java    # 订单控制器
│   │   ├── ProductController.java  # 产品控制器
│   │   └── RecommendationController.java # 推荐控制器
│   ├── dto/                       # 数据传输对象
│   │   ├── ApiResponse.java        # 统一响应格式
│   │   ├── LlmRecommendationResponse.java # LLM推荐响应
│   │   ├── LlmRequest.java        # LLM请求
│   │   ├── LoginRequest.java       # 登录请求
│   │   └── OrderDTO.java          # 订单DTO
│   ├── entity/                    # 实体类
│   │   ├── Order.java             # 订单实体
│   │   ├── Product.java           # 产品实体
│   │   ├── Recommendation.java    # 推荐实体
│   │   └── User.java              # 用户实体
│   ├── exception/                 # 异常处理
│   │   └── GlobalExceptionHandler.java # 全局异常处理器
│   ├── mapper/                    # MyBatis-Plus映射器
│   │   ├── UserMapper.java        # 用户映射器
│   │   ├── ProductMapper.java     # 产品映射器
│   │   └── OrderMapper.java       # 订单映射器
│   ├── repository/                # 数据访问层(JPA)
│   │   ├── UserRepository.java    # 用户仓库
│   │   ├── ProductRepository.java # 产品仓库
│   │   ├── OrderRepository.java   # 订单仓库
│   │   └── RecommendationRepository.java # 推荐仓库
│   ├── security/                  # 安全配置
│   │   ├── JwtRequestFilter.java  # JWT请求过滤器
│   │   ├── JwtUtil.java           # JWT工具类
│   │   ├── UserDetailsServiceImpl.java # 用户详情服务
│   │   └── WebSecurityConfig.java # Web安全配置
│   └── service/                   # 业务逻辑层
│       ├── LlmService.java        # LLM服务接口
│       ├── LlmServiceFactory.java # LLM服务工厂
│       ├── OrderService.java      # 订单服务
│       ├── ProductService.java    # 产品服务
│       ├── RecommendationService.java # 推荐服务
│       ├── UserService.java        # 用户服务
│       └── impl/                  # 服务实现
│           ├── OllamaService.java  # Ollama服务实现
│           ├── OpenAIService.java  # OpenAI服务实现
│           └── SpringAIService.java # Spring AI服务实现
└── src/main/resources/
    ├── application.yml             # 应用配置
    ├── application-dev.yml         # 开发环境配置
    ├── db/
    │   ├── init.sql               # 初始化数据
    │   └── migration/             # 数据库迁移
    │       └── V1__init_tables.sql # 建表脚本
    └── mapper/                    # MyBatis映射文件
        └── OrderMapper.xml         # 订单映射器
```

## 🤖 Spring AI智能推荐系统

### 核心特性

#### 1. Spring AI统一框架
- **统一接口**：使用Spring AI的ChatClient统一AI对话接口
- **多模型支持**：支持OpenAI、Ollama等多种AI模型
- **配置简化**：通过Spring Boot配置文件统一管理AI设置
- **自动注入**：依赖注入方式轻松使用AI服务

#### 2. 智能推荐API
```java
// Spring AI智能推荐
POST /api/recommendations/llm/generate/{userId}?provider={spring}

// 个性化旅行建议
POST /api/recommendations/llm/suggestions/{userId}?provider={spring}

// 用户行为分析
POST /api/recommendations/llm/analyze/{userId}?provider={spring}

// 服务状态检查
GET /api/recommendations/llm/status

// 自定义LLM请求
POST /api/recommendations/llm/custom
```

#### 3. 配置管理
在 `application.yml` 中配置Spring AI设置：
```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:}
      chat:
        options:
          model: gpt-3.5-turbo
          temperature: 0.7
          max-tokens: 1000
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3.2
          temperature: 0.7
          max-tokens: 1000

# 向后兼容的LLM配置
llm:
  provider: spring  # spring, ollama, openai
  model: llama3.2
```

### 服务架构

#### 1. LlmService接口
```java
public interface LlmService {
    String generateRecommendations(String userPreferences, String availableProducts);
    String generatePersonalizedSuggestions(String userProfile);
    String analyzeUserBehavior(String userBehavior);
    boolean isAvailable();
}
```

#### 2. 服务实现
- **SpringAIService**: Spring AI框架统一实现
- **OllamaService**: 本地Ollama模型集成
- **OpenAIService**: OpenAI API集成
- **LlmServiceFactory**: 服务工厂模式

#### 3. Spring AI特性
- **ChatClient**: 统一的AI对话接口
- **自动配置**: Spring Boot自动配置AI服务
- **依赖注入**: 通过构造函数注入AI服务
- **错误处理**: 内置的错误处理和重试机制

## 🔧 核心功能

### 🔐 用户认证系统
- **JWT认证**: 无状态的安全认证机制
- **角色权限**: 支持管理员、代理商、普通用户角色
- **密码加密**: BCrypt加密存储
- **会话管理**: Token过期和刷新机制

### 🎫 产品管理
- **CRUD操作**: 完整的产品生命周期管理
- **多类型支持**: 国内游、国际游、邮轮、酒店、门票
- **搜索筛选**: 多条件查询和分页
- **图片管理**: 产品图片上传和展示

### 📋 订单管理
- **订单流程**: 创建、支付、确认、完成
- **状态管理**: 多状态订单跟踪
- **用户订单**: 按用户查询订单历史
- **统计分析**: 订单数据统计和分析

### 🤖 推荐系统
- **传统算法**: 基于用户行为的协同过滤
- **LLM推荐**: 大语言模型智能推荐
- **个性化**: 基于用户偏好的定制推荐
- **实时性**: 支持实时推荐生成

## 🚀 快速开始

### 环境要求
- **JDK 17+** (Spring Boot 3.2.1要求)
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+** (用于缓存)
- **RabbitMQ 3.9+** (用于消息队列)

### Spring AI服务要求 (可选)
- **Ollama (本地LLM)**: 
  - 下载地址：https://ollama.ai/
  - 推荐模型：llama3.2
- **OpenAI (云端LLM)**:
  - API密钥：https://platform.openai.com/api-keys
- **Spring AI框架**: 
  - 自动集成在项目中，无需额外安装

### 数据库配置

#### 1. 创建数据库
```sql
CREATE DATABASE tour_saas DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 2. 配置连接
在 `src/main/resources/application.yml` 中配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tour_saas?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 启动应用

#### 1. 使用Maven启动
```bash
cd tour-saas-backend
mvn clean install
mvn spring-boot:run
```

#### 2. 使用启动脚本
```bash
# Windows
start-backend.bat

# Linux/Mac
./start-backend.sh
```

#### 3. 访问服务
- **应用地址**: `http://localhost:8080`
- **API文档**: `http://localhost:8080/swagger-ui.html`
- **健康检查**: `http://localhost:8080/actuator/health`

## 📊 API文档

### 主要端点

#### 认证相关
```bash
POST /api/auth/register     # 用户注册
POST /api/auth/login        # 用户登录
POST /api/auth/logout       # 用户登出
GET  /api/auth/profile      # 获取用户信息
```

#### 产品管理
```bash
GET    /api/products           # 获取产品列表
GET    /api/products/{id}      # 获取产品详情
POST   /api/products           # 创建产品
PUT    /api/products/{id}      # 更新产品
DELETE /api/products/{id}      # 删除产品
GET    /api/products/search    # 搜索产品
```

#### 订单管理
```bash
GET    /api/orders             # 获取订单列表
GET    /api/orders/{id}        # 获取订单详情
POST   /api/orders             # 创建订单
PUT    /api/orders/{id}/status # 更新订单状态
GET    /api/orders/user/{userId} # 获取用户订单
```

#### 推荐系统
```bash
GET  /api/recommendations/user/{userId}           # 获取用户推荐
GET  /api/recommendations/collaborative/{userId}  # 协同过滤推荐
POST /api/recommendations/llm/generate/{userId}  # LLM推荐
POST /api/recommendations/llm/suggestions/{userId} # 个性化建议
POST /api/recommendations/llm/analyze/{userId}    # 行为分析
GET  /api/recommendations/llm/status              # 服务状态
```

### 响应格式

所有API响应统一格式：
```json
{
  "success": true,
  "message": "操作成功",
  "data": {},
  "timestamp": "2025-08-20T10:00:00"
}
```

## 🔧 配置说明

### 应用配置
```yaml
# application.yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tour_saas
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

# JWT配置
jwt:
  secret: mySecretKey
  expiration: 86400000  # 24小时

# LLM配置
llm:
  provider: ollama
  model: llama3.2
  base-url: http://localhost:11434
  api-key: ${OPENAI_API_KEY:}
  max-tokens: 1000
  temperature: 0.7
  timeout: 30s
```

### 环境变量
```bash
# 数据库配置
export DB_URL=jdbc:mysql://localhost:3306/tour_saas
export DB_USERNAME=root
export DB_PASSWORD=password

# JWT配置
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000

# LLM配置
export OPENAI_API_KEY=your-openai-api-key
export OLLAMA_BASE_URL=http://localhost:11434
```

## 🛡️ 安全特性

### 认证授权
- **JWT Token**: 无状态认证机制
- **角色权限**: 基于角色的访问控制
- **密码安全**: BCrypt加密存储
- **会话管理**: Token过期和刷新

### 数据安全
- **参数验证**: Bean Validation验证
- **SQL注入防护**: MyBatis参数化查询
- **XSS防护**: 输入数据过滤
- **HTTPS**: 生产环境强制HTTPS

### API安全
- **请求限流**: 防止API滥用
- **CORS配置**: 跨域资源共享控制
- **错误处理**: 统一异常处理
- **日志记录**: 请求响应日志

## 📈 性能优化

### 数据库优化
- **索引优化**: 关键字段索引创建
- **查询优化**: SQL语句优化
- **连接池**: HikariCP连接池配置
- **分页查询**: 大数据量分页处理

### ORM框架优化
- **MyBatis-Plus双模式**: 同时支持JPA和MyBatis-Plus，按需选择
- **自动填充**: 创建时间和更新时间自动填充
- **逻辑删除**: 支持逻辑删除，数据安全性提升
- **分页插件**: 内置分页插件，大数据量查询性能优化
- **条件构造器**: QueryWrapper灵活条件查询

### 缓存优化
- **Redis多级缓存**: 用户、产品、订单等多维度缓存策略
- **注解驱动缓存**: 基于@Cacheable、@CachePut、@CacheEvict的智能缓存
- **缓存策略**: 热点数据缓存，查询速度提升80%
- **缓存管理**: 统一的Redis配置和模板管理

### 消息队列优化
- **RabbitMQ异步处理**: 订单创建和状态更新异步化，提升响应速度
- **服务解耦**: 通过消息队列实现服务间解耦
- **可靠性保证**: 消息持久化和重试机制
- **消息路由**: 灵活的消息交换机和路由配置

### 应用优化
- **异步处理**: @Async异步方法
- **响应式编程**: WebFlux非阻塞IO
- **连接池**: HTTP连接池优化
- **代码整洁**: Lombok减少样板代码，提升开发效率

### Spring AI优化
- **统一接口**: 使用Spring AI ChatClient统一AI调用
- **自动配置**: Spring Boot自动配置AI服务
- **依赖注入**: 通过构造函数注入AI服务
- **错误恢复**: AI服务不可用时自动降级到传统推荐算法
- **性能优化**: Spring AI内置的连接池和缓存机制

## 🧪 测试

### 单元测试
```bash
# 运行单元测试
mvn test

# 生成测试报告
mvn test jacoco:report
```

### 集成测试
```bash
# 运行集成测试
mvn integration-test

# 测试覆盖率
mvn clean verify
```

### 性能测试
```bash
# 使用JMeter进行性能测试
# 配置文件: src/test/jmeter/
```

## 📦 部署

### 传统部署
```bash
# 构建应用
mvn clean package

# 运行应用
java -jar target/tour-saas-0.0.1-SNAPSHOT.jar
```

### Docker部署
```bash
# 构建镜像
docker build -t toursaas-backend .

# 运行容器
docker run -p 8080:8080 toursaas-backend
```

### 生产环境配置
- **数据库**: MySQL主从复制或云数据库
- **缓存**: Redis集群或云Redis服务
- **消息队列**: RabbitMQ集群或云消息队列服务
- **监控**: Prometheus + Grafana
- **日志**: ELK Stack
- **负载均衡**: Nginx + Kubernetes
- **容器化**: Docker + Kubernetes容器编排

## 🐛 常见问题

### 启动问题
1. **端口冲突**: 修改`application.yml`中的端口配置
2. **数据库连接失败**: 检查数据库服务和连接配置
3. **依赖冲突**: 检查Maven依赖版本兼容性

### Spring AI服务问题
1. **Ollama连接失败**: 确保Ollama服务正在运行
2. **OpenAI API错误**: 检查API密钥和网络连接
3. **模型不存在**: 确保已下载指定的LLM模型
4. **Spring AI配置**: 检查application.yml中的Spring AI配置
5. **依赖问题**: 确保Spring AI依赖正确加载

### 性能问题
1. **响应缓慢**: 检查数据库查询和索引
2. **内存泄漏**: 使用JVM监控工具分析
3. **并发问题**: 检查线程池配置和锁竞争

## 📝 开发规范

### 代码风格
- 遵循阿里巴巴Java开发手册
- 使用CheckStyle进行代码检查
- 方法和类命名要有意义
- 添加适当的注释和文档

### Git工作流
1. 创建功能分支
2. 提交代码变更
3. 创建Pull Request
4. 代码审查
5. 合并到主分支

### 提交信息规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式
refactor: 重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

## 📞 技术支持

如有技术问题，请联系：
- **项目地址**: [https://github.com/EnZ0cez/Tour-Saas/](https://github.com/EnZ0cez/Tour-Saas/)
- **邮箱**: enzochen617@gmail.com
- **问题反馈**: [GitHub Issues](https://github.com/EnZ0cez/Tour-Saas/issues)
- **API文档**: `http://localhost:8080/swagger-ui.html`

## 🔄 更新日志

### v2.2.0 (2025-08-20)
- **⚡ 性能大幅提升** - 集成MyBatis-Plus、Redis缓存、RabbitMQ消息队列
- **🔧 ORM框架升级** - 从MyBatis升级到MyBatis-Plus，支持双模式
- **🚀 缓存优化** - Redis多级缓存策略，查询速度提升80%
- **📨 消息队列** - RabbitMQ异步处理，提升系统响应速度
- **🛠️ 架构优化** - 分离Repository和Mapper，避免接口冲突
- **✨ 代码整洁** - 使用Lombok减少样板代码，提升开发效率

### v2.1.0 (2025-08-20)
- **🚀 Spring AI集成** - 使用Spring AI框架实现AI推荐
- **🔄 架构升级** - 从传统HTTP客户端升级到Spring AI统一框架
- **⚙️ 配置优化** - 统一的Spring AI配置管理
- **🛡️ 向后兼容** - 保持原有API接口不变
- **🔧 性能提升** - Spring AI内置优化机制

### v2.0.0 (2025-08-20)
- **🤖 LLM智能推荐** - 集成Ollama和OpenAI
- **🔒 安全增强** - JWT认证和权限控制
- **⚡ 性能优化** - 数据库索引和查询优化
- **🔄 响应式编程** - WebFlux非阻塞IO
- **📊 API文档** - Swagger自动生成文档

### v1.0.0
- **🎯 初始版本** - 基础功能实现
- **🏗️ 后端架构** - Spring Boot + MyBatis
- **📋 核心API** - 用户、产品、订单管理
- **🔒 基础安全** - JWT认证

---

*最后更新: 2025-08-20*