/*
 * @Description: vite配置
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-27
 * @LastEditors: zhuoda
 */
import { resolve } from 'path';
import vue from '@vitejs/plugin-vue';

const pathResolve = (dir: string): any => {
  return resolve(__dirname, '.', dir);
};

export default {
  base: process.env.NODE_ENV === 'production' ? '/manages/' : '/',
  root: process.cwd(),
  resolve: {
    alias: [
      // 国际化替换
      {
        find: 'vue-i18n',
        replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
      },
      // 绝对路径重命名：/@/xxxx => src/xxxx
      {
        find: /\/@\//,
        replacement: pathResolve('src') + '/',
      },
      {
        find: /^~/,
        replacement: '',
      },
    ],
  },
  // 服务端渲染
  server: {
    host: '0.0.0.0',
    port: 8081,
  },
  plugins: [vue()],
  optimizeDeps: {
    include: ['ant-design-vue/es/locale/zh_CN', 'moment/dist/locale/zh-cn', 'ant-design-vue/es/locale/en_US', 'moment/dist/locale/eu'],
    exclude: ['vue-demi'],
  },
  build: {
    terserOptions: {
      compress: {
        keep_infinity: true,
        drop_console: true,
      },
    },
    brotliSize: false,
    chunkSizeWarningLimit: 2000,
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {
          hack: `true; @import (reference) "${resolve('/@/theme/index.less')}";`,
        },
        javascriptEnabled: true,
      },
    },
  },
  define: {
    __INTLIFY_PROD_DEVTOOLS__: false,
    'process.env': process.env,
  },
};
