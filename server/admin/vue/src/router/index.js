/* eslint-disable */
import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading

Vue.use(Router)

/* layout */
import Layout from '../views/layout/Layout'

/**
* icon : the icon show in the sidebar
* hidden : if `hidden:true` will not show in the sidebar
* redirect : if `redirect:noredirect` will no redirct in the levelbar
* noDropdown : if `noDropdown:true` will has no submenu
* meta : { role: ['admin'] }  will control the page role
**/
export const constantRouterMap = [
  { path: '/login', component: _import('/login/index'), hidden: true },
  { path: '/register', component: _import('/login/register'), hidden: true },
  { path: '/authredirect', component: _import('/login/authredirect'), hidden: true },
  { path: '/404', component: _import('/errorPage/404'), hidden: true },
  { path: '/401', component: _import('/errorPage/401'), hidden: true },
  {
    path: '/',
    component: _import('/layout/Layout'),
    redirect: '/dashboard/index',
    name: '首页',
    hidden: true,
    children: [{ path: '/dashboard/index', component: _import('/dashboard/index') }]
  },
  {
    path: '/layout/Layout',
    component: _import('/layout/Layout'),
    //redirect: '/introduction/index',
    icon: 'people',
    noDropdown: true,
    children: [{ path: '/introduction/index', component: _import('/introduction/index'), name: '简述' }]
  }/*,
  { path: '*', redirect: '/404', hidden: true }*/

]

export default new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
/*export const asyncRouterMap = [
  {
    path: '/permission',
    component: _import('layout/Layout'),
    //redirect: '/permission/index',
    name: '权限测试',
    icon: 'password',
    meta: { role: ['admin'] },
    noDropdown: true,
    children: [{ path: 'index', component: _import('permission/index'), name: '权限测试页', meta: { role: ['admin'] }}]
  },
  {
    path: '/example',
    component: _import('layout/Layout'),
    //redirect: 'noredirect',
    name: '综合实例',
    icon: 'example',
    children: [
      {
        path: '/example/table',
        component: _import('example/table/index'),
        //redirect: '/example/table/table',
        name: 'Table',
        icon: 'table',
        children: [
          { path: 'dynamictable', component: _import('example/table/dynamictable/index'), name: '动态table' },
          { path: 'dragtable', component: _import('example/table/dragTable'), name: '拖拽table' },
          { path: 'inline_edit_table', component: _import('example/table/inlineEditTable'), name: 'table内编辑' },
          { path: 'table', component: _import('example/table/table'), name: '综合table' }
        ]
      },
      { path: 'form/edit', icon: 'form', component: _import('example/form'), name: '编辑Form', meta: { isEdit: true }},
      { path: 'form/create', icon: 'form', component: _import('example/form'), name: '创建Form' },
      { path: 'tab/index', icon: 'tab', component: _import('example/tab/index'), name: 'Tab' }
    ]
  },
  {
    path: '/member',
    component: _import('layout/Layout'),
    //redirect: 'noredirect',
    icon: 'email',
    noDropdown: true,
    meta: { role: ['admin','member'] },
    children: [
      { path: 'member', component: _import('function/Member'), name: '用户管理' }
    ]
  },
  {
    path: '/depart',
    component: _import('layout/Layout'),
    //redirect: 'noredirect',
    icon: 'star',
    noDropdown: true,
    meta: { role: ['admin','depart'] },
    children: [
      { path: 'depart', component: _import('function/Depart'), name: '部门管理' }
    ]
  },
  {
    path: '/group',
    component: _import('layout/Layout'),
    //redirect: 'noredirect',
    icon: 'drag',
    noDropdown: true,
    meta: { role: ['admin','group'] },
    children: [
      { path: 'group', component: _import('function/Group'), name: '群组管理' }
    ]
  },
  {
    path: '/discovery',
    component: _import('layout/Layout'),
    //redirect: 'noredirect',
    icon: 'example',
    noDropdown: true,
    meta: { role: ['admin','discovery'] },
    children: [
      { path: 'discovery', component: _import('function/Discovery'), name: '发现管理' }
    ]
  },
  {
    path: '/management',
    component: _import('layout/Layout'),
    //redirect: 'noredirect',
    name: '管理员权限设置',
    icon: 'lock',
    meta: { role: ['admin'] },
    children: [
      { path: 'admin', component: _import('management/Admin'), icon: 'user',name: '管理员设置' },
      { path: 'power', component: _import('management/Power'), icon: 'user',name: '角色设置' },
      { path: 'role', component: _import('management/Role'), icon: 'user',name: '权限设置' }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]*/