import Main from '@/components/main'
import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 * }
 */
export const constantRouterMap = [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: '/home',
        name: 'home',
        meta: {
          hideInMenu: true,
          title: '首页',
          notCache: true,
          icon: 'md-home'
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  /*{
    path: '/demo',
    name: 'demo',
    meta: {
      title: '实例演示',
      icon: 'ios-desktop'
    },
    component: Main,
    children: [
      {
        path: '',
        name: 'doc',
        meta: {
          title: '文档',
          href: 'https://lison16.github.io/iview-admin-doc/#/',
          icon: 'ios-book'
        }
      },
      {
        path: '/join',
        name: 'join',
        component: parentView,
        meta: {
          hideInBread: true
        },
        children: [
          {
            path: 'join_page',
            name: 'join_page',
            meta: {
              icon: '_qq',
              title: 'QQ群'
            },
            component: () => import('@/view/join-page.vue')
          }
        ]
      },
      {
        path: '/message',
        name: 'message',
        component: parentView,
        meta: {
          hideInBread: true,
          hideInMenu: true
        },
        children: [
          {
            path: 'message_page',
            name: 'message_page',
            meta: {
              icon: 'md-notifications',
              title: '消息中心'
            },
            component: () => import('@/view/single-page/message/index.vue')
          }
        ]
      },
      {
        path: '/components',
        name: 'components',
        meta: {
          icon: 'logo-buffer',
          title: '组件'
        },
        component: parentView,
        children: [
          {
            path: 'tree_select_page',
            name: 'tree_select_page',
            meta: {
              icon: 'md-arrow-dropdown-circle',
              title: '树状下拉选择器'
            },
            component: () => import('@/view/components/tree-select/index.vue')
          },
          {
            path: 'count_to_page',
            name: 'count_to_page',
            meta: {
              icon: 'md-trending-up',
              title: '数字渐变'
            },
            component: () => import('@/view/components/count-to/count-to.vue')
          },
          {
            path: 'drag_list_page',
            name: 'drag_list_page',
            meta: {
              icon: 'ios-infinite',
              title: '拖拽列表'
            },
            component: () => import('@/view/components/drag-list/drag-list.vue')
          },
          {
            path: 'drag_drawer_page',
            name: 'drag_drawer_page',
            meta: {
              icon: 'md-list',
              title: '可拖拽抽屉'
            },
            component: () => import('@/view/components/drag-drawer')
          },
          {
            path: 'org_tree_page',
            name: 'org_tree_page',
            meta: {
              icon: 'ios-people',
              title: '组织结构树'
            },
            component: () => import('@/view/components/org-tree')
          },
          {
            path: 'tree_table_page',
            name: 'tree_table_page',
            meta: {
              icon: 'md-git-branch',
              title: '树状表格'
            },
            component: () => import('@/view/components/tree-table/index.vue')
          },
          {
            path: 'cropper_page',
            name: 'cropper_page',
            meta: {
              icon: 'md-crop',
              title: '图片裁剪'
            },
            component: () => import('@/view/components/cropper/cropper.vue')
          },
          {
            path: 'tables_page',
            name: 'tables_page',
            meta: {
              icon: 'md-grid',
              title: '多功能表格'
            },
            component: () => import('@/view/components/tables/tables.vue')
          },
          {
            path: 'split_pane_page',
            name: 'split_pane_page',
            meta: {
              icon: 'md-pause',
              title: '分割窗口'
            },
            component: () => import('@/view/components/split-pane/split-pane.vue')
          },
          {
            path: 'markdown_page',
            name: 'markdown_page',
            meta: {
              icon: 'logo-markdown',
              title: 'Markdown编辑器'
            },
            component: () => import('@/view/components/markdown/markdown.vue')
          },
          {
            path: 'editor_page',
            name: 'editor_page',
            meta: {
              icon: 'ios-create',
              title: '富文本编辑器'
            },
            component: () => import('@/view/components/editor/editor.vue')
          },
          {
            path: 'icons_page',
            name: 'icons_page',
            meta: {
              icon: '_bear',
              title: '自定义图标'
            },
            component: () => import('@/view/components/icons/icons.vue')
          }
        ]
      },
      {
        path: '/update',
        name: 'update',
        meta: {
          icon: 'md-cloud-upload',
          title: '数据上传'
        },
        component: parentView,
        children: [
          {
            path: 'update_table_page',
            name: 'update_table_page',
            meta: {
              icon: 'ios-document',
              title: '上传Csv'
            },
            component: () => import('@/view/update/update-table.vue')
          },
          {
            path: 'update_paste_page',
            name: 'update_paste_page',
            meta: {
              icon: 'md-clipboard',
              title: '粘贴表格数据'
            },
            component: () => import('@/view/update/update-paste.vue')
          }
        ]
      },
      {
        path: '/excel',
        name: 'excel',
        meta: {
          icon: 'ios-stats',
          title: 'EXCEL导入导出'
        },
        component: parentView,
        children: [
          {
            path: 'upload-excel',
            name: 'upload-excel',
            meta: {
              icon: 'md-add',
              title: '导入EXCEL'
            },
            component: () => import('@/view/excel/upload-excel.vue')
          },
          {
            path: 'export-excel',
            name: 'export-excel',
            meta: {
              icon: 'md-download',
              title: '导出EXCEL'
            },
            component: () => import('@/view/excel/export-excel.vue')
          }
        ]
      },
      {
        path: '/tools_methods',
        name: 'tools_methods',
        meta: {
          hideInBread: true
        },
        component: parentView,
        children: [
          {
            path: 'tools_methods_page',
            name: 'tools_methods_page',
            meta: {
              icon: 'ios-hammer',
              title: '工具方法',
              beforeCloseName: 'before_close_normal'
            },
            component: () => import('@/view/tools-methods/tools-methods.vue')
          }
        ]
      },
      {
        path: '/i18n',
        name: 'i18n',
        meta: {
          hideInBread: true
        },
        component: parentView,
        children: [
          {
            path: 'i18n_page',
            name: 'i18n_page',
            meta: {
              icon: 'md-planet',
              title: 'i18n - {{ i18n_page }}'
            },
            component: () => import('@/view/i18n/i18n-page.vue')
          }
        ]
      },
      {
        path: '/error_store',
        name: 'error_store',
        meta: {
          hideInBread: true
        },
        component: parentView,
        children: [
          {
            path: 'error_store_page',
            name: 'error_store_page',
            meta: {
              icon: 'ios-bug',
              title: '错误收集'
            },
            component: () => import('@/view/error-store/error-store.vue')
          }
        ]
      },
      {
        path: '/error_logger',
        name: 'error_logger',
        meta: {
          hideInBread: true,
          hideInMenu: true
        },
        component: parentView,
        children: [
          {
            path: 'error_logger_page',
            name: 'error_logger_page',
            meta: {
              icon: 'ios-bug',
              title: '错误收集'
            },
            component: () => import('@/view/single-page/error-logger.vue')
          }
        ]
      },
      {
        path: '/directive',
        name: 'directive',
        meta: {
          hideInBread: true
        },
        component: parentView,
        children: [
          {
            path: 'directive_page',
            name: 'directive_page',
            meta: {
              icon: 'ios-navigate',
              title: '指令'
            },
            component: () => import('@/view/directive/directive.vue')
          }
        ]
      }
    ]
  },
  {
    path: '/multilevel',
    name: 'multilevel',
    meta: {
      icon: 'md-menu',
      title: '多级菜单'
    },
    component: Main,
    children: [
      {
        path: 'level_2_1',
        name: 'level_2_1',
        meta: {
          icon: 'ios-reorder',
          title: '二级-1'
        },
        component: () => import('@/view/multilevel/level-2-1.vue')
      },
      {
        path: 'level_2_2',
        name: 'level_2_2',
        meta: {
          access: ['super_admin'],
          icon: 'md-funnel',
          showAlways: true,
          title: '二级-2'
        },
        component: parentView,
        children: [
          {
            path: 'level_2_2_1',
            name: 'level_2_2_1',
            meta: {
              icon: 'md-funnel',
              title: '三级'
            },
            component: () => import('@/view/multilevel/level-2-2/level-2-2-1.vue')
          },
          {
            path: 'level_2_2_2',
            name: 'level_2_2_2',
            meta: {
              icon: 'md-funnel',
              title: '三级'
            },
            component: () => import('@/view/multilevel/level-2-2/level-2-2-2.vue')
          }
        ]
      },
      {
        path: 'level_2_3',
        name: 'level_2_3',
        meta: {
          icon: 'md-funnel',
          title: '二级-3'
        },
        component: () => import('@/view/multilevel/level-2-3.vue')
      }
    ]
  },
  {
    path: '/argu',
    name: 'argu',
    meta: {
      hideInMenu: true
    },
    component: Main,
    children: [
      {
        path: 'params/:id',
        name: 'params',
        meta: {
          icon: 'md-flower',
          title: route => `{{ params }}-${route.params.id}`,
          notCache: true,
          beforeCloseName: 'before_close_normal'
        },
        component: () => import('@/view/argu-page/params.vue')
      },
      {
        path: 'query',
        name: 'query',
        meta: {
          icon: 'md-flower',
          title: route => `{{ query }}-${route.query.id}`,
          notCache: true
        },
        component: () => import('@/view/argu-page/query.vue')
      }
    ]
  },*/
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]

export const asyncRouterMap = [
  {
    path: '/ums',
    name: 'ums',
    meta: {
      icon: 'md-school',
      title: '用户'
    },
    component: Main,
    children: [
      {
        path: 'user',
        name: 'user',
        meta: {
          icon: 'ios-briefcase',
          title: '用户列表'
        },
        component: () => import('@/view/ums/user')
      },
      {
        path: 'customer',
        name: 'customer',
        meta: {
          icon: 'ios-book',
          title: '顾客列表'
        },
        component: () => import('@/view/ums/customer')
      }
    ]
  }, {
    path: '/rms',
    name: 'rms',
    meta: {
      icon: 'md-list-box',
      title: '自习室'
    },
    component: Main,
    children: [
      {
        path: 'stadium',
        name: 'stadium',
        meta: {
          icon: 'ios-book',
          title: '场馆列表'
        },
        component: () => import('@/view/ums/stadium')
      },
      {
        path: 'room',
        name: 'room',
        meta: {
          icon: 'ios-document',
          title: '自习室列表'
        },
        component: () => import('@/view/ums/room')
      },
      {
        path: 'editRoom',
        name: 'editRoom',
        meta: {
          icon: 'ios-create',
          title: '编辑自习室'
        },
        component: () => import('@/view/ums/editRoom')
      },
      {
        path: 'createRoom',
        name: 'createRoom',
        meta: {
          icon: 'ios-add-circle-outline',
          title: '创建自习室'
        },
        component: () => import('@/view/ums/createRoom')
      }
    ]
  },
  {
    path: '/oms',
    name: 'oms',
    meta: {
      icon: 'md-list-box',
      title: '订单'
    },
    component: Main,
    children: [
      {
        path: 'order',
        name: 'order',
        meta: {
          icon: 'md-list',
          title: '订单列表'
        },
        component: () => import('@/view/oms/order')
      },
      {
        path: 'orderDetail',
        name: 'orderDetail',
        meta: {
          title: '订单详情',
          hideInMenu: true
        },
        component: () => import('@/view/oms/orderDetail')
      },
      {
        path: 'orderSetting',
        name: 'orderSetting',
        meta: {
          icon: 'md-settings',
          title: '订单设置'
        },
        component: () => import('@/view/oms/orderSetting')
      }
    ]
  },
  {
    path: '/pms',
    name: 'pms',
    meta: {
      icon: 'md-list-box',
      title: '商品'
    },
    component: Main,
    children: [
      {
        path: 'product',
        name: 'product',
        meta: {
          icon: 'md-list',
          title: '商品列表'
        },
        component: () => import('@/view/pms/product')
      },
      {
        path: 'coupon',
        name: 'coupon',
        meta: {
          icon: 'md-list',
          title: '优惠劵列表'
        },
        component: () => import('@/view/pms/coupon')
      },
      {
        path: 'vipCard',
        name: 'vipCard',
        meta: {
          icon: 'md-list',
          title: '会员卡列表'
        },
        component: () => import('@/view/pms/vipCard')
      },
      {
        path: 'editVipCard',
        name: 'editVipCard',
        meta: {
          icon: 'md-settings',
          title: '编辑会员卡'
        },
        component: () => import('@/view/pms/editVipCard')
      }
    ]
  },
  {
    path: '/auth',
    name: 'auth',
    meta: {
      icon: 'md-lock',
      title: '权限'
    },
    component: Main,
    children: [
      {
        path: 'admin',
        name: 'admin',
        meta: {
          icon: 'md-person',
          title: '管理员列表'
        },
        component: () => import('@/view/auth/admin')
      },
      {
        path: 'role',
        name: 'role',
        meta: {
          icon: 'ios-people',
          title: '角色列表'
        },
        component: () => import('@/view/auth/role')
      },
      {
        path: 'menu',
        name: 'menu',
        meta: {
          icon: 'ios-cube-outline',
          title: '菜单列表'
        },
        component: () => import('@/view/auth/menu')
      }
    ]
  }
]
export default constantRouterMap
