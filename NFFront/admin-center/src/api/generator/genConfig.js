import request from '@/utils/request'

export function get(tableName) {
  return request({
    url: 'api-user/genConfig/' + tableName,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'api-user/genConfig',
    data,
    method: 'put'
  })
}
