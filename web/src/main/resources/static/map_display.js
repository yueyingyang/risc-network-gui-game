const display_map = (tooltip_formatter_fn) => {
    const myChart = echarts.init(document.getElementById('map'));

    const innerLabelFormatter = "{@name}";
    const dimensions = ['name', 'owner', 'resources', 'value'];
    let option;
    option = {
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
                    formatter: innerLabelFormatter,
                },
                labelLine: {
                    show: false
                },
                dimensions: dimensions,
            }
        ],
        dataset: {
            source: graphData
        }
    };
    myChart.setOption(option);
}