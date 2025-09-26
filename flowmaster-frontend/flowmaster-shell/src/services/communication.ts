// 微前端应用间通信
export interface MessageData {
  type: string
  payload: any
  source: string
  target?: string
}

export interface AuthData {
  token: string
  refreshToken: string
  user: any
  permissions: string[]
}

export interface ThemeData {
  theme: 'light' | 'dark'
  primaryColor: string
}

// 消息类型枚举
export enum MessageType {
  // 认证相关
  AUTH_LOGIN = 'AUTH_LOGIN',
  AUTH_LOGOUT = 'AUTH_LOGOUT',
  AUTH_REFRESH = 'AUTH_REFRESH',
  AUTH_SYNC = 'AUTH_SYNC',
  
  // 主题相关
  THEME_CHANGE = 'THEME_CHANGE',
  THEME_SYNC = 'THEME_SYNC',
  
  // 导航相关
  NAVIGATE = 'NAVIGATE',
  NAVIGATE_BACK = 'NAVIGATE_BACK',
  
  // 数据同步
  DATA_SYNC = 'DATA_SYNC',
  DATA_REQUEST = 'DATA_REQUEST',
  
  // 通知相关
  NOTIFICATION = 'NOTIFICATION',
  TOAST = 'TOAST',
  
  // 应用状态
  APP_LOADED = 'APP_LOADED',
  APP_ERROR = 'APP_ERROR',
  APP_UNLOAD = 'APP_UNLOAD'
}

// 通信管理器
export class MicroAppCommunicator {
  private messageHandlers: Map<string, (data: MessageData) => void> = new Map()
  private authData: AuthData | null = null
  private themeData: ThemeData = {
    theme: 'light',
    primaryColor: '#667eea'
  }

  constructor() {
    this.setupMessageListener()
    this.loadStoredData()
  }

  // 设置消息监听器
  private setupMessageListener() {
    window.addEventListener('message', (event) => {
      // 安全检查：只接受来自可信源的消息
      if (this.isTrustedOrigin(event.origin)) {
        this.handleMessage(event.data)
      }
    })
  }

  // 检查是否为可信源
  private isTrustedOrigin(origin: string): boolean {
    const trustedOrigins = [
      'http://localhost:3000', // 主容器
      'http://localhost:3001', // 用户工作台
      'http://localhost:3002', // 管理后台
      'http://localhost:3003'  // 流程设计器
    ]
    return trustedOrigins.includes(origin)
  }

  // 处理接收到的消息
  private handleMessage(data: MessageData) {
    const handler = this.messageHandlers.get(data.type)
    if (handler) {
      handler(data)
    }
  }

  // 注册消息处理器
  registerHandler(type: string, handler: (data: MessageData) => void) {
    this.messageHandlers.set(type, handler)
  }

  // 发送消息到所有子应用
  broadcast(type: string, payload: any, source: string = 'shell') {
    const message: MessageData = {
      type,
      payload,
      source
    }

    // 通过 localStorage 实现跨应用通信
    localStorage.setItem(`micro-app-message-${Date.now()}`, JSON.stringify(message))
    
    // 触发 storage 事件
    window.dispatchEvent(new StorageEvent('storage', {
      key: `micro-app-message-${Date.now()}`,
      newValue: JSON.stringify(message)
    }))
  }

  // 发送消息到特定应用
  sendToApp(appName: string, type: string, payload: any) {
    const message: MessageData = {
      type,
      payload,
      source: 'shell',
      target: appName
    }

    localStorage.setItem(`micro-app-${appName}-message-${Date.now()}`, JSON.stringify(message))
  }

  // 认证相关方法
  setAuthData(authData: AuthData) {
    this.authData = authData
    localStorage.setItem('micro-app-auth', JSON.stringify(authData))
    this.broadcast(MessageType.AUTH_SYNC, authData)
  }

  getAuthData(): AuthData | null {
    return this.authData
  }

  clearAuthData() {
    this.authData = null
    localStorage.removeItem('micro-app-auth')
    this.broadcast(MessageType.AUTH_LOGOUT, null)
  }

  // 主题相关方法
  setThemeData(themeData: ThemeData) {
    this.themeData = themeData
    localStorage.setItem('micro-app-theme', JSON.stringify(themeData))
    this.broadcast(MessageType.THEME_SYNC, themeData)
  }

  getThemeData(): ThemeData {
    return this.themeData
  }

  // 导航相关方法
  navigateTo(path: string) {
    this.broadcast(MessageType.NAVIGATE, { path })
  }

  // 加载存储的数据
  private loadStoredData() {
    // 加载认证数据
    const storedAuth = localStorage.getItem('micro-app-auth')
    if (storedAuth) {
      try {
        this.authData = JSON.parse(storedAuth)
      } catch (error) {
        console.error('Failed to parse stored auth data:', error)
      }
    }

    // 加载主题数据
    const storedTheme = localStorage.getItem('micro-app-theme')
    if (storedTheme) {
      try {
        this.themeData = JSON.parse(storedTheme)
      } catch (error) {
        console.error('Failed to parse stored theme data:', error)
      }
    }
  }

  // 清理资源
  cleanup() {
    this.messageHandlers.clear()
  }
}

// 全局通信实例
export const microAppCommunicator = new MicroAppCommunicator()
