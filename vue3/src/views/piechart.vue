<template>
    <div ref="pieChart"></div>
</template>

<script>
    import { onMounted, ref } from 'vue';
    import Plotly from 'plotly.js-dist';
    import axios from 'axios';
    import router from '@/router';
    export default {
        name: 'PieChart',
        
        
        setup() {
            const pieChart = ref(null);
            const data = [{
                values: [1,2,3,4,5],
                labels: ['Residential', 'Non-Residential', 'Utility'],
                type: 'pie'
            }];

            onMounted(() => {
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
                            if (!(iterator.targetId in components)) {
                                components.push(iterator.targetId)
                            }
                            const targetId=iterator.targetId;
                            if(targetId in targetCounts){
                                targetCounts[targetId]++;
                            } else{
                                targetCounts[targetId]=1;
                            }
                            
                           
                            
                            
                            
                        }
                        const setofcomponents=new Set(components)
                        const unique_components=[...setofcomponents]
                        const result=[]
                        for ( const[targetId,count] of Object.entries(targetCounts)){
                            result.push(count);
                        }
                        console.log("컴포넌트 중복제거는:",unique_components)
                        data[0].labels=unique_components
                        data[0].values=result
                        console.log("변형 후 data",data,result)
                        Plotly.newPlot(pieChart.value, data, layout, config);
       
                    })
                    .catch(err=>{
                        console.log(err)
                    })
                
                const layout = { title: 'A Simple Pie Chart' };
                const config = { responsive: true };

                // Plotly.newPlot(pieChart.value, data, layout, config);
            });

            return { pieChart };
        },
    };
</script>
