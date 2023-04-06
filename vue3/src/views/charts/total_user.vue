<template>
        <div class="widget widget-total-order">
            <div class="widget-heading">
                <div>
                    <div class="w-numeric-title">total Visit</div>
                    <div class="w-value">{{ store.state.totalDailyUser }}</div>
                </div>
            </div>
            <div class="widget-content p-0">
                <apex-chart v-if="state.total_orders_options" height="290" type="area" :options="state.total_orders_options" :series="state.total_orders_series"></apex-chart>
            </div>
        </div>
</template>

<script setup>
    import { useRoute } from 'vue-router'
    import { useStore } from 'vuex'
    import ApexChart from 'vue3-apexcharts';
    import { computed, onMounted, onUpdated, reactive, ref } from 'vue';
    
    const route = useRoute();
    const store = useStore();
    const state=  reactive({
        components: { ApexChart },
        data: {
            serviceId : route.params.id,
            totalDailyUser : null,
            total_orders_series : [],
            total_orders_options : {},
        },
    });
    
    const fetchTotalDailyUser = async (baseTime, interval, serviceId)=> {
        await store.dispatch('fetchDailyUser', {baseTime, interval, serviceId});
        await  updateTotalDailyUserGraph();
    }
    
    const updateTotalDailyUserGraph = async ()=>{
        state.total_orders_series = ref([{ name: 'Sales', data: [28, 40, 36, 52, 38, 60, 38, 52, 36, 40] }]);
        state.total_orders_options = computed(() => {
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
                tooltip: { enabled:false },
            };
        });
    }
    
    onMounted(()=> {
        fetchTotalDailyUser(Date.now(),'all', state.data.serviceId);
    })

    onUpdated(()=>{
        const totalDailyUser = state.data.totalDailyUser;
    })
</script>
