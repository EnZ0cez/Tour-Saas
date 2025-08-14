@echo off
title Tour SaaS Frontend

echo ==========================================
echo   Tour SaaS 前端 - 启动脚本
echo ==========================================

echo 正在检查Node.js环境...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js，请先安装Node.js 14或更高版本
    pause
    exit /b 1
)

echo 正在检查npm环境...
npm -v >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到npm，请先安装Node.js (包含npm)
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   正在安装前端依赖...
echo ==========================================
cd /d "%~dp0tour-saas-frontend"

if not exist "node_modules" (
    echo 首次运行，正在安装依赖包...
    npm install
    if %errorlevel% neq 0 (
        echo 错误: 依赖包安装失败
        pause
        exit /b 1
    )
) else (
    echo 依赖包已存在，跳过安装
)

echo.
echo ==========================================
echo   正在启动前端开发服务器...
echo ==========================================
echo 前端服务将运行在 http://localhost:8081
echo.

npm run serve

echo.
echo ==========================================
echo   前端服务启动失败
echo ==========================================
echo 请检查错误信息
echo.

pause