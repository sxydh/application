import Vue from 'vue'
import Router from 'vue-router'
import ElectronicDoubleSlitExperiment from '@/view/ElectronicDoubleSlitExperiment'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'ElectronicDoubleSlitExperiment',
      component: ElectronicDoubleSlitExperiment
    }
  ]
})
