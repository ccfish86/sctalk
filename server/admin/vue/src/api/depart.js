import fetch from '@/utils/fetch'

export function listDepartRequest() {
  
  return fetch({
    url: '/depart/list',
    method: 'get'
  })
}

export function addDepartRequest(data) {
  return fetch({
    url: '/depart/add',
    method: 'post',
    data
  })
}

export function removeDepartRequest(data) {
  return fetch({
    url: '/depart/remove',
    method: 'post',
    data
  })
}

export function updateDepartRequest(data) {
  return fetch({
    url: '/depart/update',
    method: 'post',
    data
  })
}
