import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Contacts from '../views/Contacts.vue'
import Contact from '../views/Contact.vue'
import Clients from '../views/Clients.vue'
import Client from '../views/Client.vue'
import Distributors from '../views/Distributors.vue'
import Distributor from '../views/Distributor.vue'
import Ingredients from '../views/Ingredients.vue'
import Ingredient from '../views/Ingredient.vue'
import Materials from '../views/Materials.vue'
import Material from '../views/Material.vue'
import Formulations from '../views/Formulations.vue'
import Formulation from '../views/Formulation.vue'
import Regulations from '../views/Regulations.vue'
import Regulation from '../views/Regulation.vue'
import Suppliers from '../views/Suppliers.vue'
import Supplier from '../views/Supplier.vue'

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/contacts',
    name: 'Contacts',
    component: Contacts
  },
  {
    path: '/contact/:id?',
    name: 'Contact',
    component: Contact
  },
  {
    path: '/clients',
    name: 'Clients',
    component: Clients
  },
  {
    path: '/client/:id?',
    name: 'Client',
    component: Client
  },
  {
    path: '/distributors',
    name: 'Distributors',
    component: Distributors
  },
  {
    path: '/distributor/:id?',
    name: 'Distributor',
    component: Distributor
  },
  {
    path: '/ingredients',
    name: 'Ingredients',
    component: Ingredients
  },
  {
    path: '/ingredient/:id?',
    name: 'Ingredient',
    component: Ingredient
  },
  {
    path: '/materials',
    name: 'Materials',
    component: Materials
  },
  {
    path: '/material/:id?',
    name: 'Material',
    component: Material
  },
  {
    path: '/formulations',
    name: 'Formulations',
    component: Formulations
  },
  {
    path: '/formulation',
    name: 'Formulation',
    component: Formulation
  },
  {
    path: '/regulations',
    name: 'Regulations',
    component: Regulations
  },
  {
    path: '/regulation/:id?',
    name: 'Regulation',
    component: Regulation
  },
  {
    path: '/suppliers',
    name: 'Suppliers',
    component: Suppliers
  },
  {
    path: '/supplier/:id?',
    name: 'Supplier',
    component: Supplier
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
