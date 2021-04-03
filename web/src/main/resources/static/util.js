// Enable select
$('select.dropdown')
    .dropdown();

// Set interaction of action tabs
$('#actions a.item').on('click', function () {
    $(this)
        .addClass('active')
        .siblings()
        .removeClass('active');
})


function toggleActionAndRefresh() {
    $('#refresh').toggleClass('disabled');
    $("#commit").toggleClass("active").toggleClass("disabled");
    $("div.field").toggleClass("disabled");
    $(".submit.button.basic").toggleClass("disabled");
}
