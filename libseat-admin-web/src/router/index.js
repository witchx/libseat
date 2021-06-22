import Vue from 'vue'
import Router from 'vue-router'
import store from '../store'
import iView from 'iview'
import { setToken, getToken, canTurnTo, setTitle } from '@/libs/util'
import config from '@/config'

const { homeName } = config;
const LOGIN_PAGE_NAME = 'login';

const originalPush = Router.prototype.push;
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch(err => err)
};
Vue.use(Router);

const createRouter = () =>  new Router({
  routes: store.state.router.constantRouters,
  mode: 'history'
});

//重置路由的方法:切换用户后，或者退出时清除动态加载的路由
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}

const router = createRouter();

const turnTo = (to, access, next) => {
  if (canTurnTo(to.name, access, store.state.router.routers)) {// 有权限，可访问
    next()
  } else { // 无权限，重定向到401页面
    next({ replace: true, name: 'error_401' })
  }
};

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start();
  const token = getToken();
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
  } else if ( to.name === 'error_404' && from.name === null) {
    var routeTo404 = sessionStorage.getItem('routerTo');
    buildRouter(false, routeTo404, next);
  } else {
    if (store.state.router.hasGetInfo&&store.state.router.hasGenerateMenu) {
      turnTo(to, store.state.user.access, next)
    } else {
      buildRouter(true, null, next);
    }
  }
});

router.afterEach(to => {
  setTitle(to, router.app);
  iView.LoadingBar.finish();
  window.scrollTo(0, 0);
  sessionStorage.setItem('routerTo', to.path)
});

export function buildRouter(isReset,toPath,next) {
  // 拉取用户信息，通过用户权限和跳转的页面的name来判断是否有权限访问;access必须是一个数组，如：['super_admin'] ['super_admin', 'admin']
  store.dispatch('getUserInfo').then(user => {
    let menu = user.menus;
    store.dispatch('generateRoutes', {menu}).then(( dynamicRoutes) => {
      if (isReset) {
        // 重置路由表
        resetRouter()
      }
      // 动态添加可访问路由表
      router.addRoutes(dynamicRoutes);
      store.commit('setHasGenerateMenu', true);
      if (toPath ===  null) {
        // hack方法 确保addRoutes已完成
        next({ ...to, replace: true })
      } else {
        next({path: toPath})
      }
    })
  }).catch(() => {
    setToken('');
    next({
      name: 'login'
    })
  })
}

export default router
