import store from '@/store'

export default {
  inserted(el, binding) {

    const { value } = binding
    const roles = store.getters && store.getters.roles

    //更改查询方式
    const permissions=[]
    roles.forEach(function(role, i) {
      permissions.push(role.code)
      role.permissions.forEach(function (permission,j) {
        permissions.push(permission.code)
      })
    })


    if (value && value instanceof Array) {
      if (value.length > 0) {
        const permissionRoles = value
        const hasPermission = permissions.some(role => {
          return permissionRoles.includes(role)
        })
        if (!hasPermission) {
          el.parentNode && el.parentNode.removeChild(el)
        }
      }
    } else {
      throw new Error(`使用方式： v-permission="['admin','editor']"`)
    }
  }
}
