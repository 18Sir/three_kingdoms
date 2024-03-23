import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './stores'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import PrimeVue from "primevue/config";
import Tooltip from 'primevue/tooltip';

//css样式
import "element-plus/theme-chalk/el-loading.css";
import "element-plus/theme-chalk/el-message.css";
import "element-plus/theme-chalk/el-notification.css";
import "element-plus/theme-chalk/el-message-box.css";
import "element-plus/theme-chalk/el-drawer.css";
import 'primevue/resources/themes/aura-light-cyan/theme.css'
import "primeicons/primeicons.css"
import 'primeflex/primeflex.scss';

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router).use(store).use(PrimeVue).directive('tooltip', Tooltip).mount('#app')
