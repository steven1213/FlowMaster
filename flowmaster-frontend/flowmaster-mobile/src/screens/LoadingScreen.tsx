import React from 'react'
import { View, StyleSheet } from 'react-native'
import { ActivityIndicator, Text } from 'react-native-paper'

const LoadingScreen: React.FC = () => {
  return (
    <View style={styles.container}>
      <ActivityIndicator size="large" color="#1890ff" />
      <Text style={styles.text}>加载中...</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f0f2f5'
  },
  text: {
    marginTop: 16,
    fontSize: 16,
    color: '#666'
  }
})

export default LoadingScreen
