import api from './api'

class RecommendationService {
  // 为用户生成推荐
  generateRecommendations(userId) {
    return api.post(`/recommendations/generate/${userId}`)
  }

  // 获取用户推荐
  getRecommendationsByUserId(userId) {
    return api.get(`/recommendations/user/${userId}`)
  }

  // 获取协同推荐
  getCollaborativeRecommendations(userId) {
    return api.get(`/recommendations/collaborative/${userId}`)
  }
}

export default new RecommendationService()