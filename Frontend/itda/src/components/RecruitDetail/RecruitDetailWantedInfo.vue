<template>
  <v-container class="detailwantedinfo white py-0">
    <v-divider></v-divider>
    <v-row>
      <div class="m-0">
        <v-btn v-if="wanted.active" text small class="wanted_info_active_btn m-3" style="color:white; cursor:default;">채용중</v-btn>
        <v-btn small text class="wanted_info_active_btn2 m-3" style="color:white;cursor:default;">{{ leftDate }}</v-btn>
        <span class="m-3" style="color:#aaaaaa;">{{wanted.startDate.substring(0, 16)}} ~ {{wanted.endDate.substring(0, 16)}}</span>
      </div>
    </v-row>
    <v-row>
      <h2 class="m-2">공고</h2>
    </v-row>
    <v-row>
      <v-col align-self="end">
        <v-btn v-for="stack in stacks" :key="stack.sid" style="color: white; background-color:rgba(0, 170, 179);"
          class="m-1" @click="goSearch(stack.tname)">
          #{{stack.tname}}
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col class="pb-0" v-html="wanted.detail" align-self="center"></v-col>
    </v-row>
  </v-container>
</template>

<script>
import router from '../../router'
export default {
  name: "detailwantedinfo",
  props: {
    wanted: {
      type: Object
    },
    stacks: {
      type: Array
    }
  },
  data() {
    return {
      leftDate: ''
    }
  },
  methods: {
    goSearch(content) {
      router.push( {
          name:"searchresult", query: {
            cont: content, type:3
          }
        }

      )
    },
  },
  mounted() {
    if (this.wanted.endDate.slice(0, 4)=="2037") {
      this.leftDate="상시"
    }
    else {
      var endday=new Date(this.wanted.endDate) 
      var today=new Date() 
      var datediff=parseInt((endday-today)/(24*60*60*1000)) 
      if (datediff > 0) 
        this.leftDate="D-"+datediff 
      else 
        if (datediff==0) 
          this.leftDate="D-day"
        else 
          this.leftDate="마감"
    } 
  }
}
</script>

<style>
.renewal_template_style4 {
    margin-left: auto;
    margin-right: auto;
  }
  .new_template_style4 {
    margin-left: auto;
    margin-right: auto;
  }
  .wanted_info_active_btn {
    background-color: #006064;
  }
  .wanted_info_active_btn2 {
    background-color: #00897B;
  }
</style>