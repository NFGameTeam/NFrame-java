import request from '@/utils/request'

export function getErrDetail(id) {
  return request({
    url: 'api-user/logs/error/' + id,
    method: 'get'
  })
}

export function delAllError() {
  return request({
    url: 'api-user/logs/del/error',
    method: 'delete'
  })
}

export function delAllInfo() {
  return request({
    url: 'api-user/logs/del/info',
    method: 'delete'
  })
}
