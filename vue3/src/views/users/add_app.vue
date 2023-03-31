<template>
    <div class="form auth-boxed">
        <div class="form-container outer">
            <div class="form-form">
                <div class="form-form-wrap">
                    <div class="form-container">
                        <div class="form-content">
                            <h1 class="m-4">서비스 등록</h1>
                            <div class="row justify-content-center">
                                <div class="col-xl-7 col-lg-8 col-md-10 col-sm-12 col-12 layout-spacing">
                                    <div class=''>
                                        <div class="widget layout-spacing my-3">
                                            <form class="text-start p-5" @submit.prevent="add_app">
                                                <div class="form">
                                                    <div id="name-field" class="field-wrapper input mb-5">
                                                        <p>등록할 서비스의 이름을 정합니다. </p>
                                                        <input type="text" class="form-control" placeholder="서비스 이름" v-model="state.name"/>
                                                    </div>
                                                    <div id="url-field" class="field-wrapper input mb-5">
                                                        <p>코드 주입이 완료된 사이트의 최 상단 URL을 기입합니다. HTTP 프로토콜을 포함하며 마지막 사선 구분자를 제거한 형태이어야 합니다.</p>
                                                        <p>http://localhost:3000/ 또는 www.naver.com의 형식은 올바르지 않은 형식입니다.</p>
                                                        <p>http://localhost:3000 또는 https://www.naver.com의 형식은 올바른 형식입니다.</p>
                                                        <input type="url" class="form-control" placeholder="서비스 URL" v-model="state.url" />
                                                    </div>
                
                                                    <div id="category-field" class="field-wrapper input mb-5">
                                                        <p>사이트의 목적을 선택합니다.</p>
                                                        <select name="category" id="category" class='form-select selectable-dropdown' v-model="state.category">
                                                            <option value="" selected='selected'>카테고리</option>
                                                            <option value="BLOG">블로그</option>
                                                            <option value="PORTAL">포탈</option>
                                                            <option value="COMMERCE">커머스</option>
                                                        </select>
                                                    </div>
                
                                                    <div class="d-sm-flex justify-content-between">
                                                        <div class="field-wrapper">
                                                            <button type="submit" class="btn btn-primary">서비스 등록!</button>
                                                        </div>
                                                    </div>
                                                    
                                                </div>
                                            </form>
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

<script setup>
    import Default from 'vue-easy-lightbox';
    import { useMeta } from '@/composables/use-meta';
    import { reactive } from 'vue';
    import { useStore } from 'vuex';
    useMeta({title: 'Add project'})
    const store = useStore();
    
    const state = reactive({
        category: null,
        url: null,
        name: null,
    })

    const add_app = function (){
        const category=this.category
        const url=this.url
        const name=this.name
        const payload={
            category:category,
            url:url,
            name:name,
        }
        console.log("뷰에서",payload)
        store.dispatch('add_App',payload)
    }
</script>
