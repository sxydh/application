import Vue from 'vue'
import Router from 'vue-router'
import store from '../store/index'
import myHttp from '../utils/myHttp'

Vue.use(Router)

let router = new Router({
  // mode: "history",
  routes: [
    {
      path: '/',
      name: 'Frame',
      redirect: '/entrance/login',
      component: resolve => require(['@/view/Frame'], resolve),
      meta: [],
      children: [
        {
          path: 'user',
          name: 'User',
          component: resolve => require(['@/view/User'], resolve)
        },
        {
          path: 'water',
          name: 'Water',
          component: resolve => require(['@/view/Water'], resolve)
        },
        {
          path: 'wallet',
          name: 'Wallet',
          component: resolve => require(['@/view/Wallet'], resolve),
          meta: [],
          children: [
            {
              path: 'recharge',
              name: 'Recharge',
              component: resolve => require(['@/view/Recharge'], resolve)
            }
          ]
        },
        {
          path: 'news',
          name: 'News',
          component: resolve => require(['@/view/News'], resolve)
        },
        {
          path: 'home',
          name: 'Home',
          component: resolve => require(['@/view/Home'], resolve)
        },
        {
          path: 'help',
          name: 'Help',
          component: resolve => require(['@/view/Help'], resolve)
        }
      ]
    },
    {
      path: '/entrance',
      name: 'Entrance',
      component: resolve => require(['@/view/Entrance'], resolve),
      meta: [],
      children: [
        {
          path: 'login',
          name: 'Login',
          component: resolve => require(['@/view/Login'], resolve)
        },
        {
          path: 'register',
          name: 'Register',
          component: resolve => require(['@/view/Register'], resolve)
        },
        {
          path: 'reset-p',
          name: 'ResetP',
          component: resolve => require(['@/view/ResetP'], resolve)
        },
      ]
    },
    {
      path: '/test',
      name: 'Test',
      component: resolve => require(['@/view/Test'], resolve)
    }
  ]
});
router.beforeEach(async (to, from, next) => {
  console.log('router.beforeEach( to: ' + to.path + ', from: ' + from.path + ', next: ' + next.path + ' )');
  if (to.path.indexOf('/entrance/') >= 0) {
    return next();
  }
  let user = store.getters.takeUser;
  console.log('router.beforeEach(async (to, from, next) -> cachedUser -> ' + JSON.stringify(user));
  if (!user) {
    return next("/entrance/login");
  }
  let auth = store.getters.takeAuth == null ? { flush: true } : store.getters.takeAuth;
  console.log('router.beforeEach(async (to, from, next) -> auth.fush: ' + auth.flush);
  if (auth.flush) {
    let authCheck = myHttp.asyncGet({ url: '/user/auth' });
    authCheck.then((resolve) => {
      if (resolve[0]) {
        auth.flush = false;
        store.commit('updateAuth', auth);
      }
    })
  }
  return next();
});

export default router;
