@echo off
REM 启动前端服务脚本

echo 正在启动智慧旅游SaaS平台前端服务...

REM 检查Node.js是否安装
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js，请先安装Node.js
    exit /b 1
)

REM 进入前端目录
cd tour-saas-frontend

REM 检查依赖包是否安装
if not exist "node_modules" (
    echo 正在安装依赖包...
    npm install
)

REM 启动开发服务器
echo 正在启动Vue开发服务器...
npm run serve

echo 前端服务启动完成！