<template>
  <div class="calendar-box">
    <el-calendar v-model="value" class="calendar">
      <template slot="dateCell" slot-scope="{data}">
        <span :class="data.isSelected ? 'is-selected' : ''">{{ data.day.split('-').slice(2).join('-') }}</span> 
          <el-button type="text" v-if="data.type === 'current-month' && hasReservations(data.day)"
            size="mini" class="chakan-btn"
            @click="TodayDialog(data.day)">查看预约</el-button>
          <el-button v-if="data.type === 'current-month' && canReserve(data.day)" type="text" 
            size="mini" class="calendar-btn"  
            @click="openDialog(data.day)">+ 预约</el-button>
      </template>
    </el-calendar>
    
    <el-dialog title="查看预约详情" :visible.sync="TodayDialogVisible" width="600px" >
      <div v-if="currentAppointments.length > 0">
        <div class="today-card" v-for="(appointment,index) in currentAppointments" :key="index">
          <div class="today-header">
            <h3>{{ appointment.title }}</h3>
            <el-tag :type="getDisplayStatusType(appointment)" size="small">
              {{ getDisplayStatusText(appointment) }}
            </el-tag>
          </div>
          <div class="today-info">
            <div class="info-row">
              <span class="label">预约日期:</span>
              <span>{{ appointment.reserveDate || appointment.date }}</span>
            </div>
            <div class="info-row">
              <span class="label">预约时间:</span>
              <span>{{ appointment.startTime }} - {{ appointment.endTime }}</span>
            </div>
            <div class="info-row">
              <span class="label">会议室:</span>
              <span>{{ getRoomName(appointment.roomId) }}</span>
            </div>
            <div class="info-row">
              <span class="label">预约人:</span>
              <span>{{ appointment.userName || appointment.name || '未知' }}</span>
            </div>
            <div class="info-row" v-if="appointment.remark">
              <span class="label">备注:</span>
              <span>{{ appointment.remark }}</span>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="today-footer">
        <el-button type="primary" @click="TodayDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    
    <el-dialog title="会议室预约" :visible.sync="dialogVisible" width="500px" >
      <el-form ref="form" :model="form" label-width="100px" :rules="formRules">
          <el-form-item label="预约日期" prop="date">
            <el-input v-model="form.date" disabled></el-input>
          </el-form-item>
          <el-form-item label="会议主题" prop="title">
            <el-input v-model="form.title" placeholder="请输入主题"></el-input>
          </el-form-item>
          <el-form-item label="会议室" prop="roomId">
            <el-radio-group v-model="form.roomId" @change="checkConflict">
                <el-radio 
                v-for="item in rooms"
                :key="item.id"
                :label="item.id">
                  {{ item.roomName || item.name }}
                </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-time-select v-model="form.startTime" 
              :picker-options="timePick" 
              placeholder="选择开始时间"
              @change="checkConflict">
            </el-time-select>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-time-select v-model="form.endTime" 
              :picker-options="{start: '08:00',step: '00:30',end: '20:00',minTime:form.startTime}" 
              placeholder="选择结束时间"
              @change="checkConflict">
            </el-time-select>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注信息" :rows="3" type="textarea"></el-input>
          </el-form-item>

          <div v-if="conflictMessage" class="conflict-message">
            <i class="el-icon-warning"></i> {{ conflictMessage }}
          </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit" :disabled="hasConflict">确认预约</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'CalendarPage',
  
  data() {
    return {
      value: new Date(),
      dialogVisible: false,
      TodayDialogVisible: false,
      currentSelectedDate: '',
      currentAppointments: [],
      radio: '1',
      
      form: {
        title: '',
        roomId: '',
        date: '',
        startTime: '',
        endTime: '',
        name: '',
        remark: ''
      },
      
      rooms: [],
      reservations: [],
      currentUser: null,
      conflictMessage: '',
      hasConflict: false,
      
      formRules: {
        title: [
          { required: true, message: '请输入会议主题', trigger: 'change' }
        ],
        roomId: [
          { required: true, message: '请选择会议室', trigger: 'change' }
        ],
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ]
      }
    }
  },
  computed:{
    now(){
      return new Date()
    },
    todayStr(){
      const date = new Date()
      return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`
    },
    oneMonthLater(){
      const date = new Date()
      date.setMonth(date.getMonth() + 1)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    },
    timePick(){
      if(this.form.date === this.todayStr){
        const now = new Date()
        const currentHour = now.getHours()
        const currentMinute = now.getMinutes()

        let startHour = currentHour
        let startMinute = currentMinute <= 30 ? 30 : 60
        if (startMinute === 60) {
          startHour += 1
          startMinute = 0
        }
        const minTime = `${String(startHour).padStart(2, '0')}:${String(startMinute).padStart(2, '0')}`
        return {
          start: minTime,
          step: '00:30',
          end: '20:00'
        }
      }
       return {
        start: '08:00',
        step: '00:30',
        end: '20:00'
      };
      }
    },

  methods: {
    canReserve(day){
      if(day > this.oneMonthLater){
        return false
      }
      if(day< this.todayStr){
        return false
      }
      return true
    },
    isToday(dateStr) {
      return dateStr === this.todayStr
    },
    getCurrentUser() {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        this.currentUser = JSON.parse(userStr)
        if (this.currentUser.name) {
          this.form.name = this.currentUser.name
        }
      }
    },
    
    async loadRooms() {
      try {
        const res = await request.get('/rooms')
        if (res.code === 200) {
          this.rooms = res.data
          console.log('会议室数据：', this.rooms)
        }
      } catch(error) {
        console.error('加载会议室失败', error)
      }
    },
    
    async loadReservations() {
      try {
        const year = this.value.getFullYear()
        const month = this.value.getMonth() + 1
        
        const res = await request.get('/reservations/calendar', { params: { year, month } })
        if (res.code === 200) {
          this.reservations = res.data.filter(item => item.status === '1' || item.status === '2' || item.status === '3')
          console.log('预约数据：', this.reservations)
        }
      } catch(error) {
        console.error('加载日历数据失败', error)
      }
    },
    
    openDialog(day) {
      this.currentSelectedDate = day;
      this.form.date = day;
      if (this.isToday(day)) {
        const now = new Date()
        const currentHour = now.getHours()
        const currentMinute = now.getMinutes()
        let startHour = currentHour
        let startMinute = currentMinute <= 30 ? 30 : 60
        if (startMinute === 60) {
          startHour += 1
          startMinute = 0
        }
        this.form.startTime = `${String(startHour).padStart(2, '0')}:${String(startMinute).padStart(2, '0')}`
        this.form.endTime = ''
      } else {
        this.form.startTime = ''
        this.form.endTime = ''
      }
      this.form.startTime = '';
      this.form.title = '';
      this.form.roomId = this.rooms[0]?.id || '';
      this.form.endTime = '';
      this.form.name = this.currentUser?.name || '';
      this.form.remark = '';
      this.conflictMessage = '';
      this.hasConflict = false;
      this.dialogVisible = true;
    },

    checkConflict() {
      if (!this.form.roomId || !this.form.startTime || !this.form.endTime) {
        this.conflictMessage = '';
        this.hasConflict = false;
        return;
      }
      
      const newDate = this.form.date;
      const newRoom = this.form.roomId;  
      const newStart = this.form.startTime;
      const newEnd = this.form.endTime;
      
      const conflict = this.reservations.some(reservation => {
        if (reservation.reserveDate === newDate && reservation.roomId === newRoom) {
          const existingStart = reservation.startTime;
          const existingEnd = reservation.endTime;
          const isOverlap = (newStart < existingEnd) && (newEnd > existingStart);
          return isOverlap;
        }
        return false;
      });
      
      if (conflict) {
        this.conflictMessage = '该时段已被预约，请选择其他时间';
        this.hasConflict = true;
      } else {
        this.conflictMessage = '';
        this.hasConflict = false;
      }
    },

    async submit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            const checkRes = await request.post('/reservations/check', {
              roomId: this.form.roomId,  
              date: this.form.date,
              startTime: this.form.startTime,
              endTime: this.form.endTime
            })
            
            if (checkRes.code === 200 && checkRes.data === true) {
              this.$message.error('该时间段已被预约，请选择其他时间')
              return
            }

            const creatRes = await request.post('/reservations', {
              roomId: this.form.roomId,  
              title: this.form.title,
              date: this.form.date,
              startTime: this.form.startTime,
              endTime: this.form.endTime
            })
            
            if (creatRes.code === 200) {
              this.$message.success('预约申请已提交，请等待审批')
              this.dialogVisible = false
              this.loadReservations()
            } else {
              this.$message.error(creatRes.msg || '预约失败')
            }
          } catch(error) {
            console.error('预约失败', error)
            this.$message.error('预约失败')
          }
        } else {
          this.$message.error('请填写完整的预约信息')
        }
      })
    },
    
    hasReservations(day) {
      return this.reservations.some(reservation => reservation.reserveDate === day)
    },
    
    getReservationsByDate(day) {
      return this.reservations.filter(reservation => reservation.reserveDate === day)
    },
    
    TodayDialog(day) {
      this.currentAppointments = this.getReservationsByDate(day)
      this.TodayDialogVisible = true
    },
    
    getRoomName(roomId) {
      const room = this.rooms.find(r => r.id === roomId);
      return room ? (room.roomName || room.name) : '未知会议室';
    },
    
    getActualStatus(reservation) {
      const now = new Date()
      const reserveDate = new Date(reservation.reserveDate)
      const startTime = reservation.startTime.split(':')
      const endTime = reservation.endTime.split(':')
  
      const startDateTime = new Date(reserveDate)
      startDateTime.setHours(parseInt(startTime[0]), parseInt(startTime[1]), 0)
  
      const endDateTime = new Date(reserveDate)
      endDateTime.setHours(parseInt(endTime[0]), parseInt(endTime[1]), 0)

      if (reservation.status === '0') {
        return 'cancelled'
      }

      if (now > endDateTime) {
        return 'completed'
      }

      if (now >= startDateTime && now < endDateTime) {
        return 'ongoing'
      }

      return 'booked'
  },


  getDisplayStatusType(reservation) {
    const status = this.getActualStatus(reservation)
    const typeMap = {
      'completed': 'info',      
      'ongoing': 'warning',     
      'booked': 'success',      
      'cancelled': 'info'      
    }
    return typeMap[status] || 'info'
  },


  getDisplayStatusText(reservation) {
    const status = this.getActualStatus(reservation)
    const textMap = {
      'completed': '已完成',
      'ongoing': '进行中',
      'booked': '已预约',
      'cancelled': '已取消'
    }
    return textMap[status] || '未知'
  }
},
  
  watch: {
    value() {
      this.loadReservations()
    }
  },
  
  created() {
    this.getCurrentUser()
    this.loadRooms()
    this.loadReservations()
  }
}
</script>

<style scoped>
.calendar-box {
  padding: 10px;
  background-color: white;
}

.calendar .el-calendar-table .el-calendar-day {
  border: 1px solid #dcdfe6 !important;
  box-sizing: border-box;
}

.calendar .calendar-btn {
  font-size: 14px;
  line-height: 14px;
  display: block;
  margin: 0 auto;
  text-align: center;
}
::v-deep .el-calendar-table .el-calendar-day {
  height: 100px !important;      
}
.calendar .chakan-btn {
  font-size: 14px;
  line-height: 14px;
  display: block;
  margin: 0 auto;
  color: #67c23a !important;
}

.is-selected {
  color: #1989FA;
}
::v-deep .el-calendar-table td.is-today {
  color: black !important;

}



.today-card {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
  background-color: #fafafa;
}

.today-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.today-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.today-info {
  font-size: 14px;
}

.info-row {
  margin-bottom: 8px;
  line-height: 1.5;
}

.info-row .label {
  display: inline-block;
  width: 70px;
  color: #909399;
  font-weight: 500;
}

.conflict-message {
  margin-top: 10px;
  padding: 8px 12px;
  background-color: #fef0f0;
  color: #f56c6c;
  border-radius: 4px;
  font-size: 13px;
}
</style>