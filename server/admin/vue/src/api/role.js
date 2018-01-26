import fetch from '@/utils/fetch'

export function listRoleRequest() {
  
  return fetch({
    url: '/role/list',
    method: 'get'
  })
}

export function addRoleRequest(data) {
  return fetch({
    url: '/role/add',
    method: 'post',
    data
  })
}

export function removeRoleRequest(data) {
  return fetch({
    url: '/role/remove',
    method: 'post',
    data
  })
}

export function updateRoleRequest(data) {
  return fetch({
    url: '/role/modify',
    method: 'post',
    data
  })
}

export function changePower(data) {
  return fetch({
    url: '/role/changePower',
    method: 'post',
    data
  })
}

export function getRole(data) {
  return fetch({
    url: '/role/getRole',
    method: 'post',
    data
  })
}