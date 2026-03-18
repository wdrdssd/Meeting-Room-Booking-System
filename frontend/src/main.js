import Vue from 'vue'
import App from './App.vue'
import 'element-ui/lib/theme-chalk/index.css'
import router from "./router"
import ElementUI from 'element-ui' 

Vue.use(ElementUI) 

Vue.config.devtools = true
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router
}).$mount('#app')
