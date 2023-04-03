<template>
    <div class="col-xl-8 col-lg-12 col-md-12 col-sm-12 col-12 layout-spacing">
        <div class="widget widget-">
            <div class="widget-heading">
                <h5>Durations</h5>
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
                            class="feather feather-more-horizontal">
                            <circle cx="12" cy="12" r="1"></circle>
                            <circle cx="19" cy="12" r="1"></circle>
                            <circle cx="5" cy="12" r="1"></circle>
                        </svg>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="ddlRevenue">
                        <li v-for='item in state.data.period'>
                            <a href="javascript:;" class="dropdown-item" v-on:click="state.data.selectedPeriod = item.value">{{item.label}}</a>
                        </li>   
                    </ul>
                </div>
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
                            class="feather feather-more-horizontal">
                            <circle cx="12" cy="12" r="1"></circle>
                            <circle cx="19" cy="12" r="1"></circle>
                            <circle cx="5" cy="12" r="1"></circle>
                        </svg>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="tlnRevenue">
                        <li v-for="item in state.data.timeLine">
                            <a href="javascript:;" class="dropdown-item" v-on:click="state.data.selectedTimeLine = item.value">{{ item.label }}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="widget-content">
                <div class="chart-title">서비스 체류 시간 구간 <span class="text-primary ms-1">{{store.state.durations.length}}</span> 개</div>
                <apex-chart v-if="duration_options" height="325" type="area" :options="duration_options" :series="duration_series"></apex-chart>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { useRoute } from 'vue-router'
    import { useStore } from 'vuex'
    import ApexChart from 'vue3-apexcharts';
    import { computed, onMounted, reactive, ref, watchEffect } from 'vue';

    const route = useRoute();
    const store = useStore();
    const state=  reactive({
        components: { ApexChart },
        data: {
            selectedTimeLine : Date.now(),
            selectedPeriod : '1d',
            serviceId : route.params.id,
            accessToken: localStorage.getItem("accessToken"),
            
            period : [
                {label: 'Daily', value: 1},
                {label: 'Weekly', value: 7},
                {label: 'Monthly', value: 30},
                {label: 'Yearly', value: 365},
            ],
            timeLine : [
                {label: '5m', value: '5'},
                {label: '10m', value: '10'},
                {label: '30m', value: '30'},
                {label: '1h', value: '60'},
                {label: '6h', value: '360'},
                {label: '12h', value: '720'},
            ],
        },
        config : {
            components: {
                interval: '1d'
            }
        }
    });

    const fetchData = async (baseTime, interval, serviceId) => {
        await store.dispatch('fetchDurations', { baseTime, interval, serviceId });
    }
    
    onMounted(()=> {
        fetchData(Date.now(), '1d', route.params.id);
        console.log(state.data.period);
    });
    watchEffect(()=>{
        const { selectedTimeLine, selectedPeriod } = state.data;
        fetchData(selectedTimeLine, selectedPeriod, route.params.id);
    })
    
    // onUpdated(()=>{
    //     fetchData(
    //         state.data.selectedTimeLine, 
    //         state.data.selectedPeriod, 
    //         state.data.serviceId);
    // });
    
    const duration_series = ref([
        { name: '인원 수', data: store.state.durations.map(duration => duration.totalSession)}
    ]);
    const duration_options = computed(() => {
        const is_dark = store.state.is_dark_mode;
        // console.log(list[0]);
        // const list = store.state.durations.map(duration => JSON.stringify(duration));
        const list = store.state.durations.map(duration => duration);
        console.log(list);
        // console.log(list[0].updateTimestamp);
        console.log(list.map(duration => duration.updateTimestamp));
        console.log(list.map(duration => new Date(duration.updateTimestamp)));
        console.log(list.map(duration => duration.totalSession));
        
        
        return {
            chart: {
                fontFamily: 'Nunito, sans-serif',
                zoom: { enabled: false },
                toolbar: { show: false },
            },
            dataLabels: { enabled: false },
            stroke: { show: true, curve: 'smooth', width: 2, lineCap: 'square' },
            dropShadow: { enabled: true, opacity: 0.2, blur: 10, left: -7, top: 22 },
            colors: is_dark ? ['#2196f3', '#e7515a'] : ['#1b55e2', '#e7515a'],
            markers: {
                discrete: 
                    { seriesIndex: 0, dataPointIndex: 6, fillColor: '#1b55e2', strokeColor: '#fff', size: 7 }
                    // { seriesIndex: 1, dataPointIndex: 5, fillColor: '#e7515a', strokeColor: '#fff', size: 7 },
                
            },
            // labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            labels: list.map(duration =>new Date(duration.updateTimestamp).toISOString().split('T')[0]),
            xaxis: {
                axisBorder: { show: false },
                axisTicks: { show: false },
                crosshairs: { show: true },
                labels: {
                    // formatter: function(value){
                    //     return new Date(value)
                    // },
                    
                    offsetX: 0,
                    offsetY: 5,
                    style: {
                        fontSize: '12px',
                        fontFamily: 'Nunito, sans-serif',
                        cssClass: 'apexcharts-xaxis-title'
                    }
                },
            },
            yaxis: {
                tickAmount: list.length, // default - 7
                labels: {
                    formatter: function(value) {
                        return value;
                    },
                    offsetX: -10,
                    offsetY: 0,
                    style: {
                        fontSize: '12px',
                        fontFamily: 'Nunito, sans-serif',
                        cssClass: 'apexcharts-yaxis-title'
                    },
                },
            },
            
            grid: {
                borderColor: is_dark ? '#191e3a' : '#e0e6ed',
                strokeDashArray: 5,
                xaxis: { lines: { show: true } },
                yaxis: { lines: { show: false } },
                padding: { top: 0, right: 0, bottom: 0, left: 0 },
            },
            legend: {
                position: 'top',
                horizontalAlign: 'right',
                offsetY: 0,
                fontSize: '16px',
                fontFamily: 'Nunito, sans-serif',
                markers: {
                    width: 10,
                    height: 10,
                    strokeWidth: 0,
                    strokeColor: '#fff',
                    fillColors: undefined,
                    radius: 12,
                    onClick: undefined,
                    offsetX: 0,
                    offsetY: 0
                },
                itemMargin: { horizontal: 20, vertical: 5 },
            },
            tooltip: { theme: 'dark', marker: { show: true }, x: { show: false } },
            fill: {
                type: 'gradient',
                gradient: {
                    type: 'vertical',
                    shadeIntensity: 1,
                    inverseColors: !1,
                    opacityFrom: is_dark ? 0.19 : 0.28,
                    opacityTo: 0.05,
                    stops: is_dark ? [100, 100] : [45, 100],
                },
            },
        };
    })
</script>
