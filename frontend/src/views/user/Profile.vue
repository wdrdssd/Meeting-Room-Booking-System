<template>
    <div class="profile-box">
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card class="user-card">
                    <div slot="header" class="card-header">
                        <span>个人信息</span>
                    </div>
                    <div class="avatar-container">
                        <el-avatar :size="100" icon="el-icon-user-solid"></el-avatar>
                        <el-button type="text" style="margin-top: 10px;">更换头像</el-button>
                    </div>
                    <div class="info-item">
                        <span class="label">姓名：</span>
                        <span class="value">{{ userInfo.name }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">部门：</span>
                        <span class="value">{{ userInfo.department }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">邮箱：</span>
                        <span class="value">{{ userInfo.email }}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">手机：</span>
                        <span class="value">{{ userInfo.phone }}</span>
                    </div>
                    <el-button type="primary" style="width: 100%;margin-top: 20px;" @click="editProfile">编辑资料</el-button>
                </el-card>
            </el-col>
            <el-col :span="16">
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-card class="stat-card">
                            <div class="stat-num">{{ statistics.total }}</div>
                            <div class="stat-label">总预约次数</div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card class="stat-card">
                            <div class="stat-num">{{ statistics.ongoing }}</div>
                            <div class="stat-label">进行中</div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card class="stat-card">
                            <div class="stat-num">{{ statistics.completed }}</div>
                            <div class="stat-label">已完成</div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-card style="margin-top: 20px;">
                    <div slot="header">
                        <span>最近预约</span>
                    </div>
                    <el-timeline >
                        <el-timeline-item 
                        v-for="(item, index) in recentReservations" 
                        :key="index" :timestamp="item.data" :type="item.type">
                            {{item.content}}
                        </el-timeline-item>
                    </el-timeline>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name:'ProfilePage',
        data() {
            return {
                userInfo:{
                    name:'张三',
                    department: '技术研发部',
                    email: 'zhangsan@company.com',
                    phone: '13800138000'
                },
                 statistics: {
                    total: 28,     
                    ongoing: 2,     
                    completed: 24   
                },
                recentReservations: [
                    {
                        date: '2024-01-20',
                        type: 'primary',  
                        content: '预约了A201会议室 10:00-11:00'
                    },
                    {
                        date: '2024-01-19',
                        type: 'success',  
                        content: '完成了产品评审会议'
                    },
                    {
                        date: '2024-01-18',
                        type: 'danger',   
                        content: '取消了B301会议室预约'
                    }
                ]
            }
        },
        methods:{
            editProfile(){
                this.$message.info('编写资料功能开发中')
            }
        }
    }
</script>

<style >
.profile-box{
    padding: 10px;
}
.user-card{
    text-align: center;
}

.avatar-container{
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
}
.info-item{
    text-align: left;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;
}
.info-item .lable{
    color: #999;
    width: 60px;
    display: inline-block;
}
.info-item .value{
    color: #333;
}
.stat-card{
    text-align: center;
    padding: 15px;
}
.stat-num{
    font-size: 32px;
    font-weight: bold;
    color: #409EFF;
}
.stat-label{
    font-size: 14px;
    color: #999;
    margin-top: 5px;
}
</style>