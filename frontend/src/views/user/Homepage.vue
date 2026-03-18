<template>
    <el-container class="homepage-container">
        <el-aside width="200px" class="aside">
            <div class="logo">
                <h2>会议室系统</h2>
            </div>
            <el-menu :default-active="activeMenu" class="menu" router background-color="#304156" text-color="#fff" active-text-color="#ffd046">
                <el-menu-item index="/user/calender">
                    <i class="el-icon-date"></i>
                    <span>日历视图</span>
                </el-menu-item>
                <el-menu-item index="/user/my-reservations">
                    <i class="el-icon-tickets"></i>
                    <span>我的预约</span>
                </el-menu-item>
                <el-menu-item index="/user/myself">
                    <i class="el-icon-user"></i>
                    <span>个人中心</span>
                </el-menu-item>
            </el-menu>
        </el-aside>
        <el-container>
            <el-header class="header">
                <div class="header-left">
                    <i class="el-icon-s-fold" @click="toggleMenu"></i>
                    <span class="page-title"> {{ pageTitle }}</span>
                </div>
                <div class="header-right">
                    <el-dropdown @command="handleCommand">
                        <span class="user-info">
                            {{ username }}<i class="el-icon-arrow-down"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="myself">个人中心</el-dropdown-item>
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
        name:'userHomepage',
        data() {
            return {
                username:'张三',
                activeMenu:'/user/calendar'
            }
        },
        computed:{
            pageTitle(){
                const path = this.$route.path
                if(path.includes('calender'))return'日历视图'
                if(path.includes('my-reservations'))return'我的预约'
                if(path.includes('myself'))return'个人中心'
                return '会议室预约系统'
            }
        },
        methods:{
            toggleMenu(){
                console.log('折叠菜单')
            },
            handleCommand(command){
                if(command == 'logout'){
                    this.logout()
                }else if(command == 'myself'){
                    this.$router.push('/user/myself')
                }
            },
            logout(){
                this.$confirm('确定要退出登录吗?','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    localStorage.removeItem('token')
                    localStorage.removeItem('user')
                    this.$message.success('退出成功')
                    this.$router.push('/login')
                }).catch(()=>{

                })
            },
        },
         watch:{
                '$route.path':{
                    handler(path){
                        this.activeMenu = path
                    }
                },
                immediate:true
            }
    }
</script>

<style scoped >
.homepage-container{
    height: 100vh;
}
.aside{
    background-color: #304156;
    overflow: hidden;
}
.logo{
    height: 60px;
    line-height: 60px;
    text-align: center;
    background-color: #1f2d3a;
    color: white;
    font-size: 16px;
}
.menu{
    border-right: none;
}
.header{
    background-color: white;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
}
.header-left{
    display: flex;
    align-items: center;
}
.header-left i{
    font-size: 20px;
    margin-right: 15px;
    cursor: pointer;
}
.page-title{
    font-size: 16px;
    font-weight: bold;
    color: #333;
}
.main{
    background-color: #f0f2f5;
    padding: 20px;
}
</style>