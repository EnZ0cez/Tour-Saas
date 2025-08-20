<template>
  <header class="header">
    <div class="container">
      <div class="header-content">
        <div class="logo">
          <router-link to="/" class="logo-link">
            <h1>TourSAAS</h1>
            <span>智慧旅游平台</span>
          </router-link>
        </div>
        
        <nav class="nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/products" class="nav-link">旅游产品</router-link>
          <router-link v-if="isAuthenticated" to="/recommendations" class="nav-link">智能推荐</router-link>
          <router-link v-if="isAuthenticated" to="/orders" class="nav-link">我的订单</router-link>
        </nav>
        
        <div class="user-actions">
          <template v-if="isAuthenticated">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                {{ currentUser?.username }}
                <el-icon><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="btn btn-secondary">登录</router-link>
            <router-link to="/register" class="btn btn-primary">注册</router-link>
          </template>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'

export default {
  name: 'Header',
  components: {
    ArrowDown
  },
  setup() {
    const store = useStore()
    const router = useRouter()

    const isAuthenticated = computed(() => store.getters.isAuthenticated)
    const currentUser = computed(() => store.getters.currentUser)

    const handleCommand = (command) => {
      if (command === 'logout') {
        store.dispatch('logout')
        router.push('/')
      } else if (command === 'profile') {
        router.push('/profile')
      }
    }

    return {
      isAuthenticated,
      currentUser,
      handleCommand
    }
  }
}
</script>

<style scoped>
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem 0;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-link {
  text-decoration: none;
  color: white;
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo h1 {
  margin: 0;
  font-size: 1.8rem;
  font-weight: bold;
}

.logo span {
  font-size: 0.9rem;
  opacity: 0.8;
}

.nav {
  display: flex;
  gap: 2rem;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.3s;
}

.nav-link:hover,
.nav-link.router-link-active {
  opacity: 0.8;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: rgba(255,255,255,0.1);
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-primary {
  background-color: #409eff;
  color: white;
}

.btn-primary:hover {
  background-color: #66b1ff;
}

.btn-secondary {
  background-color: transparent;
  color: white;
  border: 1px solid white;
}

.btn-secondary:hover {
  background-color: rgba(255,255,255,0.1);
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 1rem;
  }
  
  .nav {
    gap: 1rem;
  }
  
  .user-actions {
    width: 100%;
    justify-content: center;
  }
}
</style>