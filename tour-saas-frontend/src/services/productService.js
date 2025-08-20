import api from './api'

export default {
  // 获取所有产品
  getAllProducts() {
    return api.get('/products')
  },
  
  // 获取产品详情
  getProductById(id) {
    return api.get(`/products/${id}`)
  },
  
  // 按类型获取产品
  getProductsByType(type) {
    return api.get(`/products/type/${type}`)
  },
  
  // 搜索产品
  searchProducts(keyword) {
    return api.get('/products/search', { params: { keyword } })
  },
  
  // 创建产品（管理员功能）
  createProduct(product) {
    return api.post('/products', product)
  },
  
  // 更新产品（管理员功能）
  updateProduct(id, product) {
    return api.put(`/products/${id}`, product)
  },
  
  // 删除产品（管理员功能）
  deleteProduct(id) {
    return api.delete(`/products/${id}`)
  }
}