import request from '@/utils/request'
import { getToken, setToken, removeToken } from '@/utils/auth'

export function getMenusTree(pid) {
  return request({
    url: 'api-user/menus/lazy?pid=' + pid,
    method: 'get'
  })
}

export function getMenus(params) {
  return request({
    url: 'api-user/menus',
    method: 'get',
    params
  })
}

export function getMenuSuperior(ids) {
  const data = ids.length || ids.length === 0 ? ids : Array.of(ids)
  return request({
    url: 'api-user/menus/superior',
    method: 'post',
    data
  })
}

export function getChild(id) {
  return request({
    url: 'api-user/menus/child?id=' + id,
    method: 'get'
  })
}

export function buildMenus() {


  var token= JSON.parse(getToken())

  return request({
    url: 'api-user/menus/build?access_token='+token.access_token,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api-user/menus',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api-user/menus',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api-user/menus',
    method: 'put',
    data
  })
}

export default { add, edit, del, getMenusTree, getMenuSuperior, getMenus, getChild }
