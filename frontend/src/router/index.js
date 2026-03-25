
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const router = new VueRouter({
    mode:'history',
    routes:[
        { name:'login',path:'/login',component:()=>import('../views/user/Login.vue')},
        { path: '/', redirect: '/login' },
        { path:'/user',component:()=>import('../views/user/Homepage.vue'),
            meta: { requiresAuth: true }, 
            children:[
                {
                path:'calendar',
                name:'calendar',
                component:()=>import('../views/user/Calendar.vue'),
                meta: { requiresAuth: true, requiresAdmin: true }
                },
                {
                path:'profile',
                name:'profile',
                component:()=>import('../views/user/Profile.vue'),
                meta: { requiresAuth: true, requiresAdmin: true }
                },
                {
                path:'my-reservations',
                name:'MyReservations',
                component:()=>import('../views/user/MyReservations.vue'),
                meta: { requiresAuth: true, requiresAdmin: true }
                },
                {
                    path:'',
                    redirect:'calender'
                }
            ]
        },
        { path:'/admin',component:()=>import('../views/admin/Homepage.vue'),
            meta: { requiresAuth: true , requiresAdmin: true},
            children: [
                { path: 'approvals', name: 'Approval', component: () => import('../views/admin/Approval.vue'),meta: { requiresAuth: true, requiresAdmin: true } },
                { path: 'rooms', name: 'RoomManage', component: () => import('../views/admin/RoomManage.vue'),meta: { requiresAuth: true, requiresAdmin: true } },
                { path: 'statistics', name: 'Statistics', component: () => import('../views/admin/Statistics.vue'),meta: { requiresAuth: true, requiresAdmin: true } },
                { path: '', redirect: 'approvals' }
            ]
        },
    ],
    linkActiveClass:'on'
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth) {
    if (!token) {
      next({
        path: '/login',
        query: { redirect: to.fullPath }  
      })
    } 
    else if (to.meta.requiresAdmin) {
      const userStr = localStorage.getItem('user')
      const user = userStr ? JSON.parse(userStr) : null
      if (!user || user.role !== 1) {
        next('/user/calendar')
      } else {
        next()
      }
    } 
    else {
      next()
    }
  } 
  else if (to.path === '/login' && token) {
    const userStr = localStorage.getItem('user')
    const user = userStr ? JSON.parse(userStr) : null
    if (user && user.role === 1) {
      next('/admin/rooms')
    } else {
      next('/user/calendar')
    }
  } 
  else {
    next()
  }
})
export default router