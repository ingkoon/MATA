
<template>
    <svg ref="svgRef"></svg>
</template>

<script>
    import * as d3 from 'd3';
    import { sankey, sankeyLinkHorizontal } from 'd3-sankey';
    import axios from 'axios';
    import { ref, watch, onMounted, reactive, watchEffect, onUpdated } from 'vue';
    import { useRoute } from 'vue-router';
    import { useStore } from 'vuex';
    export default {

        setup() {
            const store = useStore();
            const route = useRoute();
            const state = reactive({
                serviceId: null,
                accessToken: localStorage.getItem("accessToken"),
                clientToken: null,
                curNode: null,
            });
            const svgRef = ref(null);
            const items = {
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
            };
            let svg = null;

            onMounted(() => {
                // 기본 페이지
                store.state.serviceId = route.path.split('/')[2]
                // store.state.journals.curNode = "/"
                getJournalsInfo();
                console.log("axios exit.............." + store.state.journals.curNode)
            });

            watch(() => route.path, (newServiceId, oldServiceId) => {
                // 페이지 변경 감지, curNode를 기본주소로...
                store.state.serviceId = route.path.split('/')[2]
                console.log("serviceId.................", store.state.serviceId)
                getJournalsInfo();
            })

            watch(() => store.state.journals.curNode, () => {
                if(store.state.journals.curNode !== "/") {
                    // 현재 노드 변경
                    console.log("curNode..................", store.state.journals.curNode)
                    drawgraph(store.state.journals.curNode);
                }
            })


            async function changeNodeAndLink(data) {
                // 노드, 링크 변경 -> 그림 다시 그리기
                // items.nodes = []
                // items.links = []
                
                const fromNode = store.state.journals.curNode;
                console.log("fromNode......................",fromNode)
                console.log(data)
                items.nodes.push({name: fromNode, id: fromNode})
                data.forEach((d) => {
                    items.nodes.push({name: d.locationTo, id: d.locationTo})
                    items.links.push({source: fromNode, target: d.locationTo, value: d.totalJournals})
                })
                
                console.log(items.nodes)

                // nodes: [
                //     { node: 0, name: 'node0', id: 'node0', color: 'red' },
                //     { node: 1, name: 'node1', id: 'node1', color: 'orange' },
                //     { node: 2, name: 'node2', id: 'node2', color: 'blue' },
                //     { node: 3, name: 'node3', id: 'node3', color: 'green' },
                //     { node: 4, name: 'node4', id: 'node4', color: 'brown' },
                // ],
                //     links: [
                //     { source: 'node0', target: 'node2', value: 1, color: 'red' },
                //     { source: 'node1', target: 'node2', value: 2, color: 'orange' },
                //     { source: 'node1', target: 'node3', value: 2, color: 'orange' },
                //     { source: 'node0', target: 'node4', value: 3, color: 'red' },
                // ],
                
            }

            const getJournalsInfo = async () => {
                console.log("getJournalsInfo..........axios")
                let resp = await axios({
                    method:'get',
                    url: process.env.VUE_APP_API_HOST+`/api/v1/weblog/journals?basetime=${Date.now()}&interval=all&serviceid=${store.state.serviceId}`,
                    headers:{
                        "Authorization": `Bearer ${state.accessToken}`,
                    },
                })
                let data = resp.data;
                
                // Initialize an empty object to hold the grouped data
                const groupedData = {};
                
                // Iterate through the array of data
                data.forEach((item) => {
                    // Check if the locationFrom already exists in the groupedData object
                    if (groupedData[item.locationFrom]) {
                        // If it exists, update the existing object with the new values
                        groupedData[item.locationFrom].push({
                            "locationTo":item.locationTo, 
                            "totalJournals":item.totalJournal
                        });
                    } else {
                        // If it doesn't exist, create a new object with the values
                        groupedData[item.locationFrom] = [{
                            "locationTo": item.locationTo,
                            "totalJournals": item.totalJournal,
                        }];
                    }
                });
                let shortestKey;
                for (let key in groupedData) {
                    if (shortestKey != "None" || !shortestKey || key.length < shortestKey.length) {
                        shortestKey = key;
                    }
                }
                // console.log(groupedData[shortestKey])
                
                // 기본 세팅
                store.state.journals.curNode = shortestKey;
                
                drawgraph(groupedData[shortestKey])
            }

            async function drawgraph(data) {
                console.log("grawgraph..................")
                svg = null;
                
                await changeNodeAndLink(data);
                const width = 600;
                const height = 800;
                const nodeWidth = 80;
                const nodeHeight = 160;
                const nodePadding = 200;
                const ENABLE_LINKS_GRADIENTS = true;
                svg = d3.select(svgRef.value).attr('viewBox', [0, -100, width, height + 200]);

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
                    .on("click", function() {
                        d3.select(this)
                    })
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
                    .join('g')
                    .style("mix-blend-mode", "multiply");

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
            }
            return {
                svgRef,
            };
        },
    };
</script>
