<template>
  <div class="recommendations-page">
    <div class="container">
      <div class="page-header">
        <h1>智能推荐</h1>
        <p>基于AI技术的个性化旅游推荐</p>
      </div>
      
      <!-- 推荐控制面板 -->
      <div class="recommendation-controls">
        <div class="control-panel">
          <div class="provider-selection">
            <label>推荐引擎：</label>
            <el-radio-group v-model="selectedProvider" @change="handleProviderChange">
              <el-radio-button label="traditional">传统算法</el-radio-button>
              <el-radio-button label="ollama">Ollama (本地)</el-radio-button>
              <el-radio-button label="openai">OpenAI (云端)</el-radio-button>
            </el-radio-group>
          </div>
          
          <div class="action-buttons">
            <el-button 
              type="primary" 
              :loading="loading" 
              @click="generateRecommendations"
              size="large"
            >
              生成推荐
            </el-button>
          </div>
        </div>
        
        <!-- 服务状态 -->
        <div class="service-status">
          <el-tag 
            v-for="(status, provider) in serviceStatus" 
            :key="provider"
            :type="status ? 'success' : 'danger'"
            class="status-tag"
          >
            {{ getProviderLabel(provider) }}: {{ status ? '在线' : '离线' }}
          </el-tag>
        </div>
      </div>
      
      <!-- 推荐结果 -->
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="recommendations.length > 0" class="recommendations-section">
        <h2>为您推荐</h2>
        <div class="recommendations-grid">
          <div v-for="recommendation in recommendations" :key="recommendation.id" class="recommendation-card">
            <div class="recommendation-content">
              <div class="recommendation-header">
                <h3>{{ recommendation.product?.name || '推荐产品' }}</h3>
                <div class="recommendation-score">
                  <el-rate 
                    :model-value="recommendation.score / 2" 
                    disabled 
                    text-color="#ff9900"
                  />
                  <span class="score-text">{{ recommendation.score?.toFixed(1) || '0.0' }}</span>
                </div>
              </div>
              
              <p class="recommendation-description">
                {{ recommendation.product?.description || '暂无描述' }}
              </p>
              
              <div class="recommendation-meta">
                <span class="price">¥{{ recommendation.product?.price || '0' }}</span>
                <span class="duration">{{ recommendation.product?.duration || 0 }}天</span>
              </div>
              
              <div class="recommendation-actions">
                <router-link 
                  :to="'/products/' + recommendation.product?.id" 
                  class="btn btn-primary"
                >
                  查看详情
                </router-link>
                <button 
                  @click="handleBook(recommendation.product)" 
                  class="btn btn-secondary"
                >
                  立即预订
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 个性化建议 -->
      <div v-if="personalizedSuggestions" class="suggestions-section">
        <h2>个性化旅行建议</h2>
        <el-card class="suggestions-card">
          <div class="suggestions-content" v-html="formattedSuggestions"></div>
        </el-card>
      </div>
      
      <!-- 用户行为分析 -->
      <div v-if="behaviorAnalysis" class="analysis-section">
        <h2>用户行为分析</h2>
        <el-card class="analysis-card">
          <div class="analysis-content" v-html="formattedAnalysis"></div>
        </el-card>
      </div>
      
      <!-- 无推荐数据 -->
      <div v-if="!loading && recommendations.length === 0 && !personalizedSuggestions && !behaviorAnalysis" class="no-recommendations">
        <el-empty description="暂无推荐数据">
          <el-button type="primary" @click="generateRecommendations">生成推荐</el-button>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import recommendationService from '../services/recommendationService'

