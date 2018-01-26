import fetch from '@/utils/fetch'

export function listUsersRequest() {
  return fetch({
    url: '/user/list',
    method: 'get'
  })
}

export function addUserRequest(data) {
  return fetch({
    url: '/user/add',
    method: 'post',
    data
  })
}

export function removeUserRequest(data) {
  return fetch({
    url: '/user/remove',
    method: 'post',
    data
  })
}

export function updateUserRequest(data) {
  return fetch({
    url: '/user/update',
    method: 'post',
    data
  })
}

export function updatePasswordRequest(data) {
  return fetch({
    url: '/user/updatePassword',
    method: 'post',
    data
  })
}
