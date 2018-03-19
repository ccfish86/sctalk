import { loginByUsername, logout, getUserInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    manager_id: 0,
    status: '',
    code: '',
    token: getToken(),
    name: '',
    avatar: '',
    introduction: '',
    password: '',
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_ID: (state, id) => {
      state.manager_id = id
    },
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PASSWORD: (state,password) =>{
      state.password = password
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(username, userInfo.password).then(response => {
          let {data,msg,code} =response.data
          if(code==0){
            var jsondata =JSON.parse(data)
            setToken(jsondata.token)
            commit('SET_TOKEN', jsondata.token)
            resolve()
          }else
          {   
             reject(msg)
          }
          
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
         let {data,msg,code} =response.data
         if(code==0){
          var dataJson = JSON.parse(data)
          commit('SET_PASSWORD',dataJson.password)
          commit('SET_NAME', dataJson.username)
          commit('SET_AVATAR', dataJson.avatar)
          commit('SET_INTRODUCTION', dataJson.introduction)
          commit('SET_ID', dataJson.id)
          resolve(response)
         }else
         {
          reject(msg)
         }
        }).catch(error => {
          reject(error)
        })
      })
    },


    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        //window.sessionStorage.removeItem("addRouters")
        //window.sessionStorage.removeItem("routers")
        removeToken()
        resolve()
      })
    }
 }
}

export default user
