import request from '@/utils/request'

export function testDbConnect(data) {
  return request({
    url: 'api-user/database/testConnect',
    method: 'post',
    data
  })
}

export function testServerConnect(data) {
  return request({
    url: 'api-user/serverDeploy/testConnect',
    method: 'post',
    data
  })
}
