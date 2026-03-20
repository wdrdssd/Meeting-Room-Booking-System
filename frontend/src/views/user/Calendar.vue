<template>
  <div class="calendar-box">
    <el-calendar v-model="value" class="calendar">
      <template slot="dateCell" slot-scope="{data}">
        <span :class="data.isSelected ? 'is-selected' : ''">{{ data.day.split('-').slice(2).join('-') }}</span> 
        <el-button v-if="data.type === 'current-month'" type="primary" size="small" class="calendar-btn"  
          @click="openDialog(data.day)">预约</el-button>
      </template>
    </el-calendar>
    <el-dialog title="会议室预约" :visible.sync="dialogVisible" width="500px" >
      <el-form ref="form" :model="form" label-width="100px" :rules="formRules">
          <el-form-item label="预约日期" prop="date">
            <el-input v-model="form.data" disabled></el-input>
          </el-form-item>
          <el-form-item label="预约时间" prop="time">
            <el-time-select v-model="form.time" :picker-options="{start: '08:00',step: '00:30',end: '20:00'}" placeholder="选择时间">
            </el-time-select>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注信息" :row="3" type="textarea"></el-input>
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
        currentSelectedDate:'',
        form:{
          data:'',
          time:'',
          name:'',
          remark:''
        },
        formRules:{
          time:[
            {required:true, message:'请选择预约时间' , triger:'change'}
          ],
          name:[
            {required:true, message:'请输入姓名' , triger:'blur'}
          ]
        }
      }
    },
    methods:{
      openDialog(day){
        this.currentSelectedDate = day;
        this.form.data = day;
        this.form.time='';
        this.form.name='';
        this.form.remark='';
        this.dialogVisible = true;
      },
      submit(){
        this.$refs.form.validate(valid=>{
          if(valid){
            console.log('预约信息:',this.form);
            this.$message.success(`预约成功！日期：${this.form.data}，时间：${this.form.time}`);
            this.dialogVisible = false;
          }else{
            this.$message.error('请填写完整的预约信息');
            return false
          }
        })
      },
      
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
.is-selected {
    color: #1989FA;
}
.el-backtop, .el-calendar-table td.is-today{
  color: black;
}
</style>