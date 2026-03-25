
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
                component:()=>import('../views/user/Calendar.vue'),
                meta: { requiresAuth: true }
                },
                {
                path:'profile',
                name:'profile',
                component:()=>import('../views/user/Profile.vue'),
                meta: { requiresAuth: true }
                },
                {
                path:'my-reservations',
                name:'MyReservations',
                component:()=>import('../views/user/MyReservations.vue'),
                meta: { requiresAuth: true }
                },
                {
                    path:'',
                    redirect:'calender'
                }
            ]
        },
        { path:'/admin',component:()=>import('../views/admin/Homepage.vue'),
            children: [
                { path: 'approvals', name: 'Approval', component: () => import('../views/admin/Approval.vue'),meta: { requiresAuth: true, requiresAdmin: true } },
                { path: 'statistics', name: 'Statistics', component: () => import('../views/admin/Statistics.vue'),meta: { requiresAuth: true, requiresAdmin: true } },
                { path: '', redirect: 'approvals' }
            ]
        },
    ],
    linkActiveClass:'on'
})

router.beforeEach((to, from, next) => {
    const token = sessionStorage.getItem('token')
    const userStr = sessionStorage.getItem('user')
    const user = userStr ? JSON.parse(userStr) : null
    
    const isLoggedIn = !!token && user !== null

    if (to.meta.requiresAuth && !isLoggedIn) {
        next({
            path: '/login',
            query: { redirect: to.fullPath }
        })
        return
    }

    if (to.meta.requiresAdmin && (!user || user.role !== 1)) {
        next('/user/calendar')
        return
    }

    if (to.path === '/login' && isLoggedIn) {
        if (user.role === 1) {
            next('/admin/approvals')
        } else {
            next('/user/calendar')
        }
        return
    }

    next()
})
export default router