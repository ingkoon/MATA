<template>
  <div  ref="heatmapTarget">
    <iframe ref="iframeRef" src="http://localhost:3001/" width="1280" height="720"></iframe>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import heatmap from 'heatmap.js';
import axios from 'axios';

export default {
  setup() {
    const heatmapTarget = ref(null);
    let heatmapInstance = null;
    const heatmapData = ref([]);
    const tempData = ref([]);
    const data = [
      ]

    const fetchClickData = async () => {
      try{
        const response = await axios.get(`http://ec2-3-38-85-143.ap-northeast-2.compute.amazonaws.com/api/v1/weblog/clicks?basetime=20230331085028&interval=1h&serviceid=2`); // API 엔드포인트에 맞게 수정해주세요
        tempData.value = response.data;
        
        heatmapData.value = tempData.value.map(item=>{
        return {
          x: item.positionX * 0.66,
          y: item.positionY * 0.66,
          value : item.totalClick * 10
        }
        
      })
      for (const i of heatmapData.value) {
        const transform = {
          x: i.x,
          y: i.y,
          value : i.value
        }
        data.push(transform);
      }

      console.log(data);
      setHeatmapData();
      
      } catch (error){
        console.error();
      }
    };


    const setHeatmapData = () => {
      heatmapInstance.setData({
        max:100,
        data
      });
    };
    


    
    onMounted(() => {
      fetchClickData();

      heatmapInstance = heatmap.create({
        container: heatmapTarget.value,
        radius: 25,
        maxOpacity: 0.6,
        minOpacity: 0,
        blur: 0.75,
        gradient: {
          '.3': 'blue',
          '.5': 'green',
          '.7': 'red'
        },        
      })
    });

    
    return {
      heatmapTarget
    };
  }
}
</script>