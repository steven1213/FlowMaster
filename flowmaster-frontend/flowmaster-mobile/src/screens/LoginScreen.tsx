import React, { useState } from 'react'
import {
  View,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView
} from 'react-native'
import {
  TextInput,
  Button,
  Card,
  Title,
  Paragraph,
  ActivityIndicator,
  Snackbar
} from 'react-native-paper'
import { useDispatch } from 'react-redux'
import { AppDispatch } from '@/store'
import { login } from '@/store/slices/authSlice'
import { LoginRequest } from '@/types'

const LoginScreen: React.FC = () => {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  
  const dispatch = useDispatch<AppDispatch>()

  const handleLogin = async () => {
    if (!username.trim() || !password.trim()) {
      setError('请输入用户名和密码')
      return
    }

    setLoading(true)
    setError('')

    try {
      const credentials: LoginRequest = { username, password }
      await dispatch(login(credentials)).unwrap()
    } catch (err: any) {
      setError(err.message || '登录失败')
    } finally {
      setLoading(false)
    }
  }

  return (
    <KeyboardAvoidingView 
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        <Card style={styles.card}>
          <Card.Content style={styles.content}>
            <Title style={styles.title}>FlowMaster</Title>
            <Paragraph style={styles.subtitle}>工作流管理系统</Paragraph>

            <TextInput
              label="用户名"
              value={username}
              onChangeText={setUsername}
              mode="outlined"
              style={styles.input}
              autoCapitalize="none"
              autoCorrect={false}
              disabled={loading}
            />

            <TextInput
              label="密码"
              value={password}
              onChangeText={setPassword}
              mode="outlined"
              secureTextEntry
              style={styles.input}
              disabled={loading}
            />

            <Button
              mode="contained"
              onPress={handleLogin}
              style={styles.button}
              disabled={loading}
              contentStyle={styles.buttonContent}
            >
              {loading ? <ActivityIndicator color="white" /> : '登录'}
            </Button>

            <Paragraph style={styles.hint}>
              默认账号：admin / admin123
            </Paragraph>
          </Card.Content>
        </Card>
      </ScrollView>

      <Snackbar
        visible={!!error}
        onDismiss={() => setError('')}
        duration={3000}
      >
        {error}
      </Snackbar>
    </KeyboardAvoidingView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f0f2f5'
  },
  scrollContainer: {
    flexGrow: 1,
    justifyContent: 'center',
    padding: 20
  },
  card: {
    elevation: 4,
    borderRadius: 12
  },
  content: {
    padding: 24
  },
  title: {
    textAlign: 'center',
    fontSize: 28,
    fontWeight: 'bold',
    color: '#1890ff',
    marginBottom: 8
  },
  subtitle: {
    textAlign: 'center',
    fontSize: 16,
    color: '#666',
    marginBottom: 32
  },
  input: {
    marginBottom: 16
  },
  button: {
    marginTop: 8,
    marginBottom: 16
  },
  buttonContent: {
    paddingVertical: 8
  },
  hint: {
    textAlign: 'center',
    fontSize: 12,
    color: '#999'
  }
})

export default LoginScreen
