<template>
    <!--  BEGIN SIDEBAR  -->
    <div class="sidebar-wrapper sidebar-theme">
        <nav ref="menu" id="sidebar">
            <div class="shadow-bottom"></div>

            <perfect-scrollbar class="list-unstyled menu-categories" tag="ul" :options="{ wheelSpeed: 0.5, swipeEasing: !0, minScrollbarLength: 40, maxScrollbarLength: 300, suppressScrollX: true }">
                <li class="menu" v-for="(item,index) in payload" :key="index">
                    <router-link v-bind:to="`/service/${item.id}`" class="dropdown-toggle" @click="toggleMobileMenu">
                        <div class="">
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
                                class="feather feather-target"
                            >
                                <circle cx="12" cy="12" r="10"></circle>
                                <circle cx="12" cy="12" r="6"></circle>
                                <circle cx="12" cy="12" r="2"></circle>
                            </svg>
                            <span>{{item.name}}</span>
                        </div>
                    </router-link>
                </li>
                
                <li class="menu">
                    <router-link to="/users/add-app" class="dropdown-toggle" @click="toggleMobileMenu">
                        <div class="">
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
                                class="feather feather-target"
                            >
                                <circle cx="12" cy="12" r="10"></circle>
                                <circle cx="12" cy="12" r="6"></circle>
                                <circle cx="12" cy="12" r="2"></circle>
                            </svg>
                            <span>{{ $t('서비스 추가') }}</span>
                        </div>
                    </router-link>
                </li>
            
                
            </perfect-scrollbar>
        </nav>
    </div>
    <!--  END SIDEBAR  -->
</template>

<script setup>
    import { onMounted, ref, onBeforeMount } from 'vue';
    import { useStore } from 'vuex';
    import VueJwtDecode from 'vue-jwt-decode'
    import axios from 'axios';
    const store = useStore();
    const payload=ref([])
    const menu_collapse = ref('dashboard');
    function getProjectList(){
            // console.log(token)
            const token=localStorage.getItem('accessToken')
            axios({
              method:'get',
              
              url:'http://localhost:8080/api/v1/project/',
              headers:{
                
                "Authorization": `Bearer ${token}`,
              },
            
            })
              .then(res=>{
              console.log(`axios done ${res}`,res)
              payload.value=res.data
              console.log('asd')
              this.$store.dispatch('get_service_list',payload)
              
              console.log(store.state.service)
              })
              .catch(err=>{
              console.log(err.response)
              })
          }
    // onBeforeMount(() => {
    //     getProjectList()
    // }),
    onMounted(() => {
        console.log("sidebar mount start")
        getProjectList()
        // console.log('getProjectList:', getProjectList())
        // const payload=getProjectList()
        // console.log(`payload: ${payload}`)
    
        const selector = document.querySelector('#sidebar a[href="' + window.location.pathname + '"]');
        if (selector) {
            const ul = selector.closest('ul.collapse');
            if (ul) {
                let ele = ul.closest('li.menu').querySelectorAll('.dropdown-toggle');
                if (ele) {
                    ele = ele[0];
                    setTimeout(() => {
                        ele.click();
                    });
                }
            } else {
                selector.click();
            }
        }
        console.log('sidebar mount done')

    });

    const toggleMobileMenu = () => {
        if (window.innerWidth < 991) {
            store.commit('toggleSideBar', !store.state.is_show_sidebar);
        }
    };
</script>
