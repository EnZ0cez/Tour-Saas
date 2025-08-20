<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <div class="container">
        <div class="hero-content">
          <h1>智慧旅游SaaS平台</h1>
          <p>为中小型旅行社提供完整的数字化解决方案，整合旅游产品展示、智能推荐及多端管理功能</p>
          <div class="hero-actions">
            <router-link to="/products" class="btn btn-primary">探索产品</router-link>
            <router-link v-if="!isAuthenticated" to="/register" class="btn btn-secondary">立即注册</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features">
      <div class="container">
        <h2 class="section-title">平台特色</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">🚀</div>
            <h3>高性能架构</h3>
            <p>支持日均10,000+次并发请求，接口响应成功率99.8%</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🤖</div>
            <h3>智能推荐系统</h3>
            <p>基于用户行为的个性化推荐算法，集成大语言模型</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📱</div>
            <h3>多端支持</h3>
            <p>统一API支持Web、App、小程序等多种终端</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔒</div>
            <h3>安全可靠</h3>
            <p>JWT令牌鉴权，企业级安全防护</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Products Preview -->
    <section class="products-preview">
      <div class="container">
        <h2 class="section-title">热门产品</h2>
        <div v-if="loading" class="loading">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="products.length > 0" class="products-grid">
          <div v-for="product in products.slice(0, 6)" :key="product.id" class="product-card">
            <div class="product-image">
              <img :src="product.imageUrl || 'https://via.placeholder.com/300x200'" :alt="product.name">
            </div>
            <div class="product-info">
              <h3>{{ product.name }}</h3>
              <p class="product-description">{{ product.description }}</p>
              <div class="product-meta">
                <span class="price">¥{{ product.price }}</span>
                <span class="duration">{{ product.duration }}天</span>
              </div>
              <router-link :to="'/products/' + product.id" class="btn btn-primary">查看详情</router-link>
            </div>
          </div>
        </div>
        <div v-else class="no-products">
          <p>暂无产品数据</p>
        </div>
        <div class="text-center">
          <router-link to="/products" class="btn btn-secondary">查看更多产品</router-link>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta">
      <div class="container">
        <div class="cta-content">
          <h2>开始您的智慧旅游之旅</h2>
          <p>立即注册，体验智能推荐带来的个性化服务</p>
          <router-link v-if="!isAuthenticated" to="/register" class="btn btn-primary">立即注册</router-link>
          <router-link v-else to="/recommendations" class="btn btn-primary">查看推荐</router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import productService from '../services/productService'

export default {
  name: 'Home',
  setup() {
    const store = useStore()
    const products = ref([])
    const loading = ref(true)

    const isAuthenticated = computed(() => store.getters.isAuthenticated)

    const loadProducts = async () => {
      try {
        const response = await productService.getAllProducts()
        products.value = response.data
      } catch (error) {
        console.error('加载产品失败:', error)
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      loadProducts()
    })

    return {
      products,
      loading,
      isAuthenticated
    }
  }
}
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 6rem 0;
  text-align: center;
}

.hero-content h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.hero-content p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.features {
  padding: 4rem 0;
  background: white;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 3rem;
  color: #2c3e50;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  text-align: center;
  padding: 2rem;
  border-radius: 8px;
  background: #f8f9fa;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.feature-card h3 {
  margin-bottom: 1rem;
  color: #2c3e50;
}

.products-preview {
  padding: 4rem 0;
  background: #f8f9fa;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 1.5rem;
}

.product-info h3 {
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

.product-description {
  color: #666;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.product-meta {
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
}

.loading {
  padding: 2rem;
}

.no-products {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.cta {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 4rem 0;
  text-align: center;
}

.cta-content h2 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.cta-content p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2rem;
  }
  
  .hero-content p {
    font-size: 1rem;
  }
  
  .hero-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .features-grid,
  .products-grid {
    grid-template-columns: 1fr;
  }
}
</style>