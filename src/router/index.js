import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import DeptManage from '@/views/DeptManage.vue'
import StaffManage from '@/views/StaffManage.vue'
import Login from '@/views/Login.vue'
import MainLayout from '@/layouts/MainLayout.vue'
import LifePlan from '@/views/LifePlan.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', redirect: '/home' },
      { path: 'home', component: Home },
      // { path: 'class-manage', component: ClassManage },
      { path: 'dept-manage', component: DeptManage },
      { path: 'staff-manage', component: StaffManage },
      { path: 'life-plan', component: LifePlan }
    ]
  },
  { 
    path: '/login',
    component: Login,
    meta: { requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：未登录跳转到登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  // 如果需要登录且没有token
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router 