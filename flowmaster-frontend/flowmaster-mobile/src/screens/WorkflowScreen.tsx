import React from 'react'
import { View, StyleSheet } from 'react-native'
import { Card, Title, Paragraph } from 'react-native-paper'
import Icon from 'react-native-vector-icons/MaterialIcons'

const WorkflowScreen: React.FC = () => {
  return (
    <View style={styles.container}>
      <Card style={styles.card}>
        <Card.Content style={styles.content}>
          <Icon name="work" size={64} color="#d9d9d9" />
          <Title style={styles.title}>工作流管理</Title>
          <Paragraph style={styles.subtitle}>
            工作流管理功能开发中...
          </Paragraph>
        </Card.Content>
      </Card>
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
    borderRadius: 8
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
  }
})

export default WorkflowScreen
