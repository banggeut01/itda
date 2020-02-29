<template>
    <div>
        <v-container>
            <v-row class="ml-0" align="center">
                <p id="headStudy">{{study.stname}}</p>
                <v-spacer></v-spacer>
                <v-btn class="text-white" id="btn-colored" @click="goStudyMain">스터디 메인</v-btn>
            </v-row>
            <v-row class="mx-0 mb-3">
                <span id="stype" class="pa-1">{{category.category1[study.stype-1]}}</span>
                <span id="sgroup" class="pa-1">{{category.category2[study.sgroup-1]}}</span>
                <span id="typename" class="pa-1">{{study.typeName}}</span>
            </v-row>
            <v-row class="ml-2" v-text="study.content" style="color:#b9b9b9"></v-row>
            <v-divider class="mb-0"></v-divider>
        </v-container>
        <v-container v-if="person.length">
            <v-row class="ml-1 my-1">
                <p id="studyMemberTitle">스터디 관리자</p>
            </v-row>
            <v-row class="ml-3 mb-2">
                <img :src="person[0].uimg" :alt="person[0].uname" class="mr-3" id="studyMemberImg" v-if="person[0].uimg">
                <img :src="getImgUrl" id="studyMemberImg" class="mr-3" v-else>
                <span id="studyMember">{{person[0].uname}} ({{person[0].email}})</span>
            </v-row>
            <v-row class="ml-1 my-1" v-if="person.length > 1">
                <p id="studyMemberTitle">스터디 멤버</p>
            </v-row>
            <v-row class="ml-3 mb-2" v-for="i in person.length" :key="i">
                <img :src="person[i-1].uimg" :alt="person[i-1].uname" class="mr-3" id="studyMemberImg" v-if="i!=1 && person[i-1].uimg">
                <img :src="getImgUrl" id="studyMemberImg" class="mr-3" v-else-if="i!=1 && !person[i-1].uimg">
                <span id="studyMember" v-if="i!=1">{{person[i-1].uname}} ({{person[i-1].email}})</span>
            </v-row>
            <v-divider></v-divider>
        </v-container>
        <study-chat :person="person" :stid="study.stid" :captain="study.captain"/>
        <study-schedule :study="study" :meetings="meetings" v-on:update="update"/>
        <v-container>
            <v-row class="ml-1 my-1">
                <p id="studyMemberTitle">스터디 파일</p>
            </v-row>
            <v-row>
                <v-col cols="10">
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
                <v-col cols="2" align-self="center">
                    <v-btn @click.prevent="upload">제출</v-btn>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'
    import router from '../router'
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
                category:{
                    category1:[
                        '공고 대비',
                        '기업 대비',
                        '기술 대비',
                        '기타 등등'
                    ],
                    category2:[
                        '자소서 준비',
                        '필기 준비',
                        '면접 준비',
                        '기타 등등'
                    ]
                },
            }
        },
        methods: {
            goStudyMain(){
                router.push({name:"studymain"})
            },
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
        },
        computed:{
            getImgUrl() {
                return require('../assets/NoPersonImg.png')
            },
        }
    }
</script>

<style lang='scss'>
@import '~@fullcalendar/core/main.css';
@import '~@fullcalendar/timegrid/main.css';
#headStudy {
	font-weight: bold;
    font-size: 3rem;
    margin-left: 0px;
    margin-right: 0px;
	margin-bottom: 0px;
	overflow: hidden;
  	text-overflow: ellipsis;
  	display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}
#studyMemberTitle{
    font-weight: bold;
    font-size: 1.5rem;
    margin-left: 0px;
    margin-right: 0px;
	margin-bottom: 0px;
    overflow: hidden;
  	text-overflow: ellipsis;
  	display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}
#studyMemberImg{
    height:50px;
    width:50px;
    border-radius:50%;
    border:1px solid black;
}
#studyMember{
    font-size: 1.2rem;
    line-height: 50px;
    height: 50px;
    margin-left: 0px;
    margin-right: 0px;
	margin-bottom: 0px;
}
#stype{
  background: #FFD166;
  color: #ffffff;
  margin-left: 10px;
}

#sgroup{
  background: #06D6A0;
  color: #ffffff;
  margin-left: 10px;
}

#typename{
  background: #118AB2;
  color: #ffffff;
  margin-left: 10px;
}
#btn-colored {
  background:rgba(0, 170, 179);
}
</style>