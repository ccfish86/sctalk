import jsonFormatSidebarTree from '@/utils/jsonFormatSidebarTree'
import MenuUtils from '@/utils/MenuUtils'
import { constantRouterMap } from '@/router'
import { getToken } from '@/utils/auth'
import { getRoute } from '@/api/power'
/*import menu from '@/menu'*/


const getRoutes = {
    state: {
      routers: constantRouterMap,
      addRouters: [],
      token:getToken()
    },
    mutations: {
        SET_ROUTERS: (state, routers) => {
          routers.push({ path: '*', redirect: '/404', hidden: true })
          state.addRouters = routers
          state.routers = constantRouterMap.concat(routers)
        }
    },
    actions:{
        generatorRoutes( { commit},token){
        return new Promise((resolve, reject) => {
            getRoute(token).then(response => {
              let {code, msg, data}=response.data
              if(code==0){
                 let power_router=JSON.parse(data).power
                 let routes_ex=[]
                 jsonFormatSidebarTree(routes_ex,power_router)
                 MenuUtils(routes_ex)
                 commit('SET_ROUTERS', routes_ex)
                 resolve(response)
              }
              else
              {
                 reject(msg)
              }
              
            }).catch(error =>{
               reject(error)
            })
        })
        }
    }
}

export default getRoutes
