<template>
  <div class="product-detail-page">
    <div class="container">
      <div v-if="loading" class="loading">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="product" class="product-detail">
        <!-- 产品基本信息 -->
        <div class="product-header">
          <div class="product-image">
            <img :src="product.imageUrl || 'https://via.placeholder.com/600x400'" :alt="product.name">
          </div>
          <div class="product-info">
            <h1>{{ product.name }}</h1>
            <div class="product-meta">
              <span class="price">¥{{ product.price }}</span>
              <span class="duration">{{ product.duration }}天</span>
              <span class="type">{{ getTypeLabel(product.type) }}</span>
            </div>
            <div class="product-description">
              <h3>产品介绍</h3>
              <p>{{ product.description }}</p>
            </div>
            <div v-if="product.destination" class="product-destination">
              <h3>目的地</h3>
              <p>{{ product.destination }}</p>
            </div>
            <div class="product-actions">
              <button 
                v-if="isAuthenticated" 
                @click="showBookingDialog = true" 
                class="btn btn-primary"
              >
                立即预订
              </button>
              <router-link v-else to="/login" class="btn btn-primary">登录预订</router-link>
              <router-link to="/products" class="btn btn-secondary">返回列表</router-link>
            </div>
          </div>
        </div>
        
        <!-- 产品详情 -->
        <div class="product-details">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="产品详情" name="details">
              <div class="detail-content">
                <h3>详细说明</h3>
                <p>这是一个精心设计的旅游产品，为您提供难忘的旅行体验。我们的专业团队将确保您的旅程安全、舒适且充满乐趣。</p>
                
                <h3>服务包含</h3>
                <ul>
                  <li>专业导游服务</li>
                  <li>舒适交通安排</li>
                  <li>精选住宿</li>
                  <li>特色餐饮</li>
                  <li>景点门票</li>
                </ul>
                
                <h3>注意事项</h3>
                <ul>
                  <li>请携带有效身份证件</li>
                  <li>建议购买旅游保险</li>
                  <li>注意天气变化，准备合适衣物</li>
                  <li>遵守当地风俗习惯</li>
                </ul>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="行程安排" name="itinerary">
              <div class="itinerary-content">
                <div v-for="day in product.duration" :key="day" class="day-item">
                  <h3>第 {{ day }} 天</h3>
                  <p>精彩的行程安排，让您体验当地文化和风景。</p>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="用户评价" name="reviews">
              <div class="reviews-content">
                <div class="review-item">
                  <div class="review-header">
                    <span class="reviewer">张三</span>
                    <el-rate :model-value="5" disabled />
                  </div>
                  <p>非常棒的旅行体验，导游很专业，服务很周到！</p>
                </div>
                <div class="review-item">
                  <div class="review-header">
                    <span class="reviewer">李四</span>
                    <el-rate :model-value="4" disabled />
                  </div>
                  <p>整体不错，就是住宿条件还可以改善。</p>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      
      <div v-else class="not-found">
        <el-empty description="产品不存在或已下架">
          <router-link to="/products" class="btn btn-primary">返回产品列表</router-link>
        </el-empty>
      </div>
    </div>
    
    <!-- 预订对话框 -->
    <el-dialog 
      v-model="showBookingDialog" 
      title="产品预订" 
      width="500px"
      @close="resetBookingForm"
    >
      <el-form 
        ref="bookingForm" 
        :model="bookingForm" 
        :rules="bookingRules" 
        label-width="100px"
      >
        <el-form-item label="出行日期" prop="travelDate">
          <el-date-picker 
            v-model="bookingForm.travelDate" 
            type="date" 
            placeholder="选择出行日期"
            :disabled-date="disabledDate"
          />
        </el-form-item>
        
        <el-form-item label="出行人数" prop="travelersCount">
          <el-input-number 
            v-model="bookingForm.travelersCount" 
            :min="1" 
            :max="10" 
            placeholder="出行人数"
          />
        </el-form-item>
        
        <el-form-item label="总价">
          <span class="total-price">¥{{ totalPrice }}</span>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showBookingDialog = false">取消</el-button>
        <el-button type="primary" :loading="bookingLoading" @click="handleBooking">
          确认预订
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import productService from '../services/productService'
import orderService from '../services/orderService'

