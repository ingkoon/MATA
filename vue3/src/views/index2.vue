<template>
    <div ref="plotlyChart"></div>
    <piechart></piechart>
</template>

<script>
    import { onMounted, ref } from 'vue';
    import Plotly from 'plotly.js-dist';
    import piechart from '@/views/piechart.vue';
    export default {
        components: {
            piechart,
        },
        setup() {
            const plotlyChart = ref(null);

            onMounted(() => {
                const data = [{ x: [1, 2, 3], y: [2, 6, 3], type: 'scatter' }];
                const layout = {
                    title: 'My Plot',
                    xaxis: {
                        range: [1, 3] // 초기 x축 범위 설정
                    }
                };
                const config = { responsive: true };

                Plotly.newPlot(plotlyChart.value, data, layout, config);

                plotlyChart.value.on('plotly_relayout', (eventData) => {
                    if (eventData['xaxis.range[0]'] && eventData['xaxis.range[1]']) {
                        const updatedLayout = {
                            xaxis: {
                                range: [eventData['xaxis.range[0]'], eventData['xaxis.range[1]']]
                            }
                        };
                        Plotly.update(plotlyChart.value, {}, updatedLayout);
                    }
                });
            });

            return {
                plotlyChart,
            };
        },
    };
</script>
