<template>
  <tr @click="openRead">
    <td class="text-center">{{study.stid}}</td>
    <td class="text-center" id="response">{{category1[study.stype-1]}}</td>
    <td class="text-center">{{study.typeName}}</td>
    <td class="text-center" id="response">{{category2[study.sgroup-1]}}</td>
    <td class="text-left">{{study.stname}}</td>
    <td class="text-center"> {{study.pcnt}} / {{study.maxPcnt}}</td>
    <v-dialog v-model="overlayRead"  max-width="500px" white>
        <v-card>
            <v-card-title class="headline justify-center pb-0">{{study.stname}}</v-card-title>
            <v-divider></v-divider>
            <v-card-text class="pa-0 pl-6 pb-3">
                <p>스터디 종류 : {{category1[study.stype-1]}}</p>
                <p>스터디 주제 : {{study.typeName}}</p>
                <p>스터디 목적 : {{category2[study.sgroup-1]}}</p>
                <div>
                     스터디 팀원 : 
                    <span v-for="people in person" :key="people.uid+people.uname"> {{people.uname }}   </span>
                    <span v-if="study.maxPcnt <= study.pcnt" class= "body-2 font-italic font-weight-light 
                    red--text ">현원: {{study.pcnt}}명[최대:{{study.maxPcnt}}명]</span>
                    <span v-else class= "body-2 font-italic font-weight-light ">
                        현원: {{study.pcnt}}명[최대:{{study.maxPcnt}}명]</span>
                               
                </div>
             
            </v-card-text> 
            <v-divider></v-divider>
            <!-- <v-card-title class= "ml-5">스터디원 정보 </v-card-title>
                <v-card-title v-if="study.maxPcnt <= study.pcnt" class= "body-2 font-italic font-weight-light 
               red--text 
               ">현원: {{study.pcnt}}명[최대:{{study.maxPcnt}}명] </v-card-title>
               <v-card-title v-else class= "body-2 font-italic font-weight-light ">
                   현원: {{study.pcnt}}명[최대:{{study.maxPcnt}}명] </v-card-title> -->
                <!-- <v-card-text>
                <p v-for="people in person" :key="people.uid+people.uname">{{people.uname}} </p>
                </v-card-text> -->
                <v-card-title >스터디 내용</v-card-title>
                <v-card-text>
                <p>{{study.content}}</p>
                
                </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red darken-1" text @click="del" v-if="isLogin&&ismember&study.captain==userInfo.user.uid">스터디 삭제</v-btn>
                <v-btn color="blue darken-1" text @click="enter" v-if="isLogin&&!ismember&&study.pcnt<study.maxPcnt">스터디 가입</v-btn>
                <v-btn color="blue darken-1" text @click="exit" v-if="isLogin&&ismember">스터디 탈퇴</v-btn>
                <v-btn color="black darken-1" text @click="close">Close</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
  </tr>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'

export default {
    name:"studydetail",
    props:{
        study:{type:Object}
    },
    data(){
        return{
            person:[],
            overlayRead:false,
            category1:['공고 대비','기업 대비','기술 대비','기타 등등',],
            category2:['자소서 준비', '필기 준비', '면접 준비', '기타 등등',],
        }
    },
    methods:{
        openRead(){
            this.overlayRead = true
        },
        close() {
            this.overlayRead = false
        },
        enter() {
            axios.post('https://i02b201.p.ssafy.io:8197/itda/api/createStudyGroup',{"stid":this.study.stid},{'headers' : {"jwt-auth-token": localStorage.getItem("access_token")}})
            .then(
                ()=>{
                    alert('스터디 가입이 완료되었습니다.')
                    this.getStudyDetail(this.study.stid)
                    this.$emit('update')
            })
        },
        exit() {
            axios.delete('https://i02b201.p.ssafy.io:8197/itda/api/deleteStudyGroup/' + this.study.stid ,{'headers' : {"jwt-auth-token": localStorage.getItem("access_token")}})
            .then(
                ()=>{
                    alert('스터디 탈퇴가 완료되었습니다.')
                    this.getStudyDetail(this.study.stid)
                    this.$emit('update')
            })
        },
        del() {
            axios.delete('https://i02b201.p.ssafy.io:8197/itda/api/deleteStudy/' + this.study.stid ,{'headers' : {"jwt-auth-token": localStorage.getItem("access_token")}})
            .then(
                ()=>{
                    alert('스터디 삭제가 완료되었습니다.')
                    this.getStudyDetail(this.study.stid)
                    this.$emit('update')
            })
        },
        getStudyDetail(id){
            axios.get('https://i02b201.p.ssafy.io:8197/itda/api/getStudyGroup/'+id)
                .then(response => {
                    this.person = response.data
                })
        },
    },
    mounted(){
        this.getStudyDetail(this.study.stid)
    },
    computed: {
        ...mapState(["isLogin", "userInfo"]),
        ismember(){
            for (var i=0;i<this.person.length;i++){
                if(this.person[i].uid === this.userInfo.user.uid)
                    return true
            }
            return false
        },
    },
}
</script>

<style>
@media only screen and (max-width: 1030px){
    #response{
        display: none;
    }
}
</style>