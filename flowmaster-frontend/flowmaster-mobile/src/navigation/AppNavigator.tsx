import React from 'react'
import { createStackNavigator } from '@react-navigation/stack'
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'
import { useSelector } from 'react-redux'
import { RootState } from '@/store'
import Icon from 'react-native-vector-icons/MaterialIcons'

// 导入屏幕组件
import LoginScreen from '@/screens/LoginScreen'
import DashboardScreen from '@/screens/DashboardScreen'
import WorkflowScreen from '@/screens/WorkflowScreen'
import ApprovalScreen from '@/screens/ApprovalScreen'
import ProfileScreen from '@/screens/ProfileScreen'
import WorkflowDetailScreen from '@/screens/WorkflowDetailScreen'
import ApprovalDetailScreen from '@/screens/ApprovalDetailScreen'
import SettingsScreen from '@/screens/SettingsScreen'

import { RootStackParamList, MainTabParamList } from '@/types'

const Stack = createStackNavigator<RootStackParamList>()
const Tab = createBottomTabNavigator<MainTabParamList>()

// 主标签导航
const MainTabNavigator = () => {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          let iconName: string

          switch (route.name) {
            case 'Dashboard':
              iconName = 'dashboard'
              break
            case 'Workflow':
              iconName = 'work'
              break
            case 'Approval':
              iconName = 'check-circle'
              break
            case 'Profile':
              iconName = 'person'
              break
            default:
              iconName = 'help'
          }

          return <Icon name={iconName} size={size} color={color} />
        },
        tabBarActiveTintColor: '#1890ff',
        tabBarInactiveTintColor: 'gray',
        headerShown: false
      })}
    >
      <Tab.Screen 
        name="Dashboard" 
        component={DashboardScreen}
        options={{ title: '仪表盘' }}
      />
      <Tab.Screen 
        name="Workflow" 
        component={WorkflowScreen}
        options={{ title: '工作流' }}
      />
      <Tab.Screen 
        name="Approval" 
        component={ApprovalScreen}
        options={{ title: '审批' }}
      />
      <Tab.Screen 
        name="Profile" 
        component={ProfileScreen}
        options={{ title: '我的' }}
      />
    </Tab.Navigator>
  )
}

// 主应用导航
const AppNavigator = () => {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth)

  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      {isAuthenticated ? (
        <>
          <Stack.Screen name="Main" component={MainTabNavigator} />
          <Stack.Screen 
            name="WorkflowDetail" 
            component={WorkflowDetailScreen}
            options={{ title: '工作流详情' }}
          />
          <Stack.Screen 
            name="ApprovalDetail" 
            component={ApprovalDetailScreen}
            options={{ title: '审批详情' }}
          />
          <Stack.Screen 
            name="Settings" 
            component={SettingsScreen}
            options={{ title: '设置' }}
          />
        </>
      ) : (
        <Stack.Screen name="Login" component={LoginScreen} />
      )}
    </Stack.Navigator>
  )
}

export default AppNavigator
