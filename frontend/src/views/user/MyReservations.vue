<template>
  <div class="m-r-box">
    <el-tabs v-model="activeTab" @tab-click="handleClick">
      <el-tab-pane label="全部" name="all"></el-tab-pane>
      <el-tab-pane label="已预约" name="1"></el-tab-pane>
      <el-tab-pane label="进行中" name="2"></el-tab-pane>
      <el-tab-pane label="已完成" name="3"></el-tab-pane>
      <el-tab-pane label="待审批" name="4"></el-tab-pane>
      <el-tab-pane label="已取消" name="0"></el-tab-pane>
    </el-tabs>
    
    <el-table :data="filteredReservations" style="width: 100%" v-loading="loading">
      <el-table-column label="会议室" width="120">
        <template slot-scope="scope">
          <strong>{{ getRoomName(scope.row.roomId) }}</strong>
        </template>
      </el-table-column>
      
      <el-table-column label="日期" width="120">
        <template slot-scope="scope">
          {{ scope.row.reserveDate }}
        </template>
      </el-table-column>
      
      <el-table-column label="时间" width="180">
        <template slot-scope="scope">
          {{ scope.row.startTime }} - {{ scope.row.endTime }}
        </template>
      </el-table-column>
      
      <el-table-column label="主题" min-width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
          <el-tag size="mini" :type="getStatusType(scope.row.status)" style="margin-left: 10px;">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button 
            v-if="scope.row.status === '1'" 
            type="text" 
            size="small" 
            @click="cancelReservation(scope.row)">
            取消预约
          </el-button>
          <span v-if="scope.row.status === '2'" style="color: #999;">进行中</span>
          <el-button 
            v-if="scope.row.status === '3'" 
            type="text" 
            size="small" 
            @click="viewDetail(scope.row)">
            查看详情
          </el-button>
          <el-button 
            v-if="scope.row.status === '4'" 
            type="text" 
            size="small" 
            @click="cancelReservation(scope.row)">
            取消申请
          </el-button>
          <el-button 
            v-if="scope.row.status === '0'" 
            type="text" 
            size="small" 
            @click="rebook(scope.row)">
            重新预约
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
      :total="totalCount"
      style="margin-top: 20px; text-align: right;">
    </el-pagination>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'MyReservations',
  
  data() {
    return {
      activeTab: 'all',
      currentPage: 1,
      pageSize: 10,
      loading: false,
      allReservations: [],
      rooms: []
    }
  },
  
  computed: {
    filteredReservations() {
      let list = [...this.allReservations]
      
      if (this.activeTab !== 'all') {
        list = list.filter(item => item.status === this.activeTab)
      }

      list.sort((a, b) => new Date(b.reserveDate) - new Date(a.reserveDate))
  
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return list.slice(start, end)
    },

    totalCount() {
      let list = [...this.allReservations]
      if (this.activeTab !== 'all') {
        list = list.filter(item => item.status === this.activeTab)
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
      return room ? (room.roomName || room.name) : '未知会议室'
    },
    
    async loadReservations() {
      this.loading = true
      try {
        const res = await request.get('/reservations/my')
        if (res.code === 200) {
          this.allReservations = res.data
        } else {
          this.$message.error(res.msg || '加载失败')
        }
      } catch (error) {
        console.error('加载预约失败', error)
        this.$message.error('加载预约失败')
      } finally {
        this.loading = false
      }
    },
    
    handleClick(tab) {
      console.log('切换到标签：', tab.name)
      this.currentPage = 1
    },
    
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
    },
    
    handleCurrentChange(val) {
      this.currentPage = val
    },
    
    async cancelReservation(row) {
      const statusText = this.getStatusText(row.status)
      const confirmMsg = statusText === '待审批' ? '确定要取消这个申请吗？' : '确定要取消这个预约吗？'
      
      this.$confirm(confirmMsg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await request.put(`/reservations/${row.id}/cancel`)
          if (res.code === 200) {
            this.$message.success('取消成功')
            this.loadReservations()
          } else {
            this.$message.error(res.msg || '取消失败')
          }
        } catch (error) {
          this.$message.error('取消失败')
        }
      }).catch(() => {})
    },
    
    viewDetail(row) {
      this.$alert(
        `<div style="padding: 10px">
          <p><strong>会议主题：</strong>${row.title}</p>
          <p><strong>会议室：</strong>${this.getRoomName(row.roomId)}</p>
          <p><strong>日期：</strong>${row.reserveDate}</p>
          <p><strong>时间：</strong>${row.startTime} - ${row.endTime}</p>
          <p><strong>状态：</strong>${this.getStatusText(row.status)}</p>
          <p><strong>预约人：</strong>${row.userName || '未知'}</p>
          <p><strong>备注：</strong>${row.remark || '无'}</p>
        </div>`, 
        '预约详情', 
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '关闭'
        }
      )
    },
    
    rebook(row) {
        this.$router.push({
            path: '/user/calendar',
            query: {
                date: row.reserveDate,      
                roomId: row.roomId,          
                title: row.title,            
                startTime: row.startTime,    
                endTime: row.endTime        
            }
        })
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
    }
  },
  
  created() {
    this.loadRooms()
    this.loadReservations()
  }
}
</script>

<style scoped>
.m-r-box {
  background-color: white;
  padding: 20px;
  border-radius: 4px;
}
</style>