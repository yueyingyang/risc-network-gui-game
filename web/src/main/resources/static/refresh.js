function showWinnerInfoModal(winnerInfo) {
    clearInterval(map_refresh_timer);
    // show the result
    $('#lost .content').empty().append(winnerInfo);
    // remove the choice of "continue to watch"
    $('#lost .green.button').remove();
    $('#lost').modal('show')
}

function startOneTurn(res) {
    clearInterval(map_refresh_timer);
    graphData = res;
    // display map
    display_map(full_map_formatter)
    // change click event to resolve combat and also disable refresh
    toggleActionAndRefresh();

}

// Refresh map ajax call
const refresh_map = () => {
    const config = {
        "url": "/update_map",
        "async": true,
        "type": "get",
        "success": function (res) {
            // 1. rejoin but game ends, show the modal with winner info
            if (res && res['winnerInfo']) {
                showWinnerInfoModal(res['winnerInfo']);
            } else if (res && res['lose'] === true) {
                // 2. lose game pop up for continue to watch or back to lobby
                clearInterval(map_refresh_timer);
                $('#lost').modal('show');
            } else if (res) {
                // 3. receive mapview
                startOneTurn(res);
            } else {
                // 3. no updates yet
                // make sure showing the loading in the map location
                $('#map_box').empty().append(load_html);
            }
        },
        "error": ajax_error_handler
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
        "error": ajax_error_handler
    });
}

function startNextTurn(resBody) {
    graphData = resBody['graphData'];
    // display map
    display_map(full_map_formatter)
    toggleActionAndRefresh();
}

// try to fetch resolve combat
const resolve_combat = () => {
    $.ajax({
        type: "GET",
        url: '/resolve_combat',
        "async": true,
        success: function (resBody) {
            clearInterval(combat_resolve_timer)
            if (resBody) {
                $('#msg_box').append('<p style="user-select: auto;"> [COMBAT RESULT]' + resBody['valRes'].replace(new RegExp('\r?\n', 'g'), '<br />') + '</p>');
                if (resBody['graphData'] != null) {
                    startNextTurn(resBody);
                } else if (resBody['winnerInfo']) {
                    showWinnerInfoModal(resBody['winnerInfo'])
                } else {
                    if (resBody['win']) {
                        $('#winner').modal('show');
                    } else {
                        $('#lost').modal('show');
                    }
                }
            }
        },
        "error": ajax_error_handler
    });
}

function watch_game() {
    $('#map_box').empty().append(load_html);
    $.ajax({
        type: "POST",
        url: '/choose_watch',
        "async": true,
        success: function (res) {
            graphData = res;
            // display map
            display_map(full_map_formatter)
            $('#msg_box').append('<p style="user-select: auto;"> You\'ll continue to watch game! </p>');
        }
    });
    const recv_watch_updates = () => {
        $.ajax({
            type: "GET",
            url: '/watch_game',
            "async": true,
            success: function (resBody) {
                if (resBody) {
                    $('#msg_box').append('<p style="user-select: auto;"> [COMBAT RESULT]' + resBody['valRes'].replace("\n", "<br />") + '</p>');
                    if (resBody['graphData'] != null) {
                        graphData = resBody['graphData'];
                        // display map
                        display_map(full_map_formatter)
                    } else {
                        showWinnerInfoModal(resBody['winnerInfo'])
                    }
                }
            }
        });
    }
    setInterval(recv_watch_updates, 2000);
}

$('#commit').click(submit_commit);
$('#lost .green.button').click(watch_game);