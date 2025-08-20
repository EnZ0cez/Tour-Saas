import api from './api'

export default {
  // 创建订单
  createOrder(orderData) {
    const params = new URLSearchParams()
    params.append('userId', orderData.userId)
    params.append('productId', orderData.productId)
    params.append('travelersCount', orderData.travelersCount)
    params.append('travelDate', orderData.travelDate)
    
    return api.post('/orders', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  },
  
  // 获取用户订单
  getOrdersByUserId(userId) {
    return api.get(`/orders/user/${userId}`)
  },
  
  // 获取订单详情
  getOrderById(id) {
    return api.get(`/orders/${id}`)
  },
  
  // 获取所有订单（管理员功能）
  getAllOrders() {
    return api.get('/orders')
  },
  
  // 更新订单状态
  updateOrderStatus(id, status) {
    return api.put(`/orders/${id}/status`, null, {
      params: { status }
    })
  }
}