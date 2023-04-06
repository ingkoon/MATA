<template>
    <div class="layout-px-spacing dash_1">
        <div class='row layout-top-spacing'>
            <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                <div class='widget p-3'>
                    <div style='float: right'>
                        <div class="dropdown btn-group">
                            <a href="javascript:;" id="ddlRevenue" class="btn dropdown-toggle btn-icon-only" data-bs-toggle="dropdown" aria-expanded="false">
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
                                    class="feather feather-more-horizontal"
                                >
                                    <circle cx="12" cy="12" r="1"></circle>
                                    <circle cx="19" cy="12" r="1"></circle>
                                    <circle cx="5" cy="12" r="1"></circle>
                                </svg>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="ddlRevenue">
                                <li><a href="javascript:;" class="dropdown-item">새로운 토큰을 발급받으려면 재발급 버튼을 클릭하세요!</a></li>
                            </ul>
                        </div>
                    </div>
                    <h4>사용자 토큰</h4>
                    <div>
                        <span style='font-weight: bold'> 프로젝트명 : </span><span class="text-primary ms-3">{{ state.serviceName }}</span>
                    </div>
                    <div>
                        <span style='font-weight: bold'> 토큰 : </span> <span class="text-primary ms-3">{{ state.clientToken }}</span>
                    </div>
                    <button @click="get_token" style='font-weight: bold; text-align: center; color: white; background: #1B2E4BFF; border-radius: 5px; box-shadow: 2px 2px 2px gray; border: none; margin-top: 10px;'>재발급</button>
                </div>
            </div>
        </div>

        <div class='navbar-nav flex-row' style='margin-top: 3%;'>
            <total_user/>
        </div>

        <teleport to="#breadcrumb">
            <ul class="navbar-nav flex-row">
                <li>
                    <div class="page-header">
                        <nav class="breadcrumb-one" aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="javascript:;">google</a></li>
                            </ol>
                        </nav>
                    </div>
                </li>
            </ul>
        </teleport>

        <div class="row layout-top-spacing">
            <div class="col-xl-4 col-lg-4 col-md-12 col-sm-12 col-12 layout-spacing">
                <div class="widget widget-revenue">
                    <div class="widget-heading">
                        <sankeyChart/>
                    </div>
                </div>
            </div>
            <div class="col-xl-4 col-lg-4 col-md-12 col-sm-12 col-12 layout-spacing" >
                <!--             <div class="m-4 px-3 col-xl-7 col-lg-6 m-auto" > -->
                <heatmap/>
            </div>
            <div>
                <Linear_chart/>
            </div>

            <div>
                <linechart></linechart>
            </div>
        </div>
    </div>
</template>

