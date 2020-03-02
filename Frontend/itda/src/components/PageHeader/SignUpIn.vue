<template>
  <!-- 컨테이너 크기를 500으로해 해주고, fill-heigt를 통해서아래 align center를 이용할 수 있게 됨. -->
  <v-layout wrap="no wrap">
    <v-overlay :value="overlay">
        <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
    <v-flex xs12="xs12">
      <v-alert class="mb-1" :value="reChk" type="error">아이디와 비밀번호를 확인해주세요.</v-alert>
      <v-alert
        class="mb-1"
        v-model="isPwError"
        :value="isPwError"
        type="error"
      >이메일 형식, 비밀번호를 확인해주세요.</v-alert>
      <v-alert class="mb-1" v-model="isFormError" :value="isFormError" type="error">입력 값을 모두 넣어주세요.</v-alert>
      <v-alert
        class="mb-1"
        v-model="isRegisterError"
        :value="isRegisterError"
        type="error"
      >이미 존재하는 회원의 ID 입니다.</v-alert>
      <v-alert class="mb-1" :value="isCong" type="success">회원가입을 축하합니다! 로그인 해주세요.</v-alert>
      <v-card v-if="isChangeLoginRegi">
        <v-toolbar flat="flat">
          <v-toolbar-title>회원가입</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon dark @click="$emit('statusControl',false)">
            <v-icon color="black">mdi-close</v-icon>
          </v-btn>
          <!-- <v-btn color="green darken-1" text="text" @click="dialog = false">Close</v-btn> -->
        </v-toolbar>
        <div class="pa-4" style="max-width: 540px" name="regi">
          <v-text-field v-model="name" :value="name" label="이름*"></v-text-field>
          <v-text-field v-model="email_rg" :value="email_rg" label="이메일*"></v-text-field>
          <v-row>
            <v-col cols="6">
              <v-text-field
                v-model="password_rg"
                :value="password_rg"
                type="password"
                label="비밀번호*"
              ></v-text-field>
            </v-col>
            <v-col cols="6">
              <v-text-field
                v-model="password_rg1"
                :value="password_rg1"
                type="password"
                label="비밀번호 확인*"
                @keyup.enter="goRegister()"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-layout>
            <v-btn
              class="mb-1"
              color="rgba(0, 170, 179)"
              depressed="depressed"
              block="block"
              large="large"
              @click="goRegister()"
              style="color:white;"
            >회원가입</v-btn>
          </v-layout>
          <v-layout>
            <v-btn
              class="mb-1"
              depressed="depressed"
              block="block"
              large="large"
              @click="isChangeLoginRegi = !isChangeLoginRegi; goInit()"
            >취소</v-btn>
          </v-layout>
        </div>
      </v-card>
      <v-card v-else>
        <v-toolbar flat="flat">
          <v-toolbar-title>로그인</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon dark @click="$emit('statusControl',false)">
            <v-icon color="black darken-2">mdi-close</v-icon>
          </v-btn>
          <!-- <v-btn color="green darken-1" text="text" @click="$emit.dialog = !$emit.dialog">Close</v-btn> -->
        </v-toolbar>
        <div class="pa-4" style="max-width: 540px">
          <!-- pa는 패딩오토 -->
          <v-text-field v-model="email_lg" 
          @keyup.enter="goLogin()"
          label="이메일을 입력하세요"></v-text-field>
          <v-text-field
            v-model="password_lg"
            type="password"
            label="비밀번호를 입력하세요"
            @keyup.enter="goLogin()"
          ></v-text-field>
          <v-btn
            class="mb-1"
            color="rgba(0, 170, 179)"
            depressed="depressed"
            block="block"
            dark
            large="large"
            @click="login(
              {
                  email: email_lg,
                  pw: password_lg
              },
                 isCong= false
              )
              goLogin()" 
          >로그인</v-btn>
          <!-- social login -->
          <v-btn
            class="mb-1"
            color="#dd4b39"
            dark
            depressed="depressed"
            block="block"
            large="large"
            @click="socialLogin()"
          >Google 계정으로 로그인</v-btn>
          <v-btn
            class="mb-1"
            depressed="depressed"
            block="block"
            large="large"
            @click="isChangeLoginRegi = !isChangeLoginRegi; goInit('isChangeLoginRegi')"
          >회원가입</v-btn>
        </div>
      </v-card>
    </v-flex>
  </v-layout>
</template>
<script>
import { mapState, mapActions, mapMutations } from "vuex";
import axios from "axios"
import router from "../../router"
import firebase from "firebase";

