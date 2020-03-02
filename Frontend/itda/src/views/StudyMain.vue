<template>
  <div class="studymain">
      <ImgBanner :imgSrc="bannerImg"/>
      <v-container white class="my-0" v-if="isLogin">
          <v-row>
              <v-col>
                <v-icon>edit</v-icon>
                <span>&nbsp;내가 가입한 <b>스터디</b></span>
              </v-col>
              <v-spacer></v-spacer>
                <v-btn class="mr-5 study-create-btn" style="color: grey;" text @click.stop="overlay = !overlay" v-if="isLogin"><b>새 스터디</b></v-btn>
                <v-dialog v-model="overlay" scrollable max-width="500px">
                    <v-card>
                        <v-card-title>스터디 등록</v-card-title>
                        <v-card-text>
                        <v-form ref="form" v-model="valid"> 
                            <v-text-field v-model="formData.stname" :rules="[v => !!v || '스터디 이름을 입력해주세요']" label="스터디명" required></v-text-field>
                            <v-text-field v-model.number="formData.maxPcnt"  min="2" max="20" type="number" :rules="[v => !!v || '인원수를 입력해주세요(최대20명)']" label="인원수" required></v-text-field>
                            <v-select v-model="formData.stype" :items="formCategory.category1" :rules="[v => !!v || '스터디 종류를 선택해주세요']" label="스터디 종류" required></v-select>
                            <v-btn v-if="formData.stype && formData.stype!=4" @click="openPopup(formData.stype)">스터디 주제 입력</v-btn>
                            <v-select v-model="formData.sgroup" :items="formCategory.category2" :rules="[v => !!v || '스터디 내용를 선택해주세요']" label="스터디 내용" required></v-select>
                            <v-textarea v-model="formData.content" solo :rules="[v => !!v || '스터디 정보를 입력해주세요']" label="스터디 정보를 입력해주세요"></v-textarea>
                        </v-form>
                        </v-card-text>
                        <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="success" text :disabled="!valid" @click="validate">submit</v-btn>
                        <v-btn color="blue darken-1" text @click="reset">Reset</v-btn>
                        <v-btn color="blue darken-1" text @click="close">Close</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
          </v-row>
          <v-row>
              
          </v-row>
          <study-login-content :myStudyList="loginStudies" v-if="loginStudies.length" v-on:update="update"/>
          <v-row v-else>
              <v-col>가입한 스터디가 없어요.</v-col>
          </v-row>
      </v-container>
      <v-container class="my-0" white>
          <v-row><v-col><span><b>전체</b></span></v-col></v-row>
          <study-filter :options="this.options" style="max-width:930px"/>
          <study-default-content :options="this.options" :allstudy="this.defaultStudies" :islogin="isLogin" v-on:update="update"/>
      </v-container>
 </div>
</template>

<script>
import StudyLoginContent from '../components/StudyMain/StudyLoginContent.vue'
import StudyFilter from '../components/StudyMain/StudyFilter.vue'
import StudyDefaultContent from '../components/StudyMain/StudyDefaultContent.vue'
import axios from 'axios'
import { mapState } from 'vuex'
import ImgBanner from '../components/RecruitMain/ImgBanner.vue'
export default {
    name:"studymain",
    components:{
        ImgBanner,
        StudyLoginContent,
        StudyFilter,
        StudyDefaultContent,
    },
    methods:{
        getDefaultStudies(){
            axios.get('https://i02b201.p.ssafy.io:8197/itda/api/getAllStudy')
            .then(response=>{
                this.defaultStudies = response.data
                if (!this.defaultStudies)
                    this.defaultStudies = []
            })
        },
        getLoginStudies(){
            if (this.isLogin){
                axios.get("https://i02b201.p.ssafy.io:8197/itda/api/getUser", {headers:{"jwt-auth-token": localStorage.getItem("access_token")}})
                    .then(response => {
                        this.loginStudies = response.data.myStudies
                        var len = this.loginStudies.length % 4
                        if (len){
                            for (var i=0;i<4-len;i++)
                                this.loginStudies.push({})
                        }
                })
            }
        },  
        update(){
            this.getDefaultStudies()
            this.getLoginStudies()
        },
        validate () {
        var select = JSON.parse(localStorage.getItem('select'))
        if (!select && this.formData.stype!=4){
            alert('스터디 주제를 확인해주세요')
        }
        else{
            if (select){
                this.formData.typeFk = select.id
                this.formData.typeName = select.Nm
            }
            else{
                this.formData.typeFk = 0
                this.formData.typeName = '기타'
            }
            if (this.$refs.form.validate()) {
                axios.post('https://i02b201.p.ssafy.io:8197/itda/api/createStudy', this.formData, {'headers' : {"jwt-auth-token": localStorage.getItem("access_token")}})
                .then(()=>{
                    alert('스터디를 생성하였습니다.')
                    this.$refs.form.reset()
                    this.overlay = false
                    localStorage.removeItem('select')
                    this.getDefaultStudies()
                    this.getLoginStudies()
                })
                .catch()
                }
            }
        },
        reset () {
            this.$refs.form.reset()
            localStorage.removeItem('select')
        },
        close() {
            this.formData = {stname:'',content:'',maxPcnt:null,stype:null,sgroup:null,typeFk:null,typeName:''},
            this.$refs.form.reset()
            localStorage.removeItem('select')
            this.overlay = false
        },
        openPopup(type){
            localStorage.setItem('pre','')
            let routeData = this.$router.resolve({name: 'searchdata', params: {type: type}})
            window.open(routeData.href, '_blank', 'width=500,height=700,')
        },
        getImgUrl(img) {
                return require('../assets/' + img)
        },
    },
    data(){
        return{
            options:{
                category1 : 0,
                category2 : 0,
                keyword : "",
            },
            valid:false,
            formData:{
                stname:'',
                content:'',
                maxPcnt:null,
                stype:null,
                sgroup:null,
                typeFk:null,
                typeName:'',
            },
            formCategory:{
                category1:[
                    {text: '공고 대비', value: 1},
                    {text: '기업 대비', value: 2},
                    {text: '기술 대비', value: 3},
                    {text: '기타 등등', value: 4},
                    ],
                category2:[
                    {text: '자소서 준비', value: 1},
                    {text: '필기 준비', value: 2},
                    {text: '면접 준비', value: 3},
                    {text: '기타 등등', value: 4},
                    ]
            },
            overlay:false,
            defaultStudies:[],
            loginStudies:[],
            bannerImg:[
                    this.getImgUrl('banner1.png'),
                    this.getImgUrl('banner2.png')
                ]
        }
    },
    mounted(){
        this.getDefaultStudies()
        this.getLoginStudies()
    },
    watch:{
        isLogin:{
            deep:true,
            immediate:true,
            handler:'getLoginStudies'
        }
    },
    computed: {
        ...mapState(["isLogin"])
    },
}
</script>

<style>
.study-create-btn {
    border: 1px solid rgba(0, 170, 179);
}
.study-create-btn:hover {
    color: rgba(0, 170, 179) !important; 
}
</style>