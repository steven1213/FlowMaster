import React, { useEffect, useState } from 'react'
import {
  View,
  StyleSheet,
  ScrollView,
  RefreshControl
} from 'react-native'
import {
  Card,
  Title,
  Paragraph,
  ActivityIndicator,
  Surface,
  Text
} from 'react-native-paper'
import { useSelector } from 'react-redux'
import { RootState } from '@/store'
import Icon from 'react-native-vector-icons/MaterialIcons'

interface DashboardStats {
  totalUsers: number
  activeWorkflows: number
  pendingApprovals: number
  completedTasks: number
}

const DashboardScreen: React.FC = () => {
  const { user } = useSelector((state: RootState) => state.auth)
  const [loading, setLoading] = useState(true)
  const [refreshing, setRefreshing] = useState(false)
  const [stats, setStats] = useState<DashboardStats>({
    totalUsers: 0,
    activeWorkflows: 0,
    pendingApprovals: 0,
    completedTasks: 0
  })

  const loadDashboardData = async () => {
    try {
      // 模拟数据加载
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      setStats({
        totalUsers: 156,
        activeWorkflows: 23,
        pendingApprovals: 8,
        completedTasks: 1247
      })
    } catch (error) {
      console.error('Failed to load dashboard data:', error)
    } finally {
      setLoading(false)
    }
  }

  const onRefresh = async () => {
    setRefreshing(true)
    await loadDashboardData()
    setRefreshing(false)
  }

  useEffect(() => {
    loadDashboardData()
  }, [])

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#1890ff" />
        <Text style={styles.loadingText}>加载中...</Text>
      </View>
    )
  }

  const StatCard = ({ title, value, icon, color }: {
    title: string
    value: number
    icon: string
    color: string
  }) => (
    <Surface style={[styles.statCard, { borderLeftColor: color }]}>
      <View style={styles.statContent}>
        <View style={styles.statInfo}>
          <Text style={styles.statValue}>{value}</Text>
          <Text style={styles.statTitle}>{title}</Text>
        </View>
        <Icon name={icon} size={32} color={color} />
      </View>
    </Surface>
  )

  return (
    <ScrollView
      style={styles.container}
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
      }
    >
      <View style={styles.header}>
        <Title style={styles.welcomeText}>
          欢迎回来，{user?.username}！
        </Title>
        <Paragraph style={styles.subtitle}>
          今天是 {new Date().toLocaleDateString('zh-CN')}
        </Paragraph>
      </View>

      <View style={styles.statsContainer}>
        <StatCard
          title="总用户数"
          value={stats.totalUsers}
          icon="people"
          color="#3f8600"
        />
        <StatCard
          title="活跃工作流"
          value={stats.activeWorkflows}
          icon="work"
          color="#1890ff"
        />
        <StatCard
          title="待审批"
          value={stats.pendingApprovals}
          icon="schedule"
          color="#faad14"
        />
        <StatCard
          title="已完成任务"
          value={stats.completedTasks}
          icon="check-circle"
          color="#52c41a"
        />
      </View>

      <Card style={styles.card}>
        <Card.Content>
          <Title>最近活动</Title>
          <View style={styles.emptyState}>
            <Icon name="history" size={48} color="#d9d9d9" />
            <Text style={styles.emptyText}>暂无最近活动</Text>
          </View>
        </Card.Content>
      </Card>

      <Card style={styles.card}>
        <Card.Content>
          <Title>系统状态</Title>
          <View style={styles.statusContainer}>
            <View style={styles.statusItem}>
              <Icon name="check-circle" size={20} color="#52c41a" />
              <Text style={styles.statusText}>系统运行正常</Text>
            </View>
            <View style={styles.statusItem}>
              <Icon name="wifi" size={20} color="#52c41a" />
              <Text style={styles.statusText}>网络连接正常</Text>
            </View>
          </View>
        </Card.Content>
      </Card>
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f0f2f5'
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f0f2f5'
  },
  loadingText: {
    marginTop: 16,
    fontSize: 16,
    color: '#666'
  },
  header: {
    padding: 20,
    paddingBottom: 10
  },
  welcomeText: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#000'
  },
  subtitle: {
    fontSize: 14,
    color: '#666',
    marginTop: 4
  },
  statsContainer: {
    paddingHorizontal: 20,
    paddingBottom: 20
  },
  statCard: {
    marginBottom: 12,
    padding: 16,
    borderRadius: 8,
    borderLeftWidth: 4,
    elevation: 2
  },
  statContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  statInfo: {
    flex: 1
  },
  statValue: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#000'
  },
  statTitle: {
    fontSize: 14,
    color: '#666',
    marginTop: 4
  },
  card: {
    marginHorizontal: 20,
    marginBottom: 16,
    elevation: 2
  },
  emptyState: {
    alignItems: 'center',
    paddingVertical: 32
  },
  emptyText: {
    marginTop: 12,
    fontSize: 16,
    color: '#999'
  },
  statusContainer: {
    paddingVertical: 16
  },
  statusItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8
  },
  statusText: {
    marginLeft: 8,
    fontSize: 14,
    color: '#000'
  }
})

export default DashboardScreen
