
<!--<template>-->
<!--    <div ref="chartRef"></div>-->
<!--</template>-->

<!--<script>-->
<!--    import { defineComponent, ref, onMounted } from 'vue';-->
<!--    import { sankey, sankeyLinkHorizontal } from 'd3-sankey';-->
<!--    import { scaleLinear, select } from 'd3';-->

<!--    export default defineComponent({-->
<!--        name: 'SankeyChart',-->
<!--        setup() {-->
<!--            const chartRef = ref(null);-->

<!--            onMounted(() => {-->
<!--                const data = {-->
<!--                    nodes: [-->
<!--                        { name: 'Node 1' },-->
<!--                        { name: 'Node 2' },-->
<!--                        { name: 'Node 3' },-->
<!--                        { name: 'Node 4' },-->
<!--                    ],-->
<!--                    links: [-->
<!--                        { source: 0, target: 1, value: 10 },-->
<!--                        { source: 1, target: 2, value: 20 },-->
<!--                        { source: 2, target: 3, value: 30 },-->
<!--                    ],-->
<!--                };-->

<!--                const width = 600;-->
<!--                const height = 400;-->

<!--                const svg = -->
<!--                    // d3-->
<!--                    select(chartRef.value)-->
<!--                    .append('svg')-->
<!--                    .attr('viewBox', [0, 0, width, height]);-->

<!--                const sankeyLayout = sankey()-->
<!--                    .nodeWidth(15)-->
<!--                    .nodePadding(10)-->
<!--                    .extent([-->
<!--                        [0, 0],-->
<!--                        [width, height],-->
<!--                    ]);-->

<!--                const { nodes, links } = sankeyLayout(data);-->

<!--                svg-->
<!--                    .append('g')-->
<!--                    .selectAll('rect')-->
<!--                    .data(nodes)-->
<!--                    .join('rect')-->
<!--                    .attr('x', (d) => d.x0)-->
<!--                    .attr('y', (d) => d.y0)-->
<!--                    .attr('height', (d) => d.y1 - d.y0)-->
<!--                    .attr('width', (d) => d.x1 - d.x0)-->
<!--                    .attr('fill', 'blue');-->

<!--                svg-->
<!--                    .append('g')-->
<!--                    .attr('stroke-opacity', 0.2)-->
<!--                    .selectAll('line')-->
<!--                    .data(links)-->
<!--                    .join('path')-->
<!--                    .attr(-->
<!--                        'd',-->
<!--                        sankeyLinkHorizontal()-->
<!--                            .source((d) => [d.source.x1, d.y0 + (d.y1 - d.y0) / 2])-->
<!--                            .target((d) => [d.target.x0, d.y0 + (d.y1 - d.y0) / 2])-->
<!--                    )-->
<!--                    .attr('stroke', 'black')-->
<!--                    .attr('stroke-width', (d) => d.width)-->
<!--                    .attr('fill', 'none');-->
<!--            });-->

<!--            return {-->
<!--                chartRef,-->
<!--            };-->
<!--        },-->
<!--    });-->
<!--</script>-->


<!--<template>-->
<!--    <div ref="sankeyRef"></div>-->
<!--</template>-->

<!--<script>-->
<!--    import { ref, onMounted } from 'vue';-->
<!--    import { sankey } from 'd3-sankey';-->
<!--    import { select } from 'd3-selection';-->
<!--    import axios from 'axios';-->

