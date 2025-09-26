import React, { useEffect } from 'react'
import { StatusBar, Platform } from 'react-native'
import { Provider } from 'react-redux'
import { PersistGate } from 'redux-persist/integration/react'
import { NavigationContainer } from '@react-navigation/native'
import { PaperProvider } from 'react-native-paper'
import { GestureHandlerRootView } from 'react-native-gesture-handler'

import { store, persistor } from './store'
import AppNavigator from './navigation/AppNavigator'
import LoadingScreen from './screens/LoadingScreen'
import { theme } from './utils/theme'

const App: React.FC = () => {
  useEffect(() => {
    // 设置状态栏样式
    StatusBar.setBarStyle('dark-content')
    if (Platform.OS === 'android') {
      StatusBar.setBackgroundColor('#ffffff')
    }
  }, [])

  return (
    <Provider store={store}>
      <PersistGate loading={<LoadingScreen />} persistor={persistor}>
        <GestureHandlerRootView style={{ flex: 1 }}>
          <PaperProvider theme={theme}>
            <NavigationContainer>
              <AppNavigator />
            </NavigationContainer>
          </PaperProvider>
        </GestureHandlerRootView>
      </PersistGate>
    </Provider>
  )
}

export default App
