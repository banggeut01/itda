import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios"
//import router from '../router/index.js';
Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    userInfo: null, // 필요한 이유는 계속 이메일과 패스워드를 확인 할 수 없으니까, 
    // selectedUser가 allUsers에 찾은 사람을 객체로 userInfo를 저장한다. 
    isLogin: false,
    isLoginError: false,
    isDialog: true,
    isManager: false, 
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
      state.isManager = false;
      localStorage.clear()
    },
    managerlogin(state)
    {
      state.isManager = true;
    },
  },
  actions:
  {
    login({ dispatch }, { email, pw }) {
      axios
        .post('https://192.168.31.54:8197/itda/api/login', {
          email, pw
        })
        .then(res => {
          if (res.data.state == 'success') {
            let token = res.headers['jwt-auth-token']
            localStorage.setItem("access_token", token)
            localStorage.setItem("uid", res.data.uid)
            dispatch("getMemberInfo")
          }
          else {
            return;
          }
        })
        .catch(err => {
          console.log("hi" + err);
          alert("에러!!!")
        });
    },
    logout({ commit }) {
      alert("성공적으로 로그아웃 되었습니다.")

      commit("logout")
    }
    ,
    getMemberInfo({ commit }) {
      let token = localStorage.getItem("access_token")
      if(!token){
        return;
      }
      let config =
      {
        headers:
        {
          "jwt-auth-token": token
        }
      }
      axios
        .get("https://192.168.31.54:8197/itda/api/getUser", config)
        .then(res => {
          let userInfo = res.data
          
          commit('loginSuccess', userInfo)
          if( userInfo.user.auth === 0 )
          {
            commit("managerlogin")
          }
        })
        .catch(() => {
          localStorage.clear();
          alert("다시 로그인해주세요!")
        })
    }
  },
})