export default {
  data() {
    return {
      reChk: false,
      isPwError: false,
      isRegisterError: false,
      isFormError: false,
      isChangeLoginRegi: false,
      isCong: false,
      email_rg: "",
      email_lg: "",
      password_rg: "",
      password_rg1: "",
      password_lg: "",
      name: "",
      dialog: false,
      token: localStorage.getItem("access_token"),
      overlay:false,
    };
  },
  beforecreated: {
    ...mapState(["isLogin", "isDialog"])
  },
  components: {},
  methods: {
    goRegister() {
      //회원가입.
      if (
        this.name == null ||
        this.email_rg == null ||
        this.password_rg == null ||
        this.password_rg1 == null
      ) {
        this.isFormError = true;
        this.isregisterError = false;
        this.isPwError = false;
      } else if (
        this.email_rg.indexOf("@") == -1 ||
        this.email_rg.indexOf("@") == 0 ||
        this.email_rg.length === this.email_rg.indexOf("@") + 1 ||
        this.password_rg !== this.password_rg1
      ) {
        this.isFormError = false;
        this.isRegisterError = false;
        this.isPwError = true;
      }
      else {
        axios
          .post("https://i02b201.p.ssafy.io:8197/itda/api/signUp", {
            email: this.email_rg,
            pw: this.password_rg,
            uname: this.name
          })
          .then(res => {
            if (res.data.state == "success") {
              this.isChangeLoginRegi = !this.isChangeLoginRegi;
              this.isFormError = false;
              this.isRegisterError = false;
              this.isPwError = false;

              this.isCong = !this.isCong;
            } else {
              this.isPwError = false;
              this.isFormError = false;
              this.isRegisterError = true;
              return;
            }
          })
      }
    },
    ...mapActions(["login", "register", ]),
    ...mapMutations(["FisrtloginSocialSuccess"]),
    goInit() {
      //제출 후 초기화.
        (this.email_lg = null),
        (this.password_lg = null),
        (this.name = null),
        (this.email_rg = null),
        (this.password_rg = null),
        (this.password_rg1 = null);
      if (this.isChangeLoginRegi) {
        this.reChk = false;
        this.isCong = false;
          this.isFormError = false;
      } else {
        this.isRegisterError = false;
        this.isPwError = false;
        this.isFormError = false;
      }
    },
    goLogin() {
      if (
        this.email_lg == '' ||
        this.password_lg== ''
      ) {
        this.isFormError = true;
      } 
      this.$store.dispatch("login", {
        email: this.email_lg,
        pw: this.password_lg
      });

    },
    socialLogin() {
      const provider = new firebase.auth.GoogleAuthProvider();
      this.overlay = true
      firebase
        .auth()
        .signInWithPopup(provider)
        .then(result => {
          var social_email = result.user.email;
          var social_uid = result.user.uid;
          var social_name = result.user.displayName;
          axios
            .get(
              "https://i02b201.p.ssafy.io:8197/itda/api/emailCheck/" + social_email
            )
            .then(response => {
              if (response.data.state == "fail") {
                //이미 있는 email이라면?
                axios
                  .post("https://i02b201.p.ssafy.io:8197/itda/api/login", {
                    email: social_email,
                    pw: social_uid
                  })
                  .then(response => {
                    if (response.data.state == "fail") {
                      //로그인 실패면 이미 해당 이메일로 회원가입한 적이 있을 것!
                      alert("해당 email로 가입한 적이 있습니다!");
                      this.overlay = true
                      return;
                    } else {
                      let token = response.headers["jwt-auth-token"]
                      localStorage.setItem("access_token", token)
                      localStorage.setItem("social", "social")
                      axios.get("https://i02b201.p.ssafy.io:8197/itda/api/getUser", {headers:{"jwt-auth-token": token}})
                          .then(res => {
                            let userInfo = res.data
                            this.$store.commit('loginSuccess', userInfo)
                            localStorage.setItem("mid",userInfo.user.auth)
                            this.overlay = false
                            router.push({'name':'recruitmain'}).catch(()=>{})
                          })
                    }
                  });
              } else {
                //회원가입 시킨 뒤 로그인 시키자.
                axios
                  .post("https://i02b201.p.ssafy.io:8197/itda/api/signUp", {
                    email: social_email,
                    pw: social_uid,
                    uname: social_name
                  })
                  .then(response => {
                    if (response.data.state == "success") {
                      axios
                        .post("https://i02b201.p.ssafy.io:8197/itda/api/login", {
                          email: social_email,
                          pw: social_uid
                        })
                        .then(response => {
                          if (response.data.state == "success") {
                            let token = response.headers["jwt-auth-token"]
                            localStorage.setItem("access_token", token)
                            localStorage.setItem("social", "social")
                            axios.get("https://i02b201.p.ssafy.io:8197/itda/api/getUser", {headers:{"jwt-auth-token": token}})
                            .then(res => {
                              let userInfo = res.data
                              this.$store.commit('loginSuccess', userInfo)
                              localStorage.setItem("mid",userInfo.user.auth)
                              this.overlay = false
                              router.push({'name':'recruitmain'}).catch(()=>{})
                            })
                          }
                        });
                    }
                  });
              }
            });
        })
        .catch(err => {
          alert("Google Login이 정상적으로 실행되지 않았습니다!" + err.message);
          this.overlay = false
        });
    }
  }
};
</script>