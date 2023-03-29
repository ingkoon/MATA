<template>
    <div class="form auth-boxed">
        <div class="form-container outer">
            <div class="form-form">
                <div class="form-form-wrap">
                    <div class="form-container">
                        <div class="form-content">
                            <h1 class="m-4">시작하기</h1>
                            <div class="row justify-content-center">
                                <div class="col-xl-7 col-lg-8 col-md-10 col-sm-12 col-12 layout-spacing">
                                    <div class=''>
                                        <div class="widget layout-spacing my-3">
                                            <div class="widget-heading">
                                                <h3>
                                                    <span style="font-weight:bold">Common JS</span>에서 시작하기 
                                                </h3>
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
                                                <h3>
                                                    <span style="font-weight:bold">React</span>에서 시작하기
                                                </h3>
                                            </div>
                                            <div class="widget-content ">
                                                <div class="widget-content m-2">
                                                    <p>
                                                        React에서는 다음과 같은 코드를 추가할 수 있습니다.
                                                    </p>
                                                    <p class='bg-light p-4'>
                                                        
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
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Getting-started',
        methods:{
            add_app(){
                const category=this.category
                const url=this.url
                const name=this.name
                const payload={
                    category:category,
                    url:url,
                    name:name,
                }
                console.log("뷰에서",payload)
                this.$store.dispatch('add_App',payload)
            },
        },
        data(){
            return{
                category:null,
                url:null,
                name:null,
            }
        }

    }

    // import { ref } from 'vue';
    // import '@/assets/sass/authentication/auth-boxed.scss';

    // import { useMeta } from '@/composables/use-meta';
    // useMeta({ title: 'Register Boxed' });

    // const pwd_type = ref('password');

    // const set_pwd_type = () => {
    //     if (pwd_type.value === 'password') {
    //         pwd_type.value = 'text';
    //     } else {
    //         pwd_type.value = 'password';
    //     }
    // };
</script>
