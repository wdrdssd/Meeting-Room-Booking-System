
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const router = new VueRouter({
    mode:'history',
    routes:[
        { name:'login',path:'/login',component:()=>import('../views/user/Login.vue')},
        { path: '/', redirect: '/login' },
        { name:'userHomepage',path:'/user',component:()=>import('../views/user/Homepage.vue'),
            children:[
                {
                path:'calender',
                name:'calender',
                component:()=>import('../views/user/Calendar.vue')
                },
                {
                path:'myself',
                name:'myself',
                component:()=>import('../views/user/Myself.vue')
                },
                {
                    path:'',
                    redirect:'calender'
                }
            ]
        },
        { name:'adminHomepage',path:'/admin',component:()=>import('../views/admin/Homepage.vue')},
    ],
    linkActiveClass:'on'
})
export default router