<!--    export default {-->
<!--        name: 'SankeyChart',-->
<!--        props: {-->
<!--            width: {-->
<!--                type: Number,-->
<!--                required: false,-->
<!--                default: 600-->
<!--            },-->
<!--            height: {-->
<!--                type: Number,-->
<!--                required: false,-->
<!--                default: 400-->
<!--            },-->
<!--            margin: {-->
<!--                type: Object,-->
<!--                required: false,-->
<!--                default: () => ({ top: 10, right: 10, bottom: 10, left: 10 })-->
<!--            }-->
<!--        },-->
<!--        setup(props, context) {-->
<!--            const sankeyRef = ref(null);-->

<!--            onMounted(async () => {-->
<!--                const response = await axios.get(process.env.VUE_APP_API_HOST+"/api/v1/weblog/journals?basetime=16804104720000&interval=1d&serviceid=2");-->
<!--                const data = response.data;-->
<!--                const sankeyData = { nodes: [], links: [] };-->
<!--                data.forEach(d => {-->
<!--                    console.log(d)-->
<!--                    sankeyData.nodes.push({ name: d.locationFrom, id:d.locationFrom });-->
<!--                    sankeyData.nodes.push({ name: d.locationTo, id:d.locationTo });-->
<!--                    sankeyData.links.push({ source: d.locationFrom, target: d.locationTo, value: d.totalJournal });-->
<!--                });-->
<!--                await setting();-->
<!--                const svg = select(sankeyRef.value)-->
<!--                    .append('svg')-->
<!--                    .attr('width', props.width + props.margin.left + props.margin.right)-->
<!--                    .attr('height', props.height + props.margin.top + props.margin.bottom)-->
<!--                    .append('g')-->
<!--                    .attr('transform', `translate(${props.margin.left},${props.margin.top})`);-->

<!--                const sankeyLayout = sankey()-->
<!--                    .nodeWidth(20)-->
<!--                    .nodePadding(10)-->
<!--                    .size([props.width, props.height]);-->

<!--                const { nodes, links } = sankeyLayout(sankeyData);-->

<!--                const link = svg.append('g')-->
<!--                    .selectAll('path')-->
<!--                    .data(links)-->
<!--                    .join('path')-->
<!--                    .attr('d', sankey.linkHorizontal())-->
<!--                    .attr('fill', 'none')-->
<!--                    .attr('stroke', '#000')-->
<!--                    .attr('stroke-opacity', 0.2)-->
<!--                    .attr('stroke-width', d => Math.max(1, d.width));-->

<!--                const node = svg.append('g')-->
<!--                    .selectAll('rect')-->
<!--                    .data(nodes)-->
<!--                    .join('rect')-->
<!--                    .attr('x', d => d.x0)-->
<!--                    .attr('y', d => d.y0)-->
<!--                    .attr('height', d => d.y1 - d.y0)-->
<!--                    .attr('width', 20)-->
<!--                    .attr('fill', '#69b3a2')-->
<!--                    .attr('stroke', '#000');-->
<!--            });-->

<!--            return {-->
<!--                sankeyRef-->
<!--            };-->
<!--        },-->
<!--        setting() {-->
<!--            -->
<!--        }-->
<!--    };-->
<!--</script>-->



<template>
    <svg ref="svg" class="demo" />
