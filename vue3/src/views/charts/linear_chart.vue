<template>
    <div class="col-xl-8 col-lg-12 col-md-12 col-sm-12 col-12 layout-spacing">
        <div class="widget widget-">
            <div class="widget-heading">
                <h5>Revenue</h5>
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
                        <li v-for="(item, index) in selectedPeriod" :key="item.id">
                            <a href="javascript:;" class="dropdown-item" v-on:click="selectedPeriod = item.period">{{item.label}}</a>
                        </li>
                    </ul>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="tlnRevenue">
                        <li v-for="(item, index) in selectedPeriod" :key="item.id">
                            <a href="javascript:;" class="dropdown-item" v-on:click="selectedTimeLine = item.timeLine">{{ item.label }}</a>
                        </li>
                    </ul>
                </div>
            </div>

<!--            <div class="widget-content">-->
<!--                <div class="chart-title">Total Profit <span class="text-primary ms-1">$10,840</span></div>-->
<!--                <apex-chart v-if="revenue_options" height="325" type="area" :options="revenue_options" :series="revenue_series"></apex-chart>-->
<!--            </div>-->
        </div>
    </div>
</template>

<script>
    import { useRoute } from 'vue-router'
    import { useStore } from 'vuex'
    import ApexChart from 'vue3-apexcharts';
    import { computed, inject, onMounted, reactive, ref } from 'vue';
    export default {
        components: { ApexChart },
        
        setup() {
            // onMounted(()=>{
            //     console.log("11")
            //     fetchData(selectedTimeLine, selectedPeriod, serviceId);
            // });
            
            const store = useStore();
            const period= reactive([
                {label: 'Daily', value: 1},
                {label: 'Weekly', value: 7},
                {label: 'Monthly', value: 30},
                {label: 'Yearly', value: 365},
            ]);
            const timeLine = reactive([
                {label: '5m', value: '5'},
                {label: '10m', value: '10'},
                {label: '30m', value: '30'},
                {label: '1h', value: '60'},
                {label: '6h', value: '360'},
                {label: '12h', value: '720'},
            ]);
            
            const selectedTimeLine = ref(timeLine[0]);
            const selectedPeriod = ref(period[0]);

            
            const fetchData = (baseTime, interval, serviceId) => {
                console.log('basetime = ' + parseInt(baseTime, 10) +  ' interval = ' + interval +' serviceid = ' +  serviceId);
                console.log("test")
                store.dispatch('fetchDurations', { baseTime, interval, serviceId})
            }
            
            const serviceId = useRoute().params.id; // 'id' 파라미터 값 가져오기
            
            let date = new Date().getTime();
            
            fetchData(date, '5m', serviceId);            
            const duration_series = ref([
                {
                    name: '인원 수',
                    data: store.state.durations.map(duration => duration.count)
                },
            ]);
            const duration_options = computed(() => {
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
                            discrete: [
                                { seriesIndex: 0, dataPointIndex: 6, fillColor: '#1b55e2', strokeColor: '#fff', size: 7 },
                                { seriesIndex: 1, dataPointIndex: 5, fillColor: '#e7515a', strokeColor: '#fff', size: 7 },
                            ],
                        },
                        // labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                        labels: store.state.durations.map(duration => duration.timeLine),
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
                            tickAmount: 7,
                            labels: {
                                formatter: function(value) {
                                    return value / 1000 + 'K';
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
            return {
                store,
                duration_options,
                duration_series,
                selectedTimeLine,
                selectedPeriod,
                serviceId,
                fetchData
            }
        },
    }
</script>
