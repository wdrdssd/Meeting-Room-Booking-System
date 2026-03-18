
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const router = new VueRouter({
    mode:'history',
    routes:[
        { name:'login',path:'/login',component:()=>import('../views/user/Login.vue')},
        { name:'userHomepage',path:'/user/homepage',component:()=>import('../views/user/Homepage.vue')},
        { name:'adminHomepage',path:'/admin/homepage',component:()=>import('../views/admin/Homepage.vue')},
    ],
    linkActiveClass:'on'
})
export default router