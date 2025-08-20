<template>
  <div class="orders-page">
    <div class="container">
      <div class="page-header">
        <h1>我的订单</h1>
        <p>查看和管理您的旅游订单</p>
      </div>
      
      <!-- 订单筛选 -->
      <div class="filters-section">
        <div class="filter-options">
          <el-select v-model="selectedStatus" placeholder="订单状态" @change="loadOrders">
            <el-option label="全部状态" value="" />
            <el-option label="待支付" value="PENDING" />
            <el-option label="已支付" value="PAID" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </div>
        
        <div class="refresh-btn">
          <el-button @click="loadOrders" :loading="loading">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>
      
      <!-- 订单列表 -->
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="orders.length > 0" class="orders-list">
        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-header">
            <div class="order-info">
              <h3>订单 #{{ order.id }}</h3>
              <p class="order-date">下单时间：{{ formatDate(order.createdAt) }}</p>
            </div>
            <div class="order-status">
              <el-tag :type="getStatusType(order.status)">
                {{ getStatusLabel(order.status) }}
              </el-tag>
            </div>
          </div>
          
          <div class="order-content">
            <div class="product-info">
              <h4>{{ order.product?.name || '产品信息' }}</h4>
              <p class="product-description">{{ order.product?.description || '暂无描述' }}</p>
              <div class="product-meta">
                <span class="price">¥{{ order.totalPrice }}</span>
                <span class="travelers">{{ order.travelersCount }}人</span>
                <span class="date">{{ formatDate(order.travelDate) }}</span>
              </div>
            </div>
            
            <div class="order-actions">
              <el-button 
                v-if="order.status === 'PENDING'" 
                type="primary" 
                size="small"
                @click="handlePay(order)"
              >
                立即支付
              </el-button>
              
              <el-button 
                v-if="order.status === 'PENDING'" 
                type="danger" 
                size="small"
                @click="handleCancel(order)"
              >
                取消订单
              </el-button>
              
              <el-button 
                type="info" 
                size="small"
                @click="viewProduct(order.product)"
              >
                查看产品
              </el-button>
              
              <el-button 
                v-if="order.status === 'COMPLETED'" 
                type="success" 
                size="small"
                @click="handleReview(order)"
              >
                评价
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-orders">
        <el-empty description="暂无订单数据">
          <router-link to="/products" class="btn btn-primary">浏览产品</router-link>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import orderService from '../services/orderService'

export default {
  name: 'Orders',
  components: {
    Refresh
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const orders = ref([])
    const loading = ref(false)
    const selectedStatus = ref('')
    
    const currentUser = computed(() => store.getters.currentUser)
    
    const loadOrders = async () => {
      if (!currentUser.value) {
        ElMessage.warning('请先登录')
        return
      }
      
      loading.value = true
      try {
        const response = await orderService.getOrdersByUserId(currentUser.value.id)
        let filteredOrders = response.data
        
        if (selectedStatus.value) {
          filteredOrders = filteredOrders.filter(order => order.status === selectedStatus.value)
        }
        
        orders.value = filteredOrders
      } catch (error) {
        ElMessage.error('加载订单失败')
        console.error('加载订单失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return '未设置'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }
    
    const getStatusType = (status) => {
      const types = {
        PENDING: 'warning',
        PAID: 'primary',
        CONFIRMED: 'success',
        COMPLETED: 'success',
        CANCELLED: 'danger'
      }
      return types[status] || 'info'
    }
    
    const getStatusLabel = (status) => {
      const labels = {
        PENDING: '待支付',
        PAID: '已支付',
        CONFIRMED: '已确认',
        COMPLETED: '已完成',
        CANCELLED: '已取消'
      }
      return labels[status] || status
    }
    
    const handlePay = (order) => {
      ElMessage.success('支付功能开发中...')
    }
    
    const handleCancel = async (order) => {
      try {
        await ElMessageBox.confirm('确定要取消这个订单吗？', '确认取消', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await orderService.updateOrderStatus(order.id, 'CANCELLED')
        ElMessage.success('订单已取消')
        loadOrders()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('取消订单失败')
        }
      }
    }
    
    const viewProduct = (product) => {
      if (product) {
        router.push(`/products/${product.id}`)
      }
    }
    
    const handleReview = (order) => {
      ElMessage.success('评价功能开发中...')
    }
    
    onMounted(() => {
      loadOrders()
    })
    
    return {
      orders,
      loading,
      selectedStatus,
      loadOrders,
      formatDate,
      getStatusType,
      getStatusLabel,
      handlePay,
      handleCancel,
      viewProduct,
      handleReview
    }
  }
}
</script>

<style scoped>
.orders-page {
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

.filters-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.filter-options {
  flex: 1;
}

.refresh-btn {
  margin-left: 1rem;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.order-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.order-card:hover {
  transform: translateY(-2px);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e4e7ed;
}

.order-info h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.2rem;
}

.order-date {
  color: #666;
  margin: 0.5rem 0 0 0;
}

.order-content {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 1.5rem;
  align-items: center;
}

.product-info h4 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
}

.product-description {
  color: #666;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.product-meta {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.price {
  font-weight: bold;
  color: #e74c3c;
}

.travelers,
.date {
  color: #666;
  background: #f8f9fa;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
}

.order-actions {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.loading {
  padding: 2rem;
}

.no-orders {
  text-align: center;
  padding: 3rem;
}

@media (max-width: 768px) {
  .filters-section {
    flex-direction: column;
    gap: 1rem;
  }
  
  .order-header {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .order-content {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .order-actions {
    flex-direction: row;
    flex-wrap: wrap;
  }
  
  .product-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
}
</style>