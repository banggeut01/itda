<template>
  <tr @click="openRead">
    <td class="text-center" id="response">{{study.stid}}</td>
    <td class="text-center" id="response">{{category1[study.stype-1]}}</td>
    <td class="text-center"><p class="mb-0" id="notOverLine">{{study.typeName}}</p></td>
    <td class="text-center" id="response">{{category2[study.sgroup-1]}}</td>
    <td class="text-left">{{study.stname}}</td>
    <td class="text-center"> {{study.pcnt}} / {{study.maxPcnt}}</td>
    <v-dialog v-model="overlayRead"  max-width="500px" white>
        <v-card>
            <v-card-title class="headline justify-center pb-0"
                style="color:rgba(0, 170, 179);">
                <v-icon style="color:rgba(0, 170, 179);" class="ma-2">edit</v-icon>
                {{study.stname}}
            </v-card-title>
            <br>
            <v-card-text class="text-center p-3">
                <p class="m-auto">{{study.content}}</p>
            </v-card-text>
            <v-card-text class="pa-0 pl-6 pb-3">
                <v-container>
                    <v-row class="text-center">
                        <v-col class="col-4">
                            종류<br>
                            <h5 style="color:black;"><b>{{category1[study.stype-1]}}</b></h5>
                        </v-col>
                        <v-col class="col-4">
                            주제<br>
                            <h5 style="color:black;"><b>{{study.typeName}}</b></h5>
                        </v-col>
                        <v-col class="col-4">
                            목적<br>
                            <h5 style="color:black;"><b>{{category2[study.sgroup-1]}}</b></h5>
                        </v-col>
                    </v-row>
                </v-container>
                <div class="p-2">
                    <v-icon style="color:rgba(0, 170, 179);" >group</v-icon>
                    <span v-for="people in person" :key="people.uid+people.uname"> {{people.uname }}   </span>
                    <span v-if="study.maxPcnt <= study.pcnt" class= "body-2 font-italic font-weight-light 
                    red--text ">현원: {{study.pcnt}}명[최대:{{study.maxPcnt}}명]</span>
                    <span v-else class= "body-2 font-italic font-weight-light ">
                        현원: {{study.pcnt}}명[최대:{{study.maxPcnt}}명]</span>          
                </div>
            </v-card-text> 

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red darken-1" text @click="del" v-if="isLogin&&ismember&study.captain==userInfo.user.uid">스터디 삭제</v-btn>
                <v-btn color="blue darken-1" text @click="enter" v-if="isLogin&&!ismember&&study.pcnt<study.maxPcnt">스터디 가입</v-btn>
                <v-btn color="blue darken-1" text @click="exit" v-if="isLogin&&ismember">스터디 탈퇴</v-btn>
                <v-btn color="black darken-1" text @click="close">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
  </tr>
</template>

<script>
import axios from 'axios'
import router from '../../../router'
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
            if(confirm("가입하시겠습니까?") == true){
                axios.post('https://i02b201.p.ssafy.io:8197/itda/api/createStudyGroup',{"stid":this.study.stid},{'headers' : {"jwt-auth-token": localStorage.getItem("access_token")}})
                .then(
                    ()=>{
                        alert('스터디 가입이 완료되었습니다.')
                        this.getStudyDetail(this.study.stid)
                        this.$emit('update')
                        if (confirm("스터디 페이지로 가시겠어요?")){
                            router.push({name:'studydetail', params:{id:this.study.stid}})
                        }
                })
            }
        },
        exit() {
            if (confirm("탈퇴하시겠습니까?") == true){
                axios.delete('https://i02b201.p.ssafy.io:8197/itda/api/deleteStudyGroup/' + this.study.stid ,{'headers' : {"jwt-auth-token": localStorage.getItem("access_token")}})
                .then(
                    ()=>{
                        alert('스터디 탈퇴가 완료되었습니다.')
                        this.getStudyDetail(this.study.stid)
                        this.$emit('update')
                })
            }
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
#notOverLine{
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}
#cardtitle{
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}
#stype2{
  background: #FFD166;
  color: #ffffff;
  font-size: 0.7rem;
  margin-bottom: 0px;
  border-radius: 10%;
  padding-left: 10px;
  padding-right: 10px;
}

#sgroup2{
  background: #06D6A0;
  color: #ffffff;
  margin-left: 10px;
  font-size: 0.7rem;
  margin-bottom: 0px;
  border-radius: 10%;
  padding-left: 10px;
  padding-right: 10px;
}

#typename2{
  background: #118AB2;
  color: #ffffff;
  margin-left: 10px;
  font-size: 0.7rem;
  margin-bottom: 0px;
  border-radius: 10%;
  padding-left: 10px;
  padding-right: 10px;
}
</style>