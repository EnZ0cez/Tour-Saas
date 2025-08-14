import { createStore } from 'vuex'
import Cookies from 'js-cookie'

export default createStore({
  state: {
    token: Cookies.get('token') || '',
    userInfo: null
  },
  getters: {
    isLoggedIn: state => !!state.token,
    username: state => state.userInfo ? state.userInfo.username : ''
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      Cookies.set('token', token)
    },
    CLEAR_TOKEN(state) {
      state.token = ''
      state.userInfo = null
      Cookies.remove('token')
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
    }
  },
  actions: {
    login({ commit }, token) {
      commit('SET_TOKEN', token)
    },
    logout({ commit }) {
      commit('CLEAR_TOKEN')
    },
    setUserInfo({ commit }, userInfo) {
      commit('SET_USER_INFO', userInfo)
    }
  },
  modules: {
  }
})