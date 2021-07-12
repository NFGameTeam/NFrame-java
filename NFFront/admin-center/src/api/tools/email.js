import request from '@/utils/request'

export function get() {
  return request({
    url: 'api-user/email',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'api-user/email',
    data,
    method: 'put'
  })
}

export function send(data) {
  return request({
    url: 'api-user/email',
    data,
    method: 'post'
  })
}
