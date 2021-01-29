import Vue from 'vue'
import Router from 'vue-router'
import store from '../store'
import iView from 'iview'
import { setToken, getToken, canTurnTo, setTitle } from '@/libs/util'
import config from '@/config'
const { homeName } = config

const originalPush = Router.prototype.push
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)
const createRouter = () =>  new Router({
  routes: store.state.router.constantRouters,
  mode: 'history'
})
const LOGIN_PAGE_NAME = 'login'

const router = createRouter();

export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}
const turnTo = (to, access, next) => {
  if (canTurnTo(to.name, access, store.state.router.routers)) {// 有权限，可访问
    next()
  } else { // 无权限，重定向到401页面
    next({ replace: true, name: 'error_401' })
  }
}

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()
  const token = getToken()
  if (!token && to.name !== LOGIN_PAGE_NAME) {
    // 未登录且要跳转的页面不是登录页
    next({
      name: LOGIN_PAGE_NAME // 跳转到登录页
    })
  } else if (!token && to.name === LOGIN_PAGE_NAME) {
    // 未登陆且要跳转的页面是登录页
    next() // 跳转
  } else if (token && to.name === LOGIN_PAGE_NAME) {
    // 已登录且要跳转的页面是登录页
    next({
      name: homeName // 跳转到homeName页
    })
  } else {
    if (store.state.user.hasGetInfo&&store.state.user.hasGenerateMenu) {
      turnTo(to, store.state.user.access, next)
    } else {
      store.dispatch('getUserInfo').then(user => {
        let menu = user.menus;
        // 拉取用户信息，通过用户权限和跳转的页面的name来判断是否有权限访问;access必须是一个数组，如：['super_admin'] ['super_admin', 'admin']
        store.dispatch('GenerateRoutes', {menu}).then(() => { // 生成可访问的路由表
          resetRouter()
          router.addRoutes(store.state.router.addRouters)
          store.state.user.hasGenerateMenu = true
          turnTo(to, user.access, next)
        })
      }).catch(() => {
        setToken('')
        next({
          name: 'login'
        })
      })
    }
  }
})

router.afterEach(to => {
  setTitle(to, router.app)
  iView.LoadingBar.finish()
  window.scrollTo(0, 0)
})

export default router
