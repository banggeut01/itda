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
                                                <v-checkbox class="mt-0" v-model="stacklist[3*(i-1) + j-1].value">
                                                </v-checkbox>
                                            </div>
                                            <div class="pa-0 mx-auto" style="position:relative;left:-15px">
                                                <v-img class="mx-auto" height="20" width="20"
                                                    :src="getImgUrl(3*(i-1) + j)" aspect-ratio="1" contain></v-img>
                                                <p class="text-center">{{stacklist[3*(i-1) + j-1].tname}}</p>
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
                imgsrc1: [
                    '../../assets/stacks/1.png',
                    '../../assets/stacks/2.png',
                    '../../assets/stacks/3.png',
                    '../../assets/stacks/4.png',
                    '../../assets/stacks/5.png',
                    '../../assets/stacks/6.png',
                    '../../assets/stacks/7.png',
                    '../../assets/stacks/8.png',
                    '../../assets/stacks/9.png',
                    '../../assets/stacks/10.png',
                    '../../assets/stacks/11.png',
                    '../../assets/stacks/12.png',
                    '../../assets/stacks/13.png',
                    '../../assets/stacks/14.png',
                    '../../assets/stacks/15.png',
                    '../../assets/stacks/16.png',
                    '../../assets/stacks/17.png',
                    '../../assets/stacks/18.png',
                    '../../assets/stacks/19.png',
                    '../../assets/stacks/20.png',
                    '../../assets/stacks/21.png',
                    '../../assets/stacks/22.png',
                    '../../assets/stacks/23.png',
                    '../../assets/stacks/24.png',
                    '../../assets/stacks/25.png',
                    '../../assets/stacks/26.png',
                    '../../assets/stacks/27.png',
                    '../../assets/stacks/28.png',
                    '../../assets/stacks/29.png',
                    '../../assets/stacks/30.png',
                    '../../assets/stacks/31.png',
                    '../../assets/stacks/32.png',
                    '../../assets/stacks/33.png',
                    '../../assets/stacks/34.png',
                    '../../assets/stacks/35.png',
                    '../../assets/stacks/36.png',
                    '../../assets/stacks/37.png',
                    '../../assets/stacks/38.png',
                    '../../assets/stacks/39.png',
                    '../../assets/stacks/40.png',
                    '../../assets/stacks/41.png'
                ]
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
                for(var i=0; i<this.stacklist.length; i++){
                    if(this.stacklist[i].value){
                        temp.push(this.stacklist[i].sid)
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
            ...mapState(["stacklist", "recruitList"]),
            getImgUrl() {
                return (img) => {
                    return require("../../assets/stacks/" + img + '.png')
                }
            },
        }
    }
</script>

<style></style>