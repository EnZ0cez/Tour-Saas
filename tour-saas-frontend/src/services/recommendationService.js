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

  // LLM智能推荐
  generateLlmRecommendations(userId, provider = 'ollama') {
    return api.post(`/recommendations/llm/generate/${userId}`, null, {
      params: { provider }
    })
  }

  // 个性化旅行建议
  generatePersonalizedSuggestions(userId, provider = 'ollama') {
    return api.post(`/recommendations/llm/suggestions/${userId}`, null, {
      params: { provider }
    })
  }

  // 用户行为分析
  analyzeUserBehavior(userId, provider = 'ollama') {
    return api.post(`/recommendations/llm/analyze/${userId}`, null, {
      params: { provider }
    })
  }

  // 检查LLM服务状态
  checkLlmServices() {
    return api.get('/recommendations/llm/status')
  }

  // 自定义LLM请求
  customLlmRequest(request) {
    return api.post('/recommendations/llm/custom', request)
  }
}

export default new RecommendationService()