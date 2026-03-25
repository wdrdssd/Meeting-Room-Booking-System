<template>
  <el-container class="admin-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <h2>管理后台</h2>
      </div>
      <el-menu :default-active="activeMenu" class="menu" router background-color="#304156" text-color="#fff" active-text-color="#ffd046">
        <el-menu-item index="/admin/approvals">
          <i class="el-icon-check"></i>
          <span>预约审批</span>
        </el-menu-item>
        <el-menu-item index="/admin/rooms">
          <i class="el-icon-office-building"></i>
          <span>会议室管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/statistics">
          <i class="el-icon-data-line"></i>
          <span>数据统计</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span class="page-title">{{pageTitle}}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              {{ username }}<i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="main">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'AdminHomepage',
  data() {
    return {
      username: '',
      activeMenu: '/admin/approvals'
    }
  },
  methods: {
    getUserInfo() {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        const user = JSON.parse(userStr)
        this.username = user.name || user.username || '管理员'
      }
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.logout()
      } 
    },
    logout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        this.$message.success('退出成功')
        this.$router.push('/login')
      }).catch(() => {})
    }
  },
  computed: {
    pageTitle() {
        const path = this.$route.path
        if (path.includes('approvals')) return '预约审批'
        if (path.includes('rooms')) return '会议室管理'
        if (path.includes('statistics')) return '数据统计'
        return '管理后台'
    }
  },
  watch: {
    '$route.path': {
      handler(path) {
        this.activeMenu = path
      },
      immediate: true
    }
  },
  created() {
    this.getUserInfo()
  }
}
</script>

<style scoped>
.admin-container {
  height: 100vh;
}
.aside {
  background-color: #304156;
  overflow: hidden;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  background-color: #1f2d3a;
  color: white;
  font-size: 16px;
}
.menu {
  border-right: none;
}
.header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}
.header-left {
  display: flex;
  align-items: center;
}
.page-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}
.user-info {
  cursor: pointer;
  color: #333;
}
.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>