<template>
  <v-col class="wantedview" cols="12" md="6">
    <div class="main-box">
      <div class="main-box-h-div">
        <h5 class="mb-0 main-box-h p-1">인기순</h5>
      </div>
      <v-carousel v-if="popularList.length" hide-delimiters :show-arrows="false" cycle interval="2500" height="400">
        <v-carousel-item v-for="j in 2" :key="j">
          <v-container white>
            <v-row class="main-list" id="wanted" v-for="i in 5" :key="i" style="height:78px;" @click="goDetailPage(popularList[5*(j-1)+i-1].wanted.wid)">
              <v-col cols="3">
                <v-img v-if="popularList[5*(j-1)+i-1].company.logo" :src="popularList[5*(j-1)+i-1].company.logo" :alt="popularList[5*(j-1)+i-1].company.corpNm" :contain="true" max-width="150" aspect-ratio="2.67"></v-img>
                <v-img v-else :src="getImgUrl('noimg.png')" alt="noimg" max-width="150" aspect-ratio="2.67" :contain="true"></v-img>
                </v-col>
              <v-col cols="9">
                <v-row id="corpNm">{{popularList[5*(j-1)+i-1].company.corpNm}}</v-row>
                <v-row id="wantedTitle">{{popularList[5*(j-1)+i-1].wanted.wantedTitle}}</v-row>
              </v-col>
            </v-row>
          </v-container>
        </v-carousel-item>
      </v-carousel>
    </div>
  </v-col>
</template>

<script>
import router from '../../../router'

export default {
  name: "recruitpopularlist",
  props:{
    popularList:{type:Array}
  },
  methods:{

    getImgUrl(img) {
      return require('../../../assets/' + img)
    },
    goDetailPage(wid){
      router.push({name:'recruitdetail',params:{id:wid}})
    }
	},
}
</script>

<style>
#corpNm{
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}
#wantedTitle{
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}
#wanted:hover{
  background-color: #eeeeee;
}
/* .main-box {
  border: 1px solid rgba(0, 170, 179);
  background: white;
  box-shadow: 1px 1px 1px 1px #cccccc;
  border-radius: 5px;
}
.main-box-h {
  color: white;
  text-align: center;
}
.main-box-h-div {
  position: relative;
  top: -30px;
  background: rgba(0, 170, 179);
  display: inline-block;
  padding: 4px;
  border-radius: 5px;
} */
</style>