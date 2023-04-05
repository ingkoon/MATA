<template>
    <div>
        <div ref="pieChart"></div>
        <div ref="barChart"></div>
    </div>
</template>

<script>
    import axios from 'axios';
    import { ref, watch, onMounted, reactive, watchEffect, onUpdated } from 'vue';
    import { useRoute } from 'vue-router';
    import ApexCharts from 'apexcharts';
    import { useStore } from 'vuex';

    export default {
        setup() {
            const store = useStore();
            const route = useRoute();
            const state = reactive({
                serviceId: null,
                accessToken: localStorage.getItem("accessToken"),
                clientToken: null,
                curNode: null,
            });
            const pieChart = ref(null);
            const barChart = ref(null);
            const items = {
                nodes: [],
                links: [],
            };

            // console.log("url : " + process.env.VUE_APP_API_HOST+`/api/v1/weblog/refersall?basetime=${Date.now()}&interval=1h&serviceid=${store.state.serviceId}`);
            onMounted(async () => {
                store.state.serviceId = route.path.split('/')[2];
                let response = await axios({
                    method:'get',
                    url: process.env.VUE_APP_API_HOST+`/api/v1/weblog/refersall?basetime=${Date.now()}&interval=all&serviceid=2`,
                    headers:{
                        "Authorization": `Bearer ${state.accessToken}`,
                    },
                })


                console.log("------------response----------------");
                console.log(response);
                console.log("------------response.data----------------");

                console.log(response.data);
                const data = response.data;

                // Calculate the pageenterPercentage for each object in the data
                data.forEach(obj => {
                    obj.pageenterPercentage = obj.totalPageenter / obj.totalSession;
                });

                // Group the data by referrer
                const referrerGroups = data.reduce((groups, item) => {
                    const referrer = item.referrer;
                    const existingGroup = groups.find(group => group.x === referrer);
                    if (existingGroup) {
                        existingGroup.y += item.totalPageenter;
                    } else {
                        if (referrer !== null) {
                            groups.push({
                                x: referrer,
                                y: item.totalPageenter
                            });
                        }
                    }
                    return groups;
                }, []);


                console.log("------------referrerGroups----------------");
                console.log(referrerGroups);

                const chartData = referrerGroups.filter(group => group.x !== null);

                // pie option
                const options = {
                    series: chartData.map(data => data.y),
                    chart: {
                        type: 'pie',
                        height: 350,
                        background: '#fff',
                    },
                    labels: chartData.map(data => data.x),
                    responsive: [{
                        breakpoint: 480,
                        options: {
                            chart: {
                                width: 200
                            },
                            legend: {
                                position: 'left'
                            }
                        }
                    }]
                }

                const barOptions = {
                    series: [
                        {
                            name: "Page enter",
                            data: chartData.map((item) => item.z),
                        },
                    ],
                    chart: {
                        type: "bar",
                        height: 350,
                        background: "#fff",
                    },
                    xaxis: {
                        categories: chartData.map((item) => item.x),
                    },
                };

                const chartInstance = new ApexCharts(pieChart.value, options);
                const barChartInstance = new ApexCharts(barChart.value, barOptions);
                await chartInstance.render();
                await barChartInstance.render();

            });

            return {
                pieChart
            };
        }
    };
</script>
