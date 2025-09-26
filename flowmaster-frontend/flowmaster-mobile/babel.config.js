module.exports = {
  presets: ['module:metro-react-native-babel-preset'],
  plugins: [
    [
      'module-resolver',
      {
        root: ['./src'],
        extensions: ['.ios.js', '.android.js', '.js', '.ts', '.tsx', '.json'],
        alias: {
          '@': './src',
          '@components': './src/components',
          '@screens': './src/screens',
          '@hooks': './src/hooks',
          '@utils': './src/utils',
          '@services': './src/services',
          '@store': './src/store',
          '@types': './src/types',
          '@assets': './src/assets',
          '@navigation': './src/navigation'
        }
      }
    ],
    'react-native-reanimated/plugin'
  ]
}
