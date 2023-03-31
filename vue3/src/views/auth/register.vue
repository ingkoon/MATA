<template>
    <div class="form full-form auth-cover">
        <div class="form-container">
            <div class="form-form">
                <div class="form-form-wrap">
                    <div class="form-container">
                        <div class="form-content">
                            <h1 class="">
                                Get started in a <br />
                                <router-link to="/"><span class="brand-name">MATA</span></router-link>
                            </h1>
                            <p class="signup-link">계정이 있으신가요? <router-link to="/auth/login">로그인</router-link></p>
                            <form class="text-start" @submit.prevent="signUp">
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
                                        <input type="email" class="form-control" placeholder="이메일" v-model="email"/>
                                    </div>
                                    <div id="username-field" class="field-wrapper input" >
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
                                            class="feather feather-user"
                                        >
                                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                            <circle cx="12" cy="7" r="4"></circle>
                                        </svg>
                                        <input type="text" class="form-control" placeholder="이름" v-model="name" />
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
                                        <input type="password" class="form-control" placeholder="비밀번호" v-model="password" />
                                    </div>
                                    
                                    <div id="password2-field" class="field-wrapper input mb-2">
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
                                        <input type="password" class="form-control" placeholder="비밀번호 확인" v-model="password2"/>
                                    </div>

                                    <div class="d-sm-flex justify-content-between">
                                        <div class="field-wrapper">
                                            <button type="submit" class="btn btn-primary">가입하기</button>
                                        </div>
                                    </div>
                                </div>
                            </form>
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
    import { onMounted,computed } from 'vue';
    import { ref, reactive } from "vue";
    import { useMeta } from '@/composables/use-meta';
    import { defineEmits, defineProps } from "vue";
    useMeta({ title: 'Register Cover' });
    onMounted(()=>{console.log('hi')});
    import { useStore } from 'vuex';
    
    const store=useStore();
    
    const name=null
    const email=null
    const password=null
    const password2=null
    
    function signUp(){
        const payload={
            name:name,
            email: email,
            password: password,
            password2:password2,
        }
        console.log(payload)
        store.dispatch('signUp',payload)
    }
    


</script>

<!--<script>-->
<!--export default {-->
<!--  name: 'SignupView',-->
<!--  methods:{-->
<!--    signUp(){-->
<!--      const name=this.name-->
<!--      const email=this.email-->
<!--      const password=this.password-->
<!--      const password2=this.password2-->
<!--      const payload={-->
<!--        name:name,-->
<!--        email: email,-->
<!--        password: password,-->
<!--        password2:password2,-->
<!--      }-->
<!--      console.log(payload)-->
<!--      this.$store.dispatch('signUp',payload)-->
<!--    },-->
<!--  },-->
<!--  data(){-->
<!--    return{-->
<!--      name:null,-->
<!--      email:null,-->
<!--      password:null,-->
<!--      password2:null,-->
<!--    }-->
<!--  }-->

<!--}-->
<!--</script>-->


