function getLabelFormatter(item) {
    let formatter = []
    formatter.push('{terrName|' + item['name'] + '}')
    if (!item['isOutline']) {
        formatter.push('{ownerName|' + item['owner'] + '}')
    }
    return formatter.join('\n')
}

function getOption(tooltip_formatter_fn) {
    let colors = graphData["data"].map((territory) => territory["color"]);
    // hide tooltips on edges
    graphData["links"].map((link) => {
        link["tooltip"] = {show: false};
        return link;
    });
    graphData["data"].map((item) => {
        // 1. label
        item["label"] = {
            show: true,
            position: "inside",
            formatter: getLabelFormatter(item),
            rich: {
                terrName: {
                    fontSize: 20,
                    fontWeight: "bold",
                    color: "#fff",
                    align: 'center'
                },
                ownerName: {
                    fontSize: 13,
                    color: "#fff",
                }
            },
        }
        if (item["isOutline"] === true) {
            item["tooltip"] = {
                show: false,
            }
            item["label"]["rich"]["terrName"]['color'] = "#B1A7A6";
            item["label"]["rich"]["ownerName"]['color'] = "#B1A7A6";
        }
        item["itemStyle"] = {
            color: item["color"],
            borderWidth: 1,
            borderColor: '#B1A7A6',
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
                    color: "#B1A7A6",
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


const info_display = {
    "techLevel": "Tech Level",
    "foodRes": "Food Resources",
    "techRes": "Tech Resources",
    "missile": "Missile",
    "ship": "Ship",
    "sword": "Sword",
    "shield": "Shield"
}

function toStatHTML(player_info) {
    let stat_html = ""
    for (const [key, value] of Object.entries(info_display)) {
        stat_html += '            <div class="statistic">\n' +
            '                <div class="value">\n' +
            player_info[key] +
            '                </div>\n' +
            '                <div class="label">\n' +
            value +
            '                </div>\n' +
            '            </div>\n';
    }
    if (player_info['isResCloak'] === true) {
        stat_html += '  <div class="statistic" style="user-select: auto;">\n' +
            '    <div class="value" style="user-select: auto;">\n' +
            '      <i class="book icon" style="user-select: auto;"></i> ' +
            '    </div>\n' +
            '    <div class="label" style="user-select: auto;">' +
            '      Cloak\n' +
            '    </div>\n' +
            '  </div>'
    }
    return stat_html
}

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
        toStatHTML(player_info) +
        '        </div>'
}

function renderTerrList(my_terr, enemy_terr) {
    // fromName: my_terr except for move_spy's fromName
    $("select[name*='fromName']").empty().append(my_terr)
    // toName should be my_terr for my_terr, and enemy_terr for attack
    $("form#move select[name*='toName']").empty().append(my_terr)
    $("form#attack select[name*='toName']").empty().append(enemy_terr)
    // spy and tool's toName are all terr
    $("form#move_spy select[name*='toName']").empty().append(my_terr).append(enemy_terr)
    $("form#move_spy select[name*='fromName']").empty().append(my_terr).append(enemy_terr)
    $("form#tools select[name*='toName']").empty().append(my_terr).append(enemy_terr)
}

// to HTML string
function to_option_dom(terr_list) {
    let option_dom = ""
    $.each(terr_list, function (key, value) {
        option_dom += $('<option></option>').attr("value", key).text(key)[0].outerHTML;
    })
    return option_dom
}

const display_map = (tooltip_formatter_fn) => {
    $("#map_box").empty().append(map_html);
    const myChart = echarts.init(document.getElementById("map"));
    let option;
    option = getOption(tooltip_formatter_fn);
    myChart.setOption(option);
    $("#player_info").empty().append(display_playerInfo(graphData["playerInfo"][0]));
    renderTerrList(to_option_dom(graphData['myTerr'][0]), to_option_dom(graphData['enemyTerr'][0]));
};

const tooltip_display = () => {
    console.log("toogle map display")
    const myChart = echarts.init(document.getElementById("map"));
    let option;
    option = getOption(full_map_formatter);

    myChart.setOption(option);
    myChart.dispatchAction({
        type: 'showTip',
        seriesIndex:1,  // 显示第几个series
        dataIndex: new Date().getHours() // 显示第几个数据
    });

}