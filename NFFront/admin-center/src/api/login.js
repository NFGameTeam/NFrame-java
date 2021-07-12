import request from '@/utils/request'
import axios from "axios";
import { getToken } from '@/utils/auth'

export function login(user) {
  console.log("login",user)

  var data = new FormData()
  data.append('username', user.username)
  data.append('password', user.password)
  data.append('validCode', user.code)
  data.append('deviceId', user.uuid)
  data.append('grant_type', process.env.VUE_APP_GRANT_TYPE)
  data.append('client_id', process.env.VUE_APP_CLIENT_ID)
  data.append('client_secret', process.env.VUE_APP_CLIENT_SECRET)
  data.append('scope', process.env.VUE_APP_SCOPE)

  const config = {
    headers: { 'Authorization': getToken() }
  }
  config.headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8'
  return axios.post('api-auth/oauth/token', data, config)
}

export function getInfo() {
  var token= JSON.parse(getToken())

  return request({
    url: 'api-auth/oauth/userinfo?access_token='+token.access_token,
    method: 'get'
  })
}

// export function getCodeImg() {
//   return request({
//     url: 'auth/code',
//     method: 'get'
//   })
// }

export function logout() {

  return request({
    url: 'api-user/out',
    method: 'delete'
  })
}
