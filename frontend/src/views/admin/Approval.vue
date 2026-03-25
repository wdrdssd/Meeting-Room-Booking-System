<template>
    <div class="approval">
        <el-card>
            <div slot="header">
                <span>预约审批</span>
                <el-radio-group v-model="statusFilter" size="small" style="float: right;">
                    <el-radio-button label="pending">待审批</el-radio-button>
                    <el-radio-button label="all">全部</el-radio-button>
                </el-radio-group>
            </div>
            <el-table :data="filteredReservations" style="width: 100%" v-loading="loading">
                <el-table-column prop="title" label="会议主题" width="200"></el-table-column>
                <el-table-column   label="会议室" width="152">
                    <template slot-scope="scope">
                        {{ getRoomName(scope.row.roomId) }}
                    </template>
                </el-table-column>
                <el-table-column label="日期" width="202">
                    <template slot-scope="scope">
                        {{ scope.row.reserveDate }}
                    </template>
                </el-table-column>
                <el-table-column label="时间" width="202">
                    <template slot-scope="scope">
                        {{ scope.row.startTime }} - {{ scope.row.endTime }}
                    </template>
                </el-table-column>
                <el-table-column label="预约人" width="180">
                    <template slot-scope="scope">
                        {{ scope.row.userName || '未知' }}
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="200">
                    <template slot-scope="scope">
                        <el-tag :type="getStatusType(scope.row.status)" size="small">
                            {{ getStatusText(scope.row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column   label="操作" width="250" >
                    <template slot-scope="scope">
                        <template v-if="scope.row.status === '4'">
                            <el-button type="success" size="small" @click="approve(scope.row,true)">通过</el-button>
                            <el-button type="danger" size="small" @click="approve(scope.row,false)">拒绝</el-button>
                        </template>
                        <span v-else>- - - - - -</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 30]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next"
                :total="total"
                style="margin-top: 20px; text-align: right;">
            </el-pagination>
        </el-card>
    </div>
</template>

<script>
import request from '@/utils/request';
    export default {
        name:'ApprovalPage',
        data() {
            return {
                allReservations:[],
                rooms:[],
                loading:false,
                statusFilter:'pending',
                currentPage:1,
                pageSize:10,
            }
        },
        computed:{
            filteredReservations(){
                let list = [...this.allReservations]
                if(this.statusFilter === 'pending'){
                    list = list.filter(item => item.status === '4')
                }
                list.sort((a, b) => new Date(b.reserveDate) - new Date(a.reserveDate))
      
                const start = (this.currentPage - 1) * this.pageSize
                const end = start + this.pageSize
                return list.slice(start, end)
            },
            totalCount() {
                let list = [...this.allReservations]
                if (this.statusFilter === 'pending') {
                    list = list.filter(item => item.status === '4')
                }
                return list.length
            }
        },
        methods: {
            async loadRooms() {
                try {
                    const res = await request.get('/rooms')
                    if (res.code === 200) {
                    this.rooms = res.data
                }
                } catch (error) {
                    console.error('加载会议室失败', error)
                }
            },
    
            getRoomName(roomId) {
                const room = this.rooms.find(r => r.id === roomId)
                return room ? room.roomName : '未知'
            },
    
            async loadReservations() {
                this.loading = true
                try {
                    const res = await request.get('/reservations/all')
                    if (res.code === 200) {
                    this.allReservations = res.data
                    }
                } catch (error) {
                    console.error('加载预约失败', error)
                    this.$message.error('加载失败')
                } finally {
                    this.loading = false
                }
            },
    
            async approve(row, isApprove) {
                const action = isApprove ? '通过' : '拒绝'
                this.$confirm(`确定要${action}预约"${row.title}"吗？`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    try {
                        const res = await request.put(`/reservations/${row.id}/approve`, null, {params: { approve: isApprove }})
                        if (res.code === 200) {
                            this.$message.success(`${action}成功`)
                            this.loadReservations()
                        } else {
                            this.$message.error(res.msg || `${action}失败`)
                        }
                    } catch (error) {
                        this.$message.error(`${action}失败`)
                    }
                }).catch(() => {})
            },
    
            getStatusType(status) {
                const types = {
                    '0': 'info',
                    '1': 'success',
                    '2': 'warning',
                    '3': 'primary',
                    '4': 'danger'
                }
                return types[status] || 'info'
            },
    
            getStatusText(status) {
                const texts = {
                    '0': '已取消',
                    '1': '已预约',
                    '2': '进行中',
                    '3': '已完成',
                    '4': '待审批'
                }
                return texts[status] || '未知'
            },
    
            handleSizeChange(val) {
                this.pageSize = val
                this.currentPage = 1
            },
    
            handleCurrentChange(val) {
                this.currentPage = val
            }
        },
        created() {
            this.loadRooms()
            this.loadReservations()
        }
    }
</script>

<style  scoped>
.approval {
  padding: 10px;
}
</style>