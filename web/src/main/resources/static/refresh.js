// Refresh map ajax call
const refresh_map = () => {
    const config = {
        "url": "/update_map",
        "async": true,
        "type": "get",
        "success": res => {
            graphData = res;
            // display map
            display_map(full_map_formatter)
            $('#msg_box').append('<p style="user-select: auto;"> Update map </p>');
            $("#refresh").unbind();
            // change click event to resolve combat and also disable refresh
            $('#refresh').click(resolve_combat);
            toggleActionAndRefresh();
        },
        "error": () => {
            $('#msg_box').append('<p style="user-select: auto;"> Please wait... </p>');
            // make sure showing the loading in the map location
            $('#map_box').empty().append(load_html);
        }
    }
    $.ajax(config);
}

// end this turn, start to wait for combating result
const submit_commit = () => {
    $.ajax({
        "url": "/commit",
        "async": true,
        "type": "post",
        "success": () => {
            $('#msg_box').append('<p style="user-select: auto;"> Commit this turn successfully. Please wait for resolving combat... </p>');
            // make sure showing the loading in the map location
            $('#map_box').empty().append(resolve_html);
            toggleActionAndRefresh();
        },
    });
}

// try to fetch resolve combat
const resolve_combat = () => {
    $.ajax({
        type: "GET",
        url: '/resolve_combat',
        "async": true,
        success: function (resBody) {
            $('#msg_box').append('<p style="user-select: auto;"> [COMBAT RESULT]' + resBody['valRes'] + '</p>');
            if (resBody['graphData'] != null) {
                graphData = resBody['graphData'];
                // display map
                display_map(full_map_formatter)
                toggleActionAndRefresh();
            } else {
                if (resBody['win']) {
                    location.href = '/game/win';
                } else {
                    location.href = '/game/lose';
                }
            }
        }
    });
}

$('#commit').click(submit_commit);
$('#refresh').click(refresh_map);