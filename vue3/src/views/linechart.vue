<template>
    <div ref="linechart"></div>
</template>

<script>
    import { ref, onMounted } from 'vue';
    import Plotly from 'plotly.js-dist';
    import axios from 'axios';

    export default {
        setup() {
            const linechart = ref(null);
            const trace1 = {
                x: [1, 2, 3, 7],
                y: [10, 11, 12, 13],
                mode: 'lines',
                name: 'Line 1'
            };

            const trace2 = {
                x: [1, 2, 3, 4],
                y: [12, 13, 14, 15],
                mode: 'lines',
                name: 'Line 2'
            };

            const data = [];
            const tempo3=[]
            const layout = {
                title: 'Multiple Lines Chart',
                type: 'date',
                tickformat: '%Y-%m-%d %H:%M:%S',
            };
            const datetimes=[]
            
            onMounted(() => {
                const present=Date.now()
                const tempo={}
                console.log(present)
                axios({
                    method:'get',
                    url: process.env.VUE_APP_API_HOST+"/api/v1/weblog/refers?interval=5m&serviceid=2&basetime=1680625761225",

                })
                    .then(res=>{
                        console.log(res)
                        for (const i of res.data) {
                            let dateFormat=new Date(i.updateTimestamp)
                            let referrer=i.referrer
                            let sessionCount=i.totalSession
                            if ( referrer in tempo) {
                                tempo[referrer].sessionCount.push(sessionCount)
                                tempo[referrer].dateFormat.push(dateFormat)
                            } else {
                                tempo[referrer]={}
                                tempo[referrer].sessionCount=sessionCount
                                tempo[referrer].dateFormat=[]
                                tempo[referrer].dateFormat.push(dateFormat)
                                tempo[referrer].sessionCount=[]
                                tempo[referrer].sessionCount.push(sessionCount)
                            }
                            
                            
                            
                            
                            datetimes.push(dateFormat)
                            
                            
                        }
                        console.log(tempo)
                        
                        
                        trace1.x=datetimes
                        trace2.x=datetimes
                        console.log("잡다한것:",datetimes,trace1,trace2)
                        console.log('data:',data)
                        
                        for (const re in tempo) {
                            console.log(re)
                            
                            if (re==null){
                                
                            }
                            
                            else{data.push( {x: tempo[re].dateFormat, y: tempo[re].sessionCount, mode: 'lines',name:re}   )}
                        }
                        console.log(data)
                        
                        Plotly.newPlot(linechart.value, data, layout);
                        

                    })
                    .catch(err=>{
                        console.log(err)
                    })    
                
                
                

                // Plotly.newPlot(linechart.value, data, layout);
            });

            return {
                linechart
            };
        }
    }
</script>