<script setup>
    import '@/assets/sass/widgets/widgets.scss';
    import { computed, ref, onMounted, onUpdated, reactive, watch, watchEffect } from 'vue';
    import { useStore } from 'vuex';
    import ApexChart from 'vue3-apexcharts';
    import sankeyChart from './charts/sankey_chart.vue';
    import heatmap from './charts/click_heatmap.vue';
    import VueJwtDecode from 'vue-jwt-decode'
    import { useMeta } from '@/composables/use-meta';
    import axios from 'axios';
    import { useRoute } from 'vue-router';
    import BarChart from './component_bar_chart.vue'
    import Linear_chart from '@/views/charts/linear_chart.vue';
    import Total_user from '@/views/charts/total_user.vue';
    import linechart from './linechart.vue'


    useMeta({ title: 'Sales Admin' });
    const route =useRoute();
    const store = useStore();

    const state = reactive({
        serviceId: route.path.split('/')[2],
        accessToken: localStorage.getItem("accessToken"),
        clientToken: null,
        serviceName: null,
        pageDurations: {

        },
        configs: {
            components: {
                interval: '1d'
            }
        },
        data: {
            components: {
                totalNum: 0,
                totalClickSum: 0,
                list: [],
            }
        }
    });

    onMounted(() => {
        getComponentStats('1d');
        get_project();
    })

    watchEffect(()=> {
        const token = state.clientToken;
    })

    const get_project = async ()=> {
        let resp = await axios({
            method:'get',
            url: process.env.VUE_APP_API_HOST+`/api/v1/project/${route.params.id}`,
            headers:{
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
            },
        })
        let body = resp.data;
        console.log("------------body---------");
        console.log(body);
        state.serviceName = body.name;
        state.clientToken = body.token == null ? "아직 발급받지 않은 상태입니다." : body.token;
    }

    //Revenue
    const get_token = async () => {
        let resp = await axios({
            method:'post',
            url: process.env.VUE_APP_API_HOST+'/api/v1/project/token',
            headers:{
                "Content-Type": 'application/json',
            },
            data:{
                "projectId": state.serviceId
            },
        })
        let body = resp.data
        console.log(body);
        state.clientToken = body.token == null ? "아직 발급받지 않은 상태입니다." : body.token;
    }

    const getComponentStats = async (interval) => {
        state.configs.components.interval = interval
        let resp = await axios({
            method:'get',
            url: process.env.VUE_APP_API_HOST+`/api/v1/weblog/components?basetime=${Date.now()}&interval=${ state.configs.components.interval }&serviceid=${state.serviceId}`,
            headers:{
                "Authorization": `Bearer ${state.accessToken}`,
            },
        })
        let body = resp.data;
        body = [{
            totalClick : 12,
            targetId : "btn-login",
            location : "localhost",
            updateTimestamp : "",
            serviceId : 2
        },{
            totalClick : 6,
            targetId : "btn-join",
            location : "localhost/first",
            updateTimestamp : "",
            serviceId : 2
        },{
            totalClick : 5,
            targetId : "btn-details",
            location : "localhost/first",
            updateTimestamp : "",
            serviceId : 2
        },{
            totalClick : 10,
            targetId : "btn-submit",
            location : "localhost/second",
            updateTimestamp : "",
            serviceId : 2
        },];
        body.sort(function(a, b) {
            return b.totalClick - a.totalClick;
        })
        state.data.components.list = body;
        state.data.components.totalNum = state.data.components.list;
        state.data.components.totalClickSum = state.data.components.list.reduce((acc, cur) => acc + cur.totalClick, 0)
        console.log()
    }

    //Sales by Category
    const sales_donut_series = ref([985, 737, 270]);
    const sales_donut_options = computed(() => {
        const is_dark = store.state.is_dark_mode;
        const option = {
            chart: {},
            dataLabels: { enabled: false },
            expandOnClick: is_dark ? false : true,
            stroke: { show: true, width: 25, colors: is_dark ? '#0e1726' : '#fff' },
            colors: is_dark ? ['#5c1ac3', '#e2a03f', '#e7515a', '#e2a03f'] : ['#e2a03f', '#5c1ac3', '#e7515a'],
            legend: {
                position: 'bottom',
                horizontalAlign: 'center',
                fontSize: '14px',
                markers: { width: 10, height: 10 },
                height: 50,
                offsetY: 20,
                itemMargin: { horizontal: 8, vertical: 0 },
            },
            plotOptions: {
                pie: {
                    donut: {
                        size: '65%',
                        background: 'transparent',
                        labels: {
                            show: true,
                            name: { show: true, fontSize: '29px', fontFamily: 'Nunito, sans-serif', offsetY: -10 },
                            value: {
                                show: true,
                                fontSize: '26px',
                                fontFamily: 'Nunito, sans-serif',
                                color: is_dark ? '#bfc9d4' : undefined,
                                offsetY: 16,
                                formatter: function (val) {
                                    return val;
                                },
                            },
                            total: {
                                show: true,
                                label: 'Total',
                                color: '#888ea8',
                                fontSize: '29px',
                                formatter: function (w) {
                                    return w.globals.seriesTotals.reduce(function (a, b) {
                                        return a + b;
                                    }, 0);
                                },
                            },
                        },
                    },
                },
            },
            labels: ['Apparel', 'Sports', 'Others'],
        };
        if (is_dark) {
            option['states'] = {
                hover: { filter: { type: 'none' } },
                active: { filter: { type: 'none' } },
            };
        }
        return option;
    });
</script>
