<template>
    <div>
        <v-container>
            
                <v-list-item >
                    <v-row>
                        <v-col class="pl-5" cols='4'>
                            <v-list-item-title>
                                스터디 종류
                            </v-list-item-title>
                        </v-col>
                        <v-col cols='8'>
                            <v-list-item-title >{{study.stname}}</v-list-item-title>
                        </v-col>
                    </v-row>
                </v-list-item >
                <v-list-item>
                    <v-row>
                        <v-col class="pl-5" cols='4'>
                            <v-list-item-title>
                                스터디 주제
                            </v-list-item-title>
                        </v-col>
                        <v-col cols='8'>
                            <v-list-item-title >{{study.typeName}}</v-list-item-title>
                        </v-col>
                    </v-row>
                </v-list-item>
                <v-list-item>
                    <v-row>
                        <v-col class="pl-5" cols='4'>
                            <v-list-item-title>
                                스터디 목적
                            </v-list-item-title>
                        </v-col>
                        <v-col cols='8'>
                            <v-list-item-title >{{study.content}}</v-list-item-title>
                        </v-col>
                    </v-row>
                </v-list-item>
                <v-list-item>
                    <v-row>
                        <v-col class="pl-5" cols='4'>
                            <v-list-item-title>
                                스터디 인원
                            </v-list-item-title>
                        </v-col>
                        <v-col cols='8'>
                            <v-list-item-title >{{person.length}}명</v-list-item-title>
                        </v-col>
                    </v-row>
                </v-list-item>
            <v-divider></v-divider>
        </v-container>
        <study-schedule :study="study" :meetings="meetings" v-on:update="update"/>
        <v-container>
            <h2>파일 업로드</h2>
            <v-row>
                <v-col cols="9">
                    <label for="fileUpload"></label>
                    <v-file-input
                        v-model="uploadFile"
                        label="선택한 파일"
                        placeholder="파일을 입력해주세요"
                        accept="image/*,.pdf,.doc,.docx,.ppt,.pptx,.hwp,.hwpx,.xlsx,.xls"
                        show-size
                        dense
                        outlined>
                    </v-file-input>
                </v-col>
                <v-col cols="3">
                    <v-btn @click.prevent="upload">제출</v-btn>
                </v-col>
            </v-row>
        </v-container>
        <StudyChat :person="person" :stid="study.stid" :captain="study.captain"/>
    </div>
</template>

<script>
    import axios from 'axios'
    import StudyChat from '../components/StudyDetail/StudyChat.vue'
    import StudySchedule from '../components/StudyDetail/StudySchedule.vue'
    export default {
        name: "studydetail",
        components: {
            StudyChat,
            StudySchedule,
        },
        data(){
            return{
                study:{},
                meetings:[],
                person:[],
                overlayRead:false,
                files:[],
                uploadFile:null,
            }
        },
        methods: {
            update(){
                this.getStudy()
            },
            changeOverlay() {
                this.overlayRead = !this.overlayRead
            },
            upload() {
                if (this.uploadFile){
                    if (confirm("파일을 업로드 하시겠습니까?")){
                        var formdata = new FormData()
                        formdata.set("file", this.uploadFile)
                        const config = {
                            headers: {
                            "Content-Type": "multipart/form-data",
                            "jwt-auth-token": localStorage.getItem("access_token")
                            }
                        }
                        axios.post("https://i02b201.p.ssafy.io:8197/itda/api/uploadFile", formdata, {params:{stid:this.study.stid}}, config)
                            .then(()=>{
                                alert('파일 업로드가 완료되었습니다.'),
                                this.getStudy()
                            })
                    }
                }
                else
                    alert('파일을 입력해주세요!')
            },
            getStudy() {
                axios.get('https://i02b201.p.ssafy.io:8197/itda/api/getStudy/' + this.$route.params.id)
                    .then(response => {
                        this.study = response.data.study
                        this.meetings = response.data.meetings
                        this.files = response.data.files
                        })
            },
            getPerson() {
                axios.get('https://i02b201.p.ssafy.io:8197/itda/api/getStudyGroup/' + this.$route.params.id)
                    .then(response => {this.person = response.data})
            },
        },
        created() {
            this.getStudy()
            this.getPerson()
        }
    }
</script>

<style lang='scss'>
    @import '~@fullcalendar/core/main.css';
    @import '~@fullcalendar/timegrid/main.css';
</style>