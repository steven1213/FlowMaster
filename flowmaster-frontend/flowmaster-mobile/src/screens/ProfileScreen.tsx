import React from 'react'
import { View, StyleSheet } from 'react-native'
import { Card, Title, Paragraph, Button } from 'react-native-paper'
import { useDispatch } from 'react-redux'
import { AppDispatch } from '@/store'
import { logout } from '@/store/slices/authSlice'
import Icon from 'react-native-vector-icons/MaterialIcons'

const ProfileScreen: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>()

  const handleLogout = () => {
    dispatch(logout())
  }

  return (
    <View style={styles.container}>
      <Card style={styles.card}>
        <Card.Content style={styles.content}>
          <Icon name="person" size={64} color="#d9d9d9" />
          <Title style={styles.title}>个人资料</Title>
          <Paragraph style={styles.subtitle}>
            个人资料功能开发中...
          </Paragraph>
        </Card.Content>
      </Card>

      <Button
        mode="contained"
        onPress={handleLogout}
        style={styles.logoutButton}
        buttonColor="#f5222d"
      >
        退出登录
      </Button>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f0f2f5',
    padding: 20
  },
  card: {
    elevation: 2,
    borderRadius: 8,
    marginBottom: 20
  },
  content: {
    alignItems: 'center',
    paddingVertical: 40
  },
  title: {
    marginTop: 16,
    fontSize: 20,
    color: '#000'
  },
  subtitle: {
    marginTop: 8,
    fontSize: 14,
    color: '#666',
    textAlign: 'center'
  },
  logoutButton: {
    marginTop: 'auto',
    marginBottom: 20
  }
})

export default ProfileScreen
