import Vue from 'vue'
import VueRouter from 'vue-router'
import Mypage from '../views/Mypage.vue'
import Admin from '../views/Admin.vue'
import NotFound from '../views/NotFound.vue'
import QnA from '../views/QnA.vue'
import RecruitMain from '../views/RecruitMain.vue'
import RecruitDetail from '../views/RecruitDetail.vue'
import RecruitCalender from '../views/RecruitCalender.vue'
import StudyMain from '../views/StudyMain.vue'
import SearchFilter from '../components/SearchResult/SearchFilter.vue'
import SearchData from '../views/SearchData.vue'
import UserModify from "../components/Mypage/UserModify.vue"
import AboutUs from '../components/Aboutus/AboutUs.vue'
import store from '../store/index.js'
//import { cilViewStream } from '@coreui/icons'
Vue.use(VueRouter)
// 관리자가 0 , 기본 1
const rejectAuthUser = (to, from, next)=>
{
  //새로운 페이지로 들어올 때 오류가 있구나, 
  // this.$store.dispatch("getMemberInfo");
  if(store.state.isManager === false)   
  { 
    alert("관리자페이지입니다.")
    next("/") //from에서 / (home,to기능)으로 간다. 
  } 
  else 
  {
    next() //to=> from으로 간다. 
  }
}


//후에 마이페이지나 관리자를 만들 때 쓰자.
// const onlyAuthUser = (to, from, next)=>
// {
//   if(store.state.userInfo.auth ==! 1) 
//   {
//     alert("관리자 페이지입니다.")
//     next("/") //from에서 / (home,to기능)으로 간다. 
//   } 
//   else {
//     next() //to=> from으로 간다. 
//   }
// }

const routes = [
  {
    path: '*',
    name: 'notfound',
    component: NotFound
  },
  {
    path: '/',
    name: 'recruitmain',
    component: RecruitMain
  },
  {
    path: '/recruit/detail/:id',
    name: 'recruitdetail',
    component: RecruitDetail
  },
  {
    path: '/recruitlist',
    name: 'recruitcalender',
    component: RecruitCalender
  },
  {
    path: '/mypage',
    name: 'mypage',
    component: Mypage
  },
  {
    path: '/admin',
    name: 'admin',
    // 로그인이 이미 되었는데, 들어갈필요없가 없으니,  라우터 전에 확인하는 것이다. 
    beforeEnter : rejectAuthUser, 
    component: Admin
  },
  { 
    path: "/qna",
    name: "QnA",
    component: QnA
  },
  { 
    path: "/study",
    name: "studymain",
    component: StudyMain
  },
  {
    path: "/searchdata/:type",
    name: "searchdata",
    component: SearchData
  },

  {
    path: "/searchfilter",
    name: "searchfilter",
    component: SearchFilter
  },
  { 
    path: "/login",
    name: "login",
    // beforeEnter 로그인이 이미 되었는데, 들어갈필요없가 없으니,  라우터 전에 확인하는 것이다. 
    beforeEnter: rejectAuthUser, 
    component: ()=>
    import("../views/RecruitMain.vue")
  },
  {
    path: "/usermodify/:userInfo",
    name: "usermodify",
    component: UserModify

  },
  {
    path: "/aboutus",
    name: "aboutus",
    component: AboutUs
  }

  ]
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
