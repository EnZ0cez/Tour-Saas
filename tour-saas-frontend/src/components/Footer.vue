<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>TourSAAS</h3>
          <p>智慧旅游SaaS平台，为中小型旅行社提供完整的数字化解决方案</p>
        </div>
        
        <div class="footer-section">
          <h4>快速链接</h4>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/products">旅游产品</router-link></li>
            <li><router-link to="/recommendations">智能推荐</router-link></li>
            <li><router-link to="/orders">我的订单</router-link></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h4>关于我们</h4>
          <ul>
            <li><a href="#">公司介绍</a></li>
            <li><a href="#">联系我们</a></li>
            <li><a href="#">服务条款</a></li>
            <li><a href="#">隐私政策</a></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h4>联系方式</h4>
          <ul>
            <li>📧 enzochen617@gmail.com</li>
            <li>🌐 GitHub: EnZ0cez</li>
            <li>📱 问题反馈</li>
          </ul>
        </div>
      </div>
      
      <div class="footer-bottom">
        <p>&copy; 2025 TourSAAS. All rights reserved. | 智慧旅游SaaS平台</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
.footer {
  background: #2c3e50;
  color: white;
  padding: 3rem 0 1rem;
  margin-top: auto;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.footer-section h3 {
  margin: 0 0 1rem 0;
  color: #409eff;
}

.footer-section h4 {
  margin: 0 0 1rem 0;
  color: #ecf0f1;
}

.footer-section p {
  line-height: 1.6;
  color: #bdc3c7;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 0.5rem;
}

.footer-section ul li a {
  color: #bdc3c7;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section ul li a:hover {
  color: #409eff;
}

.footer-bottom {
  border-top: 1px solid #34495e;
  padding-top: 1rem;
  text-align: center;
  color: #95a5a6;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    text-align: center;
  }
  
  .footer-section ul {
    text-align: left;
  }
}
</style>