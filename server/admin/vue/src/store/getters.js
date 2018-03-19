const getters = {
  sidebar: state => state.app.sidebar,
  visitedViews: state => state.app.visitedViews,
  manager_id: state => state.user.manager_id,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  introduction: state => state.user.introduction,
  status: state => state.user.status,
  password: state => state.user.password,
  setting: state => state.user.setting,
  permission_routers: state => state.getRoutes.routers,
  addRouters: state => state.getRoutes.addRouters
}
export default getters
