<template>
  <div class="llm-recommendations">
    <el-card class="llm-card">
      <template #header>
        <div class="card-header">
          <h3>🤖 LLM智能推荐</h3>
          <div class="controls">
            <el-select v-model="selectedProvider" placeholder="选择LLM提供商" size="small">
              <el-option label="Ollama (本地)" value="ollama" />
              <el-option label="OpenAI (云端)" value="openai" />
            </el-select>
            <el-button 
              type="primary" 
              size="small" 
              @click="generateRecommendations"
              :loading="loading"
            >
              生成智能推荐
            </el-button>
          </div>
        </div>
      </template>

      <!-- 服务状态 -->
      <div class="service-status">
        <el-tag 
          v-for="(status, provider) in serviceStatus" 
          :key="provider"
          :type="status ? 'success' : 'danger'"
          class="status-tag"
        >
          {{ provider === 'ollama' ? 'Ollama' : 'OpenAI' }}: {{ status ? '在线' : '离线' }}
        </el-tag>
      </div>

      <!-- 推荐结果 -->
      <div v-if="recommendations.length > 0" class="recommendations">
        <h4>智能推荐结果</h4>
        <el-row :gutter="20">
          <el-col :span="8" v-for="recommendation in recommendations" :key="recommendation.id">
            <el-card class="recommendation-card">
              <div class="recommendation-content">
                <h5>{{ recommendation.productName }}</h5>
                <p class="description">{{ recommendation.description }}</p>
                <div class="score">
                  <el-rate 
                    v-model="recommendation.score / 2" 
                    disabled 
                    text-color="#ff9900"
                  />
                  <span class="score-value">{{ recommendation.score.toFixed(1) }}</span>
                </div>
                <el-button type="primary" size="small" @click="viewProduct(recommendation.productId)">
                  查看详情
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 个性化建议 -->
      <div v-if="personalizedSuggestions" class="suggestions">
        <h4>个性化旅行建议</h4>
        <el-card class="suggestions-card">
          <div class="suggestions-content" v-html="formattedSuggestions"></div>
        </el-card>
      </div>

      <!-- 用户行为分析 -->
      <div v-if="behaviorAnalysis" class="analysis">
        <h4>用户行为分析</h4>
        <el-card class="analysis-card">
          <div class="analysis-content" v-html="formattedAnalysis"></div>
        </el-card>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>

      <!-- 错误信息 -->
      <div v-if="error" class="error">
        <el-alert 
          :title="error" 
          type="error" 
          show-icon 
          @close="error = ''"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import recommendationService from '@/services/recommendationService'

export default {
  name: 'LlmRecommendations',
  props: {
    userId: {
      type: [Number, String],
      required: true
    }
  },
  setup(props) {
    const router = useRouter()
    const selectedProvider = ref('ollama')
    const loading = ref(false)
    const error = ref('')
    const recommendations = ref([])
    const personalizedSuggestions = ref('')
    const behaviorAnalysis = ref('')
    const serviceStatus = ref({})

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
      } catch (err) {
        console.error('检查服务状态失败:', err)
      }
    }

    const generateRecommendations = async () => {
      if (!props.userId) {
        error.value = '请先登录'
        return
      }

      loading.value = true
      error.value = ''
      
      try {
        // 并行请求所有LLM服务
        const [recs, suggestions, analysis] = await Promise.all([
          recommendationService.generateLlmRecommendations(props.userId, selectedProvider.value),
          recommendationService.generatePersonalizedSuggestions(props.userId, selectedProvider.value),
          recommendationService.analyzeUserBehavior(props.userId, selectedProvider.value)
        ])

        recommendations.value = recs.data
        personalizedSuggestions.value = suggestions.data
        behaviorAnalysis.value = analysis.data
      } catch (err) {
        error.value = '生成推荐失败: ' + (err.response?.data?.message || err.message)
        console.error('LLM推荐失败:', err)
      } finally {
        loading.value = false
      }
    }

    const viewProduct = (productId) => {
      router.push(`/product/${productId}`)
    }

    onMounted(() => {
      checkServiceStatus()
    })

    return {
      selectedProvider,
      loading,
      error,
      recommendations,
      personalizedSuggestions,
      behaviorAnalysis,
      serviceStatus,
      formattedSuggestions,
      formattedAnalysis,
      generateRecommendations,
      viewProduct
    }
  }
}
</script>

<style scoped>
.llm-recommendations {
  margin: 20px 0;
}

.llm-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #409eff;
}

.controls {
  display: flex;
  gap: 10px;
  align-items: center;
}

.service-status {
  margin-bottom: 20px;
}

.status-tag {
  margin-right: 10px;
}

.recommendations {
  margin: 20px 0;
}

.recommendations h4 {
  margin-bottom: 15px;
  color: #333;
}

.recommendation-card {
  margin-bottom: 15px;
  height: 100%;
}

.recommendation-content {
  padding: 10px;
}

.recommendation-content h5 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.description {
  color: #666;
  font-size: 14px;
  margin-bottom: 15px;
  line-height: 1.4;
}

.score {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.score-value {
  font-weight: bold;
  color: #ff9900;
}

.suggestions,
.analysis {
  margin: 20px 0;
}

.suggestions h4,
.analysis h4 {
  margin-bottom: 15px;
  color: #333;
}

.suggestions-card,
.analysis-card {
  background: #f8f9fa;
  border-left: 4px solid #409eff;
}

.suggestions-content,
.analysis-content {
  padding: 15px;
  line-height: 1.6;
  color: #333;
}

.loading {
  padding: 20px;
}

.error {
  margin: 20px 0;
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .controls {
    width: 100%;
    justify-content: space-between;
  }
}
</style>