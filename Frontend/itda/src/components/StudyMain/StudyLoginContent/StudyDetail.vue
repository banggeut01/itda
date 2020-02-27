<template>
  <v-col class="studydetail" cols="12" md="3" v-if="study.stid">
    <v-hover v-slot:default="{ hover }">
      <v-card light align-center height="200" :elevation="hover ? 16 : 2" 
      class="mx-auto " @click.prevent="goDetail(study.stid)"
      id="study-card">
        <v-container class="py-0" >
          <v-row class="pt-2 px-0">
            <p class="mx-auto px-2 title mb-0" id="cardtitle">{{study.stname}}</p>
          </v-row>
        </v-container>
        <v-divider class="my-0"></v-divider>
        <v-container class="pb-0">
            <span class="mx-1" style="color: grey;">{{category1[study.stype-1]}}</span>
            <p class="mx-1 my-4" id="p-typename">{{study.typeName}}</p>
        </v-container>  
          <div class="card-btm">
            <span v-if="study.sgroup == 1" class="mx-1 pa-2 white--text stype-span" id="sg1">{{category2[study.sgroup-1]}}</span>
            <span v-else-if="study.sgroup == 2" class="mx-1 pa-2 white--text stype-span" id="sg2">{{category2[study.sgroup-1]}}</span>
            <span v-else-if="study.sgroup == 3" class="mx-1 pa-2 white--text stype-span" id="sg3">{{category2[study.sgroup-1]}}</span>
            <span v-else-if="study.sgroup == 4" class="mx-1 pa-2 white--text stype-span" id="sg4">{{category2[study.sgroup-1]}}</span>
            <span class="span-pcnt" style="color: grey;">{{study.pcnt}}/{{study.maxPcnt}} </span>
          </div>
      </v-card>
    </v-hover>
  </v-col>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
import router from '../../../router'

export default {
    name:'studydetail',
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
    computed:{
      ...mapState(["isLogin", "userInfo"]),
      ismember(){
            for (var i=0;i<this.person.length;i++){
                if(this.person[i].uid === this.userInfo.user.uid)
                    return true
            }
            return false
        },
    },
    methods:{
      getStudyDetail(id){
        if (id) {
          axios.get('https://i02b201.p.ssafy.io:8197/itda/api/getStudyGroup/'+id)
              .then(response => {this.person = response.data})
        }
      },
      goDetail(id){
        router.push({name:'studydetail', params:{id:id}})
      }
    },
    mounted(){
        this.getStudyDetail(this.study.stid)
    },
    
}
</script>

<style>
#cardtitle{
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}

#p-typename {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
} 

.stype-span {
  border-radius: 5px;
}
.span-pcnt {
  position: absolute;
  right: 20px;
}
.card-btm {
  width: 100%;
  position: absolute;
  left: 10px;
  bottom: 20px;
}

#sg1{
  background: #EF476F;
}

#sg2{
  background: #FFD166;
}

#sg3{
  background: #06D6A0;
}

#sg4{
  background: #118AB2;
}

</style>