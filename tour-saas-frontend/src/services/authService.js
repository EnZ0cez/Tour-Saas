import api from './api'

export default {
  // 用户登录
  login(credentials) {
    return api.post('/auth/login', credentials)
  },
  
  // 用户注册
  register(userData) {
    return api.post('/auth/register', userData)
  }
}