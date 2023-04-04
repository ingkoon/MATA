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
            fetch('http://ec2-3-38-85-143.ap-northeast-2.compute.amazonaws.com//api/v1/weblog/refers?basetime=30&interval=5')
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
