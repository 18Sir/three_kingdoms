import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver,PrimeVueResolver } from 'unplugin-vue-components/resolvers'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver(),PrimeVueResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver(),PrimeVueResolver()],
    }),

  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    cors: true,
    proxy: {
      '/api': {
        //target 我们要连接的后端地址
        target: 'http://localhost:8080',
        secure: false,
        ws: true,
        changeOrigin: true, //是否跨域
        rewrite: (path) => path.replace(/^\/api/, '') // 将api替换为空
      },
    },
  },
  cssPreprocessOptions: {
    scss: {
      additionalData: '@import "primeflex/primeflex.scss";' // 全局公共样式
    }
  }

})
