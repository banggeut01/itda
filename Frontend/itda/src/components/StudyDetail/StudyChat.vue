<template>
    <v-container v-if="comments.length">
        <v-row class="ml-1 my-1">
            <p id="studyMemberTitle">스터디 공지사항</p>
        </v-row>
        <v-row v-for="comment in comments" :key="comment.cmid">
            <v-col cols="2" v-if="comment.uid == userInfo.user.uid" align-self="center">
                <p v-if="person.length" class="ma-0">{{person[findPerson(comment.uid)-1].uname}}</p>
                <v-img></v-img>
            </v-col>
            <v-col cols="8" sm="9" align-self="center">
                {{comment.content}}
                {{comment.createdAt}}
            </v-col>
            <v-col cols="1" align-self="center"><v-icon v-if="comment.uid == userInfo.user.uid || captain == userInfo.user.uid" @click="deleteComment(comment.cmid)" style="cursor:pointer">close</v-icon></v-col>
            <v-col cols="2" v-if="comment.uid != userInfo.user.uid" align-self="center">
                <p v-if="findPerson(comment.uid)" class="ma-0">{{person[findPerson(comment.uid)-1].uname}}</p>
                <p v-else class="ma-0">스터디를 탈퇴한 회원</p>
            </v-col>
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
                    console.log(this.newContent)
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
            }
        }
    }
</script>