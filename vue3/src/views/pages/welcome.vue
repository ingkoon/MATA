<template>
    <div class="layout-px-spacing">
        <div class="maintanence text-center">
            <div class="container-fluid maintanence-content">
                <div class="">
                    <div class="maintanence-hero-img">
                        <img alt="logo" src="@/assets/images/logo2.svg" />
                    </div>
                    <h1 class="error-title">MATA Dashboard</h1>
                    <p class="error-text">CRM을 위한 UX 분석을 돕습니다</p>
                    <p class="text">
                        로그 데이터 수집과 분석을 한눈에 하세요<br />
                        반복적인 리포트를 자동화 하세요
                    </p>
                    <p class="text">고객님의 Data-Driven 여정을 함께합니다.</p>
<!--                    <router-link to="/" class="btn btn-info mt-4">Home</router-link>-->
                    
                </div>
            </div>
            <div class="row justify-content-center">
                <div class="col-xl-7 col-lg-8 col-md-10 col-sm-12 col-12 layout-spacing">
                    <div class=''>
                        <div class="widget layout-spacing my-3">
                            <div class="widget-heading m-3">
                                <h2>
                                    손쉬운 시작
                                </h2>
                            </div>
                            <div class="row widget-content">
                                <div class="col-xl-8 col-lg-7 col-md-12 col-sm-12">
                                    <img src='@/assets/images/script-preview.png' class='rounded-3 border-3 shadow-lg w-100'>
                                </div>
                                <div class="row col-xl-4 col-lg-5 col-md-12 col-sm-12 align-items-center">
                                    <p class='text-start p-4' style='font-weight: bold'>단 몇줄을 추가하여 복잡한 데이터 수집을 시작할 수 있습니다.</p>
                                    <p class='text-start p-4' style='font-weight: bold'>수집된 데이터를 신경쓰지 않아도 MATA가 적재, 가공을 진행 해줍니다.</p>
                                </div>
                            </div>
                        </div>
                        <div class="widget layout-spacing my-3">
                            <div class="widget-heading m-3">
                                <h2>
                                    컴포넌트 별 이벤트 분석
                                </h2>
                            </div>
                            <div class="row widget-content">
                                <div class="row col-xl-4 col-lg-5 col-md-12 col-sm-12 align-items-center">
                                    <p class='text-start p-4' style='font-weight: bold'>비스니스적으로 주목할 만한 버튼이나 링크의 이벤트를 집계할 수 있습니다.</p>
                                </div>
                                <div class="col-xl-8 col-lg-7 col-md-12 col-sm-12">
                                    <img src='@/assets/images/dashboard-preview-components.png' class='rounded-3 border-3 shadow-lg w-100'>
                                </div>
                            </div>
                        </div>
                        <div class="widget layout-spacing my-3">
                            <div class="widget-heading">
                                <h2>
                                    마참내!
                                </h2>
                            </div>
                            <div class="widget-content ">
                                <div class="content-section m-2">
                                    <p>
                                        콘텐츠
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="widget layout-spacing my-3">
                            <div class="widget-heading">
                                <h2>
                                    즐겁다
                                </h2>
                            </div>
                            <div class="widget-content ">
                                <div class="content-section m-2">
                                    <p>
                                        콘텐츠
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import '@/assets/sass/pages/error/style-maintanence.scss';
    import { useMeta } from '@/composables/use-meta';
    import { onMounted, ref } from 'vue';
    import { useStore } from 'vuex';
    import { useRouter } from 'vue-router';
    
    useMeta({ title: 'Maintenence', layout: 'auth' });
    const store = useStore();
    const router = useRouter();
    
    if (localStorage.getItem('accessToken')) router.push('/user');
        
</script>
