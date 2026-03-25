<template>
  <div class="profile-box">
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card class="user-card">
          <div slot="header" class="card-header">
            <span>个人信息</span>
            <el-button type="text" @click="openEditDialog">修改密码</el-button>
          </div>
          
          <div class="info-item">
            <span class="label">姓名：</span>
            <span class="value">{{ userInfo.name || userInfo.username }}</span>
          </div>
          
          <div class="info-item">
            <span class="label">邮箱：</span>
            <span class="value">{{ userInfo.email || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">角色：</span>
            <span class="value">{{ userInfo.role === 1 ? '管理员' : '普通用户' }}</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="14">
        <el-card class="stat-card total-card">
          <div class="total-num">{{ statistics.total }}</div>
          <div class="total-label">总预约次数</div>
        </el-card>

        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <el-card class="stat-card ongoing-card">
              <div class="stat-num">{{ statistics.ongoing }}</div>
              <div class="stat-label">进行中</div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="stat-card completed-card">
              <div class="stat-num">{{ statistics.completed }}</div>
              <div class="stat-label">已完成</div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-dialog title="修改密码" :visible.sync="editDialogVisible" width="450px">
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password></el-input>
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password></el-input>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password></el-input>
        </el-form-item>
      </el-form>
      
      <span slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePassword" :loading="saving">确认修改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'ProfilePage',
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      }  else {
        callback()
      }
    }
    
    const validateConfirmPass = (ruel, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    
    return {
      userInfo: {
        id: null,
        name: '',
        username: '',
        email: '',
        role: 0
      },
      statistics: {
        total: 0,
        ongoing: 0,
        completed: 0
      },
      editDialogVisible: false,
      saving: false,

      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
 
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPass, trigger: 'blur' }
        ]
      }
    }
  },
  
  methods: {
    getCurrentUser() {
      const userStr = sessionStorage.getItem('user')
      if (userStr) {
        this.userInfo = JSON.parse(userStr)
      }
    },

    async loadReservations() {
      try {
        const res = await request.get('/reservations/my')
        if (res.code === 200) {
          const reservations = res.data
 
          this.statistics.total = reservations.length
          this.statistics.ongoing = reservations.filter(r => r.status === '2').length
          this.statistics.completed = reservations.filter(r => r.status === '3').length
        }
      } catch (error) {
        console.error('加载预约数据失败', error)
      }
    },

    openEditDialog() {
      this.passwordForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.editDialogVisible = true
    },

    async savePassword() {
      this.$refs.passwordForm.validate(async (valid) => {
        if (!valid) return this.saving = true
        try {
          const res = await request.put(`/users/${this.userInfo.id}/password`, {
            oldPassword: this.passwordForm.oldPassword,
            newPassword: this.passwordForm.newPassword
          })
          
          if (res.code === 200) {
            this.$message.success('密码修改成功，请重新登录')
            this.editDialogVisible = false
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            
            setTimeout(() => {
              this.$router.push('/login')
            }, 1500)
          } else {
            this.$message.error(res.msg || '修改失败')
          }
        } catch (error) {
          console.error('修改密码失败', error)
          this.$message.error('修改失败，请稍后重试')
        } finally {
          this.saving = false
        }
      })
    }
  },
  
  created() {
    this.getCurrentUser()
    this.loadReservations()
  }
}
</script>

<style scoped>
.profile-box {
  padding: 10px;
}

.user-card {
  text-align: center;
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  text-align: left;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item .label {
  display: inline-block;
  width: 60px;
  color: #909399;
  font-weight: 500;
}

.info-item .value {
  color: #333;
}

.total-card {
  text-align: center;
  padding: 30px 15px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.total-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.total-num {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 10px;
}

.total-label {
  font-size: 16px;
  opacity: 0.9;
}

.stat-card {
  text-align: center;
  padding: 20px 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.ongoing-card {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.completed-card {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stat-num {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}
</style>