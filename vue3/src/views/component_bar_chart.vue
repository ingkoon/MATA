<template>
    <div id="app">
        <div ref="barChart"></div>
    </div>
</template>

<script>
    import { onMounted, ref } from 'vue';
    import Plotly from 'plotly.js-dist';
    import axios from 'axios';

    export default {
        name: 'BarChart',
        setup() {
            const barChart = ref(null);

            onMounted(() => {
                const data = [{ x: ['giraffes', 'orangutans', 'monkeys'], y: [20, 14, 23], type: 'bar' }];
                const layout = { title: 'A Simple Bar Chart' };
                const config = { responsive: true };
                console.log(data)

                axios({
                    method:'get',
                    url: process.env.VUE_APP_API_HOST+'/api/v1/weblog/components?basetime=20230331085028&interval=1h&serviceid=2',

                })
                    .then(res=>{
                        console.log(res)
                        const components=[]
                        const targetCounts={}
                        for (const iterator of res.data) {
                            console.log(iterator.targetId)
                            const targetId=iterator.targetId;
                            if(targetId in targetCounts){
                                targetCounts[targetId]++;
                            } else{
                                targetCounts[targetId]=1;
                            }
                        }
                        const result=[]
                        for ( const[targetId,count] of Object.entries(targetCounts)){
                            result.push(count);
                            components.push(targetId)
                        }
                        console.log("컴포넌트 중복제거는:",components)
                        data[0].x=components
                        data[0].y=result
                        console.log("변형 후 data",data,result)
                        Plotly.newPlot(barChart.value, data, layout, config);

                    })
                    .catch(err=>{
                        console.log(err)
                    })
                
                Plotly.newPlot(barChart.value, data, layout, config);
            });

            return { barChart };
        },
    };
</script>
