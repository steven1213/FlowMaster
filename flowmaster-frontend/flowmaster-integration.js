// FlowMaster 前端服务整合配置
const FlowMasterConfig = {
    // 服务配置
    services: {
        web: {
            name: '用户工作台',
            url: 'http://localhost:3000',
            port: 3000,
            description: '面向最终用户的工作流操作界面',
            icon: '🌐',
            features: ['任务处理', '审批流程', '工作监控', '个人中心']
        },
        admin: {
            name: '管理后台',
            url: 'http://localhost:3003',
            port: 3003,
            description: '系统管理员界面',
            icon: '⚙️',
            features: ['用户管理', '权限配置', '系统监控', '审计日志']
        },
        designer: {
            name: '流程设计器',
            url: 'http://localhost:3001',
            port: 3001,
            description: '可视化流程设计工具',
            icon: '🎨',
            features: ['流程建模', 'BPMN设计', '模板管理', '版本控制']
        }
    },
    
    // 后端API配置
    backend: {
        userService: 'http://localhost:8081/user-service',
        authService: 'http://localhost:8081/auth-service',
        workflowService: 'http://localhost:8082/workflow-service',
        gateway: 'http://localhost:8083/gateway'
    },
    
    // 统一认证配置
    auth: {
        tokenKey: 'flowmaster_token',
        refreshTokenKey: 'flowmaster_refresh_token',
        userInfoKey: 'flowmaster_user_info'
    },
    
    // 主题配置
    theme: {
        primaryColor: '#667eea',
        secondaryColor: '#764ba2',
        successColor: '#52c41a',
        warningColor: '#faad14',
        errorColor: '#f5222d'
    }
};

// 服务状态检查
class ServiceMonitor {
    constructor() {
        this.statusCache = new Map();
        this.checkInterval = 30000; // 30秒检查一次
    }
    
    async checkServiceStatus(serviceKey) {
        const service = FlowMasterConfig.services[serviceKey];
        if (!service) return false;
        
        try {
            const response = await fetch(service.url, {
                method: 'HEAD',
                mode: 'no-cors',
                cache: 'no-cache'
            });
            return true;
        } catch (error) {
            return false;
        }
    }
    
    async checkAllServices() {
        const statusPromises = Object.keys(FlowMasterConfig.services).map(async (key) => {
            const isOnline = await this.checkServiceStatus(key);
            this.statusCache.set(key, isOnline);
            return { key, isOnline };
        });
        
        return Promise.all(statusPromises);
    }
    
    startMonitoring() {
        this.checkAllServices();
        setInterval(() => {
            this.checkAllServices();
        }, this.checkInterval);
    }
    
    getServiceStatus(serviceKey) {
        return this.statusCache.get(serviceKey) || false;
    }
}

// 统一认证管理
class AuthManager {
    constructor() {
        this.token = localStorage.getItem(FlowMasterConfig.auth.tokenKey);
        this.refreshToken = localStorage.getItem(FlowMasterConfig.auth.refreshTokenKey);
        this.userInfo = JSON.parse(localStorage.getItem(FlowMasterConfig.auth.userInfoKey) || 'null');
    }
    
    setAuth(token, refreshToken, userInfo) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userInfo = userInfo;
        
        localStorage.setItem(FlowMasterConfig.auth.tokenKey, token);
        localStorage.setItem(FlowMasterConfig.auth.refreshTokenKey, refreshToken);
        localStorage.setItem(FlowMasterConfig.auth.userInfoKey, JSON.stringify(userInfo));
    }
    
    clearAuth() {
        this.token = null;
        this.refreshToken = null;
        this.userInfo = null;
        
        localStorage.removeItem(FlowMasterConfig.auth.tokenKey);
        localStorage.removeItem(FlowMasterConfig.auth.refreshTokenKey);
        localStorage.removeItem(FlowMasterConfig.auth.userInfoKey);
    }
    
    isAuthenticated() {
        return !!this.token;
    }
    
    getAuthHeaders() {
        return {
            'Authorization': `Bearer ${this.token}`,
            'Content-Type': 'application/json'
        };
    }
}

// 服务间通信
class ServiceCommunicator {
    constructor() {
        this.authManager = new AuthManager();
        this.messageHandlers = new Map();
        this.setupMessageListener();
    }
    
    setupMessageListener() {
        window.addEventListener('message', (event) => {
            if (event.origin !== window.location.origin) return;
            
            const { type, data } = event.data;
            const handler = this.messageHandlers.get(type);
            if (handler) {
                handler(data);
            }
        });
    }
    
    registerMessageHandler(type, handler) {
        this.messageHandlers.set(type, handler);
    }
    
    sendMessage(targetWindow, type, data) {
        targetWindow.postMessage({ type, data }, window.location.origin);
    }
    
    // 跨服务导航
    navigateToService(serviceKey, path = '') {
        const service = FlowMasterConfig.services[serviceKey];
        if (!service) return;
        
        const url = `${service.url}${path}`;
        window.open(url, '_blank');
    }
    
    // 同步认证状态
    syncAuthToService(serviceKey) {
        const service = FlowMasterConfig.services[serviceKey];
        if (!service || !this.authManager.isAuthenticated()) return;
        
        const authData = {
            token: this.authManager.token,
            refreshToken: this.authManager.refreshToken,
            userInfo: this.authManager.userInfo
        };
        
        // 通过localStorage同步认证信息
        localStorage.setItem(`${serviceKey}_auth_sync`, JSON.stringify(authData));
    }
}

// 初始化FlowMaster整合系统
class FlowMasterIntegration {
    constructor() {
        this.serviceMonitor = new ServiceMonitor();
        this.serviceCommunicator = new ServiceCommunicator();
        this.init();
    }
    
    init() {
        // 启动服务监控
        this.serviceMonitor.startMonitoring();
        
        // 注册消息处理器
        this.serviceCommunicator.registerMessageHandler('auth_sync', (data) => {
            this.serviceCommunicator.authManager.setAuth(data.token, data.refreshToken, data.userInfo);
        });
        
        this.serviceCommunicator.registerMessageHandler('logout', () => {
            this.serviceCommunicator.authManager.clearAuth();
            // 通知所有服务登出
            Object.keys(FlowMasterConfig.services).forEach(key => {
                localStorage.setItem(`${key}_logout`, Date.now().toString());
            });
        });
        
        // 页面卸载时清理
        window.addEventListener('beforeunload', () => {
            this.cleanup();
        });
    }
    
    cleanup() {
        // 清理定时器等资源
        if (this.serviceMonitor.checkInterval) {
            clearInterval(this.serviceMonitor.checkInterval);
        }
    }
    
    // 获取服务状态
    getServiceStatus(serviceKey) {
        return this.serviceMonitor.getServiceStatus(serviceKey);
    }
    
    // 打开服务
    openService(serviceKey, path = '') {
        this.serviceCommunicator.navigateToService(serviceKey, path);
    }
    
    // 同步认证到所有服务
    syncAuthToAllServices() {
        Object.keys(FlowMasterConfig.services).forEach(key => {
            this.serviceCommunicator.syncAuthToService(key);
        });
    }
}

// 全局实例
window.FlowMaster = new FlowMasterIntegration();

// 导出配置和工具
window.FlowMasterConfig = FlowMasterConfig;
window.ServiceMonitor = ServiceMonitor;
window.AuthManager = AuthManager;
window.ServiceCommunicator = ServiceCommunicator;
