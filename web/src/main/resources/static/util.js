// Enable select
$('select.dropdown')
    .dropdown();
$('.ui.checkbox')
    .checkbox()
$('.ui.menu a.item').on('click', function () {
    // display corresponding form
    let idx = $(this).index()
    $('#action_forms').children().eq(idx)
        .removeAttr('hidden')
        .siblings()
        .addClass().attr('hidden', '');
    // set active links
    $(this)
        .addClass('active')
        .siblings()
        .removeClass('active');
})


function toggleActionAndRefresh() {
    $("#commit").toggleClass("active").toggleClass("disabled");
    $("div.field").toggleClass("disabled");
    $(".submit.button.basic").toggleClass("disabled");
}

let ajax_error_handler = {
    // internal server error: go back to lobby
    500: function () {
        location.href = "/error"
    },
    // 400: clear all disable and append warning info
    400: () => {
        $("#msg_box")
            .append('<p style="user-select: auto;"> ' + " The action is invalid, please re-try with a valid one. " + "</p>");
        $("#commit").removeClass("disabled");
        $("div.field").removeClass("disabled");
        $(".submit.button.basic").removeClass("disabled");
    }
};