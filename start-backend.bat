@echo off
title Tour SaaS Platform

echo ==========================================
echo   Tour SaaS Platform - 启动脚本
echo ==========================================

echo 正在检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java运行环境，请先安装JDK 8或更高版本
    pause
    exit /b 1
)

echo 正在检查Maven环境...
mvn -v >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请先安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo 正在检查MySQL服务...
echo 请确保MySQL服务已在运行，且已按README.md中的说明创建了数据库和用户。
echo 如果尚未设置，请先参考README.md中的数据库配置部分。
pause

echo.
echo ==========================================
echo   正在启动后端服务...
echo ==========================================
echo 后端服务将运行在 http://localhost:8080
echo.

cd /d "%~dp0tour-saas-backend"
start "Tour SaaS Backend" mvn spring-boot:run

echo.
echo ==========================================
echo   后端服务启动命令已执行
echo ==========================================
echo 请查看新打开的终端窗口了解启动详情
echo 如遇到数据库连接问题，请检查MySQL配置
echo.

pause