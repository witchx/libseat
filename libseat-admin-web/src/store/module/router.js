import { asyncRouterMap, constantRouterMap } from '../../router/routers';
//判断是否有权限访问该菜单
function hasPermission(menus, route) {
  if (route.name) {
    let currMenu = getMenu(route.name, menus);
    if (currMenu!=null) {
      //设置菜单的标题、图标和可见性
      if (currMenu.title != null && currMenu.title !== '') {
        route.meta.title = currMenu.title;
      }
      if (currMenu.icon != null && currMenu.title !== '') {
        route.meta.icon = currMenu.icon;
      }
      if(currMenu.hidden!=null){
        route.hideInMenu = (currMenu.hidden !== 0);
      }
      if (currMenu.sort != null && currMenu.sort !== '') {
        route.sort = currMenu.sort;
      }
      if (currMenu.access != null && currMenu.access !== '' && currMenu.access !== '[]') {
        route.meta.access = currMenu.access.replace("[","").replace("]","").split(",");
      }
      return true;
    } else {
      route.sort = 0;
      if (route.hidden !== undefined && route.hidden === true) {
        return true;
      } else {
        return false;
      }
    }
  } else {
    return true
  }
}
/**根据路由名称获取菜单
 * @param name 路由名称
 * @param menus 菜单
 * @returns menu
 */
function getMenu(name, menus) {
  for (let i = 0; i < menus.length; i++) {
    let menu = menus[i];
    if (name===menu.name) {
      return menu;
    }
  }
  return null;
}
/**对菜单进行排序
 * @param accessedRouters
 * @returns
 */
function sortRouters(accessedRouters) {
  for (let i = 0; i < accessedRouters.length; i++) {
    let router = accessedRouters[i];
    if(router.children && router.children.length > 0){
      router.children.sort(compare("sort"));
    }
  }
  accessedRouters.sort(compare("sort"));
}
//降序比较函数
function compare(p){
  return function(m,n){
    let a = m[p];
    let b = n[p];
    return b - a;
  }
}

const router = {
  state: {
    routers: [],
    constantRouters: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers;
      state.routers = routers.concat(constantRouterMap);
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const menus  = data.menu;
        const accessedRouters = asyncRouterMap.filter(v => {
          //admin帐号直接返回所有菜单
          // if(username==='admin') return true;
          if (hasPermission(menus, v)) {
            if (v.children && v.children.length > 0) {
              v.children = v.children.filter(child => {
                if (hasPermission(menus, child)) {
                  return child
                }
                return false;
              });
              return v
            } else {
              return v
            }
          }
          return false;
        });
        //对菜单进行排序
        sortRouters(accessedRouters);
        commit('SET_ROUTERS', accessedRouters);
        resolve();
      })
    }
  }
};

export default router;
