# FlowMaster 后端服务管理脚本

本目录包含了FlowMaster后端服务的一键启动和停止脚本，支持Linux/macOS和Windows系统。

## 📁 文件说明

### Linux/macOS 脚本
- `start-backend.sh` - 一键启动所有后端服务
- `stop-backend.sh` - 一键停止所有后端服务

### Windows 脚本
- `start-backend.bat` - 一键启动所有后端服务
- `stop-backend.bat` - 一键停止所有后端服务

## 🚀 服务启动顺序

脚本会按照以下顺序启动服务（考虑依赖关系）：

1. **Registry Service** (8761) - 服务注册中心
2. **User Service** (8081) - 用户管理服务
3. **Auth Service** (8082) - 认证授权服务
4. **Workflow Service** (8083) - 工作流引擎服务
5. **Monitoring Service** (8085) - 监控服务
6. **Gateway Service** (8080) - API网关服务

## 📋 使用方法

### Linux/macOS

#### 启动所有服务
```bash
# 进入脚本目录
cd /Users/steven/Project/FlowMaster/scripts

# 启动所有服务
./start-backend.sh start

# 或者直接运行（默认启动）
./start-backend.sh
```

#### 停止所有服务
```bash
# 停止所有服务
./stop-backend.sh stop

# 或者直接运行（默认停止）
./stop-backend.sh
```

#### 其他操作
```bash
# 重启所有服务
./start-backend.sh restart

# 查看服务状态
./start-backend.sh status

# 查看服务日志
./start-backend.sh logs <服务名>

# 清理日志
./start-backend.sh clean

# 显示帮助
./start-backend.sh help
```

### Windows

#### 启动所有服务
```cmd
REM 进入脚本目录
cd C:\Users\steven\Project\FlowMaster\scripts

REM 启动所有服务
start-backend.bat start

REM 或者直接运行（默认启动）
start-backend.bat
```

#### 停止所有服务
```cmd
REM 停止所有服务
stop-backend.bat stop

REM 或者直接运行（默认停止）
stop-backend.bat
```

#### 其他操作
```cmd
REM 重启所有服务
start-backend.bat restart

REM 查看服务状态
start-backend.bat status

REM 强制停止所有Java进程
stop-backend.bat force

REM 清理端口和临时文件
stop-backend.bat cleanup

REM 显示帮助
start-backend.bat help
```

## 🔧 脚本功能特性

### 启动脚本功能
- ✅ **依赖检查** - 自动检查Java和Maven环境
- ✅ **项目编译** - 自动编译所有后端模块
- ✅ **顺序启动** - 按依赖关系顺序启动服务
- ✅ **健康检查** - 等待服务完全启动后再启动下一个
- ✅ **日志管理** - 自动创建日志目录和文件
- ✅ **进程管理** - 记录服务PID，便于管理
- ✅ **状态监控** - 实时显示服务启动状态
- ✅ **错误处理** - 启动失败时自动停止已启动的服务

### 停止脚本功能
- ✅ **优雅停止** - 先发送停止信号，等待进程自然结束
- ✅ **强制停止** - 超时后强制杀死进程
- ✅ **端口清理** - 清理被占用的端口
- ✅ **进程查找** - 通过PID文件和端口查找进程
- ✅ **状态检查** - 显示服务停止状态
- ✅ **文件清理** - 清理日志和临时文件

## 📊 服务端口配置

| 服务名称 | 端口 | 健康检查端点 | 说明 |
|---------|------|-------------|------|
| Registry | 8761 | `/actuator/health` | 服务注册中心 |
| User | 8081 | `/actuator/health` | 用户管理服务 |
| Auth | 8082 | `/actuator/health` | 认证授权服务 |
| Workflow | 8083 | `/actuator/health` | 工作流引擎服务 |
| Monitoring | 8085 | `/actuator/health` | 监控服务 |
| Gateway | 8080 | `/actuator/health` | API网关服务 |

## 🛠️ 环境要求

### 系统要求
- **Java**: JDK 17 或更高版本
- **Maven**: 3.6.0 或更高版本
- **操作系统**: Linux, macOS, Windows

### 网络要求
- 确保所有服务端口未被占用
- 确保可以访问本地服务

### 依赖服务
- **MySQL**: 数据库服务 (端口 3306)
- **Redis**: 缓存服务 (端口 6379)
- **Nacos**: 服务注册中心 (端口 8848) - 可选

## 📝 日志文件

### 日志目录结构
```
logs/
├── services/
│   ├── registry.log
│   ├── user.log
│   ├── auth.log
│   ├── workflow.log
│   ├── monitoring.log
│   ├── gateway.log
│   ├── registry.pid
│   ├── user.pid
│   ├── auth.pid
│   ├── workflow.pid
│   ├── monitoring.pid
│   └── gateway.pid
```

### 查看日志
```bash
# Linux/macOS
tail -f logs/services/user.log

# Windows
type logs\services\user.log
```

## ⚠️ 注意事项

1. **启动顺序很重要** - 不要手动改变服务启动顺序
2. **端口冲突** - 确保所有服务端口未被占用
3. **资源要求** - 所有服务同时运行需要足够的内存
4. **网络连接** - 确保服务间可以正常通信
5. **数据库连接** - 确保MySQL和Redis服务已启动

## 🐛 故障排除

### 常见问题

#### 1. 服务启动失败
```bash
# 检查日志
./start-backend.sh logs <服务名>

# 检查端口占用
netstat -tulpn | grep :8081

# 强制停止所有服务
./stop-backend.sh force
```

#### 2. 端口被占用
```bash
# 查找占用端口的进程
lsof -i :8081

# 强制停止占用进程
kill -9 <PID>
```

#### 3. 编译失败
```bash
# 清理并重新编译
cd /Users/steven/Project/FlowMaster/flowmaster-backend
mvn clean compile
```

#### 4. 服务无法访问
```bash
# 检查服务健康状态
curl http://localhost:8081/actuator/health

# 检查防火墙设置
sudo ufw status
```

## 📞 技术支持

如果遇到问题，请：

1. 查看服务日志文件
2. 检查系统资源使用情况
3. 确认所有依赖服务已启动
4. 联系开发团队获取支持

---

**FlowMaster Team** - 让工作流管理更简单！
