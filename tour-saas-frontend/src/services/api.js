import axios from 'axios'
import store from '@/store'
import Cookies from 'js-cookie'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端API地址
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 在请求头中添加token
    const token = store.state.token || Cookies.get('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response && error.response.status === 401) {
      // token过期或无效，清除本地token并跳转到登录页
      store.dispatch('logout')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api