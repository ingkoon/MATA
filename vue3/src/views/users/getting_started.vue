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
                                                    시작하기에 앞서
                                                </h3>
                                            </div>
                                            <div class="widget-content">
                                                <div class="widget-content m-2 ">
                                                    <p>
                                                        웹서비스에 진입하는 고객의 로그를 저희 서비스가 수집할 수 있게 하기 위해선 몇가지 단계가 필요합니다.
                                                    </p>
                                                    <hr class='bg-secondary opacity-25' style='height: 2px'/>
                                                    <h5>
                                                        사용자 토큰 발급받기
                                                    </h5>
                                                    <p>
                                                        효과적인 데이터 집계와 불필요한 수집을 하지 않기 위해 저희 서비스를 이용해주시는 사용자들께 토큰은 발급하고 있습니다.
                                                        이는 이후 서비스 이용을 위해 필수적으로 코드에 주입해야 하니 참고해주시길 바랍니다.
                                                    </p>
                                                    <p>
                                                        토큰이 도난당한다면 데이터의 집계가 부정확해질 수 있으니, 주기적으로 토큰을 재발급 받아 서비스에 적용해주시길 바랍니다.
                                                    </p>
                                                    <p>
                                                        사용자님께 발급된 토큰은 다음과 같습니다.
                                                    </p>
                                                    <div class='p-1 inline'>
                                                        <p>{{ "blahblah" }}</p>    
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
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
                                                    <p>
                                                        일반적으로 <span style="font-weight:bold">index.js</span>로 통용되는 ReactDOM의 최상단 컴포넌트는 BrowserRouter를 사용해야 합니다.
                                                    </p>
                                                    <div class='bg-light-dark opacity-25 p-4'>
                                                        <p class='text-black'>코</p>
                                                        <p class='text-black'>드</p>
                                                        <p class='text-black'>어</p>
                                                        <p class='text-black'>케</p>
                                                        <p class='text-black'>넣</p>
                                                        <p class='text-black'>음</p>
                                                    </div>
                                                    <br/>
                                                    <p>
                                                        일반적으로 React 최상단 컴포넌트인 <span style="font-weight:bold">App.js</span>에 다음과 같은 코드를 추가합니다.
                                                    </p>
                                                    <div class='bg-light-dark opacity-25 p-4'>
                                                        <p class='text-black'>코</p>
                                                        <p class='text-black'>드</p>
                                                        <p class='text-black'>어</p>
                                                        <p class='text-black'>케</p>
                                                        <p class='text-black'>넣</p>
                                                        <p class='text-black'>음</p>
                                                    </div>
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
            handleNewLine(str) {
                return String(str).replace(/(?:\r\n|\r|\n)/g,"</br>");
            },
            async getClientToken() {
                let resp = await fetch()
                let body = resp.json()
                return body.token
            }
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
