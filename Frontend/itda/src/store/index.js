import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios"
import firebase from 'firebase'

//import router from '../router/index.js';
Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    userInfo: null, // 필요한 이유는 계속 이메일과 패스워드를 확인 할 수 없으니까, 
    // selectedUser가 allUsers에 찾은 사람을 객체로 userInfo를 저장한다. 
    isLogin: false,
    isLoginError: false,
    isDialog: true,
    stacklist: [
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
    recruitList:[],
  },
  //뮤테이션과 엑션스 차이는? 
  //뮤테이션 : state 값 변경. 
  //엑션스 : 비지니스 로직이다.
  mutations: {
    //상태값 변화
    // 로그인이 성공했을 때, 
    loginSuccess(state, payload) {
      state.isLogin = true
      state.isLoginError = false
      state.isDialog = false
      state.userInfo = payload
    },
    FisrtloginSocialSuccess(state)
    {
      alert("먼저와라")
      state.isLogin = true
      alert(state.isLogin)
    },
    //로그인이 실패했을 때.
    loginError(state) {
      state.isLogin = false
      state.isLoginError = true
      state.isDialog = true
    },
    logout(state) {
      state.isLogin = false
      state.isLoginError = false
      state.isDialog = true
      state.userInfo = null
      localStorage.clear()
    },
    set_name(state, name)
    {
      state.name =name
    },
    setAllRecruit(state, response){
      state.recruitList = response.data 
    }
  },
  actions:
  {
    login({ dispatch }, { email, pw }) {
      axios
        .post('https://i02b201.p.ssafy.io:8197/itda/api/login', {
          email, pw
        })
        .then(res => {
          if (res.data.state == 'success') {
            let token = res.headers['jwt-auth-token']
            localStorage.setItem("access_token", token)
            localStorage.setItem("uname", res.data.user.uname)
            dispatch("getMemberInfo")
          }
          else {
             return;
          }
        })
    },
    logout({ commit }) {
      commit("logout")
      if(localStorage.getItem("social") == 'social'){
        
        firebase.auth().signOut()
          .then(()=>{
            alert("성공적으로 로그아웃 되었습니다.")
          })
      }
      else{
        alert("성공적으로 로그아웃 되었습니다.")
      }
    },
    getMemberInfo({commit}) {
      let token = localStorage.getItem("access_token")
      if(!token){
        return;
      }
      axios.get("https://i02b201.p.ssafy.io:8197/itda/api/getUser", {headers:{"jwt-auth-token": token}})
        .then(res => {
          let userInfo = res.data
          commit('loginSuccess', userInfo)
          localStorage.setItem("mid",userInfo.user.auth)
          
        })
        .catch(() => {
          localStorage.clear();
          alert("로그인 하시겠어요 ?!")
        })
    },
  },
  
})