export default {
  name: 'ProductDetail',
  setup() {
    const store = useStore()
    const route = useRoute()
    const router = useRouter()
    
    const product = ref(null)
    const loading = ref(false)
    const activeTab = ref('details')
    const showBookingDialog = ref(false)
    const bookingForm = ref(null)
    
    const bookingData = ref({
      travelDate: '',
      travelersCount: 1
    })
    
    const bookingLoading = ref(false)
    
    const isAuthenticated = computed(() => store.getters.isAuthenticated)
    const currentUser = computed(() => store.getters.currentUser)
    
    const totalPrice = computed(() => {
      if (product.value && bookingData.value.travelersCount) {
        return product.value.price * bookingData.value.travelersCount
      }
      return 0
    })
    
    const bookingRules = {
      travelDate: [
        { required: true, message: '请选择出行日期', trigger: 'change' }
      ],
      travelersCount: [
        { required: true, message: '请输入出行人数', trigger: 'change' }
      ]
    }
    
    const disabledDate = (date) => {
      return date < new Date()
    }
    
    const loadProduct = async () => {
      loading.value = true
      try {
        const response = await productService.getProductById(route.params.id)
        product.value = response.data
      } catch (error) {
        ElMessage.error('加载产品失败')
        console.error('加载产品失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const getTypeLabel = (type) => {
      const labels = {
        DOMESTIC: '国内游',
        INTERNATIONAL: '国际游',
        CRUISE: '邮轮',
        HOTEL: '酒店',
        TICKET: '门票'
      }
      return labels[type] || type
    }
    
    const resetBookingForm = () => {
      bookingData.value = {
        travelDate: '',
        travelersCount: 1
      }
    }
    
    const handleBooking = async () => {
      if (!bookingForm.value) return
      
      try {
        await bookingForm.value.validate()
        bookingLoading.value = true
        
        const orderData = {
          userId: currentUser.value.id,
          productId: product.value.id,
          travelersCount: bookingData.value.travelersCount,
          travelDate: bookingData.value.travelDate.toISOString().split('T')[0]
        }
        
        await orderService.createOrder(orderData)
        
        ElMessage.success('预订成功！')
        showBookingDialog.value = false
        router.push('/orders')
      } catch (error) {
        ElMessage.error('预订失败：' + (error.response?.data?.message || error.message))
      } finally {
        bookingLoading.value = false
      }
    }
    
    onMounted(() => {
      loadProduct()
    })
    
    return {
      product,
      loading,
      activeTab,
      showBookingDialog,
      bookingForm,
      bookingForm: bookingData,
      bookingRules,
      bookingLoading,
      isAuthenticated,
      totalPrice,
      disabledDate,
      getTypeLabel,
      resetBookingForm,
      handleBooking
    }
  }
}
</script>

<style scoped>
.product-detail-page {
  padding: 2rem 0;
  min-height: 100vh;
}

.product-header {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-bottom: 3rem;
}

.product-image {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.product-image img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}

.product-info h1 {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.product-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.price {
  font-size: 1.5rem;
  font-weight: bold;
  color: #e74c3c;
}

.duration {
  background: #409eff;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
}

.type {
  background: #67c23a;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
}

.product-description,
.product-destination {
  margin-bottom: 1.5rem;
}

.product-description h3,
.product-destination h3 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.product-actions {
  display: flex;
  gap: 1rem;
}

.product-actions .btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
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

.product-details {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.detail-content h3,
.itinerary-content h3 {
  color: #2c3e50;
  margin-bottom: 1rem;
}

.detail-content ul {
  padding-left: 1.5rem;
  margin-bottom: 1rem;
}

.detail-content li {
  margin-bottom: 0.5rem;
  color: #666;
}

.day-item {
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.review-item {
  margin-bottom: 1.5rem;
  padding: 1rem;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.reviewer {
  font-weight: bold;
  color: #2c3e50;
}

.total-price {
  font-size: 1.2rem;
  font-weight: bold;
  color: #e74c3c;
}

.loading {
  padding: 2rem;
}

.not-found {
  text-align: center;
  padding: 3rem;
}

@media (max-width: 768px) {
  .product-header {
    grid-template-columns: 1fr;
  }
  
  .product-image img {
    height: 250px;
  }
  
  .product-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .product-actions {
    flex-direction: column;
  }
}
</style>