export default {
  name: 'Recommendations',
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const loading = ref(false)
    const selectedProvider = ref('traditional')
    const recommendations = ref([])
    const personalizedSuggestions = ref('')
    const behaviorAnalysis = ref('')
    const serviceStatus = ref({})
    
    const currentUser = computed(() => store.getters.currentUser)
    
    const formattedSuggestions = computed(() => {
      return personalizedSuggestions.value?.replace(/\n/g, '<br>') || ''
    })
    
    const formattedAnalysis = computed(() => {
      return behaviorAnalysis.value?.replace(/\n/g, '<br>') || ''
    })
    
    const checkServiceStatus = async () => {
      try {
        const response = await recommendationService.checkLlmServices()
        serviceStatus.value = response.data
      } catch (error) {
        console.error('检查服务状态失败:', error)
      }
    }
    
    const generateRecommendations = async () => {
      if (!currentUser.value) {
        ElMessage.warning('请先登录')
        return
      }
      
      loading.value = true
      
      try {
        if (selectedProvider.value === 'traditional') {
          // 传统推荐算法
          const [recs, collabRecs] = await Promise.all([
            recommendationService.getRecommendationsByUserId(currentUser.value.id),
            recommendationService.getCollaborativeRecommendations(currentUser.value.id)
          ])
          
          recommendations.value = recs.data
          // 协同过滤推荐也作为产品推荐
          if (collabRecs.data.length > 0) {
            const collabRecommendations = collabRecs.data.map(product => ({
              id: Date.now() + Math.random(),
              product: product,
              score: 0.8 + Math.random() * 0.2
            }))
            recommendations.value.push(...collabRecommendations)
          }
        } else {
          // LLM推荐
          const [recs, suggestions, analysis] = await Promise.all([
            recommendationService.generateLlmRecommendations(currentUser.value.id, selectedProvider.value),
            recommendationService.generatePersonalizedSuggestions(currentUser.value.id, selectedProvider.value),
            recommendationService.analyzeUserBehavior(currentUser.value.id, selectedProvider.value)
          ])
          
          recommendations.value = recs.data
          personalizedSuggestions.value = suggestions.data
          behaviorAnalysis.value = analysis.data
        }
        
        ElMessage.success('推荐生成成功')
      } catch (error) {
        ElMessage.error('生成推荐失败：' + (error.response?.data?.message || error.message))
        console.error('生成推荐失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const handleProviderChange = () => {
      // 清空当前推荐结果
      recommendations.value = []
      personalizedSuggestions.value = ''
      behaviorAnalysis.value = ''
    }
    
    const handleBook = (product) => {
      if (product) {
        router.push(`/products/${product.id}`)
      }
    }
    
    const getProviderLabel = (provider) => {
      const labels = {
        ollama: 'Ollama',
        openai: 'OpenAI'
      }
      return labels[provider] || provider
    }
    
    onMounted(() => {
      checkServiceStatus()
      // 自动生成推荐
      generateRecommendations()
    })
    
    return {
      loading,
      selectedProvider,
      recommendations,
      personalizedSuggestions,
      behaviorAnalysis,
      serviceStatus,
      formattedSuggestions,
      formattedAnalysis,
      handleProviderChange,
      generateRecommendations,
      handleBook,
      getProviderLabel
    }
  }
}
</script>

<style scoped>
.recommendations-page {
  padding: 2rem 0;
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 3rem;
}

.page-header h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.page-header p {
  color: #666;
  font-size: 1.1rem;
}

.recommendation-controls {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.control-panel {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.provider-selection {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.provider-selection label {
  font-weight: bold;
  color: #2c3e50;
}

.service-status {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.status-tag {
  margin-right: 0.5rem;
}

.recommendations-section,
.suggestions-section,
.analysis-section {
  margin-bottom: 3rem;
}

.recommendations-section h2,
.suggestions-section h2,
.analysis-section h2 {
  color: #2c3e50;
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
}

.recommendations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
}

.recommendation-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.recommendation-card:hover {
  transform: translateY(-5px);
}

.recommendation-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.recommendation-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.2rem;
  flex: 1;
}

.recommendation-score {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.score-text {
  font-weight: bold;
  color: #ff9900;
}

.recommendation-description {
  color: #666;
  margin-bottom: 1rem;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recommendation-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.price {
  font-size: 1.2rem;
  font-weight: bold;
  color: #e74c3c;
}

.duration {
  color: #666;
  background: #f8f9fa;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
}

.recommendation-actions {
  display: flex;
  gap: 0.5rem;
}

.recommendation-actions .btn {
  flex: 1;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  text-align: center;
  transition: all 0.3s;
}

.btn-primary {
  background: #409eff;
  color: white;
}

.btn-primary:hover {
  background: #66b1ff;
}

.btn-secondary {
  background: #909399;
  color: white;
}

.btn-secondary:hover {
  background: #a6a9ad;
}

.suggestions-card,
.analysis-card {
  background: #f8f9fa;
  border-left: 4px solid #409eff;
}

.suggestions-content,
.analysis-content {
  padding: 1rem;
  line-height: 1.6;
  color: #333;
}

.loading {
  padding: 2rem;
}

.no-recommendations {
  text-align: center;
  padding: 3rem;
}

@media (max-width: 768px) {
  .control-panel {
    flex-direction: column;
    align-items: stretch;
  }
  
  .provider-selection {
    justify-content: center;
  }
  
  .recommendations-grid {
    grid-template-columns: 1fr;
  }
  
  .recommendation-header {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .recommendation-actions {
    flex-direction: column;
  }
}
</style>