function getOption(tooltip_formatter_fn) {
    let colors = graphData.map(territory => territory['color']);
    return {
        tooltip: {
            trigger: 'item',
            formatter: tooltip_formatter_fn
        },
        series: [
            {
                name: 'map',
                type: 'pie',
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
            }
        ],
        dataset: {
            source: graphData
        }
    };
}
const map_html = '<div id="map" style="width:600px; height:400px;"></div>';
const load_html = '            <div class="ui active inverted dimmer">\n' +
    '                <div class="ui text loader">Hey - other players are placing their soldiers. Please wait and click\n' +
    '                    refresh to check!\n' +
    '                </div>\n' +
    '            </div>';
const display_map = (tooltip_formatter_fn) => {
    $('#map_box').empty().append(map_html);
    const myChart = echarts.init(document.getElementById('map'));
    let option;
    option = getOption(tooltip_formatter_fn);
    myChart.setOption(option);
}