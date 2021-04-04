// Refresh map ajax call
const refresh_map = () => {
    const config = {
        "url": "/update_map",
        "async": true,
        "type": "get",
        "success": res => {
            if (!res) {
                // make sure showing the loading in the map location
                $('#map_box').empty().append(load_html);
            } else {
                graphData = res;
                // display map
                display_map(full_map_formatter)
                $("#refresh").unbind();
                // change click event to resolve combat and also disable refresh
                $('#refresh').click(resolve_combat);
                toggleActionAndRefresh();
                clearInterval(map_refresh_timer);
            }
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
            combat_resolve_timer = setInterval(resolve_combat, 1000);
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
                clearInterval(combat_resolve_timer);
            } else {
                if (resBody['win']) {
                    $('#winner').modal('show');
                } else {
                    $('#lost').modal('show');
                }
            }
        }
    });
}

$('#commit').click(submit_commit);
$('#refresh').click(refresh_map);