<template>
    <div>
        <h1>Donut Chart</h1>
        <apexchart type="pie" :options="chartOptions" :series="chartSeries"></apexchart>
    </div>
</template>

<script>
    export default {
        components:{
            name: 'donutChart',
        },
        data() {
            return {
                chartOptions: {
                    chart: {
                        type: 'pie'
                    },
                    labels: [],
                    responsive: [
                        {
                            breakpoint: 480,
                            options: {
                                chart: {
                                    width: 200
                                },
                                legend: {
                                    position: 'bottom'
                                }
                            }
                        }
                    ]
                },
                chartSeries: []
            };
        },
        mounted() {
            console.log('before fetch');
            console.log(Date.now());
            fetch(process.env.VUE_APP_API_HOST + `/api/v1/weblog/refersall?basetime=${Date.now()}&interval=all&serviceid=2`)
                .then(response => response.json())
                .then(data => {
                    const labels = [];
                    const series = [];
                    
                    data.forEach(item => {
                        labels.push(item.referrerId);
                        series.push(item.totalSession);
                    });

                    this.chartOptions.labels = labels;
                    this.chartSeries = series;
                });
        }
    };
</script>
