<template>
  <v-container>
    <FullCalendar
        defalutView="timeGridWeek"
        :plugins="calendarPlugins"
        :custom-buttons="customButtons"
        :header="header"
        minTime='18:00'
        maxTime='24:00'
        :config="config"
        contentHeight="auto"/>
    <v-divider></v-divider>
    <v-dialog v-model="modal" scrollable="scrollable" max-width="500px">
        <v-card>
            <v-card-title>스케줄 등록</v-card-title>
            <v-card-text>
                <v-form ref="form" v-model="valid">
                    <v-text-field v-model="formData.title" label="스케줄 정보" required="required" :rules="[v => !!v || '스터디 정보를 입력해주세요']"></v-text-field>
                    <label for="meetingStartTime">시작시간</label>
                    <datetime class="mb-3" id="meetingStartTime" type="datetime" v-model="formData.startTime" valueZone="Asia/Seoul" placeholder="시작일을 선택해주세요" :max-datetime="formData.endTime"></datetime>
                    <label for="meetingEndTime">종료시간</label>
                    <datetime class="mb-3" id="meetingEndTime" type="datetime" v-model="formData.endTime" valueZone="Asia/Seoul" placeholder="종료일을 선택해주세요" :min-datetime="formData.startTime"></datetime>
                </v-form>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="success" text="text" :disabled="!valid" @click="submit">새 스터디 등록</v-btn>
                <v-btn color="blue darken-1" text="text" @click="close">취소</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios'
import FullCalendar from '@fullcalendar/vue'
import timeGridPlugin from '@fullcalendar/timegrid'
import resourceTimelinePlugin from '@fullcalendar/resource-timeline'
// import ko from '@fullcalendar/core/locales/ko'
import {Datetime} from 'vue-datetime'


export default {
    name:"studyschedule",
    components:{
        FullCalendar,
        Datetime,
    },
    props:{
        study:{type:Object}
    },
    data() {
        return {
            modal: false,
            valid: false,
            formData:{
                stid:this.study.stid,
                title:'',
                startTime:'',
                endTime:'',
            },
            calendarPlugins: [timeGridPlugin, resourceTimelinePlugin],
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'StudyAdd'
            },
            customButtons: {
                StudyAdd: {
                    text: "일정 추가",
                    click: () => this.open()
                }
            },
            config:{
                locale:'ko'
            }
        }
    },
    methods:{
        submit() {
            console.log(this.$refs.form.validate())
            if (this.$refs.form.validate()){
                var tmp = {
                        startTime:this.formData.startTime.slice(0,10) + ' ' + this.formData.startTime.slice(11,16),
                        endTime:this.formData.endTime.slice(0,10) + ' ' + this.formData.endTime.slice(11,16),
                        stid:this.formData.stid,
                        title:this.formData.title
                    }
                axios.post('https://i02b201.p.ssafy.io:8197/itda/api/createMeeting', tmp, {'headers': {"jwt-auth-token": localStorage.getItem("access_token")}})
                    .then(() => {
                        alert('일정을 생성하였습니다.')
                        this.$refs.form.reset()
                        this.modal = false
                        this.formData.title = null
                        this.formData.startTime = ''
                        this.formData.endTime = ''
                    })
                    .catch(err=>{console.log(err)})
                }
            },
            open() {
                this.modal = true
            },
            close() {
                this.modal = false
            }
        },
}
</script>

<style>
.vdatetime-input{
    width: 180px;
    border: solid;
    border-width: thin;
}
.vdatetime{
    display: block;
}
</style>