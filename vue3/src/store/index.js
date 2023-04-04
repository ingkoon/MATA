import { createStore } from 'vuex';
import i18n from '../i18n';
import axios from 'axios'
import router from '@/router'
import createPersistedState from 'vuex-persistedstate'


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
        service:null,
        serviceId:null,
        journals: {curNode : null, data : null, nodes : {}, links : {}, clickFlag : false},
        durations: [], // 리스트 타입의 상태 변수
        
        urlList: []
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
            console.log(state.token)
        },
        set_service_list(state,payload){
            console.log('mutation 시작',payload)
            state.services=payload
            console.log('mutation done', state.services)
        },
        setDurations(state, durations) {
            state.durations = durations
            console.log("--------------------set durations -----------------")
            console.log("data is" + state.durations + state.durations.typeof);
            console.log(Object.keys(JSON.parse(state.durations)));
            console.log(JSON.parse(state.durations));
        }
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
              url: process.env.VUE_APP_API_HOST+'/api/v1/member/signup',
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
                url: process.env.VUE_APP_API_HOST+'/api/v1/member/login',
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
                console.log("commit done, mutation setToken start")    
                localStorage.setItem('accessToken',res.data.accessToken)
                router.push('/')
                })
                .catch(err=>{
                console.log(err)
                })

          },
          logOut() {
            console.log("logged out");
            localStorage.removeItem('accessToken');
            document.location.href = '/';
          },
        
          add_App(context,payload){
            const name=payload.name
            const url=payload.url
            const category=payload.category
            const token=localStorage.getItem('accessToken')
            console.log(category,token)
            axios({
                method:'post',
                url: process.env.VUE_APP_API_HOST+'/api/v1/project/add',
                headers:{
                  "Authorization": `Bearer ${token}`,
                },
                data:{
                  name : name,
                  url: url,
                  category: category,
                  // password2: password2,
                  
                },
              })
                .then(res=>{
                console.log(category,token)
                console.log(res)
                router.push('/')
                })
                .catch(err=>{
                console.log(err)
                })
          },
        async fetchDurations({ commit }, {baseTime, interval, serviceId}) {
            console.log('basetime = ' + baseTime +  ' interval = ' + interval +' serviceid = ' +  serviceId);
            console.log(123);
            const url = encodeURI(`http://ec2-3-38-85-143.ap-northeast-2.compute.amazonaws.com/api/v1/weblog/durations`);
            const params = {
                'basetmie' : baseTime, 
                'interval' : interval, 
                'serviceid' : serviceId
            };
            console.log("axios input is ...")
            await axios({
                method: 'get',
                url: process.env.VUE_APP_API_HOST 
                    + `/api/v1/weblog/durations?basetime=${baseTime}&interval=${interval}&serviceid=${serviceId}`,
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                }
            }).then(response => {
                const groupedData = {};
                response.data.forEach((item) => {
                    // Check if the locationFrom already exists in the groupedData object
                    if (groupedData[item.location]) {
                        // If it exists, update the existing object with the new values
                        groupedData[item.location].push({
                            "total_duration" : item.totalDuration,
                            "total_session" : item.totalSession,
                            "update_timestamp" : item.updateTimestamp
                        });
                    } else {
                        // If it doesn't exist, create a new object with the values
                        groupedData[item.location] = [{
                            "total_duration" : item.totalDuration,
                            "total_session" : item.totalSession,
                            "update_timestamp" : item.updateTimestamp
                        }];
                    }
                });
                console.log("----------------start------------")
                console.log(groupedData);
                commit('setDurations', JSON.stringify(groupedData));
                // const responseData = JSON.stringify(response.data);
                // console.log("return is ... " + response+ " ,,,, " + response.length);
                //
                // store.urlList = state.durations.map(duration => duration.location);
                //
                // commit('setDurations', response.data);
                // 여기서 apex-chart를 그리는 함수를 주입시켜 준다.
            }).catch(error =>{
                console.error(error + "에러가 발생했습니다.");
            })
        },
    },
    
    
    getProjectList: function (){
    // console.log(token)
    const token=localStorage.getItem('accessToken')
    axios({
        method:'get',

        url: process.env.VUE_APP_API_HOST+'/api/v1/project/',
        headers:{
            "Authorization": `Bearer ${token}`,
        },

    })
        .then(res=>{
            console.log(`axios done ${res}`,res)
            payload.value=res.data
            console.log('asd')
            
            // store.dispatch('get_service_list',payload)
            store.commit('set_service_list',res.data)
            console.log(store.state.service)
            localStorage.setItem('services',JSON.stringify(res.data))
            return res.data
        })
        .catch(err=>{
            console.log(err.response)
        })
},
    modules: {},
});
