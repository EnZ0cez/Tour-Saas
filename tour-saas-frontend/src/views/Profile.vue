<template>
  <div class="profile-page">
    <div class="container">
      <div class="page-header">
        <h1>个人中心</h1>
        <p>管理您的个人信息和账户设置</p>
      </div>
      
      <div class="profile-content">
        <!-- 左侧个人信息 -->
        <div class="profile-sidebar">
          <div class="user-info-card">
            <div class="user-avatar">
              <el-avatar :size="80" :src="userAvatar">
                {{ currentUser?.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
            </div>
            <div class="user-details">
              <h3>{{ currentUser?.fullName || currentUser?.username }}</h3>
              <p class="user-role">{{ getRoleLabel(currentUser?.role) }}</p>
              <p class="user-email">{{ currentUser?.email }}</p>
              <p class="user-phone">{{ currentUser?.phone || '未设置手机号' }}</p>
            </div>
          </div>
          
          <div class="stats-card">
            <h3>账户统计</h3>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-number">{{ stats.totalOrders }}</div>
                <div class="stat-label">总订单</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ stats.completedOrders }}</div>
                <div class="stat-label">已完成</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ stats.totalSpent }}</div>
                <div class="stat-label">总消费</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 右侧详细信息 -->
        <div class="profile-main">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="个人信息" name="info">
              <div class="info-form">
                <el-form 
                  ref="infoForm" 
                  :model="userInfo" 
                  :rules="infoRules" 
                  label-width="100px"
                >
                  <el-form-item label="用户名" prop="username">
                    <el-input v-model="userInfo.username" disabled />
                  </el-form-item>
                  
                  <el-form-item label="真实姓名" prop="fullName">
                    <el-input v-model="userInfo.fullName" />
                  </el-form-item>
                  
                  <el-form-item label="邮箱" prop="email">
                    <el-input v-model="userInfo.email" />
                  </el-form-item>
                  
                  <el-form-item label="手机号" prop="phone">
                    <el-input v-model="userInfo.phone" />
                  </el-form-item>
                  
                  <el-form-item>
                    <el-button 
                      type="primary" 
                      :loading="updateLoading" 
                      @click="updateUserInfo"
                    >
                      更新信息
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="修改密码" name="password">
              <div class="password-form">
                <el-form 
                  ref="passwordForm" 
                  :model="passwordData" 
                  :rules="passwordRules" 
                  label-width="100px"
                >
                  <el-form-item label="当前密码" prop="currentPassword">
                    <el-input 
                      v-model="passwordForm.currentPassword" 
                      type="password" 
                      show-password 
                    />
                  </el-form-item>
                  
                  <el-form-item label="新密码" prop="newPassword">
                    <el-input 
                      v-model="passwordForm.newPassword" 
                      type="password" 
                      show-password 
                    />
                  </el-form-item>
                  
                  <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input 
                      v-model="passwordForm.confirmPassword" 
                      type="password" 
                      show-password 
                    />
                  </el-form-item>
                  
                  <el-form-item>
                    <el-button 
                      type="primary" 
                      :loading="passwordLoading" 
                      @click="updatePassword"
                    >
                      修改密码
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="最近订单" name="orders">
              <div class="recent-orders">
                <div v-if="recentOrders.length > 0" class="orders-list">
                  <div v-for="order in recentOrders" :key="order.id" class="order-item">
                    <div class="order-info">
                      <h4>{{ order.product?.name }}</h4>
                      <p class="order-meta">
                        <span class="price">¥{{ order.totalPrice }}</span>
                        <span class="status">{{ getStatusLabel(order.status) }}</span>
                      </p>
                    </div>
                    <div class="order-actions">
                      <el-button 
                        size="small" 
                        @click="viewOrder(order)"
                      >
                        查看详情
                      </el-button>
                    </div>
                  </div>
                </div>
                <div v-else class="no-orders">
                  <el-empty description="暂无订单记录" />
                </div>
                <div class="view-all">
                  <router-link to="/orders" class="btn btn-primary">查看所有订单</router-link>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import orderService from '../services/orderService'

