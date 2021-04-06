function getOption(tooltip_formatter_fn) {
    let colors = graphData['data'].map(territory => territory['color']);
    return {
        tooltip: {
            trigger: 'item',
            formatter: tooltip_formatter_fn
        },
        series: [
            {
                type: "graph",
                data: graphData['data'],
                symbolSize: 80,
                roam: true,
                edgeSymbol: ['circle', 'arrow'],
                edgeSymbolSize: [4, 10],
                edgeLabel: {
                    fontSize: 20
                },
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 4,
                },
                color: colors,
                label: {
                    position: 'inner',
                    fontSize: 20,
                    fontWeight: 'bold',
                    color: "#fff",
                    formatter: "{@name}",
                },
                labelLine: {
                    show: false
                },
                links: graphData['links']
            }
        ],
    };

}

function getOption1(tooltip_formatter_fn) {
    return option = {
        title: {
            text: 'Graph 简单示例'
        },
        tooltip: {},
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
            {
                type: 'graph',
                layout: 'none',
                symbolSize: 50,
                roam: true,
                label: {
                    show: true
                },
                edgeSymbol: ['circle', 'arrow'],
                edgeSymbolSize: [4, 10],
                edgeLabel: {
                    fontSize: 20
                },
                data: graphData,
                // links: [],
                links: [{
                    source: 0,
                    target: 1,
                    symbolSize: [5, 20],
                    label: {
                        show: true
                    },
                    lineStyle: {
                        width: 5,
                        curveness: 0.2
                    }
                }, {
                    source: '节点2',
                    target: '节点1',
                    label: {
                        show: true
                    },
                    lineStyle: {
                        curveness: 0.2
                    }
                }, {
                    source: '节点1',
                    target: '节点3'
                }, {
                    source: '节点2',
                    target: '节点3'
                }, {
                    source: '节点2',
                    target: '节点4'
                }, {
                    source: '节点1',
                    target: '节点4'
                }],
                lineStyle: {
                    opacity: 0.9,
                    width: 2,
                    curveness: 0
                }
            }
        ]
    };
}

const map_html = '<div id="map" style="width:600px; height:400px;"></div>';
const load_html = '            <div class="ui active inverted dimmer">\n' +
    '                <div class="ui text loader">Hey👋 - other players are still doing actions. <br>' +
    'Please wait and click refresh to get update!\n' +
    '                </div>\n' +
    '            </div>';
const resolve_html = '            <div class="ui active inverted dimmer">\n' +
    '                <div class="ui text loader">Great job! Please wait for resolving combat!<br>' +
    'Please wait and click refresh to get update!\n' +
    '                </div>\n' +
    '            </div>';
const display_map = (tooltip_formatter_fn) => {
    $('#map_box').empty().append(map_html);
    const myChart = echarts.init(document.getElementById('map'));
    let option;
    option = getOption(tooltip_formatter_fn);
    myChart.setOption(option);
}