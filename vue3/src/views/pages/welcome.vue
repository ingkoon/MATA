<template>
    <div class="layout-px-spacing">
        <div class="maintanence text-center">
            <div class="container-fluid maintanence-content">
                <div class="">
                    <div class="maintanence-hero-img">
                        <img alt="logo" src="@/assets/images/logo2.svg" />
                    </div>
                    <h1 class="error-title">MATA Dashboard</h1>
                    <p class="error-text">웹페이지 소개 페이지 입니다.</p>
                    <p class="text">
                        좀 더 자세한 설명입니다 <br />
                        두번째 라인입니다
                    </p>
                    <p class="text">맺음말입니다</p>
<!--                    <router-link to="/" class="btn btn-info mt-4">Home</router-link>-->
                    
                </div>
            </div>
            <div class="row justify-content-center">
                <div class="col-xl-7 col-lg-8 col-md-10 col-sm-12 col-12 layout-spacing">
                    <div class=''>
                        <div class="widget layout-spacing my-3">
                            <div class="widget-heading">
                                <p>
                                    카드의 헤더 입니다
                                </p>
                            </div>
                            <div class="widget-content ">
                                <div class="widget-content m-2 bg-light">
                                    <p>
                                        콘텐츠
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="widget layout-spacing my-3">
                            <div class="widget-heading">
                                <p>
                                    카드의 헤더 입니다
                                </p>
                            </div>
                            <div class="widget-content ">
                                <div class="widget-content m-2 bg-light">
                                    <p>
                                        콘텐츠
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="widget layout-spacing my-3">
                            <div class="widget-heading">
                                <p>
                                    카드의 헤더 입니다
                                </p>
                            </div>
                            <div class="widget-content ">
                                <div class="widget-content m-2 bg-light">
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
    useMeta({ title: 'Maintenence', layout: 'auth' });
</script>
