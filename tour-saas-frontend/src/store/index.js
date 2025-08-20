import { createStore } from 'vuex'
import authService from '../services/authService'

export default createStore({
  state: {
    user: null,
    token: localStorage.getItem('token') || null,
    loading: false,
    error: null
  },
  
  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    token: state => state.token,
    isLoading: state => state.loading,
    error: state => state.error
  },
  
  mutations: {
    SET_USER(state, user) {
      state.user = user
    },
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        localStorage.setItem('token', token)
      } else {
        localStorage.removeItem('token')
      }
    },
    SET_LOADING(state, loading) {
      state.loading = loading
    },
    SET_ERROR(state, error) {
      state.error = error
    },
    CLEAR_ERROR(state) {
      state.error = null
    }
  },
  
  actions: {
    async login({ commit }, credentials) {
      commit('SET_LOADING', true)
      commit('CLEAR_ERROR')
      
      try {
        const response = await authService.login(credentials)
        commit('SET_TOKEN', response.data.token)
        commit('SET_USER', response.data.user)
        return response
      } catch (error) {
        commit('SET_ERROR', error.response?.data?.error || '登录失败')
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async register({ commit }, userData) {
      commit('SET_LOADING', true)
      commit('CLEAR_ERROR')
      
      try {
        const response = await authService.register(userData)
        return response
      } catch (error) {
        commit('SET_ERROR', error.response?.data?.error || '注册失败')
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    logout({ commit }) {
      commit('SET_TOKEN', null)
      commit('SET_USER', null)
      commit('CLEAR_ERROR')
    },
    
    clearError({ commit }) {
      commit('CLEAR_ERROR')
    }
  }
})