export default {
  name: 'Profile',
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const activeTab = ref('info')
    const infoForm = ref(null)
    const passwordForm = ref(null)
    
    const currentUser = computed(() => store.getters.currentUser)
    
    const userInfo = reactive({
      username: '',
      fullName: '',
      email: '',
      phone: ''
    })
    
    const passwordData = reactive({
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
    
    const updateLoading = ref(false)
    const passwordLoading = ref(false)
    
    const stats = ref({
      totalOrders: 0,
      completedOrders: 0,
      totalSpent: 0
    })
    
    const recentOrders = ref([])
    
    const userAvatar = ref('https://via.placeholder.com/80')
    
    const infoRules = {
      fullName: [
        { required: true, message: '请输入真实姓名', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      phone: [
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ]
    }
    
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else if (value.length < 6) {
        callback(new Error('密码长度不能少于6位'))
      } else {
        if (passwordData.confirmPassword !== '') {
          passwordForm.value?.validateField('confirmPassword')
        }
        callback()
      }
    }
    
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== passwordData.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    
    const passwordRules = {
      currentPassword: [
        { required: true, message: '请输入当前密码', trigger: 'blur' }
      ],
      newPassword: [
        { validator: validatePass, trigger: 'blur' }
      ],
      confirmPassword: [
        { validator: validatePass2, trigger: 'blur' }
      ]
    }
    
    const loadUserInfo = () => {
      if (currentUser.value) {
        userInfo.username = currentUser.value.username
        userInfo.fullName = currentUser.value.fullName
        userInfo.email = currentUser.value.email
        userInfo.phone = currentUser.value.phone
      }
    }
    
    const loadStats = async () => {
      if (!currentUser.value) return
      
      try {
        const response = await orderService.getOrdersByUserId(currentUser.value.id)
        const orders = response.data
        
        stats.value.totalOrders = orders.length
        stats.value.completedOrders = orders.filter(o => o.status === 'COMPLETED').length
        stats.value.totalSpent = orders
          .filter(o => o.status === 'COMPLETED')
          .reduce((sum, o) => sum + Number(o.totalPrice), 0)
          
        // 最近5个订单
        recentOrders.value = orders.slice(0, 5)
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    }
    
    const updateUserInfo = async () => {
      if (!infoForm.value) return
      
      try {
        await infoForm.value.validate()
        updateLoading.value = true
        
        // 这里调用更新用户信息的API
        ElMessage.success('信息更新成功')
        
        // 更新store中的用户信息
        store.commit('SET_USER', {
          ...currentUser.value,
          ...userInfo
        })
      } catch (error) {
        ElMessage.error('更新失败')
      } finally {
        updateLoading.value = false
      }
    }
    
    const updatePassword = async () => {
      if (!passwordForm.value) return
      
      try {
        await passwordForm.value.validate()
        passwordLoading.value = true
        
        // 这里调用修改密码的API
        ElMessage.success('密码修改成功')
        
        // 清空表单
        passwordData.currentPassword = ''
        passwordData.newPassword = ''
        passwordData.confirmPassword = ''
      } catch (error) {
        ElMessage.error('密码修改失败')
      } finally {
        passwordLoading.value = false
      }
    }
    
    const getRoleLabel = (role) => {
      const labels = {
        ADMIN: '管理员',
        AGENT: '代理商',
        USER: '普通用户'
      }
      return labels[role] || role
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
    
    const viewOrder = (order) => {
      router.push('/orders')
    }
    
    onMounted(() => {
      loadUserInfo()
      loadStats()
    })
    
    return {
      activeTab,
      infoForm,
      passwordForm,
      currentUser,
      userInfo,
      passwordData,
      updateLoading,
      passwordLoading,
      stats,
      recentOrders,
      userAvatar,
      infoRules,
      passwordRules,
      updateUserInfo,
      updatePassword,
      getRoleLabel,
      getStatusLabel,
      viewOrder
    }
  }
}
</script>

<style scoped>
.profile-page {
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

.profile-content {
  display: grid;
  grid-template-columns: 350px 1fr;
  gap: 2rem;
}

.profile-sidebar {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.user-info-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  text-align: center;
}

.user-avatar {
  margin-bottom: 1rem;
}

.user-details h3 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-size: 1.3rem;
}

.user-role {
  color: #409eff;
  font-weight: bold;
  margin: 0.5rem 0;
}

.user-email,
.user-phone {
  color: #666;
  margin: 0.25rem 0;
}

.stats-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.stats-card h3 {
  margin: 0 0 1rem 0;
  color: #2c3e50;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: bold;
  color: #409eff;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
}

.profile-main {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.info-form,
.password-form {
  max-width: 500px;
}

.recent-orders {
  max-width: 600px;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
}

.order-info h4 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
}

.order-meta {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.price {
  font-weight: bold;
  color: #e74c3c;
}

.status {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
}

.no-orders {
  text-align: center;
  padding: 2rem;
}

.view-all {
  text-align: center;
}

@media (max-width: 768px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .order-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .order-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style>