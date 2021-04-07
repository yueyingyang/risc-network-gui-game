function getOption(tooltip_formatter_fn) {
    let colors = graphData["data"].map((territory) => territory["color"]);
    // hide tooltips on edges
    graphData["links"].map((link) => {
        link["tooltip"] = {show: false};
        return link;
    });
    graphData["data"].map((item) => {
        item["itemStyle"] = {
            color: item["color"],
        };
        item["label"] = {
            show: true,
            position: "inside",
            fontSize: 20,
            fontWeight: "bold",
            color: "#fff",
            formatter: item["name"],
        };
        return item;
    });
    return {
        tooltip: {
            trigger: "item",
            formatter: tooltip_formatter_fn,
        },
        series: [
            {
                type: "graph",
                data: graphData["data"],
                symbolSize: 80,
                edgeSymbolSize: [4, 10],
                edgeLabel: {
                    color: "#fff",
                },
                avoidLabelOverlap: false,
                color: colors,
                labelLine: {
                    show: false,
                },
                links: graphData["links"],
                lineStyle: {
                    color: "#b1a7a6",
                    width: "1",
                    type: "solid",
                },
            },
        ],
    };
}

const map_html = '<div id="map" style="width:600px; height:400px;"></div>';
const load_html =
    '            <div class="ui active inverted dimmer">\n' +
    '                <div class="ui text loader">Hey👋 - other players are still doing actions. <br>' +
    "                </div>\n" +
    "            </div>";
const resolve_html =
    '            <div class="ui active inverted dimmer">\n' +
    '                <div class="ui text loader">Great job! Please wait for resolving combat!<br>' +
    "Please wait and click refresh to get update!\n" +
    "                </div>\n" +
    "            </div>";
const display_playerInfo = (player_info) => {
    return '<div class="ui mini statistics" style="padding-left: 50px">\n' +
        '        <div class="statistic">\n' +
        '            <div class="value">\n' +
        '                <i class="user circle outline icon"></i> \n' +
        player_info['name'] +
        '            </div>\n' +
        '            <div class="label">\n' +
        '                Player Name\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>' +
        '        <div class="ui mini statistics" style="padding-left: 50px">\n' +
        '            <div class="statistic">\n' +
        '                <div class="value">\n' +
        player_info['techLevel'] +
        '                </div>\n' +
        '                <div class="label">\n' +
        '                    Tech Level\n' +
        '                </div>\n' +
        '            </div>\n' +
        '            <div class="statistic">\n' +
        '                <div class="value">\n' +
        player_info['foodRes'] +
        '                </div>\n' +
        '                <div class="label">\n' +
        '                    Food Resources\n' +
        '                </div>\n' +
        '            </div>\n' +
        '            <div class="statistic">\n' +
        '                <div class="value">\n' +
        player_info['techRes'] +
        '                </div>\n' +
        '                <div class="label">\n' +
        '                    Tech Resources\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </div>'
}
const display_map = (tooltip_formatter_fn) => {
    $("#map_box").empty().append(map_html);
    const myChart = echarts.init(document.getElementById("map"));
    let option;
    option = getOption(tooltip_formatter_fn);
    myChart.setOption(option);
    $("#player_info").empty().append(display_playerInfo(graphData["playerInfo"][0]));
};
