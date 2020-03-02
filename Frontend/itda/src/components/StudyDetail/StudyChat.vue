<template>
    <v-container v-if="comments.length && person.length">
        <v-row class="ml-1 my-1">
            <p id="studyMemberTitle">스터디 공지사항</p>
        </v-row>
        <v-row class="ml-2" v-for="comment in comments" :key="comment.cmid">
            <img :src="person[findPerson(comment.uid)-1].uimg" :alt="person[findPerson(comment.uid)-1].uname" class="mr-3 my-auto" id="memberImg" v-if="findPerson(comment.uid) && person[findPerson(comment.uid)-1].uimg">
            <img :src="getImgUrl" id="studyMemberImg" class="mr-3 my-auto" v-else>
            <v-col cols="2" align-self="center">
                <span v-if="findPerson(comment.uid)" class="ma-0">{{person[findPerson(comment.uid)-1].uname}}</span>
                <span v-else class="ma-0">스터디를 탈퇴한 회원</span>
            </v-col>
            <v-col cols="7" sm="8" align-self="center">
                {{comment.content}}
                <v-spacer></v-spacer>                
                {{comment.createdAt}}
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="1" align-self="center"><v-icon v-if="comment.uid == userInfo.user.uid || captain == userInfo.user.uid" @click="deleteComment(comment.cmid)" style="cursor:pointer">close</v-icon></v-col>
        </v-row>
        <v-row v-if="userInfo" class="mx-2">
            <v-col cols="2" align-self="center">
                <p class="ma-0">{{userInfo.user.uname}}</p>
            </v-col>
            <v-col cols="10" align-self="center">
                <v-textarea append-outer-icon="comment" v-model="newContent" rows="1" @click:append-outer.prevent="createComment" @keyup.enter.prevent="createComment"></v-textarea>
            </v-col>
        </v-row>
        <v-divider></v-divider>
    </v-container>
    <v-container v-else>
        <v-row class="ml-1 my-1">
            <p id="studyMemberTitle">스터디 공지사항</p>
        </v-row>
        <v-row>
            <v-col>스터디 공지사항이 없습니다!</v-col>
        </v-row>
        <v-row v-if="userInfo">
            <v-col cols="2" align-self="center">
                <p class="ma-0">{{userInfo.user.uname}}</p>
            </v-col>
            <v-col cols="10" align-self="center">
                <v-textarea append-outer-icon="comment" v-model="newContent" rows="1" @click:append-outer.prevent="createComment" @keyup.enter.prevent="createComment"></v-textarea>
            </v-col>
        </v-row>
        <v-divider></v-divider>
    </v-container>
</template>

<script >
    import axios from 'axios'
    import {mapState} from "vuex";
    
    export default {
        data() {
            return {
                comments:[],
                newContent:"",
            }
        },
        props: {
            stid: {type:Number},
            person: {type:Array},
            captain:{type:Number}
        },
        computed:{
            ...mapState(["userInfo"]),
            getImgUrl() {
                return require('../../assets/NoPersonImg.png')
            },
        },
        mounted(){
            this.getComments()
        },
        methods:{
            findPerson(uid){
                for (var i=0; i<this.person.length; i++){
                    if (this.person[i].uid == uid){
                        return i+1
                        }
                    }
                return 0
            },
            getComments() {
                axios.get('https://i02b201.p.ssafy.io:8197/itda/api/getStudy/' + this.$route.params.id)
                    .then(response => {this.comments = response.data.comments})
            },
            createComment(){
                if (this.newContent.replace("\n","").replace(" ","").length){
                    var form = {stid:this.stid, content:this.newContent}
                    axios.post('https://i02b201.p.ssafy.io:8197/itda/api/createComment/', form, {"headers": {"jwt-auth-token": localStorage.getItem("access_token")}})
                        .then(() => {
                            this.newContent = ""
                            alert("글 작성이 완료되었습니다.")
                            this.getComments()
                            }
                        )
                }
                else{
                    this.newContent = ""
                    alert("글을 입력해주세요!!")
                }
            },
            deleteComment(cmid){
                var idx = undefined
                for (var i=0; i<this.comments.length; i++){
                    if (this.comments[i].cmid == cmid){
                        idx = i
                        break
                    }
                }
                if (this.comments[idx].uid == this.userInfo.user.uid || this.captain == this.userInfo.user.uid){
                    if (confirm("정말 삭제하시겠습니까??") == true){
                        axios.delete('https://i02b201.p.ssafy.io:8197/itda/api/deleteComment/'+cmid, {"headers": {"jwt-auth-token": localStorage.getItem("access_token")}})
                            .then(() => {
                                alert("글 삭제가 완료되었습니다.")
                                this.getComments()
                            }
                        )
                    }
                }
            },
        }
    }
</script>

<style>
#memberImg{
    height:50px;
    width:50px;
    border-radius:50%;
    border:1px solid black;
}   
</style>