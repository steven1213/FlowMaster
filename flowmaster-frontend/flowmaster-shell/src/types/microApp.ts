// 微前端应用配置
export interface MicroAppConfig {
  name: string
  url: string
  container: string
  activeRule: string
  props?: Record<string, any>
}

// 微前端应用注册表
export const microApps: MicroAppConfig[] = [
  {
    name: 'flowmaster-web',
    url: 'http://localhost:3001',
    container: '#micro-app-container',
    activeRule: '/web',
    props: {
      theme: 'light',
      locale: 'zh-CN'
    }
  },
  {
    name: 'flowmaster-admin',
    url: 'http://localhost:3002',
    container: '#micro-app-container',
    activeRule: '/admin',
    props: {
      theme: 'light',
      locale: 'zh-CN'
    }
  },
  {
    name: 'flowmaster-designer',
    url: 'http://localhost:3003',
    container: '#micro-app-container',
    activeRule: '/designer',
    props: {
      theme: 'light',
      locale: 'zh-CN'
    }
  }
]

// 应用状态
export interface AppState {
  currentApp: string | null
  isAuthenticated: boolean
  user: any
  theme: 'light' | 'dark'
  locale: 'zh-CN' | 'en-US'
}

// 默认应用状态
export const defaultAppState: AppState = {
  currentApp: null,
  isAuthenticated: false,
  user: null,
  theme: 'light',
  locale: 'zh-CN'
}
