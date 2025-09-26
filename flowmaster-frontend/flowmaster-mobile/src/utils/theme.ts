import { MD3LightTheme, MD3DarkTheme } from 'react-native-paper'

export const lightTheme = {
  ...MD3LightTheme,
  colors: {
    ...MD3LightTheme.colors,
    primary: '#1890ff',
    primaryContainer: '#e6f7ff',
    secondary: '#52c41a',
    secondaryContainer: '#f6ffed',
    tertiary: '#faad14',
    tertiaryContainer: '#fffbe6',
    error: '#f5222d',
    errorContainer: '#fff2f0',
    surface: '#ffffff',
    surfaceVariant: '#f5f5f5',
    background: '#f0f2f5',
    onPrimary: '#ffffff',
    onSecondary: '#ffffff',
    onTertiary: '#ffffff',
    onError: '#ffffff',
    onSurface: '#000000',
    onBackground: '#000000',
    outline: '#d9d9d9',
    shadow: '#000000'
  }
}

export const darkTheme = {
  ...MD3DarkTheme,
  colors: {
    ...MD3DarkTheme.colors,
    primary: '#40a9ff',
    primaryContainer: '#003a8c',
    secondary: '#73d13d',
    secondaryContainer: '#135200',
    tertiary: '#ffc53d',
    tertiaryContainer: '#ad6800',
    error: '#ff7875',
    errorContainer: '#5c0011',
    surface: '#1f1f1f',
    surfaceVariant: '#2f2f2f',
    background: '#141414',
    onPrimary: '#ffffff',
    onSecondary: '#ffffff',
    onTertiary: '#ffffff',
    onError: '#ffffff',
    onSurface: '#ffffff',
    onBackground: '#ffffff',
    outline: '#434343',
    shadow: '#000000'
  }
}

// 默认使用浅色主题
export const theme = lightTheme
