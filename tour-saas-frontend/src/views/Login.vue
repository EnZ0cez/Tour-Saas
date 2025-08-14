<template>
  <div class="login">
    <el-card class="login-card">
      <div slot="header" class="card-header">
        <span>用户登录</span>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')" :loading="loading">登录</el-button>
          <el-button @click="resetForm('loginForm')">重置</el-button>
          <el-button type="text" @click="goToRegister">还没有账号？立即注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import authService from '@/services/authService'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const store = useStore()
    const loading = ref(false)
    
    const loginForm = ref({
      username: '',
      password: ''
    })
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ]
    }
    
    const submitForm = async (formName) => {
      // 这里应该是表单验证和登录逻辑
      loading.value = true
      try {
        const response = await authService.login(loginForm.value)
        const { token } = response.data
        store.dispatch('login', token)
        router.push('/')
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败，请检查用户名和密码')
      } finally {
        loading.value = false
      }
    }
    
    const resetForm = (formName) => {
      loginForm.value = {
        username: '',
        password: ''
      }
    }
    
    const goToRegister = () => {
      router.push('/register')
    }
    
    return {
      loginForm,
      rules,
      loading,
      submitForm,
      resetForm,
      goToRegister
    }
  }
}
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 120px);
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}
</style>