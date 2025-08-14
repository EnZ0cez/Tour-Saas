import api from './api'

class OrderService {
  // 创建订单
  createOrder(orderData) {
    return api.post('/orders', orderData)
  }

  // 获取用户订单
  getOrdersByUserId(userId) {
    return api.get(`/orders/user/${userId}`)
  }

  // 根据ID获取订单
  getOrderById(id) {
    return api.get(`/orders/${id}`)
  }

  // 更新订单状态
  updateOrderStatus(id, status) {
    return api.put(`/orders/${id}/status?status=${status}`)
  }

  // 获取所有订单
  getAllOrders() {
    return api.get('/orders')
  }
}

export default new OrderService()