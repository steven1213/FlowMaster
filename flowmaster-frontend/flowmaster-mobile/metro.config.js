const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config')

/**
 * Metro configuration
 * https://facebook.github.io/metro/docs/configuration
 *
 * @type {import('metro-config').MetroConfig}
 */
const config = {
  resolver: {
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
  },
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: true
      }
    })
  }
}

module.exports = mergeConfig(getDefaultConfig(__dirname), config)
