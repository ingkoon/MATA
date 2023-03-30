<template>
    <div class="form full-form auth-cover">
        <div class="form-container">
            <div class="form-form">
                <div class="form-form-wrap">
                    <div class="form-container">
                        <div class="form-content">
                            <h1 class="">
                                Log In to <router-link to="/"><span class="brand-name">MATA</span></router-link>
                            </h1>
                            <p class="signup-link">새로 오셨나요? <router-link to="/auth/register">계정 생성하기</router-link></p>
                            <form class="text-start" @submit.prevent="logIn">
                                <div class="form">
                                    <div id="email-field" class="field-wrapper input">
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="24"
                                            height="24"
                                            viewBox="0 0 24 24"
                                            fill="none"
                                            stroke="currentColor"
                                            stroke-width="2"
                                            stroke-linecap="round"
                                            stroke-linejoin="round"
                                            class="feather feather-at-sign"
                                        >
                                            <circle cx="12" cy="12" r="4"></circle>
                                            <path d="M16 8v5a3 3 0 0 0 6 0v-1a10 10 0 1 0-3.92 7.94"></path>
                                        </svg>
                                        <input type="text" class="form-control" placeholder="이메일" v-model="email" />
                                    </div>

                                    <div id="password-field" class="field-wrapper input mb-2">
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="24"
                                            height="24"
                                            viewBox="0 0 24 24"
                                            fill="none"
                                            stroke="currentColor"
                                            stroke-width="2"
                                            stroke-linecap="round"
                                            stroke-linejoin="round"
                                            class="feather feather-lock"
                                        >
                                            <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                                            <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                                        </svg>
                                        <input type="password" class="form-control" placeholder="패스워드" v-model="password"/>
                                    </div>
                                    <div class="d-sm-flex justify-content-between">
                                        <div class="field-wrapper toggle-pass d-flex align-items-center">
                                            <p class="d-inline-block">비밀번호 보이기</p>
                                            <label class="switch s-primary mx-2">
                                                <input type="checkbox" class="custom-control-input" checked="" />
                                                <span class="slider round"></span>
                                            </label>
                                        </div>
                                        <div class="field-wrapper">
                                            <button type="submit" class="btn btn-primary">로그인</button>
                                        </div>
                                    </div>

                                    <div class="field-wrapper text-center keep-logged-in">
                                        <div class="checkbox-outline-primary custom-control custom-checkbox">
                                            <input type="checkbox" class="custom-control-input" value="true" id="chkRemember" />
                                            <label class="custom-control-label" for="chkRemember">로그인 유지하기</label>
                                        </div>
                                    </div>

                                    <div class="field-wrapper">
                                        <router-link to="/auth/pass-recovery" class="forgot-pass-link">비밀번호 찾기</router-link>
                                    </div>
                                </div>
                            </form>
<!--                            <p class="terms-conditions">-->
<!--                                © 2023 All Rights Reserved. <router-link to="/">MATA</router-link> is a product of Arrangic Solutions LLP. <a href="javascript:void(0);">Cookie Preferences</a>,-->
<!--                                <a href="javascript:void(0);">Privacy</a>, and <a href="javascript:void(0);">Terms</a>.-->
<!--                            </p>-->
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-image">
                <div class="l-image"></div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import '@/assets/sass/authentication/auth.scss';

    import { useMeta } from '@/composables/use-meta';
    useMeta({ title: 'Login Cover' });
</script>

<script>
export default {
  name: 'loginView',
  methods:{
    logIn(){
      
      const email=this.email
      const password=this.password
      
      const payload={
        email: email,
        password: password,
      }
      console.log(payload)
      this.$store.dispatch('logIn',payload)
    },
  },
  data(){
    return{
      name:null,
      email:null,
      password:null,
      password2:null,
    }
  }

}
</script>
