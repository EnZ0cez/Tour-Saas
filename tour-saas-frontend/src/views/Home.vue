<template>
  <div class="home">
    <el-carousel height="400px">
      <el-carousel-item v-for="item in 4" :key="item">
        <h3 class="small justify-center" text="2xl">{{ item }}</h3>
      </el-carousel-item>
    </el-carousel>
    
    <div class="features">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="feature-card">
            <div class="feature-content">
              <h3>产品展示</h3>
              <p>丰富的旅游产品展示，支持多维度筛选和搜索</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card">
            <div class="feature-content">
              <h3>智能推荐</h3>
              <p>基于用户行为的智能推荐算法，提升转化率</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card">
            <div class="feature-content">
              <h3>多端管理</h3>
              <p>统一的后台管理系统，支持Web、App、小程序</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <div class="products">
      <h2>热门旅游产品</h2>
      <el-row :gutter="20">
        <el-col :span="6" v-for="product in products" :key="product.id">
          <el-card :body-style="{ padding: '0px' }" class="product-card">
            <img :src="product.imageUrl || 'https://picsum.photos/300/200?random=' + product.id" class="image">
            <div style="padding: 14px;">
              <span>{{ product.name }}</span>
              <div class="bottom">
                <span class="price">¥{{ product.price }}</span>
                <el-button type="primary" class="button" @click="viewProduct(product.id)">查看详情</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import productService from '@/services/productService'

export default {
  name: 'Home',
  setup() {
    const router = useRouter()
    const products = ref([])
    
    const viewProduct = (id) => {
      router.push(`/product/${id}`)
    }
    
    const loadProducts = async () => {
      try {
        const response = await productService.getProducts()
        products.value = response.data.slice(0, 4) // 只显示前4个产品
      } catch (error) {
        console.error('加载产品失败:', error)
      }
    }
    
    onMounted(() => {
      loadProducts()
    })
    
    return {
      products,
      viewProduct
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.features {
  margin: 40px 0;
}

.feature-card {
  height: 150px;
}

.feature-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.products h2 {
  text-align: left;
  margin-bottom: 20px;
}

.product-card {
  margin-bottom: 20px;
}

.image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.bottom {
  margin-top: 13px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
}

.button {
  padding: 5px 10px;
}
</style>