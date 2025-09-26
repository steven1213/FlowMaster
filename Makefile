# FlowMaster Makefile
# 提供便捷的开发和部署命令

.PHONY: help build test clean deploy-docker deploy-k8s health-check

# 默认目标
help: ## 显示帮助信息
	@echo "FlowMaster 项目管理"
	@echo ""
	@echo "可用命令:"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# 开发命令
install: ## 安装所有依赖
	@echo "安装后端依赖..."
	cd flowmaster-backend && ./mvnw dependency:resolve
	@echo "安装前端依赖..."
	cd flowmaster-frontend/flowmaster-web && npm install
	cd flowmaster-frontend/flowmaster-admin && npm install
	cd flowmaster-frontend/flowmaster-designer && npm install
	cd flowmaster-frontend/flowmaster-mobile && npm install

build-backend: ## 构建后端服务
	@echo "构建后端服务..."
	cd flowmaster-backend && ./mvnw clean package -DskipTests

build-frontend: ## 构建前端应用
	@echo "构建前端应用..."
	cd flowmaster-frontend/flowmaster-web && npm run build
	cd flowmaster-frontend/flowmaster-admin && npm run build
	cd flowmaster-frontend/flowmaster-designer && npm run build

build: build-backend build-frontend ## 构建所有项目

test-backend: ## 运行后端测试
	@echo "运行后端测试..."
	cd flowmaster-backend && ./mvnw test

test-frontend: ## 运行前端测试
	@echo "运行前端测试..."
	cd flowmaster-frontend/flowmaster-web && npm run test
	cd flowmaster-frontend/flowmaster-admin && npm run test
	cd flowmaster-frontend/flowmaster-designer && npm run test

test: test-backend test-frontend ## 运行所有测试

lint-backend: ## 检查后端代码质量
	@echo "检查后端代码质量..."
	cd flowmaster-backend && ./mvnw spotbugs:check checkstyle:check

lint-frontend: ## 检查前端代码质量
	@echo "检查前端代码质量..."
	cd flowmaster-frontend/flowmaster-web && npm run lint
	cd flowmaster-frontend/flowmaster-admin && npm run lint
	cd flowmaster-frontend/flowmaster-designer && npm run lint

lint: lint-backend lint-frontend ## 检查所有代码质量

# Docker 命令
docker-build: ## 构建 Docker 镜像
	@echo "构建 Docker 镜像..."
	docker build -t flowmaster/user:latest -f flowmaster-backend/flowmaster-user/Dockerfile .
	docker build -t flowmaster/auth:latest -f flowmaster-backend/flowmaster-auth/Dockerfile .
	docker build -t flowmaster/workflow:latest -f flowmaster-backend/flowmaster-workflow/Dockerfile .
	docker build -t flowmaster/gateway:latest -f flowmaster-backend/flowmaster-gateway/Dockerfile .
	docker build -t flowmaster/monitoring:latest -f flowmaster-backend/flowmaster-monitoring/Dockerfile .
	docker build -t flowmaster/web:latest -f flowmaster-frontend/flowmaster-web/Dockerfile .
	docker build -t flowmaster/admin:latest -f flowmaster-frontend/flowmaster-admin/Dockerfile .
	docker build -t flowmaster/designer:latest -f flowmaster-frontend/flowmaster-designer/Dockerfile .

docker-up: ## 启动 Docker Compose 服务
	@echo "启动 Docker Compose 服务..."
	docker-compose up -d

docker-down: ## 停止 Docker Compose 服务
	@echo "停止 Docker Compose 服务..."
	docker-compose down

docker-logs: ## 查看 Docker Compose 日志
	docker-compose logs -f

docker-clean: ## 清理 Docker 资源
	@echo "清理 Docker 资源..."
	docker-compose down -v
	docker images | grep flowmaster | awk '{print $$3}' | xargs docker rmi -f

# Kubernetes 命令
k8s-apply: ## 应用 Kubernetes 配置
	@echo "应用 Kubernetes 配置..."
	kubectl apply -f k8s/namespaces/
	kubectl apply -f k8s/configmaps/
	kubectl apply -f k8s/secrets/
	kubectl apply -f k8s/deployments/
	kubectl apply -f k8s/services/
	kubectl apply -f k8s/ingress/

k8s-delete: ## 删除 Kubernetes 资源
	@echo "删除 Kubernetes 资源..."
	kubectl delete -f k8s/ingress/
	kubectl delete -f k8s/services/
	kubectl delete -f k8s/deployments/
	kubectl delete -f k8s/secrets/
	kubectl delete -f k8s/configmaps/
	kubectl delete -f k8s/namespaces/

k8s-status: ## 查看 Kubernetes 状态
	kubectl get pods -n flowmaster
	kubectl get services -n flowmaster
	kubectl get ingress -n flowmaster

# 部署命令
deploy-docker: docker-build docker-up ## 使用 Docker Compose 部署
	@echo "Docker Compose 部署完成"

deploy-k8s: docker-build k8s-apply ## 使用 Kubernetes 部署
	@echo "Kubernetes 部署完成"

# 开发服务器
dev-backend: ## 启动后端开发服务器
	@echo "启动后端开发服务器..."
	cd flowmaster-backend && ./mvnw spring-boot:run

dev-web: ## 启动 Web 应用开发服务器
	@echo "启动 Web 应用开发服务器..."
	cd flowmaster-frontend/flowmaster-web && npm run dev

dev-admin: ## 启动管理后台开发服务器
	@echo "启动管理后台开发服务器..."
	cd flowmaster-frontend/flowmaster-admin && npm run dev

dev-designer: ## 启动设计器开发服务器
	@echo "启动设计器开发服务器..."
	cd flowmaster-frontend/flowmaster-designer && npm run dev

# 健康检查
health-check: ## 执行健康检查
	@echo "执行健康检查..."
	@curl -f http://localhost:8080/actuator/health || echo "用户服务不健康"
	@curl -f http://localhost:8081/actuator/health || echo "认证服务不健康"
	@curl -f http://localhost:8082/actuator/health || echo "工作流服务不健康"
	@curl -f http://localhost:8083/actuator/health || echo "网关服务不健康"
	@curl -f http://localhost:8084/actuator/health || echo "监控服务不健康"

# 清理命令
clean: ## 清理构建文件
	@echo "清理构建文件..."
	cd flowmaster-backend && ./mvnw clean
	cd flowmaster-frontend/flowmaster-web && rm -rf dist
	cd flowmaster-frontend/flowmaster-admin && rm -rf dist
	cd flowmaster-frontend/flowmaster-designer && rm -rf dist

clean-all: clean docker-clean ## 清理所有文件
	@echo "清理所有文件..."

# 数据库命令
db-init: ## 初始化数据库
	@echo "初始化数据库..."
	docker-compose exec mysql mysql -u root -pflowmaster123 -e "CREATE DATABASE IF NOT EXISTS flowmaster_user;"
	docker-compose exec mysql mysql -u root -pflowmaster123 -e "CREATE DATABASE IF NOT EXISTS flowmaster_auth;"
	docker-compose exec mysql mysql -u root -pflowmaster123 -e "CREATE DATABASE IF NOT EXISTS flowmaster_workflow;"

db-migrate: ## 运行数据库迁移
	@echo "运行数据库迁移..."
	cd flowmaster-backend && ./mvnw flyway:migrate

# 监控命令
monitor: ## 查看监控面板
	@echo "监控面板地址:"
	@echo "  Grafana: http://localhost:3000"
	@echo "  Prometheus: http://localhost:9090"
	@echo "  Zipkin: http://localhost:9411"
	@echo "  Kibana: http://localhost:5601"
