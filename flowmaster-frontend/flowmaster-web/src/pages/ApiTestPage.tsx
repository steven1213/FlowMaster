import React, { useState, useEffect } from 'react'
import { Card, Button, Space, Typography, Alert, Spin, Divider } from 'antd'
import { ReloadOutlined, CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons'
import { api } from '@/services/apiClient'

const { Title, Text, Paragraph } = Typography

interface TestResult {
  success: boolean
  data?: any
  error?: string
  timestamp: string
}

const ApiTestPage: React.FC = () => {
  const [testResults, setTestResults] = useState<Record<string, TestResult>>({})
  const [loading, setLoading] = useState(false)

  const runTest = async (testName: string, testFn: () => Promise<any>) => {
    setLoading(true)
    try {
      const startTime = Date.now()
      const result = await testFn()
      const endTime = Date.now()
      
      setTestResults(prev => ({
        ...prev,
        [testName]: {
          success: true,
          data: result,
          timestamp: new Date().toLocaleString()
        }
      }))
    } catch (error: any) {
      setTestResults(prev => ({
        ...prev,
        [testName]: {
          success: false,
          error: error.message || '未知错误',
          timestamp: new Date().toLocaleString()
        }
      }))
    } finally {
      setLoading(false)
    }
  }

  const tests = [
    {
      name: 'ping',
      label: '连通性测试',
      description: '测试前后端基本连通性',
      fn: () => api.get('/test/ping')
    },
    {
      name: 'health',
      label: '健康检查',
      description: '检查服务健康状态',
      fn: () => api.get('/test/health')
    },
    {
      name: 'info',
      label: '服务信息',
      description: '获取服务基本信息',
      fn: () => api.get('/test/info')
    },
    {
      name: 'echo',
      label: '回显测试',
      description: '测试POST请求和JSON数据传递',
      fn: () => api.post('/test/echo', { message: 'Hello FlowMaster!', timestamp: Date.now() })
    }
  ]

  const runAllTests = async () => {
    for (const test of tests) {
      await runTest(test.name, test.fn)
      // 添加小延迟避免请求过快
      await new Promise(resolve => setTimeout(resolve, 500))
    }
  }

  const getStatusIcon = (result: TestResult) => {
    if (!result) return null
    return result.success ? 
      <CheckCircleOutlined style={{ color: '#52c41a' }} /> : 
      <CloseCircleOutlined style={{ color: '#f5222d' }} />
  }

  const getStatusColor = (result: TestResult) => {
    if (!result) return 'default'
    return result.success ? 'success' : 'error'
  }

  return (
    <div style={{ padding: 24 }}>
      <Title level={2}>🔧 API连通性测试</Title>
      <Paragraph>
        此页面用于测试前后端API连通性，确保服务正常运行。
      </Paragraph>

      <Card style={{ marginBottom: 24 }}>
        <Space>
          <Button 
            type="primary" 
            icon={<ReloadOutlined />} 
            onClick={runAllTests}
            loading={loading}
          >
            运行所有测试
          </Button>
          <Text type="secondary">
            点击按钮测试所有API接口的连通性
          </Text>
        </Space>
      </Card>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 16 }}>
        {tests.map(test => {
          const result = testResults[test.name]
          return (
            <Card 
              key={test.name}
              title={
                <Space>
                  {getStatusIcon(result)}
                  {test.label}
                </Space>
              }
              extra={
                <Button 
                  size="small" 
                  onClick={() => runTest(test.name, test.fn)}
                  loading={loading}
                >
                  测试
                </Button>
              }
            >
              <Paragraph type="secondary" style={{ marginBottom: 16 }}>
                {test.description}
              </Paragraph>
              
              {result && (
                <Alert
                  message={result.success ? '测试成功' : '测试失败'}
                  description={
                    result.success ? 
                      <pre style={{ margin: 0, fontSize: 12 }}>
                        {JSON.stringify(result.data, null, 2)}
                      </pre> :
                      result.error
                  }
                  type={getStatusColor(result) as any}
                  showIcon
                  style={{ marginBottom: 8 }}
                />
              )}
              
              {result && (
                <Text type="secondary" style={{ fontSize: 12 }}>
                  测试时间: {result.timestamp}
                </Text>
              )}
            </Card>
          )
        })}
      </div>

      <Divider />

      <Card title="📊 测试总结">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 16 }}>
          <div>
            <Text strong>总测试数: </Text>
            <Text>{tests.length}</Text>
          </div>
          <div>
            <Text strong>成功数: </Text>
            <Text style={{ color: '#52c41a' }}>
              {Object.values(testResults).filter(r => r.success).length}
            </Text>
          </div>
          <div>
            <Text strong>失败数: </Text>
            <Text style={{ color: '#f5222d' }}>
              {Object.values(testResults).filter(r => !r.success).length}
            </Text>
          </div>
          <div>
            <Text strong>成功率: </Text>
            <Text>
              {Object.values(testResults).length > 0 ? 
                Math.round((Object.values(testResults).filter(r => r.success).length / Object.values(testResults).length) * 100) : 0}%
            </Text>
          </div>
        </div>
      </Card>
    </div>
  )
}

export default ApiTestPage
