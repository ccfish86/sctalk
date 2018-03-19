import fetch from '@/utils/fetch'

export function listPowerRequest() {
  
  return fetch({
    url: '/power/list',
    method: 'get'
  })
}

export function addPowerRequest(data) {
  return fetch({
    url: '/power/add',
    method: 'post',
    data
  })
}

export function removePowerRequest(data) {
  return fetch({
    url: '/power/remove',
    method: 'post',
    data
  })
}

export function updatePowerRequest(data) {
  return fetch({
    url: '/power/modify',
    method: 'post',
    data
  })
}

export function getRoute(token){
  return fetch({
     url: '/getRoute',
     method: 'get',
     params: { token }
  })
}

export function getPower(data){
  return fetch({
     url: '/getPower',
     method: 'post',
     data
  })
}


