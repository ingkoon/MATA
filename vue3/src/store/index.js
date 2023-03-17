import { createStore } from 'vuex';
import i18n from '../i18n';
import axios from 'axios'
import router from '@/router'

export default new createStore({
    state: {
        layout: 'app',
        is_show_sidebar: true,
        is_show_search: false,
        is_dark_mode: false,
        dark_mode: 'light',
        locale: null,
        menu_style: 'vertical',
        layout_style: 'full',
        countryList: [
            { code: 'zh', name: 'Chinese' },
            { code: 'da', name: 'Danish' },
            { code: 'en', name: 'English' },
            { code: 'fr', name: 'French' },
            { code: 'de', name: 'German' },
            { code: 'el', name: 'Greek' },
            { code: 'hu', name: 'Hungarian' },
            { code: 'it', name: 'Italian' },
            { code: 'ja', name: 'Japanese' },
            { code: 'pl', name: 'Polish' },
            { code: 'pt', name: 'Portuguese' },
            { code: 'ru', name: 'Russian' },
            { code: 'es', name: 'Spanish' },
            { code: 'sv', name: 'Swedish' },
            { code: 'tr', name: 'Turkish' },
        ],
        token: null,
    },
    mutations: {
        setLayout(state, payload) {
            state.layout = payload;
        },
        toggleSideBar(state, value) {
            state.is_show_sidebar = value;
        },
        toggleSearch(state, value) {
            state.is_show_search = value;
        },
        toggleLocale(state, value) {
            value = value || 'en';
            i18n.global.locale = value;
            localStorage.setItem('i18n_locale', value);
            state.locale = value;
        },

        toggleDarkMode(state, value) {
            //light|dark|system
            value = value || 'light';
            localStorage.setItem('dark_mode', value);
            state.dark_mode = value;
            if (value == 'light') {
                state.is_dark_mode = false;
            } else if (value == 'dark') {
                state.is_dark_mode = true;
            } else if (value == 'system') {
                if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                    state.is_dark_mode = true;
                } else {
                    state.is_dark_mode = false;
                }
            }

            if (state.is_dark_mode) {
                document.querySelector('body').classList.add('dark');
            } else {
                document.querySelector('body').classList.remove('dark');
            }
        },

        toggleMenuStyle(state, value) {
            //horizontal|vertical|collapsible-vertical
            value = value || '';
            localStorage.setItem('menu_style', value);
            state.menu_style = value;
            if (!value || value === 'vertical') {
                state.is_show_sidebar = true;
            } else if (value === 'collapsible-vertical') {
                state.is_show_sidebar = false;
            }
        },

        toggleLayoutStyle(state, value) {
            //boxed-layout|large-boxed-layout|full
            value = value || '';
            localStorage.setItem('layout_style', value);
            state.layout_style = value;
        },
        SIGN_UP(){},
        setToken(state,token){
            state.token=token
        },
    },
    getters: {
        layout(state) {
            return state.layout;
        },
        isLogin(state){
            return state.token ==null? false : true
        },
    },
    actions: {
        signUp(context,payload){
            const username=payload.name
            const password1=payload.password
            const password2=payload.password2
            const email=payload.email
            // const userdata={
            //   username,password1,password2,email
            // }
            axios({
              method:'post',
              url:'http://127.0.0.1:8080/api/v1/member/signup/',
              headers:{
                "Content-Type": "application/json",
              },
              data:{
                name: username,
                email : email,
                password: password1,
                // password2: password2,
                
              },
            })
              .then(res=>{
              console.log(res)
              context.commit('SAVE_TOKEN', res.data.key)
              context.dispatch('logIn', { username: username, password: password2 })
              })
              .catch(err=>{
              console.log(err.response)
              })
          },
          logIn(context,payload){
            const password=payload.password
            const email=payload.email
            console.log(email,password)
            axios({
                method:'post',
                url:'http://127.0.0.1:8080/api/v1/member/login/',
                headers:{
                  "Content-Type": "application/json",
                },
                data:{
                  email : email,
                  password: password,
                  // password2: password2,
                  
                },
              })
                .then(res=>{
                console.log(email,password)
             
                
                context.commit('setToken',res.data.accessToken)
                localStorage.setItem('accessToken',res.data.accessToken)
                router.push('/')
                })
                .catch(err=>{
                console.log(err)
                })

          },

          getProjectList(context,token){
            // console.log(token)
            axios({
              method:'get',
              url:'http://127.0.0.1:8080/api/v1/project/',
              headers:{
                "Content-Type": "application/json",
                "Authorization": token,
              },
            
            })
              .then(res=>{
              console.log(res)

              })
              .catch(err=>{
              console.log(err.response)
              })
          },
      
    },
    modules: {},
});
