import Vue from 'vue'
import VueRouter from 'vue-router'
import Program from '../views/Program.vue'
import Project from '../views/Project.vue'

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Program',
    component: Program
  },
  {
    path: '/project',
    name: 'Project',
    component: Project
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
