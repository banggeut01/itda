<template>
    <v-container>
        <v-col>
            <v-row justify="center">
                <v-col cols="12" sm="4">
                    <v-select color="#00AAB3" v-model="period" :items="periodOptions" attach="attach" chips="chips"
                        label="기간" multiple="multiple" @change="optionmanager"></v-select>
                </v-col>
                <v-col cols="12" sm="4">
                    <v-select color="#00AAB3" v-model="recruit" :items="recruitmentOptions" attach="attach"
                        chips="chips" label="채용 전형" multiple="multiple" @change="optionrecruit"></v-select>
                </v-col>
                <v-col align-self="center" cols="12" sm="4">
                    <v-btn @click.stop="dialog=true" outlined color="teal" class="mx-3 btn-border-colored"
                        width="100%">
                        기술 스택
                    </v-btn>
                    <v-dialog v-model="dialog" max-width="500px">
                        <v-card width="500px">
                            <v-card-title>기술 스택</v-card-title>
                            <v-divider></v-divider>
                            <v-card-text>
                                <v-row v-for="i in 14" :key="i">
                                    <v-col class="py-0 pr-0" v-for="j in i==14?2:3" :key="j">
                                        <v-row>
                                            <div class="pa-0">
                                                <v-checkbox class="mt-0" v-model="stackList[3*(i-1) + j-1].value">
                                                </v-checkbox>
                                            </div>
                                            <div class="pa-0 mx-auto" style="position:relative;left:-15px">
                                                <v-img class="mx-auto" height="20" width="20"
                                                    :src="getImgUrl(3*(i-1) + j)" aspect-ratio="1" contain></v-img>
                                                <p class="text-center">{{stackList[3*(i-1) + j-1].tname}}</p>
                                            </div>
                                        </v-row>
                                    </v-col>
                                </v-row>
                            </v-card-text>
                            <v-divider></v-divider>
                            <v-spacer></v-spacer>
                            <v-card-actions class="mb-3">
                                <v-spacer></v-spacer>
                                <v-btn color="#00AAB3" text="text" @click="getStackList">저장</v-btn>
                                <v-btn color="black darken-1" text="text" @click="close">닫기</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </v-col>
            </v-row>
            <recruit-calender-content v-if="recruitList.length" :recruitList="recruitList" :options="options" />
        </v-col>
    </v-container>
</template>

<script>
    import {
        mapState
    } from 'vuex'
    import RecruitCalenderContent from "./RecruitCalenderContent.vue"
    export default {
        name: "recruitcalenderfilter",
        components: {
            RecruitCalenderContent
        },
        data() {
            return {
                overlay: false,
                checkedNames: [],
                options: {
                    period: 3,
                    recruit: 3,
                    selectstacklist: [],
                },
                year: "",
                period: [],
                techstack: [],
                recruit: [],
                periodOptions: [{
                    text: '시작',
                    value: '시작',
                }, {
                    text: '종료',
                    value: '종료',
                }],
                recruitmentOptions: [{
                    text: '인턴',
                    value: '인턴',

                }, {
                    text: '신입',
                    value: '신입',
                }],
                dialog: false,
                stackList: [
                            {
                                sid: 1,
                                tname: "Java",
                                cnt: 5,
                                value: false
                            },
                            {
                                sid: 2,
                                tname: "C",
                                cnt: 23,
                                value: false
                            },
                            {
                                sid: 3,
                                tname: "C++",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 4,
                                tname: "C#",
                                cnt: 2,
                                value: false
                            },
                            {
                                sid: 5,
                                tname: "Objective-C",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 6,
                                tname: "Python",
                                cnt: 4,
                                value: false
                            },
                            {
                                sid: 7,
                                tname: "PHP",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 8,
                                tname: "JavaScript",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 9,
                                tname: "Kotlin",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 10,
                                tname: "Unix",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 11,
                                tname: "Linux",
                                cnt: 3,
                                value: false
                            },
                            {
                                sid: 12,
                                tname: "Windows",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 13,
                                tname: "Android",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 14,
                                tname: "iOS",
                                cnt: 2,
                                value: false
                            },
                            {
                                sid: 15,
                                tname: "MySQL",
                                cnt: 3,
                                value: false
                            },
                            {
                                sid: 16,
                                tname: "MS-SQL",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 17,
                                tname: "Oracle",
                                cnt: 2,
                                value: false
                            },
                            {
                                sid: 18,
                                tname: "MariaDB",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 19,
                                tname: "MongoDB",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 20,
                                tname: "SQLite",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 21,
                                tname: "NoSQL",
                                cnt: 2,
                                value: false
                            },
                            {
                                sid: 22,
                                tname: "HTML",
                                cnt: 3,
                                value: false
                            },
                            {
                                sid: 23,
                                tname: "CSS",
                                cnt: 2,
                                value: false
                            },
                            {
                                sid: 24,
                                tname: "Vue.js",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 25,
                                tname: "jQuery",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 26,
                                tname: "Ajax",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 27,
                                tname: "Spring",
                                cnt: 3,
                                value: false
                            },
                            {
                                sid: 28,
                                tname: "Django",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 29,
                                tname: "Flask",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 30,
                                tname: "Node.js",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 31,
                                tname: "JSP",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 32,
                                tname: "JPA/Hibernate",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 33,
                                tname: "WAS",
                                cnt: 2,
                                value: false
                            },
                            {
                                sid: 34,
                                tname: "AWS",
                                cnt: 5,
                                value: false
                            },
                            {
                                sid: 35,
                                tname: "OOP",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 36,
                                tname: "AI",
                                cnt: 2,
                                value: false
                            },
                            {
                                sid: 37,
                                tname: "Machine Learning",
                                cnt: 1,
                                value: false
                            },
                            {
                                sid: 38,
                                tname: "Docker",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 39,
                                tname: "RESTful-API",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 40,
                                tname: "JIRA",
                                cnt: 0,
                                value: false
                            },
                            {
                                sid: 41,
                                tname: "Git",
                                cnt: 0,
                                value: false
                            }
                        ],
            }
        },
        methods: {

            optionmanager() {
                if (this.period.length == 1) {
                    if (this.period[0] == "시작") {
                        this.options.period = 1
                    } else {
                        this.options.period = 2
                    }
                } else {
                    this.options.period = 3
                }
            },
            // 1 신입만, 2 인턴, 3 신입인턴
            optionrecruit() {
                if (this.recruit.length == 1) {
                    if (this.recruit[0] == "신입") {
                        this.options.recruit = 1
                    } else {
                        this.options.recruit = 2
                    }
                } else {
                    this.options.recruit = 3
                }
            },
            getStackList(){
                var temp = []
                for(var i=0; i<this.stackList.length; i++){
                    if(this.stackList[i].value){
                        temp.push(this.stackList[i].sid)
                    }
                }
                this.options.selectstacklist = temp
                this.dialog = false
            },
            close(){
                this.dialog = false
            }
        },
        computed: {
            ...mapState(["recruitList"]),
            getImgUrl() {
                return (img) => {
                    return require("../../assets/stacks/" + img + '.png')
                }
            },
        }
    }
</script>

<style></style>