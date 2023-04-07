
<template>
    <svg ref="svgRef"></svg>
</template>

<script>
    import * as d3 from 'd3';
    import { sankey, sankeyLinkHorizontal } from 'd3-sankey';
    import { drag } from 'd3-drag';
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
                nodes: [],
                links: [],
            };

            onMounted(() => {
                // 기본 페이지
                store.state.serviceId = route.path.split('/')[2]
                getJournalsInfo();
            });

            watch(() => route.path, (newServiceId, oldServiceId) => {
                // 페이지 변경 감지, curNode를 기본주소로...
                store.state.serviceId = route.path.split('/')[2]
                console.log("serviceId.................", store.state.serviceId)
                getJournalsInfo();
            })

            watch(() => store.state.journals.clickFlag, () => {
                    // 현재 노드 변경
                    console.log("curNode..................", store.state.journals.curNode)
                    drawgraph(store.state.journals.curNode);
            })


            async function changeNodeAndLink() {
                // 노드, 링크 변경 -> 그림 다시 그리기
                items.nodes = []
                items.links = []
                
                const data = JSON.parse(store.state.journals.data);
                const fromNode = store.state.journals.curNode;
                // let fromNode = state.curNode;
                console.log("fromNode......................",fromNode)
                items.nodes.push({name: fromNode, id: fromNode})
                data[fromNode].forEach((d) => {
                    if(fromNode != d.locationTo) {
                        items.nodes.push({ name: d.locationTo, id: d.locationTo })
                        items.links.push({ source: fromNode, target: d.locationTo, value: d.totalJournals })
                    }
                })
            }

            const getJournalsInfo = async () => {
                let resp = await axios({
                    method:'get',
                    url: process.env.VUE_APP_API_HOST+`/api/v1/weblog/journals?basetime=${Date.now()}&interval=1d&serviceid=${store.state.serviceId}`,
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
                console.log(groupedData)
                
                let shortestUrl = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                
                for (let key in groupedData) {
                    console.log(key)
                    if (key != "none" && key.length < shortestUrl.length && !key.includes("localhost")) {
                        shortestUrl = key;
                    }
                }
                // 기본 세팅
                store.state.journals.curNode = shortestUrl;
                localStorage.setItem('curNode', shortestUrl);
                store.state.journals.data = JSON.stringify(groupedData);
                drawgraph()
            }

            async function drawgraph() {
                console.log("grawgraph..................")
                // 그림 초기화...
                d3.select(svgRef.value)
                    .selectAll("rect")
                    .remove();
                d3.select(svgRef.value)
                    .selectAll("g")
                    .remove();
                
                // 노드, 링크 세팅
                await changeNodeAndLink();
                
                const width = 1300;
                const height = 1000;
                const nodeWidth = 100;
                const nodeHeight = 200;
                const nodePadding = 100;
                const ENABLE_LINKS_GRADIENTS = true;
                const svg = d3.select(svgRef.value).attr('viewBox', [-30, -50, width, height + 200]);

                const { nodes, links } = sankey()
                    .nodeId((d) => d.name)
                    .nodeWidth(nodeWidth)
                    .nodePadding(nodePadding)
                    .extent([
                        [1, 1],
                        [width, height - nodeHeight],
                    ])(items);
                
                console.log("nodes :" ,nodes)
                console.log("links :" ,links)

                svg
                    .append('g')
                    .attr('stroke', '#000')
                    .attr('stroke-width', '0')
                    .selectAll('rect')
                    .data(nodes)
                    .join('rect')
                    .attr('x', (d) => d.x0)
                    .attr('y', (d) => d.y0)
                    .attr('height', (d) => d.y1 - d.y0)
                    .attr('width', (d) => 100)
                    .attr('fill', (d, i) => `rgb(${(120 + i * 932) % 256}, ${(150 + i * 124) % 256}, ${(170 + i * 634) % 256})`)
                    .attr('url', (d) => d.name)
                    .append('title')
                    .text((d) => `${d.name}\n${d.value}`);

                svg.selectAll("rect")
                    .on("click", function() {
                        console.log("click........... 아마도 히트맵 자리, node가 갖는 주소는", this.attributes.url.value)
                        store.commit('updateUrl', this.attributes.url.value);
                    })
                    .on("dblclick", function() {
                        store.state.journals.curNode = this.attributes.url.value;
                        console.log('Clicked node:', store.state.journals.curNode);
                        drawgraph();
                    })
                    .on("mouseover", function() {
                        d3.select(this)
                            .attr("stroke", "orange")
                            .attr("stroke-width", 2);
                    })
                    .on("mouseout", function() {
                        d3.select(this)
                            .attr("stroke", "none")
                            .attr("stroke-width", 0);
                    });


                const link = svg
                    .append('g')
                    .attr('fill', 'none')
                    .attr('stroke-opacity', 0.2)
                    .attr('fill', (d, i) => `rgb(255, 255, 255)`)
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
                        .attr('offset', '40%')
                        .attr('stop-color', (d) => d.source.color);

                    gradient
                        .append('stop')
                        .attr('offset', '60%')
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
                    .attr('font-size', 20)
                    .selectAll('text')
                    .data(nodes)
                    .join('text')
                    .attr('x', (d) => d.x0 - 350)
                    .attr('y', (d) => (d.y1 + d.y0) / 2)
                    .attr('dy', '0.35em')
                    .attr('text-anchor', 'start')
                    .text((d) => d.name)
                    .style('pointer-events', 'none');;
                
            }
            return {
                svgRef,
            };
        },
    };
</script>
