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
                                <h2>
                                    시간절약!
                                </h2>
                            </div>
                            <div class="widget-content ">
                                <p>
                                    콘텐츠
                                </p>
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
                                    손도 깔끔
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
    useMeta({ title: 'Maintenence', layout: 'auth' });
</script>
