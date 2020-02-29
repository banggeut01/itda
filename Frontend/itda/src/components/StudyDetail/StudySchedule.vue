<template>
  <v-container>
    <v-row class="ml-1 my-1">
        <p id="studyMemberTitle">스터디 일정</p>
    </v-row>
    <FullCalendar
        defalutView="timeGridWeek"
        :plugins="calendarPlugins"
        :custom-buttons="customButtons"
        :events="weekEvent"
        :header="header"
        minTime='12:00'
        maxTime='24:00'
        locale="ko"
        :locales="allLocales"
        contentHeight="auto"
        @eventClick="deleteCheck"/>
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
import {Datetime} from 'vue-datetime'
import allLocales from '@fullcalendar/core/locales-all'

export default {
    name:"studyschedule",
    components:{
        FullCalendar,
        Datetime,
    },
    props:{
        study:{type:Object},
        meetings:{type:Array}
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
            weekEvent:[],
            allLocales:allLocales
        }
    },
    methods:{
        submit() {
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
                        this.$emit('update')
                    })
                }
            },
            open() {
                this.modal = true
            },
            close() {
                this.modal = false
            },
            updateMeetings(){
                this.weekEvent = []
                for (var i = 0; i < this.meetings.length; i++){
                    this.weekEvent.push({start:this.meetings[i].startTime,end:this.meetings[i].endTime,title:this.meetings[i].title,mid:this.meetings[i].mid,textColor: "white"})
                }
            },
            deleteCheck(arg){
                if (confirm("정말 삭제하시겠습니까??") == true){
                    axios.delete('https://i02b201.p.ssafy.io:8197/itda/api/deleteMeeting/' + arg.event._def.extendedProps.mid, {'headers': {"jwt-auth-token": localStorage.getItem("access_token")}})
                    .then(() => {
                        alert('일정을 삭제하였습니다.')
                        this.$emit('update')
                    })
                }
            }
        },
    mounted(){
        this.updateMeetings()
    },
    watch:{
        meetings:{
            deep:true,
            immediate:true,
            handler:"updateMeetings"
        }
    }
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