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
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="tlnRevenue">
                        <li v-for="item in location"> // 여기 조심해라 인재
                            <a href="javascript:;" class="dropdown-item" v-on:click="state.data.selectedTimeLine = item">{{ item }}</a>
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
                            <a href="javascript:;" class="dropdown-item" v-on:click="state.data.selectedLocation = item.label">{{ item.label }}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="widget-content">
                <div class="chart-title">서비스 체류 시간 구간 <span class="text-primary ms-1">{{store.state.durations.length}}</span> 개</div>
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
            // location: "1",
            selectedLocation: Object.keys(JSON.parse(store.state.durations)),
            selectedTimeLine : '5m',
            selectedDuration: '1',
            serviceId : route.params.id,
            accessToken: localStorage.getItem("accessToken"),
            
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
    });

    const fetchData = async (baseTime, interval, serviceId) => {
        await store.dispatch('fetchDurations', { baseTime, interval, serviceId });
        // updateChart();
    }

    const updateChart = async ()=>{
        console.log("------------start parsing----------------");
        const parseDurations = JSON.parse(store.state.durations);
        // const parseDurations = store.state.durations;
        console.log(parseDurations.typeof);
        console.log("-------------parse-----------");
        console.log(parseDurations);
        let sessions = Object.keys(parseDurations);
        console.log("----------list--------------");
        console.log(sessions);
        let values = parseDurations[0];
        console.log("-----------values-------------");
        console.log(values);
        // let timestamps = values.map(value => value.updateTimestamp);
        // let durations = values.map(value => value.totalDuration);
        
        // let list = parseDurations.map(duration => duration.totalSession);
        // let sessions = store.state.durations.map(duration => duration.totalSession);
        let timestamps = parseDurations.map(duration => new Date(duration.updateTimestamp).toISOString().split('T')[0]);
        console.log("----------timestamp--------------");
        console.log(timestamps);
        let maxVal = Math.max(sessions);
        // let maxVal = 1;
        
        state.duration_series = ref([
            { name: '인원 수', data: sessions}
        ]);
        state.duration_options = computed(() => {
            const is_dark = store.state.is_dark_mode;
            // console.log(list[0]);
            // const list = store.state.durations.map(duration => JSON.stringify(duration));
            // const list = store.state.durations.map(duration => duration);

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
        state.data.location = Object.keys(store.state.durations);
    });

    watchEffect(()=>{
        const selectedTimeLine= state.data.selectedTimeLine;
        fetchData(Date.now(), selectedTimeLine, route.params.id);
        updateChart();
        // setInterval(()=>(updateChart(), 1000));
    });
</script>
<!--데이터를 가져오는 부분을 함수로 수정해서 series와-->
