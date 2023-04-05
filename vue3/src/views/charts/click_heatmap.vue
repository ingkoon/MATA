<template>
  <div id = "heatmapContainer" class="heatmap-container" ref="heatmapTarget" style="width: 854px; height: 480px;">
    <iframe ref="iframeRef" id="my-iframe" src='about:blank' width="100%" height="100%" ></iframe>
  </div>
</template>

<script>
import { ref, onMounted, watch} from 'vue';
import heatmap from 'heatmap.js';
import axios from 'axios';
import { useRoute } from 'vue-router';
import { useStore } from 'vuex';



export default {

  setup() {
    const route = useRoute();
    const store = useStore();
    const iframeRef = ref(null);
    const heatmapWrapper=ref(null);
    const heatmapTarget = ref(null);
    let heatmapInstance = null;
    const heatmapData = ref([]);
    const tempData = ref([]);
    const data = [      ]

    const fetchClickData = async (url) => {
      try{

        const frame = document.getElementById("my-iframe");
        console.log("url은" + url);
        const response = await axios.get(process.env.VUE_APP_API_HOST+`/api/v1/weblog/clicks?basetime=${Date.now()}&interval=5m&serviceid=${store.state.serviceId}&location="${url}"`); // API 엔드포인트에 맞게 수정해주세요
        // const response = await axios.get(process.env.VUE_APP_API_HOST+`/api/v1/weblog/clicks?basetime=${Date.now()}&interval=5m&serviceid=${store.state.serviceId}&location="http://localhost:3001/"`); // API 엔드포인트에 맞게 수정해주세요
// 
        tempData.value = response.data;
        heatmapData.value = tempData.value.map(item=>{
        return {
          x: Math.round(item.positionX *(854/1920)),
          // x: item.positionX,
          y:  Math.round(item.positionY *(480/1080)),
          y:  item.positionY,
          value : item.totalClick * 20
        }
        
      })

      // const rect = ele.getBoundingClientRect();
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
    

    const loadHeatmap = (url) =>{
      iframeRef.value.src = url;
      iframeRef.value.onload = () => {
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
      });
      fetchClickData(url);
      }
    }


    onMounted(() => {
  

      const curNode = localStorage.getItem('curNode');
      
      loadHeatmap(store.state.curUrl || curNode);
  

      watch(() => store.state.curUrl, (newVal, oldVal) => {
        if (newVal !== oldVal) {
          data.splice(0, data.length);
          heatmapInstance.setData({
          max:100,
          data: []
      });
          iframeRef.value.src = newVal;
          loadHeatmap(newVal);
        }
      });

      watch(() => store.state.serviceId, (newServiceId, oldServiceId) => {
                // 페이지 변경 감지, curNode를 기본주소로...
                console.log(`서비스Id 변경감지${oldServiceId} => ${newServiceId}`);
                data.splice(0, data.length);
                store.commit('updateUrl', "about:blank");
                loadHeatmap(store.state.curUrl);
      })

      

    });

    return {
      heatmapWrapper,
      heatmapTarget,
      iframeRef,
    };
  }
}
</script>

<style scoped>

</style>
