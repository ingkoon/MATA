<template>
    <div id="app">
        <div ref="barChart"></div>
    </div>
</template>

<script>
    import { onMounted, ref } from 'vue';
    import Plotly from 'plotly.js-dist';

    export default {
        name: 'BarChart',
        setup() {
            const barChart = ref(null);

            onMounted(() => {
                const data = [{ x: ['giraffes', 'orangutans', 'monkeys'], y: [20, 14, 23], type: 'bar' }];
                const layout = { title: 'A Simple Bar Chart' };
                const config = { responsive: true };
                
                console.log(data)

                Plotly.newPlot(barChart.value, data, layout, config);
            });

            return { barChart };
        },
    };
</script>
