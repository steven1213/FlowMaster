import React, { useEffect, useRef, useState } from 'react'
import { Spin, Alert } from 'antd'
import { microApps, MicroAppConfig } from '@/types/microApp'

interface MicroAppContainerProps {
  appName: string
  onAppLoad?: (appName: string) => void
  onAppError?: (appName: string, error: Error) => void
}

const MicroAppContainer: React.FC<MicroAppContainerProps> = ({
  appName,
  onAppLoad,
  onAppError
}) => {
  const containerRef = useRef<HTMLDivElement>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!appName) return

    const appConfig = microApps.find(app => app.name === appName)
    if (!appConfig) {
      setError(`应用 ${appName} 未找到`)
      setLoading(false)
      return
    }

    loadMicroApp(appConfig)
  }, [appName])

  const loadMicroApp = async (config: MicroAppConfig) => {
    try {
      setLoading(true)
      setError(null)

      // 动态加载微前端应用
      const appElement = document.createElement('div')
      appElement.id = config.name
      appElement.style.width = '100%'
      appElement.style.height = '100%'

      if (containerRef.current) {
        containerRef.current.innerHTML = ''
        containerRef.current.appendChild(appElement)
      }

      // 这里可以使用 single-spa 或其他微前端框架
      // 目前使用简单的 iframe 方式
      const iframe = document.createElement('iframe')
      iframe.src = config.url
      iframe.style.width = '100%'
      iframe.style.height = '100%'
      iframe.style.border = 'none'
      iframe.style.borderRadius = '8px'
      
      iframe.onload = () => {
        setLoading(false)
        onAppLoad?.(config.name)
      }

      iframe.onerror = () => {
        setError(`加载应用 ${config.name} 失败`)
        setLoading(false)
        onAppError?.(config.name, new Error('应用加载失败'))
      }

      appElement.appendChild(iframe)

    } catch (err) {
      setError(`加载应用失败: ${err}`)
      setLoading(false)
      onAppError?.(appName, err as Error)
    }
  }

  if (error) {
    return (
      <Alert
        message="应用加载失败"
        description={error}
        type="error"
        showIcon
        style={{ margin: '24px' }}
      />
    )
  }

  return (
    <div style={{ position: 'relative', height: '100%' }}>
      {loading && (
        <div style={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          zIndex: 1000
        }}>
          <Spin size="large" tip="正在加载应用..." />
        </div>
      )}
      
      <div 
        ref={containerRef}
        id="micro-app-container"
        style={{ 
          width: '100%', 
          height: '100%',
          opacity: loading ? 0.5 : 1,
          transition: 'opacity 0.3s'
        }}
      />
    </div>
  )
}

export default MicroAppContainer
