import fetch from '@/utils/fetch'

export function listManagerRequest() {
  
  return fetch({
    url: '/manager/list',
    method: 'get'
  })
}

export function addManagerRequest(data) {
  return fetch({
    url: '/manager/add',
    method: 'post',
    data
  })
}

export function removeManagerRequest(data) {
  return fetch({
    url: '/manager/remove',
    method: 'post',
    data
  })
}

export function updateManagerRequest(data) {
  return fetch({
    url: '/manager/modify',
    method: 'post',
    data
  })
}

export function updatePasswordRequest(data) {
  return fetch({
    url: '/manager/updatePassword',
    method: 'post',
    data
  })
}

export function changeRole(data) {
  return fetch({
    url: '/manager/changeRole',
    method: 'post',
    data
  })
}