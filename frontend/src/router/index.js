
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const router = new VueRouter({
    mode:'history',
    routes:[
        { name:'login',path:'/login',component:()=>import('../views/user/Login.vue')},
        { path: '/', redirect: '/login' },
        { path:'/user',component:()=>import('../views/user/Homepage.vue'),
            children:[
                {
                path:'calendar',
                name:'calendar',
                component:()=>import('../views/user/Calendar.vue')
                },
                {
                path:'profile',
                name:'profile',
                component:()=>import('../views/user/Profile.vue')
                },
                {
                path:'my-reservations',
                name:'MyReservations',
                component:()=>import('../views/user/MyReservations.vue')
                },
                {
                    path:'',
                    redirect:'calender'
                }
            ]
        },
        { path:'/admin',component:()=>import('../views/admin/Homepage.vue'),
            meta: { requiresAdmin: true },
            children: [
                { path: 'rooms', name: 'RoomManage', component: () => import('../views/admin/RoomManage.vue') },
                { path: 'approvals', name: 'Approval', component: () => import('../views/admin/Approval.vue') },
                { path: 'statistics', name: 'Statistics', component: () => import('../views/admin/Statistics.vue') },
                { path: '', redirect: 'rooms' }
            ]
        },
 ],
    linkActiveClass:'on'
})
export default router