import api from './api'

class ProductService {
  // 获取所有产品
  getProducts() {
    return api.get('/products')
  }

  // 根据ID获取产品
  getProductById(id) {
    return api.get(`/products/${id}`)
  }

  // 创建产品
  createProduct(productData) {
    return api.post('/products', productData)
  }

  // 更新产品
  updateProduct(id, productData) {
    return api.put(`/products/${id}`, productData)
  }

  // 删除产品
  deleteProduct(id) {
    return api.delete(`/products/${id}`)
  }

  // 根据类型获取产品
  getProductsByType(type) {
    return api.get(`/products/type/${type}`)
  }

  // 搜索产品
  searchProducts(keyword) {
    return api.get(`/products/search?keyword=${keyword}`)
  }
}

export default new ProductService()