<template>
  <div class="box">
    <h1 class="title">会议室预约系统</h1>
    <el-form ref="LoginForm" :model="LoginForm" :rules="rules">
      <el-form-item prop="username" style="margin-top: 100px;">
        <el-input v-model="LoginForm.username" placeholder="请输入账号" prefix-icon="el-icon-user"></el-input>
      </el-form-item>
      <el-form-item prop="password" style="margin-top: 50px;">
        <el-input v-model="LoginForm.password" show-password placeholder="请输入密码" prefix-icon="el-icon-lock"></el-input>
      </el-form-item>
      <el-form-item style="margin-top: 50px;">
        <el-button type="primary" style="margin-left: 200px;" @click="yanzheng">登录</el-button>
      </el-form-item>
      </el-form>
      </div>
</template>

<script>
  export default {
    name:'LoginPage',
    data() {
      return {
        LoginForm:{
          username:'',
          password:''
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
    methods:{
      yanzheng(){
        this.$refs.LoginForm.validate(valid =>{
          if(valid){
            console.log(this.LoginForm)
            if(this.LoginForm.username == 'admin'&& this.LoginForm.password === '123456'){
              this.$message.success('管理员登录成功')
              this.$router.push('/admin/homepage')
            }else{
              this.$message.success('登录成功')
              this.$router.push('/user/homepage')
            }
          }
        })
      }
    }
  }
</script>

<style >
.box{
  width: 500px;
  height: 400px;
  margin: 150px auto;
  border: 2px solid #ccc;
  padding: 50px;
}
.box .title{
  text-align: center;
  margin-top: 10px;
}



</style>