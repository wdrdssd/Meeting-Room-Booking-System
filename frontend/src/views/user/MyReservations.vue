<template>
    <div class="m-r-box">
        <el-tabs v-model="activeTab" @tab-click="handleClick">
            <el-tab-pane label="进行中" name="ongoing"></el-tab-pane>
            <el-tab-pane label="待开始" name="upcoming"></el-tab-pane>
            <el-tab-pane label="已完成" name="completed"></el-tab-pane>
            <el-tab-pane label="已取消" name="cancelled"></el-tab-pane>
        </el-tabs>
        <el-table :data="reservations" style="width: 100%" >
            <el-table-column  label="会议室" width="120">
                <template slot-scope="scope">
                    <strong>{{ scope.row.roomName }}</strong>
                </template>
            </el-table-column>
            <el-table-column  label="日期" width="120">
                <template slot-scope="scope">
                    {{ scope.row.date }}
                </template>
            </el-table-column>
            <el-table-column   label="时间" width="180">
                <template slot-scope="scope">
                    {{ scope.row.startTime }} - {{scope.row.endTime}}
                </template>
            </el-table-column>
            <el-table-column  label="主题" min-width="200">
                <template slot-scope="scope">
                    <span>{{ scope.row.title }}</span>
                    <el-tag size="mini" :type="getStatusType(scope.row.status)" style="margin-left: 10px;">
                        {{ getStatusText(scope.row.status) }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column  label="操作" width="200" fixed="right">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.status === 'ongoing'|| scope.row.status === 'upcoming' " 
                        type="text" size="small" 
                        @click="cancelReservation(scope.row)">取消预约
                    </el-button>
                    <el-button type="text"
                        v-if="scope.row.status ==='completed'"
                        size="small"
                        @click="viewDetail(scope.row)">查看详细
                    </el-button>
                    <el-button type="text"
                        v-if="scope.row.status === 'cancelled'"
                        size="small"
                        @click="rebook(scope.row)">重新预约
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination 
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="margin-top: 20px; text-align: right;">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name:'MyReservations',
        data() {
            return {
                activeTab:'ongoing',
                currentPage:1,
                pageSize:10,
                total:0,
                tableKey: 0, 
                reservations: [
        {
          id: 1,  
          roomName: 'A201', 
          date: '2024-01-20',  
          startTime: '10:00',  
          endTime: '11:00',   
          title: '产品需求评审',  
          status: 'ongoing'     
        },
        {
          id: 2,
          roomName: 'B301',
          date: '2024-01-20',
          startTime: '14:00',
          endTime: '15:30',
          title: '技术分享会',
          status: 'upcoming'    
        },
        {
          id: 3,
          roomName: 'C101',
          date: '2024-01-19',
          startTime: '09:00',
          endTime: '10:00',
          title: '晨会',
          status: 'completed'   
        },
        {
          id: 4,
          roomName: 'A201',
          date: '2024-01-18',
          startTime: '15:00',
          endTime: '16:00',
          title: '面试',
          status: 'cancelled'  
        }
      ]
            }
        },
        methods:{
            handleClick(tab){
                console.log('切换到标签',tab.name)
                this.currentPage=1
                this.loadData()
            },
            loadData(){
                console.log('加载数据',this.activeTab)
            },
            handleSizeChange(val){
                this.pageSize = val
                this.loadData()
            },
            handleCurrentChange(val){
                this.currentPage = val
                this.loadData()
            },
            cancelReservation(row){
                this.$confirm('确定要取消这个预约吗?','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    console.log('取消预约',row)
                    this.$message.success('取消成功')
                    this.loadData()
                }).catch(()=>{

                })
            },
            viewDetail(row){
                this.$message.info('查看详细:'+ row.id)
            },
            rebook(row){
                this.$message.info('重新预约' + row.id)
            },
            getStatusType(status){
                const types ={
                    'ongoing': 'success',   
                    'upcoming': 'warning',  
                    'completed': 'info',   
                    'cancelled': 'danger'
                }
                return types[status] || 'info'
            },
            getStatusText(status){
                const texts = {
                    'ongoing': '进行中',
                    'upcoming': '待开始',
                    'completed': '已完成',
                    'cancelled': '已取消'
                }
                return texts[status] || status
            },
            
        },
        created(){
                this.loadData()
            }
        
    }
</script>

<style>
.m-r-box{
    background-color: white;
    padding: 20px;
    border-radius: 4px;
}
</style>