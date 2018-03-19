import fetch from '@/utils/fetch'

export function loginByUsername(username, password) {
  const data = {
    username:username,
    password:password
  }
  return fetch({
    url: '/login',
    method: 'post',
    data
  })
}

export function logout() {
  return fetch({
    url: '/logout',
    method: 'post'
  })
}

export function getUserInfo(token) {
  return fetch({
    url: '/getInfo',
    method: 'get',
    params: { token }
  })
}



