<template>
  <div class="calendar-box">
    <el-calendar v-model="value" class="calendar">
      <template slot="dateCell" slot-scope="{data}">
        <span :class="data.isSelected ? 'is-selected' : ''">{{ data.day.split('-').slice(2).join('-') }}</span> 
          <el-button type="text" v-if="data.type === 'current-month' && hasReservations(data.day)"
            size="mini" class="chakan-btn"
            @click="TodayDialog(data.day)">查看预约</el-button>
          <el-button v-if="data.type === 'current-month'" type="text" size="mini" class="calendar-btn"  
          @click="openDialog(data.day)">+ 预约</el-button>
      </template>
    </el-calendar>
    <el-dialog title="查看预约详情" :visible.sync="TodayDialogVisible" width="600px" >
      <div v-if="currentAppointments.length > 0">
        <div class="today-card" v-for="(appointment,index) in currentAppointments" :key="index">
          <div class="today-header">
            <h3>{{ appointment.title }}</h3>
            <el-tag type="appointment.status === '进行中' ? 'success' : 'info'" size="small">
              {{ appointment.status || '已预约' }}
            </el-tag>
          </div>
          <div class="today-info">
            <div class="info-row">
              <span class="label">预约日期:</span>
              <span>{{ appointment.date }}</span>
            </div>
            <div class="info-row">
              <span class="label">预约时间:</span>
              <span>{{ appointment.startTime }} - {{ appointment.endTime }}</span>
            </div>
            <div class="info-row">
              <span class="label">会议室:</span>
              <span>{{ getRoomName(appointment.room )}}</span>
            </div>
            <div class="info-row">
              <span class="label">预约人:</span>
              <span>{{ appointment.name }}</span>
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
          <el-form-item label="会议室" prop="room">
            <el-radio-group v-model="form.room">
                <el-radio 
                v-for="item in rooms"
                :key="item.id"
                :label="item.id">
                  {{ item.name }}
                </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-time-select v-model="form.startTime" :picker-options="{start: '08:00',step: '00:30',end: '20:00'}" placeholder="选择开始时间">
            </el-time-select>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-time-select v-model="form.endTime" :picker-options="{start: '08:00',step: '00:30',end: '20:00',minTime:form.startTime }" placeholder="选择结束时间"  >
            </el-time-select>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注信息" :rows="3" type="textarea"></el-input>
          </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确认预约</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name:'CalendarPage',
    data() {
      return {
        value:new Date(),
        dialogVisible:false,
        TodayDialogVisible:false,
        currentSelectedDate:'',
        currentAppointments: [],
        radio:'1',
        form:{
          title:'',
          room:'',
          date:'',
          startTime:'',
          endTime:'',
          name:'',
          remark:''
        },
        rooms:[
          {id:1,name:'A101'},
          {id:2,name:'B101'},
          {id:3,name:'C101'},
          {id:4,name:'A201'},
          {id:5,name:'B301'},
          {id:6,name:'C201'},
        ],
        reservations: [
          {
            id: 1,                    
            title: '项目启动会议',     
            date: '2026-03-21',       
            startTime: '09:00',       
            endTime: '10:30',         
            room: 1,                  
            name: '张三',              
            remark: '需要准备投影仪',  
            status: '已预约'          
          },
          {
            id: 2,
            title: '技术评审会',
            date: '2026-03-21',       
            startTime: '14:00',
            endTime: '16:00',
            room: 2,
            name: '李四',
            remark: '需要准备白板',
            status: '已预约'
          },
          {
            id: 3,
            title: '周例会',
            date: '2026-03-22',       
            startTime: '10:00',
            endTime: '11:30',
            room: 1,
            name: '王五',
            remark: '每周例会',
            status: '已预约'
          },
          {
            id: 4,
            title: '产品需求讨论',
            date: '2026-03-25',       
            startTime: '15:00',
            endTime: '17:00',
            room: 3,
            name: '赵六',
            remark: '',
            status: '已预约'
          }
        ],
        formRules:{
          title:[
            {required:true,message:'请输入会议主题',trigger:'change'}
          ],

          startTime:[
            {required:true,message:'请选择开始时间',trigger:'change'}
          ],
          endTime:[
            {required:true,message:'请选择结束时间',trigger:'change'}
          ],
          name:[
            {required:true, message:'请输入姓名' , trigger:'blur'}
          ]
        }
      }
    },
    methods:{
      openDialog(day){
        this.currentSelectedDate = day;
        this.form.date = day;
        this.form.startTime='';
        this.form.title='';
        this.form.room = this.rooms[0]?.id || '';
        this.form.endTime='';
        this.form.name='';
        this.form.remark='';
        this.dialogVisible = true;
      },
      submit(){
        this.$refs.form.validate(valid=>{
          if(valid){
            console.log('预约信息:',this.form);
            this.$message.success(`预约成功！`);
            this.dialogVisible = false;
          }else{
            this.$message.error('请填写完整的预约信息');
            return false
          }
        })
      },
      hasReservations(day){
        return this.reservations.some(reservations =>reservations.date === day)
      },
      getReservationsByDate(day){
        return this.reservations.filter(reservations =>reservations.date === day)
      },
      TodayDialog(day){
        this.currentAppointments = this.getReservationsByDate(day)
        this.TodayDialogVisible=true
      },
      getRoomName(roomId){
        const room = this.rooms.find(r => r.id === roomId);
        return room.name
      }
    }
  }
</script>

<style >
.calendar-box{
  padding: 10px;
  background-color: white;
}
.calendar .el-calendar-table .el-calendar-day {
  border: 1px solid #dcdfe6 !important;
  box-sizing: border-box;
}
.calendar .calendar-btn{
  font-size: 14px;
  line-height: 14px;
  display: block;
  margin: 2px auto;
  text-align: center;

}
.calendar .chakan-btn{
  font-size: 14px;
  line-height: 14px;
  margin-left: 45px;
  text-align: center;
  color: #67c23a !important;
}
.is-selected {
    color: #1989FA;
}
.el-backtop, .el-calendar-table td.is-today{
  color: black;
}
.today-card{
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
  background-color: #fafafa;
}
.today-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}
.today-header h3{
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
.today-info{
  font-size: 14px;
}
.info-row{
  margin-bottom: 8px;
  line-height: 1.5;
}
.info-row .label{
  display: inline-block;
  width: 70px;
  color: #909399;
  font-weight: 500;
}
</style>