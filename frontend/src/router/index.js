
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const router = new VueRouter({
    mode:'history',
    routes:[
        { name:'login',path:'/login',component:()=>import('../views/user/Login.vue')},
    ],
    linkActiveClass:'on'
})
export default router