<template>
  <div  ref="heatmapTarget">
    <iframe ref="iframeRef" src="http://localhost:3001/" width="100%" height="800"></iframe>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import heatmap from 'heatmap.js';

export default {
  setup() {
    const heatmapTarget = ref(null);
    let heatmapInstance = null

    const heatmap1 = ref(null);
    
    onMounted(() => {
      heatmapInstance = heatmap.create({
        container: heatmapTarget.value,
        // container: heatmap1.value,
        
        radius: 25,
        maxOpacity: 0.6,
        minOpacity: 0,
        blur: 0.75,
        gradient: {
          '.3': 'blue',
          '.5': 'green',
          '.7': 'red'
        }

        
      })
      const data = [
        { x: 100, y: 20, value: 100 },
        { x: 101, y: 20, value: 100 },
      ]



      heatmapInstance.setData({
        max:100,
        data
      })
    });
    
    return {
      heatmapTarget
      // heatmap1
    };
  }
}
</script>