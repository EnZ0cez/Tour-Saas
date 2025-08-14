<template>
  <div class="header">
    <el-row>
      <el-col :span="4">
        <div class="logo">智慧旅游SaaS平台</div>
      </el-col>
      <el-col :span="16">
        <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          @select="handleSelect"
          background-color="#409EFF"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <el-menu-item index="1">首页</el-menu-item>
          <el-menu-item index="2">旅游产品</el-menu-item>
          <el-menu-item index="3">我的订单</el-menu-item>
          <el-menu-item index="4">智能推荐</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="4">
        <div class="user-info" v-if="isLoggedIn">
          <el-dropdown @command="handleUserCommand">
            <span class="el-dropdown-link">
              {{ username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="user-info" v-else>
          <el-button @click="goToLogin" type="primary" size="small">登录</el-button>
          <el-button @click="goToRegister" type="success" size="small">注册</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { computed } from 'vue'

export default {
  name: 'Header',
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const isLoggedIn = computed(() => store.getters.isLoggedIn)
    const username = computed(() => store.getters.username)
    
    const handleSelect = (key) => {
      switch(key) {
        case '1':
          router.push('/')
          break
        case '2':
          router.push('/products')
          break
        case '3':
          router.push('/orders')
          break
        case '4':
          router.push('/recommendations')
          break
      }
    }
    
    const handleUserCommand = (command) => {
      if (command === 'logout') {
        store.dispatch('logout')
        router.push('/login')
      } else if (command === 'profile') {
        router.push('/profile')
      }
    }
    
    const goToLogin = () => {
      router.push('/login')
    }
    
    const goToRegister = () => {
      router.push('/register')
    }
    
    return {
      activeIndex: '1',
      isLoggedIn,
      username,
      handleSelect,
      handleUserCommand,
      goToLogin,
      goToRegister
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  height: 100%;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: white;
}

.user-info {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100%;
}

.el-dropdown-link {
  cursor: pointer;
  color: #fff;
  margin-right: 20px;
}
</style>