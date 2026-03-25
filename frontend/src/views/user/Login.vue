<template>
  <div class="loginBox">
    <h1 class="title">会议室预约系统</h1>
   <el-form ref="loginForm" :model="loginForm" :rules="rules">
      <el-form-item prop="username" style="margin-top: 100px;">
        <el-input v-model="loginForm.username" placeholder="请输入账号" prefix-icon="el-icon-user"></el-input>
      </el-form-item>
      <el-form-item prop="password" style="margin-top: 50px;">
        <el-input v-model="loginForm.password" show-password placeholder="请输入密码" prefix-icon="el-icon-lock"></el-input>
      </el-form-item>
      <el-form-item style="margin-top: 50px;">
        <el-button type="primary" style="margin-left: 200px;" @click="handleLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'LoginPage',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleLogin() {      
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          try {           
            const res = await request.post('/login', {
              username: this.loginForm.username,
              password: this.loginForm.password
            })
            
            console.log('登录响应：', res)
            
            if (res.code === 200) {
              sessionStorage.setItem('token', res.data.token)
              sessionStorage.setItem('user', JSON.stringify(res.data.user))
              
              this.$message.success('登录成功')

              if (res.data.user.role === 1) {
                this.$router.push('/admin/approvals')
              } else {
                this.$router.push('/user/calendar')
              }
            } else {
              this.$message.error(res.msg || res.message || '登录失败')
            }
          } catch (error) {
            console.error('登录错误：', error)
            this.$message.error('登录失败，请检查网络或后端服务')
          }
        } else {
          console.log('表单验证失败')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.loginBox {
  width: 500px;
  height: 400px;
  margin: 150px auto;
  border: 2px solid #ccc;
  padding: 50px;
}

.title {
  text-align: center;
  margin-top: 10px;
}
</style>