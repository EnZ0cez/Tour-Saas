# Tour SaaS Platform 启动指南

## 环境准备

### 1. 安装必要软件

在启动项目之前，请确保已安装以下软件：

1. **JDK 8 或更高版本**
   - 下载地址：https://www.oracle.com/java/technologies/downloads/
   - 验证安装：`java -version`

2. **Maven 3.6 或更高版本**
   - 下载地址：https://maven.apache.org/download.cgi
   - 验证安装：`mvn -v`

3. **MySQL 8.0**
   - 下载地址：https://dev.mysql.com/downloads/mysql/
   - 验证安装：`mysql --version`

4. **Node.js 14 或更高版本**
   - 下载地址：https://nodejs.org/
   - 验证安装：`node -v` 和 `npm -v`

### 2. 数据库配置

#### 2.1 启动MySQL服务
- Windows: 在服务中启动MySQL服务
- Mac/Linux: 使用 `sudo service mysql start` 或 `brew services start mysql`

#### 2.2 创建数据库
```sql
CREATE DATABASE tour_saas DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 2.3 创建用户（可选，推荐）
```sql
-- 创建用户
CREATE USER 'toursaas'@'localhost' IDENTIFIED BY 'toursaas123';

-- 授予权限
GRANT ALL PRIVILEGES ON tour_saas.* TO 'toursaas'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES;
```

#### 2.4 修改配置文件（如使用自定义用户）
编辑 `tour-saas-backend/src/main/resources/application-dev.yml` 文件：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tour_saas?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: toursaas  # 修改为你的用户名
    password: toursaas123  # 修改为你的密码
```

## 启动项目

### 方法一：使用启动脚本（推荐）

#### 启动后端服务
双击运行 `start-backend.bat` 文件，或在命令行中执行：
```bash
start-backend.bat
```

#### 启动前端服务
双击运行 `start-frontend.bat` 文件，或在命令行中执行：
```bash
start-frontend.bat
```

### 方法二：手动启动

#### 启动后端服务
```bash
# 进入后端目录
cd tour-saas-backend

# 编译并运行
mvn clean install
mvn spring-boot:run
```

后端服务将运行在 `http://localhost:8080`

#### 启动前端服务
```bash
# 进入前端目录
cd tour-saas-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

前端服务将运行在 `http://localhost:8081`

## 访问应用

1. 后端API: http://localhost:8080
2. 前端界面: http://localhost:8081
3. API文档: http://localhost:8080/swagger-ui.html

## 默认用户

系统包含以下默认用户，可用于测试：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | password | 管理员 | 系统管理员 |
| agent1 | password | 代理商 | 旅行社代理 |
| user1 | password | 用户 | 普通用户 |

## 故障排除

### 数据库连接问题
1. 确保MySQL服务正在运行
2. 检查数据库名称、用户名和密码是否正确
3. 确保MySQL允许本地连接

### 端口占用问题
如果8080或8081端口被占用，可以修改配置：
- 后端端口：修改 `application-dev.yml` 中的 `server.port`
- 前端端口：修改 `tour-saas-frontend/vue.config.js` 中的 `devServer.port`

### 依赖安装问题
如果遇到依赖安装问题：
1. 清除npm缓存：`npm cache clean --force`
2. 删除node_modules目录和package-lock.json文件后重新安装

### 编译问题
如果遇到Java编译问题：
1. 确保JDK版本正确
2. 清理并重新编译：`mvn clean compile`

## 生产部署

### 后端部署
```bash
cd tour-saas-backend
mvn clean package
java -jar target/tour-saas-0.0.1-SNAPSHOT.jar
```

### 前端部署
```bash
cd tour-saas-frontend
npm run build
# 将dist目录下的文件部署到Web服务器
```