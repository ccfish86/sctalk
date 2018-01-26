import fetch from '@/utils/fetch'

export function listGroupRequest() {
  
  return fetch({
    url: '/group/list',
    method: 'get'
  })
}

export function addGroupRequest(data) {
  return fetch({
    url: '/group/add',
    method: 'post',
    data
  })
}

export function removeGroupRequest(data) {
  return fetch({
    url: '/group/remove',
    method: 'post',
    data
  })
}

export function updateGroupRequest(data) {
  return fetch({
    url: '/group/update',
    method: 'post',
    data
  })
}
