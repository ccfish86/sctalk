import fetch from '@/utils/fetch'

export function listDiscoveryRequest() {
  
  return fetch({
    url: '/discovery/list',
    method: 'get'
  })
}

export function addDiscoveryRequest(data) {
  return fetch({
    url: '/discovery/add',
    method: 'post',
    data
  })
}

export function removeDiscoveryRequest(data) {
  return fetch({
    url: '/discovery/remove',
    method: 'post',
    data
  })
}

export function updateDiscoveryRequest(data) {
  return fetch({
    url: '/discovery/update',
    method: 'post',
    data
  })
}