import { defineConfig } from 'vite'
import { resolve } from 'path';
import vue from '@vitejs/plugin-vue'

const pathResolve = (dir) => {
  return resolve(__dirname, '.', dir);
};

export default defineConfig({
  base: process.env.NODE_ENV === 'production' ? '/' : '/',
  root: process.cwd(),
  resolve: {
    alias: [
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
    port: 8088,
  },
  plugins: [vue()],
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "/@/theme/variables.scss" as *;`,
      },
    },
  },
  optimizeDeps: {
    // 检测需要预构建的依赖项
    entries: [],
  },
  build: {
    brotliSize: false,
    chunkSizeWarningLimit: 500,
    target: ['es2015', 'chrome86'],
    minify: 'terser', // 是否进行压缩,boolean | 'terser' | 'esbuild',默认使用terser
    manifest: false, // 是否产出maifest.json
    sourcemap: false, // 是否产出soucemap.json
    terserOptions: {
      compress: {
        keep_infinity: true,
        drop_console: true,
      },
    },
  },
  define: {
    __INTLIFY_PROD_DEVTOOLS__: false,
    'process.env': process.env,
  },
});
