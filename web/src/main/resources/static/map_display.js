function getOption(tooltip_formatter_fn) {
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

const display_map = (tooltip_formatter_fn) => {
    const myChart = echarts.init(document.getElementById('map'));
    let option;
    option = getOption(tooltip_formatter_fn);
    myChart.setOption(option);
}