<template>
    <div class="layout-px-spacing dash_1">
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
        <div class='row layout-top-spacing'>
            <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                <div class='widget p-3'>
                    <h5>사용자 토큰</h5>
                    <div> {{ $route.params.id }}</div>
                    <div>
                        토큰 : {{  }}
                    </div>
                    <button @click="get_token">재발급</button>
                </div>
            </div>
        </div>

        
        <div class="row layout-top-spacing">
            <div class="col-xl-4 col-lg-4 col-md-12 col-sm-12 col-12 layout-spacing">
                <div class="widget widget-revenue">
                    <div class="widget-heading">
                        <sankeyChart/>
                    </div>
                    
                </div>
                
            </div>
            <div class="col-xl-8 col-lg-8" >
                <heatmap/>
            </div>
            

            <div>
                <Linear_chart/>
            </div>

            <div>
                <BarChart></BarChart>
            </div>
            
            <total_user/>
        </div>
    </div>
</template>

<script setup>
    import '@/assets/sass/widgets/widgets.scss';
    import { computed, ref, onMounted, onUpdated, reactive } from 'vue';
    import { useStore } from 'vuex';
    import ApexChart from 'vue3-apexcharts';
    import sankeyChart from './charts/sankey_chart.vue';
    import heatmap from './charts/click_heatmap.vue';
    import VueJwtDecode from 'vue-jwt-decode'
    import { useMeta } from '@/composables/use-meta';
    import axios from 'axios';
    useMeta({ title: 'Sales Admin' });
    import { useRoute } from 'vue-router';
    import BarChart from './component_bar_chart.vue'
    
    
    useMeta({ title: 'Sales Admin' });
    import Linear_chart from '@/views/charts/linear_chart.vue';
    import Total_user from '@/views/charts/total_user.vue';
    
    const route =useRoute();
    const store = useStore();    
    
    const state = reactive({
        serviceId: route.path.split('/')[2],
        accessToken: localStorage.getItem("accessToken"),
        clientToken: null,
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
    })
    
    //Revenue
    const get_token = async () => {
        let resp = await axios({
            method:'post',
            url: process.env.VUE_APP_API_HOST+'/api/v1/project/token',
            headers:{
                "Content-Type": 'application/json',
            },
            data:{
                "serviceId": state.serviceId
            },
        })
        let body = resp.data
        console.log(body);
        state.clientToken = body;
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

    //Daily Sales
    const daily_sales_series = ref([
        { name: 'Sales', data: [44, 55, 41, 67, 22, 43, 21] },
        { name: 'Last Week', data: [13, 23, 20, 8, 13, 27, 33] },
    ]);
    const daily_sales_options = computed(() => {
        return {
            chart: { toolbar: { show: false }, stacked: true, stackType: '100%' },
            dataLabels: { enabled: false },
            stroke: { show: true, width: 1 },
            colors: ['#e2a03f', '#e0e6ed'],
            responsive: [{ breakpoint: 480, options: { legend: { position: 'bottom', offsetX: -10, offsetY: 0 } } }],
            xaxis: { labels: { show: false }, categories: ['Sun', 'Mon', 'Tue', 'Wed', 'Thur', 'Fri', 'Sat'] },
            yaxis: { show: false },
            fill: { opacity: 1 },
            plotOptions: { bar: { horizontal: false, columnWidth: '25%' } },
            legend: { show: false },
            grid: {
                show: false,
                xaxis: { lines: { show: false } },
                padding: { top: 10, right: -20, bottom: -20, left: -20 },
            },
        };
    });

    //Total Orders
    const total_orders_series = ref([{ name: 'Sales', data: [28, 40, 36, 52, 38, 60, 38, 52, 36, 40] }]);
    const total_orders_options = computed(() => {
        const is_dark = store.state.is_dark_mode;
        return {
            chart: { sparkline: { enabled: true } },
            stroke: { curve: 'smooth', width: 2 },
            colors: is_dark ? ['#1abc9c'] : ['#fff'],
            labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
            yaxis: { min: 0, show: false },
            grid: { padding: { top: 125, right: 0, bottom: 0, left: 0 } },
            fill: {
                opacity: 1,
                type: 'gradient',
                gradient: {
                    type: 'vertical',
                    shadeIntensity: 1,
                    inverseColors: !1,
                    opacityFrom: is_dark ? 0.3 : 0.4,
                    opacityTo: 0.05,
                    stops: is_dark ? [100, 100] : [45, 100],
                },
            },
            tooltip: { x: { show: false }, theme: 'dark' },
        };
    });

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
