#!/bin/bash
# 启动后端服务脚本

echo "正在启动智慧旅游SaaS平台后端服务..."

# 检查Maven是否安装
if ! command -v mvn &> /dev/null
then
    echo "错误: 未找到Maven，请先安装Maven"
    exit 1
fi

# 进入后端目录
cd tour-saas-backend

# 清理并编译项目
echo "正在编译项目..."
mvn clean compile

# 运行Spring Boot应用
echo "正在启动Spring Boot应用..."
mvn spring-boot:run

echo "后端服务启动完成！"