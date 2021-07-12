import request from '@/utils/request'

export function get() {
  return request({
    url: 'api-user/aliPay',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'api-user/aliPay',
    data,
    method: 'put'
  })
}

// 支付
export function toAliPay(url, data) {
  return request({
    url: 'api-user/' + url,
    data,
    method: 'post'
  })
}
