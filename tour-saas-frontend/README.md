# TourSAAS 前端文档

## 🎯 项目概述

TourSAAS 前端是基于 Vue 3 构建的现代化单页应用，为智慧旅游SaaS平台提供用户界面。前端集成了最新的 LLM 智能推荐功能，支持个性化旅行建议和用户行为分析。

## 🚀 技术栈

### 核心框架
- **Vue 3.2+** - 渐进式JavaScript框架，采用 Composition API
- **Vue Router 4** - 官方路由管理器
- **Vuex 4** - 状态管理模式
- **Element Plus** - Vue 3 UI组件库

### 构建工具
- **Vue CLI 5** - 现代化的前端构建工具
- **Babel** - JavaScript编译器
- **Sass/SCSS** - CSS预处理器

### 网络通信
- **Axios 0.27+** - HTTP客户端
- **js-cookie** - Cookie操作库

### 开发工具
- **ESLint** - 代码质量检查
- **Sass Loader** - 样式预处理
- **Hot Module Replacement** - 热重载

## 📁 项目结构

```
tour-saas-frontend/
├── public/                 # 静态资源
├── src/
│   ├── assets/            # 静态资源
│   ├── components/        # 公共组件
│   │   ├── Header.vue     # 页面头部
│   │   ├── Footer.vue     # 页面底部
│   │   └── LlmRecommendations.vue  # LLM智能推荐组件
│   ├── views/             # 页面视图
│   │   ├── Home.vue       # 首页
│   │   ├── Login.vue      # 登录页
│   │   └── Register.vue   # 注册页
│   ├── router/            # 路由配置
│   │   └── index.js
│   ├── services/          # API服务
│   │   ├── api.js         # API基础配置
│   │   ├── authService.js # 认证服务
│   │   ├── productService.js # 产品服务
│   │   ├── orderService.js # 订单服务
│   │   └── recommendationService.js # 推荐服务
│   ├── store/             # Vuex状态管理
│   │   └── index.js
│   ├── App.vue            # 根组件
│   └── main.js            # 入口文件
├── package.json           # 项目配置
├── vue.config.js          # Vue配置
└── babel.config.js        # Babel配置
```

## 🤖 LLM智能推荐功能

### 新增功能特性

#### 1. LLM推荐服务集成
- **双提供商支持**: Ollama本地模型 + OpenAI云端模型
- **智能推荐算法**: 基于大语言模型的个性化推荐
- **实时服务状态**: 动态检查LLM服务可用性
- **智能降级**: LLM服务不可用时自动降级到传统算法

#### 2. API服务方法
```javascript
// LLM智能推荐
generateLlmRecommendations(userId, provider = 'ollama')

// 个性化旅行建议
generatePersonalizedSuggestions(userId, provider = 'ollama')

// 用户行为分析
analyzeUserBehavior(userId, provider = 'ollama')

// 检查LLM服务状态
checkLlmServices()

// 自定义LLM请求
customLlmRequest(request)
```

#### 3. UI组件特性
- **LlmRecommendations.vue**: 专门的LLM推荐组件
- **服务状态监控**: 实时显示Ollama和OpenAI服务状态
- **提供商选择**: 支持切换本地和云端LLM服务
- **推荐结果展示**: 美观的推荐卡片界面
- **个性化建议**: 格式化的旅行建议显示
- **用户行为分析**: 深度的用户偏好分析报告

### 组件使用示例

```vue
<template>
  <LlmRecommendations 
    v-if="currentUser" 
    :user-id="currentUser.id" 
  />
</template>

<script>
import LlmRecommendations from '@/components/LlmRecommendations.vue'

export default {
  components: {
    LlmRecommendations
  }
}
</script>
```

## 🚀 快速开始

### 环境要求
- **Node.js 14+**
- **npm 6+**
- **现代浏览器** (Chrome 80+, Firefox 75+, Safari 13+)

### 安装步骤

1. **安装依赖**
```bash
cd tour-saas-frontend
npm install
```

2. **启动开发服务器**
```bash
npm run serve
```

3. **访问应用**
打开浏览器访问 `http://localhost:8081`

### 构建部署

```bash
# 生产环境构建
npm run build

# 代码检查
npm run lint
```

## 🔧 配置说明

### API配置
在 `src/services/api.js` 中配置后端API地址：

```javascript
const api = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8080/api',
  timeout: 10000
})
```

### 环境变量
创建 `.env` 文件配置环境变量：

```bash
VUE_APP_API_URL=http://localhost:8080/api
VUE_APP_TITLE=TourSAAS智慧旅游平台
```

## 🎨 样式指南

### 命名规范
- 使用 BEM 命名规范
- 组件样式使用 `scoped` 属性
- 全局样式放在 `src/assets/styles/` 目录

### 主题色彩
- **主色**: #409eff (蓝色)
- **成功色**: #67c23a (绿色)
- **警告色**: #e6a23c (橙色)
- **危险色**: #f56c6c (红色)

### 响应式设计
- 使用 Element Plus 的栅格系统
- 支持移动端适配
- 媒体查询断点：768px, 992px, 1200px

## 📊 性能优化

### 代码分割
- 路由级别的代码分割
- 组件懒加载
- 第三方库按需引入

### 缓存策略
- API响应缓存
- 组件级缓存
- 静态资源缓存

### 打包优化
- Tree Shaking
- 代码压缩
- 图片优化

## 🔒 安全特性

### 认证授权
- JWT Token认证
- 路由守卫
- API请求拦截

### 数据安全
- 敏感信息加密
- XSS防护
- CSRF防护

## 🚀 部署指南

### 静态部署
```bash
# 构建生产版本
npm run build

# 部署dist目录到Web服务器
cp -r dist/* /var/www/html/
```

### Nginx配置
```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /var/www/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 🐛 常见问题

### 开发环境问题
1. **端口冲突**: 修改 `vue.config.js` 中的端口配置
2. **API连接失败**: 检查后端服务是否启动
3. **依赖安装失败**: 清除缓存后重新安装

### 生产环境问题
1. **资源加载失败**: 检查静态资源路径配置
2. **API跨域**: 配置反向代理
3. **路由刷新404**: 配置URL重写规则

## 📝 开发规范

### 代码风格
- 使用 ESLint 进行代码检查
- 遵循 Vue 3 官方风格指南
- 使用 Prettier 格式化代码

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
- 项目地址: [https://github.com/EnZ0cez/Tour-Saas/](https://github.com/EnZ0cez/Tour-Saas/)
- 邮箱: enzochen617@gmail.com
- 问题反馈: [GitHub Issues](https://github.com/EnZ0cez/Tour-Saas/issues)

---

*最后更新: 2025-08-19*