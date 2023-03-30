<template>
    <div class="form auth-boxed getting-started">
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
                                            <div class="widget-content ">
                                                <h5>사용자 토큰 발급받기</h5>
                                                <hr/>
                                                <div class="widget-content m-2">
                                                    <p>
                                                        효과적인 데이터 수집과 정확한 데이터 집계를 위해 사용자 당 한개의 사용자 토큰을 발급하고 있습니다.
                                                    </p>
                                                    <p>
                                                        사용자의 코드에 스크립트를 주입할 때 발급받은 토큰을 같이 명시해주어야 합니다.
                                                    </p>
                                                    <p>
                                                        이 토큰은 사용자의 사적인 정보를 포함하고 있지 않지만, 만약 토큰이 분실되었거나 도난당했을 시 토큰을 재발급 받아야 합니다.
                                                    </p>
                                                    <p>
                                                        고객님에게 발급 된 토큰은 다음과 같습니다. 이를 복사하여 주입할 스크립트에 추가해주시길 바랍니다.
                                                    </p>
                                                    <div class='content-section rounded-2 p-2 d-inline'>
                                                        {{ "발급받은 토큰" }}
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
                                                <div class="widget-content m-2">
                                                    <p>
                                                        제공 예정입니다.
                                                    </p>
                                                    <div class='content-section rounded-2 p-3'>
                                                        <vue-code-highlight language="javascript">
                                                             <pre>
// 제공 예정입니다.
                                                             </pre>
                                                        </vue-code-highlight>
                                                    </div>
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
                                                    <div class='content-section rounded-2 p-3'>
                                                        <vue-code-highlight language="javascript">
                                                             <pre>
import {BrowserRouter} from "react-router-dom";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  {{"   <BrowserRouter>"}}
  {{"       <App />"}}
  {{"   </BrowserRouter>"}}
);                                                             </pre>
                                                        </vue-code-highlight>
                                                    </div>
                                                    <br/>
                                                    <p>
                                                        일반적으로 React 최상단 컴포넌트인 <span style="font-weight:bold">App.js</span>에 다음과 같은 코드를 추가합니다.
                                                    </p>
                                                    <div class='content-section rounded-2 p-3'>
                                                        <vue-code-highlight language="javascript">
                                                                 <pre>
import TagManager from "./module";

const tagManager = new TagManager('http://ec2-3-38-85-143.ap-northeast-2.compute.amazonaws.com/api/v1/dump', "tag-manager-service-token", ['click'])

export default function App() {
    const location = useLocation()
    useEffect(() => {
        tagManager.attach();
        return () => {
            tagManager.detach();
        }
     }, [location])
                                                                     
    // your codes goes here...
                                                                     
}                                                                 </pre>
                                                        </vue-code-highlight>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="widget layout-spacing my-3">
                                            <div class="widget-heading">
                                                <h3>
                                                    서비스 추가하기
                                                </h3>
                                            </div>
                                            <div class="widget-content ">
                                                <p>
                                                    좌측의 사이드 패널에서 서비스를 추가할 수 있습니다.
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
</template>

<script>
    import '@/assets/sass/users/getting-started.scss';
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