<!--    <div class='row layout-top-spacing'>-->
<!--        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">-->
<!--            <div class='widget p-3'>-->
<!--                <h5>사용자 토큰</h5>-->
<!--                <div> {{ $route.params.id }}</div>-->
<!--                <div>-->
<!--                    토큰 : {{  }}-->
<!--                </div>-->
<!--                <button @click="get_token">재발급</button>-->
<!--            </div>-->
<!--        </div>-->
<!--    </div>-->
</template>
<script>
    import * as d3 from 'd3';
    import { sankey, sankeyLinkHorizontal } from 'd3-sankey';
    import axios from 'axios';
    import { reactive, watch } from 'vue';

    export default {
        data() {
            return {
                items: {
                    nodes: [
                        { node: 0, name: 'node0', id: 'node0', color: 'red' },
                        { node: 1, name: 'node1', id: 'node1', color: 'orange' },
                        { node: 2, name: 'node2', id: 'node2', color: 'blue' },
                        { node: 3, name: 'node3', id: 'node3', color: 'green' },
                        { node: 4, name: 'node4', id: 'node4', color: 'brown' },
                    ],
                    links: [
                        { source: 'node0', target: 'node2', value: 1, color: 'red' },
                        { source: 'node1', target: 'node2', value: 2, color: 'orange' },
                        { source: 'node1', target: 'node3', value: 2, color: 'orange' },
                        { source: 'node0', target: 'node4', value: 3, color: 'red' },
                    ],
                },
            };
        },
        
        
        mounted() {
            const response = axios.get(process.env.VUE_APP_API_HOST+"/api/v1/weblog/journals?basetime=16804104720000&interval=1d&serviceid=2")
                .then(response => {
                    console.log(response)
                    const data = response.data;
                    this.nodes = [];
                    this.links = [];
                })

            const { items } = this;
            const width = 600;
            const height = 800;
            const nodeWidth = 80;
            const nodeHeight = 160;
            const nodePadding = 200;

            const ENABLE_LINKS_GRADIENTS = true;

            const svg = d3
                .select(this.$refs.svg)
                .attr('viewBox', [0, -100, width, height + 200]);

            const s = sankey()
                .nodeId((d) => d.name)
                .nodeWidth(80)
                .nodePadding(100)
                .extent([
                    [1, 1],
                    [width, height],
                ])(items);

            const { nodes, links } = sankey()
                .nodeId((d) => d.name)
                .nodeWidth(nodeWidth)
                .nodePadding(nodePadding)
                .extent([
                    [1, 1],
                    [width, height - nodeHeight],
                ])(items);

            svg
                .append('g')
                .attr('stroke', '#000')
                .attr('stroke-width', '0')
                .selectAll('rect')
                .data(nodes)
                .join('rect')
                .attr('x', (d) => d.x0)
                .attr('y', (d) => d.y0)
                .attr('height', (d) => 160)
                .attr('width', (d) => d.x1 - d.x0)
                .attr('fill', (d) => d.color)
                .append('title')
                .text((d) => `${d.name}\n${d.value}`);

            svg.selectAll("rect")
                .on("mouseover", function() {
                    d3.select(this)
                        .attr("fill", "yellow")
                        .attr("stroke", "orange")
                        .attr("stroke-width", 2);
                })
                .on("mouseout", function() {
                    d3.select(this)
                        .attr("fill", (d) => d.color)
                        .attr("stroke", "none")
                        .attr("stroke-width", 0);
                });

            const link = svg
                .append('g')
                .attr('fill', 'none')
                .attr('stroke-opacity', 0.5)
                .selectAll('g')
                .data(links)
                .join('g');
            //.style("mix-blend-mode", "multiply");

            if (ENABLE_LINKS_GRADIENTS) {
                const gradient = link
                    .append('linearGradient')
                    .attr('id', (d) => (d.uid = `${d.source.id}-to-${d.target.id}`))
                    .attr('gradientUnits', 'userSpaceOnUse')
                    .attr('x1', (d) => d.source.x1)
                    .attr('x2', (d) => d.target.x0);

                gradient
                    .append('stop')
                    .attr('offset', '0%')
                    .attr('stop-color', (d) => d.source.color);

                gradient
                    .append('stop')
                    .attr('offset', '100%')
                    .attr('stop-color', (d) => d.target.color);
            }

            link
                .append('path')
                .attr('d', sankeyLinkHorizontal())
                .attr('stroke', (d) =>
                    !ENABLE_LINKS_GRADIENTS ? d.color : `url(#${d.uid})`
                )
                .attr('stroke-width', (d) => Math.max(1, d.width));

            link
                .append('title')
                .text((d) => `${d.source.name} → ${d.target.name}\n${d.value}`);

            svg
                .append('g')
                .attr('font-family', 'sans-serif')
                .attr('font-size', 10)
                .selectAll('text')
                .data(nodes)
                .join('text')
                .attr('x', (d) => d.x0 + 8)
                .attr('y', (d) => (d.y1 + d.y0) / 2)
                .attr('dy', '0.35em')
                .attr('text-anchor', 'start')
                .text((d) => d.name);
            
        },

        getComponentStats : async () => {
            let resp = await axios({
                method:'get',
                url: process.env.VUE_APP_API_HOST+`/api/v1/weblog/components?basetime=${Date.now()}&interval=${ state.configs.components.interval }&serviceid=${state.serviceId}`,
                headers:{
                    "Authorization": `Bearer ${state.accessToken}`,
                },
            })
            let body = resp.data;
            body = [{
                totalClick : 12,
                targetId : "btn-login",
                location : "localhost",
                updateTimestamp : "",
                serviceId : 2
            },{
                totalClick : 6,
                targetId : "btn-join",
                location : "localhost/first",
                updateTimestamp : "",
                serviceId : 2
            },];
            body.sort(function(a, b) {
                return b.totalClick - a.totalClick;
            })
            state.data.components.list = body;
            state.data.components.totalNum = state.data.components.list;
            state.data.components.totalClickSum = state.data.components.list.reduce((acc, cur) => acc + cur.totalClick, 0)
            console.log()
        }
    };
</script>
<style scoped>
    svg {
        padding-inline: max(2rem, calc(50% - 24rem));
    }
</style>
