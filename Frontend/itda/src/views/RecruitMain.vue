<template>
    <div class="recruitmain">
        <ImgBanner :imgSrc="bannerImg" />
        <div style="background-color:white;">
            <v-container>
                <recruit-recent-list v-on:update="update" />
            </v-container>
        </div>
        <v-container>
            <recruit-login-content v-if="isLogin" :loginContent="wantedlist.loginContent" />
            <recruit-default-content :defaultContent="wantedlist.defaultContent" />
        </v-container>
        <v-container v-if="isLogin">
            <v-row>
                <v-col>
                    <v-icon>edit</v-icon>
                    <span>&nbsp;내가 가입한 <b>스터디</b></span>
                </v-col>
            </v-row>
            <study-login-content :myStudyList="loginStudies" v-if="loginStudies.length" v-on:update="update" />
            <v-row v-else>
                <v-col>가입한 스터디가 없어요.</v-col>
            </v-row>
        </v-container>
    </div>
</template>
<script>
    import RecruitLoginContent from '../components/RecruitMain/RecruitLoginContent.vue'
    import RecruitDefaultContent from '../components/RecruitMain/RecruitDefaultContent.vue'
    import RecruitRecentList from '../components/RecruitMain/RecruitRecentList.vue'
    import StudyLoginContent from '../components/StudyMain/StudyLoginContent.vue'
    import ImgBanner from '../components/RecruitMain/ImgBanner.vue'
    import {
        mapState
    } from 'vuex'
    import axios from 'axios'

    export default {
        name: 'recruitmain',
        components: {
            RecruitLoginContent,
            RecruitDefaultContent,
            RecruitRecentList,
            StudyLoginContent,
            ImgBanner,
        },
        data() {
            return {
                wantedlist: {
                    loginContent: {
                        scrapList: [],
                        stackList: []
                    },
                    defaultContent: {
                        deadlineList: [],
                        popularList: []
                    }
                },
                loginStudies: [],
                bannerImg: [
                    this.getImgUrl('banner1.png'),
                    this.getImgUrl('banner2.png')
                ]
            }
        },
        methods: {
            update() {
                this.getLoginContent()
            },
            getImgUrl(img) {
                return require('../assets/' + img)
            },
            getLoginContent() {
                if (this.isLogin) {
                    axios.get(`https://i02b201.p.ssafy.io:8197/itda/api/getWantedByScrap/`, {
                            'headers': {
                                "jwt-auth-token": localStorage.getItem("access_token")
                            }
                        })
                        .then(response => {
                            this.wantedlist.loginContent.scrapList = response.data;
                        })
                        .catch(() => {});
                    axios.get(`https://i02b201.p.ssafy.io:8197/itda/api/getWantedByStack/`, {
                            'headers': {
                                "jwt-auth-token": localStorage.getItem("access_token")
                            }
                        })
                        .then(response => {
                            this.wantedlist.loginContent.stackList = response.data;
                        })
                        .catch(() => {});
                }
            },
            getDefaultContent() {
                axios.get(`https://i02b201.p.ssafy.io:8197/itda/api/getWantedByRecent/`)
                    .then(response => {
                        this.wantedlist.defaultContent.deadlineList = response.data
                    })
                    .catch(() => {})
                axios.get(`https://i02b201.p.ssafy.io:8197/itda/api/getWantedByView/`)
                    .then(response => {
                        this.wantedlist.defaultContent.popularList = response.data
                    })
                    .catch(() => {})
            },
            getLoginStudies() {
                if (this.isLogin) {
                    axios.get("https://i02b201.p.ssafy.io:8197/itda/api/getUser", {
                            headers: {
                                "jwt-auth-token": localStorage.getItem("access_token")
                            }
                        })
                        .then(response => {
                            this.loginStudies = response.data.myStudies
                            var len = this.loginStudies.length % 4
                            if (len) {
                                for (var i = 0; i < 4 - len; i++)
                                    this.loginStudies.push({})
                            }
                        })
                }
            },
        },
        computed: {
            ...mapState(["isLogin"])
        },
        mounted() {
            if (this.isLogin) {
                this.getLoginContent()
            }
            this.getDefaultContent()
        },
        watch: {
            isLogin: {
                deep: true,
                immediate: true,
                handler() {
                    this.getLoginContent()
                    this.getLoginStudies()
                }
            },
        }
    }
</script>