<template>
  <v-container class="detailcompanyinfo white py-0">
    <v-row><v-col class="pb-0"><h2>기업정보</h2></v-col></v-row>
    <v-row>
      <v-col cols="4" sm="2" class="my-auto">
        <v-img v-if="company.logo" :src="company.logo" :alt="company.corpNm" aspect-ratio="1" :contain="true" style="border-color:#dddddd; border-style: solid; border-width: thin"></v-img>
        <v-img v-else :src="getImgUrl()" alt="noimg" aspect-ratio="1" :contain="true" style="border-color:#dddddd; border-style: solid; border-width: thin"></v-img>
      </v-col>
      <v-col cols="12" sm="10" justify="center">
        <h3>{{company.corpNm}}</h3>
        <br>
        <p v-if="company.homePg">홈페이지 : <a :href="company.homePg" style="text-decoration: none; color:black;">{{company.homePg}}</a></p>
        <p>기업주소 : {{company.corpAddr}}</p>
        <p>주요사업 : {{company.busiCont}}</p>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="3">
        <v-card class="mx-0" outlined color=white>
          <v-list-item class="wanted_info_vtn">
            <v-list-item-content>
              <v-list-item-title two-line>
                {{company.busiSize}}
              </v-list-item-title>
              <v-list-item-subtitle>기업형태</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-card>
      </v-col>
      <v-col cols="12" md="3">
        <v-card class="mx-0" outlined color=white>
          <v-list-item class="wanted_info_vtn">
            <v-list-item-content>
              <v-list-item-title two-line>
                <p class="mb-0" v-if="company.totPsncnt">{{company.totPsncnt}}</p>
                <p class="mb-0" v-else>-</p>
              </v-list-item-title>
              <v-list-item-subtitle>사원수</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-card>
      </v-col>
      <v-col cols="12" md="3">
        <v-card class="mx-0" outlined color=white>
          <v-list-item class="wanted_info_vtn">
            <v-list-item-content>
              <v-list-item-title two-line>
                <p class="mb-0" v-if="company.yrSalesAmt">{{company.yrSalesAmt}}</p>
                <p class="mb-0" v-else>-</p>
                
              </v-list-item-title>
              <v-list-item-subtitle>매출</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-card>
      </v-col>
    </v-row>
    <v-divider></v-divider>
  </v-container>
</template>

<script>
export default {
  name: "detailcompanyinfo",
  props:
  {
    company:{type:Object}
  },
  methods:{
    getImgUrl() {
      return require('../../assets/noimg.png')
    },
  },
  data(){
    return{
      on : false
    }
  }
}
</script>

<style>
.wanted_info_vtn:hover {
  background-color: white!important; 
}
</style>