<template>
  <div class="statistics">
    <el-row :gutter="20">
      <el-col :span="6" v-for="stat in stats" :key="stat.label">
        <el-card class="stat-card">
          <div class="stat-num">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header">近7天预约趋势</div>
          <div id="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header">会议室使用排行</div>
          <div id="roomChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from '@/utils/request'
import * as echarts from 'echarts'

export default {
  name: 'StatisticsPage',
  data() {
    return {
      stats: [
        { label: '总预约数', value: 0 },
        { label: '进行中', value: 0 },
        { label: '已完成', value: 0 },
        { label: '待审批', value: 0 }
      ]
    }
  },
  methods: {
    async loadStatistics() {
      try {
        const res = await request.get('/statistics/overview')
        if (res.code === 200) {
          this.stats[0].value = res.data.total
          this.stats[1].value = res.data.ongoing
          this.stats[2].value = res.data.completed
          this.stats[3].value = res.data.pending
        }
      } catch (error) {
        console.error('加载统计失败', error)
      }
    },
    
    async loadTrend() {
      try {
        const res = await request.get('/statistics/trend')
        if (res.code === 200) {
          this.renderTrendChart(res.data)
        }
      } catch (error) {
        console.error('加载趋势失败', error)
      }
    },
    
    async loadRoomRank() {
      try {
        const res = await request.get('/statistics/rooms')
        if (res.code === 200) {
          this.renderRoomChart(res.data)
        }
      } catch (error) {
        console.error('加载排行失败', error)
      }
    },
    
    renderTrendChart(data) {
      const chartDom = document.getElementById('trendChart')
      if (!chartDom) return
      
      // 销毁已存在的实例
      let chart = echarts.getInstanceByDom(chartDom)
      if (chart) {
        chart.dispose()
      }
      
      // 创建新实例
      chart = echarts.init(chartDom)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.dates },
        yAxis: { type: 'value', name: '预约次数' },
        series: [{
          name: '预约次数',
          type: 'line',
          data: data.counts,
          smooth: true,
          areaStyle: { opacity: 0.3 },
          lineStyle: { color: '#409EFF', width: 2 },
          itemStyle: { color: '#409EFF' }
        }]
      })
    },
    
    renderRoomChart(data) {
      const chartDom = document.getElementById('roomChart')
      if (!chartDom) return
      
      // 销毁已存在的实例
      let chart = echarts.getInstanceByDom(chartDom)
      if (chart) {
        chart.dispose()
      }
      
      // 创建新实例
      chart = echarts.init(chartDom)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        xAxis: { type: 'category', data: data.names, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: '预约次数' },
        series: [{
          name: '预约次数',
          type: 'bar',
          data: data.counts,
          itemStyle: { color: '#67C23A', borderRadius: [4, 4, 0, 0] }
        }]
      })
    }
  },
  created() {
    this.loadStatistics()
    this.loadTrend()
    this.loadRoomRank()
  }
}
</script>

<style scoped>
.statistics {
  padding: 10px;
}
.stat-card {
  text-align: center;
  padding: 20px;
}
.stat-num {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}
</style>