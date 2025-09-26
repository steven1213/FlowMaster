# FlowMaster Admin Application Dockerfile
# 多阶段构建

# 构建阶段
FROM node:18-alpine AS builder

WORKDIR /app

# 复制 package.json 和 package-lock.json
COPY flowmaster-frontend/flowmaster-admin/package*.json ./

# 安装依赖
RUN npm install

# 复制源代码
COPY flowmaster-frontend/flowmaster-admin/ .

# 构建应用
RUN npm run build

# 生产阶段
FROM nginx:alpine

# 复制构建的文件到 nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制 nginx 配置
COPY docker/nginx/conf.d/admin.conf /etc/nginx/conf.d/default.conf

# 创建非 root 用户
RUN addgroup -g 1001 -S nodejs && \
    adduser -S nextjs -u 1001

# 更改文件所有者
RUN chown -R nextjs:nodejs /usr/share/nginx/html

# 切换到非 root 用户
USER nextjs

# 暴露端口
EXPOSE 3003

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:3003/ || exit 1

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
