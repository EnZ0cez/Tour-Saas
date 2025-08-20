<template>
  <div class="products-page">
    <div class="container">
      <div class="page-header">
        <h1>旅游产品</h1>
        <p>发现精选旅游产品，开启美好旅程</p>
      </div>
      
      <!-- 搜索和筛选 -->
      <div class="filters-section">
        <div class="search-bar">
          <el-input 
            v-model="searchKeyword" 
            placeholder="搜索产品..."
            size="large"
            prefix-icon="Search"
            @input="handleSearch"
          />
        </div>
        
        <div class="filter-options">
          <el-select v-model="selectedType" placeholder="产品类型" size="large" @change="handleTypeChange">
            <el-option label="全部类型" value="" />
            <el-option label="国内游" value="DOMESTIC" />
            <el-option label="国际游" value="INTERNATIONAL" />
            <el-option label="邮轮" value="CRUISE" />
            <el-option label="酒店" value="HOTEL" />
            <el-option label="门票" value="TICKET" />
          </el-select>
        </div>
      </div>
      
      <!-- 产品列表 -->
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="products.length > 0" class="products-grid">
        <div v-for="product in products" :key="product.id" class="product-card">
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
            <div class="product-actions">
              <router-link :to="'/products/' + product.id" class="btn btn-primary">查看详情</router-link>
              <button 
                v-if="isAuthenticated" 
                @click="handleBook(product)" 
                class="btn btn-secondary"
              >
                立即预订
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-products">
        <el-empty description="暂无产品数据" />
      </div>
      
      <!-- 分页 -->
      <div v-if="products.length > 0" class="pagination">
        <el-pagination 
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[12, 24, 36]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import productService from '../services/productService'

export default {
  name: 'Products',
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const products = ref([])
    const loading = ref(false)
    const searchKeyword = ref('')
    const selectedType = ref('')
    const currentPage = ref(1)
    const pageSize = ref(12)
    const total = ref(0)
    
    const isAuthenticated = computed(() => store.getters.isAuthenticated)
    
    const loadProducts = async () => {
      loading.value = true
      try {
        let response
        if (searchKeyword.value) {
          response = await productService.searchProducts(searchKeyword.value)
        } else if (selectedType.value) {
          response = await productService.getProductsByType(selectedType.value)
        } else {
          response = await productService.getAllProducts()
        }
        
        products.value = response.data
        total.value = response.data.length
      } catch (error) {
        ElMessage.error('加载产品失败')
        console.error('加载产品失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const handleSearch = () => {
      currentPage.value = 1
      loadProducts()
    }
    
    const handleTypeChange = () => {
      currentPage.value = 1
      loadProducts()
    }
    
    const handleSizeChange = (size) => {
      pageSize.value = size
      loadProducts()
    }
    
    const handleCurrentChange = (page) => {
      currentPage.value = page
      loadProducts()
    }
    
    const handleBook = (product) => {
      router.push(`/products/${product.id}`)
    }
    
    onMounted(() => {
      loadProducts()
    })
    
    return {
      products,
      loading,
      searchKeyword,
      selectedType,
      currentPage,
      pageSize,
      total,
      isAuthenticated,
      handleSearch,
      handleTypeChange,
      handleSizeChange,
      handleCurrentChange,
      handleBook
    }
  }
}
</script>

<style scoped>
.products-page {
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
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.search-bar {
  flex: 1;
  min-width: 300px;
}

.filter-options {
  min-width: 200px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
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
  font-size: 1.2rem;
}

.product-description {
  color: #666;
  margin-bottom: 1rem;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.price {
  font-size: 1.3rem;
  font-weight: bold;
  color: #e74c3c;
}

.duration {
  color: #666;
  background: #f8f9fa;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
}

.product-actions {
  display: flex;
  gap: 0.5rem;
}

.product-actions .btn {
  flex: 1;
  text-align: center;
  padding: 0.5rem 1rem;
  text-decoration: none;
  border: none;
  border-radius: 4px;
  cursor: pointer;
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

.loading {
  padding: 2rem;
}

.no-products {
  text-align: center;
  padding: 3rem;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .filters-section {
    flex-direction: column;
  }
  
  .search-bar {
    min-width: auto;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .product-actions {
    flex-direction: column;
  }
}
</style>