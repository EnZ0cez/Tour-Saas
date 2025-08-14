import api from './api'

class AuthService {
  // 用户登录
  login(credentials) {
    return api.post('/auth/login', credentials)
  }

  // 用户注册
  register(userData) {
    return api.post('/auth/register', userData)
  }

  // 获取当前用户信息
  getCurrentUser() {
    return api.get('/user/profile')
  }
}

export default new AuthService()