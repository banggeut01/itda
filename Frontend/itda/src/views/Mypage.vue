<template>
    <div style="padding: 80px 0px 0px 0px">
        <v-container>
            <h2>내 정보
                <v-btn class="ma-2" outlined="outlined" large="large" fab="fab" width="40px"
                    height="40px" style="color: #00AAB3;">
                    <v-icon @click="goUserModifyPage">mdi-pencil</v-icon>
                </v-btn>
                <v-dialog v-model="overlay" scrollable="scrollable" max-width="500px" white="white">
                    <v-container class="p-0">
                        <v-row v-if="value" class="m-0">
                            <v-alert class="m-0 p-0" type="error" width="500px">비밀번호가 틀렸습니다!</v-alert>
                        </v-row>
                        <v-row class="m-0">
                            <v-card width="500px">
                                <v-card-title>비밀번호를 재확인 할께요</v-card-title>
                                <v-card-text>
                                    <v-form ref="form" v-model="valid">
                                        <v-text-field v-model="formData.pw" @keydown.enter.prevent="validate"
                                            type="password" label="비밀번호" :rules="[v => !!v || '비밀번호를 입력해주세요']">
                                        </v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-spacer></v-spacer>

                                    <v-btn type="submit" text="text" color="success" class="mr-4" @click="validate">
                                        submit</v-btn>
                                    <v-btn color="blue darken-1" text="text" @click="reset">Reset</v-btn>
                                    <v-btn color="blue darken-1" text="text" @click="close">Close</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-row>
                    </v-container>
                </v-dialog>
            </h2>
            <v-row class="mb-4">
                <v-col>
                    <user-detail :userInfo="userInfo" v-if="userInfo" />
                </v-col>
            </v-row>
            <v-row>
                <v-icon>star</v-icon>
                <span>&nbsp;스크랩 공고</span>
            </v-row>
            <v-row class="mb-4">
                <v-col v-if="userInfo" class="px-0">
                    <user-recruit :userInfo="userInfo" v-on:update="update" />
                </v-col>
            </v-row>
        </v-container>
        <v-container class="my-0" white="white">
            <v-row>
                <v-col>
                    <v-icon>edit</v-icon>
                    <span>&nbsp;내가 가입한 <b>스터디</b></span>
                </v-col>
            </v-row>
            <study-login-content :myStudyList="loginStudies" v-if="loginStudies.length" v-on:update="update" />
            <v-row v-else>
                <v-col>가입한 스터디가 없어요..</v-col>
            </v-row>
        </v-container>

    </div>
</template>

<script>
    import UserDetail from "../components/Mypage/UserDetail.vue"
    import StudyLoginContent from '../components/StudyMain/StudyLoginContent.vue'
    import UserRecruit from "../components/Mypage/UserRecruit.vue"
    import router from "../router/index.js"
    import axios from "axios"
    export default {
        name: "mypage",
        components: {
            UserDetail,
            UserRecruit,
            StudyLoginContent
        },
        data() {
            return {
                value: false,
                userInfo: {},
                loginStudies: [],
                overlay: false,
                valid: true,
                formData: {
                    pw: ''
                }
            }
        },
        mounted() {

            this.getUserInfo()
        },
        methods: {
            onChange() {
                if (confirm("사진을 업로드하시겠습니까?") === true) {
                    this.selectImg = this.$refs.uimg.files[0];
                    var formdata = new FormData();
                    formdata.append("file", this.selectImg);
                    const config = {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "jwt-auth-token": localStorage.getItem("access_token")
                        }
                    };
                    axios
                        .post(
                            "https://i02b201.p.ssafy.io:8197/itda/api/uploadImg",
                            formdata,
                            config
                        )
                        .then(response => {
                            this.imageResult = response.data.fileDownloadUri;
                            this.userInfo.user.uimg = this.imageResult;
                        });
                } else {
                    //취소
                    this.file = null;
                    return false;
                }
            },
            hellotest() {
                alert("hello")
            },
            update() {
                this.getUserInfo()
            },
            getUserInfo() {
                axios
                    .get("https://i02b201.p.ssafy.io:8197/itda/api/getUser", {
                        headers: {
                            "jwt-auth-token": localStorage.getItem("access_token")
                        }
                    })
                    .then(response => {
                        this.userInfo = response.data
                        this.loginStudies = response.data.myStudies
                        var len = this.loginStudies.length % 4
                        if (len) {
                            for (var i = 0; i < 4 - len; i++)
                                this
                                .loginStudies
                                .push({})
                        }
                    })
            },
            goUserModifyPage() {
                if (localStorage.getItem("social") != "social")
                    this.overlay = true
                else
                    router.push({
                        name: 'usermodify'
                    })
            },
            validate() {
                if (this.$refs.form.validate()) {
                    axios
                        .post('https://i02b201.p.ssafy.io:8197/itda/api/checkPW', this.formData, {
                            headers: {
                                "jwt-auth-token": localStorage.getItem("access_token")
                            }
                        })
                        .then(response => {
                            if (response.data.state == "success") {
                                router.push({
                                    name: 'usermodify'
                                })
                            } else {
                                this.value = true
                            }
                        })
                }
            },
            reset() {
                this.formData.pw = ''
                this
                    .$refs
                    .form
                    .reset()
            },
            close() {
                this.formData.pw = ''
                this
                    .$refs
                    .form
                    .reset()
                this.overlay = false
                this.value = false
            }
        }
    }
</script>

<style></style>