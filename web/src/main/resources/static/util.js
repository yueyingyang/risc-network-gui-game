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
        .addClass().attr('hidden','');
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
