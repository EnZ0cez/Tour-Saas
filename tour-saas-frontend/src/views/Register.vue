<template>
  <div class="register">
    <el-card class="register-card">
      <div slot="header" class="card-header">
        <span>用户注册</span>
      </div>
      <el-form :model="registerForm" :rules="rules" ref="registerForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="fullName">
          <el-input v-model="registerForm.fullName" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择角色">
            <el-option label="普通用户" value="USER"></el-option>
            <el-option label="旅行社代理" value="AGENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('registerForm')" :loading="loading">注册</el-button>
          <el-button @click="resetForm('registerForm')">重置</el-button>
          <el-button type="text" @click="goToLogin">已有账号？立即登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import authService from '@/services/authService'

export default {
  name: 'Register',
  setup() {
    const router = useRouter()
    const loading = ref(false)
    
    const registerForm = ref({
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      fullName: '',
      role: 'USER'
    })
    
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (registerForm.value.confirmPassword !== '') {
          this.$refs.registerForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== registerForm.value.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        { validator: validatePass2, trigger: 'blur' }
      ],
      fullName: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ]
    }
    
    const submitForm = async (formName) => {
      loading.value = true
      try {
        const { confirmPassword, ...userData } = registerForm.value
        const response = await authService.register(userData)
        ElMessage.success('注册成功')
        router.push('/login')
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    const resetForm = (formName) => {
      registerForm.value = {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        fullName: '',
        role: 'USER'
      }
    }
    
    const goToLogin = () => {
      router.push('/login')
    }
    
    return {
      registerForm,
      rules,
      loading,
      submitForm,
      resetForm,
      goToLogin
    }
  }
}
</script>

<style scoped>
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 120px);
}

.register-card {
  width: 500px;
}

.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}
</style>