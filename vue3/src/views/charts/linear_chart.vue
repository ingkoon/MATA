<template>
    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 layout-spacing">
        <div class="widget widget-">
            <div class="widget-heading">
                <h5>Durations</h5>
                <div style='float: right;'>
                    <div class="dropdown btn-group" style='margin-right: 25px; box-shadow: 2px 2px 2px gray; border-radius: 5px; background: #1B2E4BFF;' >
                        <a href="javascript:;" id="ddlRevenue" class="btn dropdown-toggle btn-icon-only" data-bs-toggle="dropdown" aria-expanded="false">
                            <p style='font-weight: bold;text-align: center; color: white; margin: 10px;'>{{state.selectedLocation}}</p>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="tlnRevenue">
                            <li v-for="item in state.location">
                                <a href="javascript:;" class="dropdown-item" v-on:click= "state.selectedLocation = item">{{ item }}</a>
                            </li>
                        </ul>
                    </div>
                    
                    <div class="dropdown btn-group" style='margin-left: 25px; box-shadow: 2px 2px 2px gray; border-radius: 5px; background: #1B2E4BFF;'>
                        <a href="javascript:;" id="ddlRevenue" class="btn dropdown-toggle btn-icon-only" data-bs-toggle="dropdown" aria-expanded="false">
                            <p style='font-weight: bold;text-align: center; margin: 10px; color: white;'>{{state.data.selectedTimeLine}}</p>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="tlnRevenue">
                            <li v-for="item in state.data.timeLine">
                                <a href="javascript:;" class="dropdown-item" v-on:click="state.data.selectedTimeLine = item.label">{{ item.label }}</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="widget-content">
                <div class="chart-title">서비스 체류 시간 단위 <span class="text-primary ms-1">{{state.data.selectedTimeLine}}</span></div>
                <div class="chart-title">체류 서비스 <span class="text-primary ms-1">{{state.selectedLocation}}</span></div>
                <apex-chart v-if="state.duration_options" height="325" type="area" :options="state.duration_options" :series="state.duration_series"></apex-chart>
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
            selectedTimeLine : '5m',
            serviceId : route.params.id,
            
            timeLine : [
                {label: '5m', value: '5'},
                {label: '10m', value: '10'},
                {label: '30m', value: '30'},
                {label: '1h', value: '60'},
                {label: '6h', value: '360'},
                {label: '12h', value: '720'},
                {label: '1d', value: '1440'},
                {label: '1w', value: '1440'},
                {label: '1m', value: '1440'},
                {label: '1mo', value: '1440'},
                {label: '6mo', value: '1440'},
                {label: '1y', value: '1440'},
                {label: 'all', value: '1440'},
            ],
        },
        duration_series: [],
        duration_options: {},
        location: [],
        selectedLocation: null,
    });
    
    const fetchData = async (baseTime, interval, serviceId) => {
        await store.dispatch('fetchDurations', { baseTime, interval, serviceId });
        if(state.location.length === 0)
            await setState();
        await updateChart();
        
    }
    
    const setState = async () =>  {
        const tmp = Object.keys(JSON.parse(store.state.durations))
        state.location = tmp;
        state.selectedLocation = tmp[0]; 
    }
    
    const updateChart = async ()=>{
        const parseDurations = JSON.parse(store.state.durations);
        const optionDataList = parseDurations[state.selectedLocation];
        let sortedOptionDataList = typeof optionDataList === "undefined" ? [] : optionDataList.sort((o1, o2) => (o1.update_timestamp - o2.update_timestamp));
        let timestamps = sortedOptionDataList.map(dataList => new Date(dataList.update_timestamp).toISOString().split('T')[1]);
        let sessions = sortedOptionDataList.map(dataList => dataList.total_session);
        let maxVal = Math.max(sessions) ^ 2;
        // let maxVal = 1;
        
        state.duration_series = ref([
            { name: '인원 수', data: sessions}
        ]);
        state.duration_options = computed(() => {
            const is_dark = store.state.is_dark_mode;

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
                        { seriesIndex: 0, dataPointIndex: 6, fillColor: '#1b55e2', strokeColor: '#fff', size: 7 },
                        // { seriesIndex: 1, dataPointIndex: 5, fillColor: '#e7515a', strokeColor: '#fff', size: 7 }
                },
                // labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                labels: timestamps,
                xaxis: {
                    axisBorder: { show: false },
                    axisTicks: { show: false },
                    crosshairs: { show: true },
                    labels: {
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
                    tickAmount: maxVal, // default - 7
                    labels: {
                        formatter: function(value) {
                            return value;
                        },
                        offsetX: 0,
                        offsetY: -10,
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
    }
    onMounted(()=> {
        fetchData(Date.now(), state.data.selectedTimeLine, route.params.id);
    });

    watchEffect(()=>{
        const selectedTimeLine = state.data.selectedTimeLine;
        const selectedLocation = state.selectedLocation;
        if(selectedTimeLine && selectedLocation){
            fetchData(Date.now(), state.data.selectedTimeLine, route.params.id);    
        }
    });
</script>
